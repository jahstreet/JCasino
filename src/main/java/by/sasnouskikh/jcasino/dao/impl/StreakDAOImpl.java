package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.StreakDAO;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.manager.JCasinoEncryptor;
import by.sasnouskikh.jcasino.logic.StreakLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * The class provides {@link StreakDAO} implementation for MySQL database.
 *
 * @author Sasnouskikh Aliaksandr
 */
class StreakDAOImpl extends StreakDAO {

    /**
     * Selects streak by its id.
     */
    private static final String SQL_SELECT_BY_ID             = "SELECT id, player_id, date, roll, offset, `lines`, bet, result " +
                                                               "FROM streak " +
                                                               "WHERE id=?";
    /**
     * Selects definite player streaks and orders them by date in descending order.
     */
    private static final String SQL_SELECT_BY_PLAYER_ID      = "SELECT id, player_id, date, roll, offset, `lines`, bet, result " +
                                                               "FROM streak " +
                                                               "WHERE player_id=? " +
                                                               "ORDER BY date DESC";
    /**
     * Selects definite player streaks where streak date is like definite pattern and orders them by date in
     * descending order.
     */
    private static final String SQL_SELECT_PLAYER_LIKE_MONTH = "SELECT id, player_id, date, roll, offset, `lines`, bet, result " +
                                                               "FROM streak " +
                                                               "WHERE player_id=? AND date LIKE ? " +
                                                               "ORDER BY date DESC";
    /**
     * Selects streaks where streak date is like definite pattern and orders them by date in descending order.
     */
    private static final String SQL_SELECT_LIKE_MONTH        = "SELECT id, player_id, date, roll, offset, `lines`, bet, result " +
                                                               "FROM streak " +
                                                               "WHERE date LIKE ? " +
                                                               "ORDER BY date DESC";
    /**
     * Inserts streak to database.
     */
    private static final String SQL_INSERT_NEW_STREAK        = "INSERT INTO streak (player_id, date, roll) " +
                                                               "VALUES (?, NOW(), ?)";
    /**
     * Updates definite streak data.
     */
    private static final String SQL_UPDATE                   = "UPDATE streak " +
                                                               "SET roll=?, offset=?, `lines`=?, bet=?, result=? " +
                                                               "WHERE id=?";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    StreakDAOImpl() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    StreakDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes {@link Streak} by its id.
     *
     * @param id id {@link Streak} to take
     * @return taken {@link Streak} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildStreak(ResultSet)
     */
    @Override
    public Streak takeStreak(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            return buildStreak(statement.executeQuery());
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking streaks. " + e);
        }
    }

    /**
     * Takes {@link List} filled by definite player {@link Streak} objects.
     *
     * @param playerId id of player whose streaks to take
     * @return {@link List} filled by definite player {@link Streak} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildStreakList(ResultSet)
     */
    @Override
    public List<Streak> takePlayerStreaks(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PLAYER_ID)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildStreakList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking streaks. " + e);
        }
    }

    /**
     * Takes {@link List} filled by definite player {@link Streak} objects due to definite streak month pattern.
     *
     * @param playerId     id of player whose loans to take
     * @param monthPattern pattern of streak date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by definite player {@link Streak} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildStreakList(ResultSet)
     */
    @Override
    public List<Streak> takePlayerStreaks(int playerId, String monthPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PLAYER_LIKE_MONTH)) {
            statement.setInt(1, playerId);
            statement.setString(2, monthPattern);
            ResultSet resultSet = statement.executeQuery();
            return buildStreakList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking streaks. " + e);
        }
    }

    /**
     * Takes {@link List} filled by {@link Streak} objects due to definite streak month pattern.
     *
     * @param monthPattern pattern of streak date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by definite player {@link Streak} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildStreakList(ResultSet)
     */
    @Override
    public List<Streak> takeStreakList(String monthPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_LIKE_MONTH)) {
            statement.setString(1, monthPattern);
            ResultSet resultSet = statement.executeQuery();
            return buildStreakList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking streaks. " + e);
        }
    }

    /**
     * Inserts {@link Streak} object.
     *
     * @param playerId id of player whose streak is inserting
     * @param roll     string of streak reel values in special format
     * @return int value of inserted streak generated id or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String, int)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public int insertStreak(int playerId, String roll) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_NEW_STREAK, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, playerId);
            statement.setString(2, roll);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting new streak. " + e);
        }
    }

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
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public boolean updateStreak(int id, String roll, String offset, String line, String bet, String result) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, roll);
            statement.setString(2, offset);
            statement.setString(3, line);
            statement.setString(4, bet);
            statement.setString(5, result);
            statement.setInt(6, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 streak associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while updating streak. " + e);
        }
    }

    /**
     * Builds {@link Streak} object by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link Streak} object or null
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    private Streak buildStreak(ResultSet resultSet) throws SQLException {
        Streak streak = null;
        if (resultSet.next()) {
            streak = new Streak();
            streak.setId(resultSet.getInt(ID));
            streak.setPlayerId(resultSet.getInt(PLAYER_ID));
            streak.setDate(resultSet.getTimestamp(DATE).toLocalDateTime());
            String roll = resultSet.getString(ROLL);
            streak.setRoll(roll);
            streak.setRollMD5(JCasinoEncryptor.encryptMD5(roll));
            streak.setOffset(resultSet.getString(OFFSET));
            streak.setLines(resultSet.getString(LINES));
            streak.setBet(resultSet.getString(BET));
            streak.setResult(resultSet.getString(RESULT));
            ArrayDeque<Roll> rolls = StreakLogic.buildRollList(streak);
            streak.setRolls(rolls);
            streak.setTotal(StreakLogic.countStreakTotal(rolls));
        }
        return streak;
    }

    /**
     * Builds {@link List} object filled by {@link Streak} objects by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link List} object or null
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     * @see #buildStreak(ResultSet)
     */
    private List<Streak> buildStreakList(ResultSet resultSet) throws SQLException {
        List<Streak> streakList = new ArrayList<>();
        Streak       streak;
        do {
            streak = buildStreak(resultSet);
            if (streak != null) {
                streakList.add(streak);
            }
        } while (streak != null);
        return !streakList.isEmpty() ? streakList : null;
    }
}