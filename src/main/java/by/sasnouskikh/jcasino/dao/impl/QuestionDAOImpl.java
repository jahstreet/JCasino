package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Question;
import com.mysql.cj.api.jdbc.Statement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAOImpl extends AbstractDAO<Integer, Question> {

    private static final String SQL_SELECT_BY_ID        = "SELECT id, player_id, email, topic, question, q_date, admin_id, answer, a_date, satisfaction " +
                                                          "FROM question " +
                                                          "WHERE id=?";
    private static final String SQL_SELECT_BY_PLAYER_ID = "SELECT id, player_id, email, topic, question, q_date, admin_id, answer, a_date, satisfaction " +
                                                          "FROM question " +
                                                          "WHERE player_id=?";
    private static final String SQL_SELECT_UNANSWERED   = "SELECT id, player_id, email, topic, question, q_date, admin_id, answer, a_date, satisfaction " +
                                                          "FROM question " +
                                                          "WHERE answer IS NULL AND topic LIKE ? " +
                                                          "ORDER BY q_date DESC";
    private static final String SQL_SELECT_ANSWERED     = "SELECT id, player_id, email, topic, question, q_date, admin_id, answer, a_date, satisfaction " +
                                                          "FROM question " +
                                                          "WHERE admin_id LIKE ? " +
                                                          "AND topic LIKE ? " +
                                                          "AND q_date LIKE ? " +
                                                          "AND answer IS NOT NULL " +
                                                          "ORDER BY q_date DESC";

    private static final String SQL_INSERT_GUEST  = "INSERT INTO question (email, topic, question, q_date) " +
                                                    "VALUES (?, ?, ?, NOW())";
    private static final String SQL_INSERT_PLAYER = "INSERT INTO question (player_id, email, topic, question, q_date) " +
                                                    "VALUES (?, ?, ?, ?, NOW())";

    private static final String SQL_UPDATE_ANSWER       = "UPDATE question " +
                                                          "SET answer=?, admin_id=?, a_date=NOW() " +
                                                          "WHERE id=?";
    private static final String SQL_UPDATE_SATISFACTION = "UPDATE question " +
                                                          "SET satisfaction=? " +
                                                          "WHERE id=?";

    QuestionDAOImpl() {
    }

    QuestionDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    public Question takeQuestion(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildQuestion(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking question. " + e);
        }
    }

    public List<Question> takePlayerQuestions(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PLAYER_ID)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildQuestionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking questions. " + e);
        }
    }

    public List<Question> takeUnanswered(String topicPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_UNANSWERED)) {
            statement.setString(1, topicPattern);
            ResultSet resultSet = statement.executeQuery();
            return buildQuestionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking unanswered list. " + e);
        }
    }

    public List<Question> takeAnswered(String topicPattern, String monthPattern, String adminPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ANSWERED)) {
            statement.setString(1, adminPattern);
            statement.setString(2, topicPattern);
            statement.setString(3, monthPattern);
            ResultSet resultSet = statement.executeQuery();
            return buildQuestionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking answered list. " + e);
        }
    }

    public boolean changeQuestionSatisfaction(int questionId, String satisfaction) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_SATISFACTION)) {
            statement.setString(1, satisfaction);
            statement.setInt(2, questionId);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 question associated with given id: '" + questionId + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing question satisfaction. " + e);
        }
    }

    public int insertQuestion(String email, String topic, String question) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_GUEST, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, email);
            statement.setString(2, topic);
            statement.setString(3, question);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting question. " + e);
        }
    }

    public int insertQuestion(int playerId, String email, String topic, String question) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_PLAYER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, playerId);
            statement.setString(2, email);
            statement.setString(3, topic);
            statement.setString(4, question);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting question. " + e);
        }
    }

    public boolean answerQuestion(int questionId, String answer, int adminId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ANSWER)) {
            statement.setString(1, answer);
            statement.setInt(2, adminId);
            statement.setInt(3, questionId);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 question associated with given id: '" + questionId + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while answering question. " + e);
        }
    }

    private Question buildQuestion(ResultSet resultSet) throws SQLException {
        Question question = null;
        if (resultSet.next()) {
            question = new Question();
            question.setId(resultSet.getInt("id"));
            question.setPlayerId(resultSet.getInt("player_id"));
            question.setEmail(resultSet.getString("email"));
            question.setTopic(Question.QuestionTopic.valueOf(resultSet.getString("topic").toUpperCase()));
            question.setQuestion(resultSet.getString("question"));
            question.setQuestionDate(resultSet.getTimestamp("q_date").toLocalDateTime());
            question.setAdminid(resultSet.getInt("admin_id"));
            question.setAnswer(resultSet.getString("answer"));
            Timestamp answerDate = resultSet.getTimestamp("a_date");
            if (answerDate != null) {
                question.setAnswerDate(answerDate.toLocalDateTime());
            }
            String satisfaction = resultSet.getString("satisfaction");
            if (satisfaction != null) {
                question.setSatisfaction(Question.Satisfaction.valueOf(satisfaction.toUpperCase()));
            }
        }
        return question;
    }

    private List<Question> buildQuestionList(ResultSet resultSet) throws SQLException {
        List<Question> questionList = new ArrayList<>();
        Question       question;
        do {
            question = buildQuestion(resultSet);
            if (question != null) {
                questionList.add(question);
            }
        } while (question != null);
        return !questionList.isEmpty() ? questionList : null;
    }
}