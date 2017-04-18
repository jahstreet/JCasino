package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.QuestionDAO;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Question;
import com.mysql.cj.api.jdbc.Statement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * The class provides {@link QuestionDAO} implementation for MySQL database.
 *
 * @author Sasnouskikh Aliaksandr
 */
class QuestionDAOImpl extends QuestionDAO {

    /**
     * Selects definite question by its id.
     */
    private static final String SQL_SELECT_BY_ID        = "SELECT id, player_id, email, topic, question, q_date, admin_id, answer, a_date, satisfaction " +
                                                          "FROM question " +
                                                          "WHERE id=?";
    /**
     * Selects questions of definite player.
     */
    private static final String SQL_SELECT_BY_PLAYER_ID = "SELECT id, player_id, email, topic, question, q_date, admin_id, answer, a_date, satisfaction " +
                                                          "FROM question " +
                                                          "WHERE player_id=?";
    /**
     * Selects unanswered questions where topic is like definite pattern
     */
    private static final String SQL_SELECT_UNANSWERED   = "SELECT id, player_id, email, topic, question, q_date, admin_id, answer, a_date, satisfaction " +
                                                          "FROM question " +
                                                          "WHERE answer IS NULL AND topic LIKE ? " +
                                                          "ORDER BY q_date DESC";
    /**
     * Selects unanswered questions where admin id, tipic, question date are like definite patterns and orders the by
     * question date in descending order.
     */
    private static final String SQL_SELECT_ANSWERED     = "SELECT id, player_id, email, topic, question, q_date, admin_id, answer, a_date, satisfaction " +
                                                          "FROM question " +
                                                          "WHERE admin_id LIKE ? " +
                                                          "AND topic LIKE ? " +
                                                          "AND q_date LIKE ? " +
                                                          "AND answer IS NOT NULL " +
                                                          "ORDER BY q_date DESC";

    /**
     * Inserts question asked by guest with definite e-mail.
     */
    private static final String SQL_INSERT_GUEST  = "INSERT INTO question (email, topic, question, q_date) " +
                                                    "VALUES (?, ?, ?, NOW())";
    /**
     * Inserts question asked by definite player.
     */
    private static final String SQL_INSERT_PLAYER = "INSERT INTO question (player_id, email, topic, question, q_date) " +
                                                    "VALUES (?, ?, ?, ?, NOW())";

    /**
     * Updates definite question answer made by definite admin.
     */
    private static final String SQL_UPDATE_ANSWER       = "UPDATE question " +
                                                          "SET answer=?, admin_id=?, a_date=NOW() " +
                                                          "WHERE id=?";
    /**
     * Updates definite question satisfaction value.
     */
    private static final String SQL_UPDATE_SATISFACTION = "UPDATE question " +
                                                          "SET satisfaction=? " +
                                                          "WHERE id=?";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    QuestionDAOImpl() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    QuestionDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes definite {@link Question} object.
     *
     * @param id id of {@link Question} to take
     * @return taken {@link Question} object or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildQuestion(ResultSet)
     */
    @Override
    public Question takeQuestion(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildQuestion(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking question. " + e);
        }
    }

    /**
     * Takes {@link List} filled by definite player {@link Question} objects.
     *
     * @param playerId id of player whose questions to take
     * @return taken {@link List} filled by {@link Question} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildQuestionList(ResultSet)
     */
    @Override
    public List<Question> takePlayerQuestions(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PLAYER_ID)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildQuestionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking questions. " + e);
        }
    }

    /**
     * Takes {@link List} filled by unanswered {@link Question} objects due to definite question topic pattern.
     *
     * @param topicPattern pattern of question topic conforming to <code>SQL LIKE</code> operator
     * @return taken {@link List} filled by {@link Question} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildQuestionList(ResultSet)
     */
    @Override
    public List<Question> takeUnanswered(String topicPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_UNANSWERED)) {
            statement.setString(1, topicPattern);
            ResultSet resultSet = statement.executeQuery();
            return buildQuestionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking unanswered list. " + e);
        }
    }

    /**
     * Takes {@link List} filled by answered {@link Question} objects due to definite question topic, asking month
     * and admin id who answered this question patterns.
     *
     * @param topicPattern pattern of question topic conforming to <code>SQL LIKE</code> operator
     * @param monthPattern pattern of question asking month conforming to <code>SQL LIKE</code> operator
     * @param adminPattern pattern of admin id who answered this question conforming to <code>SQL LIKE</code> operator
     * @return taken {@link List} filled by {@link Question} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildQuestionList(ResultSet)
     */
    @Override
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

    /**
     * Inserts {@link Question} asked by guest.
     *
     * @param email    guest e-mail
     * @param topic    question topic
     * @param question question text
     * @return int value of inserted question generated id or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String, int)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
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

    /**
     * Inserts {@link Question} asked by player.
     *
     * @param playerId player id
     * @param email    player e-mail
     * @param topic    question topic
     * @param question question text
     * @return int value of inserted question generated id or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String, int)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
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

    /**
     * Updates {@link Question} satisfaction value.
     *
     * @param questionId   id of updating question
     * @param satisfaction players satisfaction with the admin answer
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
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

    /**
     * Updates {@link Question} answer made by definite admin.
     *
     * @param questionId id of updating question
     * @param answer     text of answer to the updating question
     * @param adminId    id of admin who answered the updating question
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
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

    /**
     * Builds {@link Question} object by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link Question} object or null
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    private Question buildQuestion(ResultSet resultSet) throws SQLException {
        Question question = null;
        if (resultSet.next()) {
            question = new Question();
            question.setId(resultSet.getInt(ID));
            question.setPlayerId(resultSet.getInt(PLAYER_ID));
            question.setEmail(resultSet.getString(EMAIL));
            question.setTopic(Question.QuestionTopic.valueOf(resultSet.getString(TOPIC).toUpperCase()));
            question.setQuestion(resultSet.getString(QUESTION));
            question.setQuestionDate(resultSet.getTimestamp(Q_DATE).toLocalDateTime());
            question.setAdminId(resultSet.getInt(ADMIN_ID));
            question.setAnswer(resultSet.getString(ANSWER));
            Timestamp answerDate = resultSet.getTimestamp(A_DATE);
            if (answerDate != null) {
                question.setAnswerDate(answerDate.toLocalDateTime());
            }
            String satisfaction = resultSet.getString(SATISFACTION);
            if (satisfaction != null) {
                question.setSatisfaction(Question.Satisfaction.valueOf(satisfaction.toUpperCase()));
            }
        }
        return question;
    }

    /**
     * Builds {@link List} object filled by {@link Question} objects by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link List} object or null
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     * @see LoanDAOImpl#buildLoan(ResultSet)
     */
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