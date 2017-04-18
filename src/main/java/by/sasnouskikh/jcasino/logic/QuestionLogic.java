package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.QuestionDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.PlayerProfile;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.mailer.MailerException;
import by.sasnouskikh.jcasino.mailer.MailerSSL;
import by.sasnouskikh.jcasino.manager.MessageManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class QuestionLogic {
    private static final Logger LOGGER = LogManager.getLogger(QuestionLogic.class);

    private QuestionLogic() {
    }

    public static Question takeQuestion(int id) {
        Question question = null;
        try (DAOHelper daoHelper = new DAOHelper()) {
            QuestionDAO questionDAO = daoHelper.getQuestionDAO();
            question = questionDAO.takeQuestion(id);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return question;
    }

    public static List<Question> takePlayerQuestions(int playerId) {
        List<Question> questions = null;
        try (DAOHelper daoHelper = new DAOHelper()) {
            QuestionDAO questionDAO = daoHelper.getQuestionDAO();
            questions = questionDAO.takePlayerQuestions(playerId);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return questions;
    }

    public static List<Question> takeUnanswered(String topic) {
        List<Question> questionList = null;
        String         topicPattern;
        if (topic != null) {
            topicPattern = topic;
        } else {
            topicPattern = EMPTY_STRING;
        }
        topicPattern = topicPattern.trim().toLowerCase() + PERCENT;
        try (DAOHelper daoHelper = new DAOHelper()) {
            QuestionDAO questionDAO = daoHelper.getQuestionDAO();
            questionList = questionDAO.takeUnanswered(topicPattern);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return questionList;
    }

    public static List<Question> takeAnswered(String topic, String month, boolean showMy, Admin admin, boolean satisfactionSort) {
        List<Question> questionList = null;
        int            adminId      = admin.getId();
        String         adminPattern;
        String         topicPattern;
        String         monthPattern;
        if (topic != null) {
            topicPattern = topic.trim().toLowerCase();
        } else {
            topicPattern = EMPTY_STRING;
        }
        if (month != null && !month.isEmpty()) {
            monthPattern = month.trim();
        } else {
            monthPattern = EMPTY_STRING;
        }
        if (showMy) {
            adminPattern = String.valueOf(adminId);
        } else {
            adminPattern = EMPTY_STRING;
        }
        topicPattern = topicPattern + PERCENT;
        monthPattern = monthPattern + PERCENT;
        try (DAOHelper daoHelper = new DAOHelper()) {
            QuestionDAO questionDAO = daoHelper.getQuestionDAO();
            questionList = questionDAO.takeAnswered(topicPattern, monthPattern, adminPattern);
            if (satisfactionSort) {
                sortBySatisfaction(questionList);
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return questionList;
    }

    public static boolean sendSupport(JCasinoUser user, String email, String topic, String question) {
        email = email.trim().toLowerCase();
        topic = topic.trim().toLowerCase();
        question = question.trim();
        try (DAOHelper daoHelper = new DAOHelper()) {
            QuestionDAO questionDAO = daoHelper.getQuestionDAO();
            if (user == null) {
                return questionDAO.insertQuestion(email, topic, question) != 0;
            } else {
                int id = user.getId();
                return questionDAO.insertQuestion(id, email, topic, question) != 0;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean answerSupport(Question question, String answer, Admin admin, String locale) {
        int questionId = question.getId();
        int adminId    = admin.getId();
        answer = answer.trim();
        try (DAOHelper daoHelper = new DAOHelper()) {
            QuestionDAO questionDAO = daoHelper.getQuestionDAO();
            daoHelper.beginTransaction();
            if (questionDAO.answerQuestion(questionId, answer, adminId)) {
                String email        = question.getEmail();
                String questionText = question.getQuestion();
                String emailPattern = MessageManager.getMessageManager(locale).getMessage(EMAIL_PATTERN_ANSWER_SUPPORT);
                String emailText    = String.format(emailPattern, questionText, answer, EMPTY_STRING);

                //defining subject name
                String subject  = null;
                int    playerId = question.getPlayerId();
                if (playerId != 0) {
                    PlayerDAO     playerDAO     = daoHelper.getPlayerDAO();
                    PlayerProfile playerProfile = playerDAO.takeProfile(playerId);
                    if (playerProfile != null) {
                        subject = playerProfile.getfName();
                    }
                    if (subject == null) {
                        subject = JCasinoUser.UserRole.PLAYER.toString();
                    }
                } else {
                    subject = JCasinoUser.UserRole.GUEST.toString();
                }
                if (MailerSSL.sendEmail(subject, emailText, email)) {
                    daoHelper.commit();
                    return true;
                }
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        } catch (MailerException e) {
            LOGGER.log(Level.ERROR, "Sending e-mail answering support error. " + e.getMessage());
        }
        return false;
    }

    public static boolean rateAnswer(int id, String satisfaction) {
        if (satisfaction != null) {
            satisfaction = satisfaction.toLowerCase();
        }
        try (DAOHelper daoHelper = new DAOHelper()) {
            QuestionDAO questionDAO = daoHelper.getQuestionDAO();
            return questionDAO.changeQuestionSatisfaction(id, satisfaction);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean resetAnswerRating(int id) {
        return rateAnswer(id, null);
    }

    private static void sortBySatisfaction(List<Question> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Collections.sort(list, ((Comparator<Question>) (o1, o2) -> {
            if (o1.getSatisfaction() == null) {
                return 1;
            }
            if (o2.getSatisfaction() == null) {
                return -1;
            }
            return o1.getSatisfaction().compareTo(o2.getSatisfaction());
        }).thenComparing((o1, o2) -> o2.getQuestionDate().compareTo(o1.getQuestionDate())));
    }


}