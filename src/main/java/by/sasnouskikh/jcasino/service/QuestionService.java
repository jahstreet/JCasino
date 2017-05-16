package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.QuestionDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
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

/**
 * The class provides Service layer actions with support questions.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class QuestionService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger(QuestionService.class);

    /**
     * Default instance constructor.
     */
    public QuestionService() {
    }

    /**
     * Constructs instance using definite {@link DAOHelper} object.
     */
    public QuestionService(DAOHelper daoHelper) {
        super(daoHelper);
    }

    /**
     * Calls DAO layer to take definite {@link Question} object.
     *
     * @param id question id
     * @return taken {@link Question} object
     * @see DAOHelper
     * @see QuestionDAO#takeQuestion(int)
     */
    public Question takeQuestion(int id) {
        Question    question    = null;
        QuestionDAO questionDAO = daoHelper.getQuestionDAO();
        try {
            question = questionDAO.takeQuestion(id);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return question;
    }

    /**
     * Calls DAO layer to take {@link List} collection of definite player {@link Question} objects.
     *
     * @param playerId player id
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see QuestionDAO#takePlayerQuestions(int)
     */
    public List<Question> takePlayerQuestions(int playerId) {
        List<Question> questions   = null;
        QuestionDAO    questionDAO = daoHelper.getQuestionDAO();
        try {
            questions = questionDAO.takePlayerQuestions(playerId);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return questions;
    }

    /**
     * Calls DAO layer to take {@link List} collection of unanswered {@link Question} objects on a definite topic.
     *
     * @param topic question topic
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see QuestionDAO#takeUnanswered(String)
     */
    public List<Question> takeUnanswered(String topic) {
        List<Question> questionList = null;
        String         topicPattern;
        if (topic != null) {
            topicPattern = topic;
        } else {
            topicPattern = EMPTY_STRING;
        }
        topicPattern = topicPattern.trim().toLowerCase() + PERCENT;
        QuestionDAO questionDAO = daoHelper.getQuestionDAO();
        try {
            questionList = questionDAO.takeUnanswered(topicPattern);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return questionList;
    }

    /**
     * Calls DAO layer to take {@link List} collection of answered {@link Question} objects due to given parameters.
     *
     * @param topic            question topic
     * @param month            question asking month
     * @param showMy           marker of showing only questions processed by given admin
     * @param admin            admin whose answers to show if marker 'showMy' equals true
     * @param satisfactionSort is need to sort list by satisfaction in descending order
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see QuestionDAO#takeAnswered(String, String, String)
     * @see #sortBySatisfaction(List)
     */
    public List<Question> takeAnswered(String topic, String month, boolean showMy, Admin admin, boolean satisfactionSort) {
        if (admin == null && showMy) {
            return null;
        }
        List<Question> questionList = null;
        String         adminPattern = showMy ? String.valueOf(admin.getId()) : PERCENT;
        String         topicPattern = (topic != null ? topic.trim().toLowerCase() : EMPTY_STRING) + PERCENT;
        String         monthPattern = (month != null ? month.trim() : EMPTY_STRING) + PERCENT;
        QuestionDAO    questionDAO  = daoHelper.getQuestionDAO();
        try {
            questionList = questionDAO.takeAnswered(topicPattern, monthPattern, adminPattern);
            if (satisfactionSort) {
                sortBySatisfaction(questionList);
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return questionList;
    }

    /**
     * Calls DAO layer to insert new question to database.
     *
     * @param user     user who asks the question
     * @param email    user e-mail
     * @param topic    question topic
     * @param question question text
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see QuestionDAO#insertQuestion(int, String, String, String)
     * @see QuestionDAO#insertQuestion(String, String, String)
     */
    public boolean sendSupport(JCasinoUser user, String email, String topic, String question) {
        if (email == null || question == null) {
            return false;
        }
        email = email.trim().toLowerCase();
        question = question.trim();
        topic = topic == null || topic.trim().isEmpty() ?
                Question.QuestionTopic.OTHER.toString().toLowerCase() :
                topic.trim().toLowerCase();
        QuestionDAO questionDAO = daoHelper.getQuestionDAO();
        try {
            if (user == null) {
                return questionDAO.insertQuestion(email, topic, question) != 0;
            } else {
                int id = user.getId();
                return questionDAO.insertQuestion(id, email, topic, question) != 0;
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to update definite question data with admin answer to this question and sends this answer to user
     * e-mail given on question asking.
     *
     * @param question {@link Question} object to be updated
     * @param answer   admin answer text
     * @param admin    {@link Admin} object who answers the question
     * @param locale   locale of answer
     * @return true if transaction proceeded successfully
     * @see DAOHelper
     * @see MessageManager
     * @see MailerSSL
     * @see QuestionDAO#answerQuestion(int, String, int)
     */
    public boolean answerSupport(Question question, String answer, Admin admin, String locale) {
        if (question == null || answer == null || admin == null) {
            return false;
        }
        int questionId = question.getId();
        int adminId    = admin.getId();
        answer = answer.trim();
        QuestionDAO questionDAO = daoHelper.getQuestionDAO();
        try {
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
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        } catch (MailerException e) {
            LOGGER.log(Level.ERROR, "Sending e-mail answering support error. " + e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to rate admin answer to definite question.
     *
     * @param id           question id
     * @param satisfaction string representation of {@link by.sasnouskikh.jcasino.entity.bean.Question.Satisfaction}
     *                     rating
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see QuestionDAO#changeQuestionSatisfaction(int, String)
     */
    public boolean rateAnswer(int id, String satisfaction) {
        if (satisfaction != null) {
            satisfaction = satisfaction.toLowerCase().trim();
        }
        QuestionDAO questionDAO = daoHelper.getQuestionDAO();
        try {
            return questionDAO.changeQuestionSatisfaction(id, satisfaction);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to reset admin answer rating of definite question.
     *
     * @param id question id
     * @return true if operation proceeded successfully
     * @see #rateAnswer(int, String)
     */
    public boolean resetAnswerRating(int id) {
        return rateAnswer(id, null);
    }

    /**
     * Sorts given {@link List} collection of {@link Question} objects by their {@link Question#satisfaction} fields.
     *
     * @param list {@link List} to be sorted
     * @see Collections#sort(List, Comparator)
     */
    private static void sortBySatisfaction(List<Question> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Collections.sort(list, ((Comparator<Question>) (o1, o2) -> {
            if (o1 == null || o1.getSatisfaction() == null) {
                return 1;
            }
            if (o2 == null || o2.getSatisfaction() == null) {
                return -1;
            }
            return o1.getSatisfaction().compareTo(o2.getSatisfaction());
        }).thenComparing((o1, o2) -> {
            if (o1 == null || o1.getQuestionDate() == null) {
                return 1;
            }
            if (o2 == null || o2.getQuestionDate() == null) {
                return -1;
            }
            return o2.getQuestionDate().compareTo(o1.getQuestionDate());
        }));
    }
}