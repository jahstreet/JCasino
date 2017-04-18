package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Streak;

import java.sql.SQLException;
import java.util.List;

/**
 * The class provides DAO abstraction for {@link Streak} objects.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AbstractDAO
 */
public abstract class StreakDAO extends AbstractDAO {

    /**
     * Column names of database table 'streak'.
     */
    protected static final String ID        = "id";
    protected static final String PLAYER_ID = "player_id";
    protected static final String DATE      = "date";
    protected static final String ROLL      = "roll";
    protected static final String OFFSET    = "offset";
    protected static final String LINES     = "lines";
    protected static final String BET       = "bet";
    protected static final String RESULT    = "result";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    protected StreakDAO() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    protected StreakDAO(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes {@link Streak} by its id.
     *
     * @param id id {@link Streak} to take
     * @return taken {@link Streak}
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract Streak takeStreak(int id) throws DAOException;

    /**
     * Takes {@link List} filled by definite player {@link Streak} objects.
     *
     * @param playerId id of player whose streaks to take
     * @return {@link List} filled by definite player {@link Streak} objects
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Streak> takePlayerStreaks(int playerId) throws DAOException;

    /**
     * Takes {@link List} filled by definite player {@link Streak} objects due to definite streak month pattern.
     *
     * @param playerId     id of player whose loans to take
     * @param monthPattern pattern of streak date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by definite player {@link Streak} objects
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Streak> takePlayerStreaks(int playerId, String monthPattern) throws DAOException;

    /**
     * Takes {@link List} filled by {@link Streak} objects due to definite streak month pattern.
     *
     * @param monthPattern pattern of streak date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by definite player {@link Streak} objects
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Streak> takeStreakList(String monthPattern) throws DAOException;

    /**
     * Inserts {@link Streak} object.
     *
     * @param playerId id of player whose streak is inserting
     * @param roll     string of streak reel values in special format
     * @return int value of inserted streak generated id
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract int insertStreak(int playerId, String roll) throws DAOException;

    /**
     * Updates definite {@link Streak} data.
     *
     * @param id     id of streak which data is updating
     * @param roll   string of streak reel values in special format
     * @param offset string of streak offset values in special format
     * @param line   string of streak line values in special format
     * @param bet    string of streak bet values in special format
     * @param result string of streak result values in special format
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean updateStreak(int id, String roll, String offset, String line, String bet, String result) throws DAOException;
}