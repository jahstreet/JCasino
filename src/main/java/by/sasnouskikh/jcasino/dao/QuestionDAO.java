package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Question;

import java.sql.SQLException;
import java.util.List;

/**
 * The class provides DAO abstraction for {@link Question} objects.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AbstractDAO
 */
public abstract class QuestionDAO extends AbstractDAO {

    /**
     * Column names of database table 'question'.
     */
    protected static final String ID           = "id";
    protected static final String PLAYER_ID    = "player_id";
    protected static final String EMAIL        = "email";
    protected static final String TOPIC        = "topic";
    protected static final String QUESTION     = "question";
    protected static final String Q_DATE       = "q_date";
    protected static final String ADMIN_ID     = "admin_id";
    protected static final String ANSWER       = "answer";
    protected static final String A_DATE       = "a_date";
    protected static final String SATISFACTION = "satisfaction";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    protected QuestionDAO() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    protected QuestionDAO(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes definite {@link Question} object.
     *
     * @param id id of {@link Question} to take
     * @return taken {@link Question} object
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract Question takeQuestion(int id) throws DAOException;

    /**
     * Takes {@link List} filled by definite player {@link Question} objects.
     *
     * @param playerId id of player whose questions to take
     * @return taken {@link List} filled by {@link Question} objects
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Question> takePlayerQuestions(int playerId) throws DAOException;

    /**
     * Takes {@link List} filled by unanswered {@link Question} objects due to definite question topic pattern.
     *
     * @param topicPattern pattern of question topic conforming to <code>SQL LIKE</code> operator
     * @return taken {@link List} filled by {@link Question} objects
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Question> takeUnanswered(String topicPattern) throws DAOException;

    /**
     * Takes {@link List} filled by answered {@link Question} objects due to definite question topic, asking month
     * and admin id who answered this question patterns.
     *
     * @param topicPattern pattern of question topic conforming to <code>SQL LIKE</code> operator
     * @param monthPattern pattern of question asking month conforming to <code>SQL LIKE</code> operator
     * @param adminPattern pattern of admin id who answered this question conforming to <code>SQL LIKE</code> operator
     * @return taken {@link List} filled by {@link Question} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Question> takeAnswered(String topicPattern, String monthPattern, String adminPattern) throws DAOException;

    /**
     * Inserts {@link Question} asked by guest.
     *
     * @param email    guest e-mail
     * @param topic    question topic
     * @param question question text
     * @return int value of inserted question generated id or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract int insertQuestion(String email, String topic, String question) throws DAOException;

    /**
     * Inserts {@link Question} asked by player.
     *
     * @param playerId player id
     * @param email    player e-mail
     * @param topic    question topic
     * @param question question text
     * @return int value of inserted question generated id or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract int insertQuestion(int playerId, String email, String topic, String question) throws DAOException;

    /**
     * Updates {@link Question} satisfaction value.
     *
     * @param questionId   id of updating question
     * @param satisfaction players satisfaction with the admin answer
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeQuestionSatisfaction(int questionId, String satisfaction) throws DAOException;

    /**
     * Updates {@link Question} answer made by definite admin.
     *
     * @param questionId id of updating question
     * @param answer     text of answer to the updating question
     * @param adminId    id of admin who answered the updating question
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean answerQuestion(int questionId, String answer, int adminId) throws DAOException;
}