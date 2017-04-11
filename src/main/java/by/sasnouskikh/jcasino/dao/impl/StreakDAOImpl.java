package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.logic.JCasinoEncryptor;
import by.sasnouskikh.jcasino.logic.StreakLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class StreakDAOImpl extends AbstractDAO<Integer, Streak> {

    private static final String SQL_SELECT_BY_ID             = "SELECT id, player_id, date, roll, offset, `lines`, bet, result " +
                                                               "FROM streak " +
                                                               "WHERE id=?";
    private static final String SQL_SELECT_BY_PLAYER_ID      = "SELECT id, player_id, date, roll, offset, `lines`, bet, result " +
                                                               "FROM streak " +
                                                               "WHERE player_id=? " +
                                                               "ORDER BY date DESC";
    private static final String SQL_SELECT_PLAYER_LIKE_MONTH = "SELECT id, player_id, date, roll, offset, `lines`, bet, result " +
                                                               "FROM streak " +
                                                               "WHERE player_id=? AND date LIKE ? " +
                                                               "ORDER BY date DESC";
    private static final String SQL_SELECT_LIKE_MONTH        = "SELECT id, player_id, date, roll, offset, `lines`, bet, result " +
                                                               "FROM streak " +
                                                               "WHERE date LIKE ? " +
                                                               "ORDER BY date DESC";
    private static final String SQL_INSERT_NEW_STREAK        = "INSERT INTO streak (player_id, date, roll) " +
                                                               "VALUES (?, NOW(), ?)";
    private static final String SQL_UPDATE                   = "UPDATE streak " +
                                                               "SET roll=?, offset=?, `lines`=?, bet=?, result=? " +
                                                               "WHERE id=?";

    StreakDAOImpl() {
    }

    StreakDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    public Streak takeStreak(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            return buildStreak(statement.executeQuery());
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking streaks. " + e);
        }
    }

    public List<Streak> takePlayerStreaks(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PLAYER_ID)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildStreakList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking streaks. " + e);
        }
    }

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

    public List<Streak> takeStreakList(String monthPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_LIKE_MONTH)) {
            statement.setString(1, monthPattern);
            ResultSet resultSet = statement.executeQuery();
            return buildStreakList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking streaks. " + e);
        }
    }

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

    private Streak buildStreak(ResultSet resultSet) throws SQLException {
        Streak streak = null;
        if (resultSet.next()) {
            streak = new Streak();
            streak.setId(resultSet.getInt("id"));
            streak.setPlayerId(resultSet.getInt("player_id"));
            streak.setDate(resultSet.getTimestamp("date").toLocalDateTime());
            String roll = resultSet.getString("roll");
            streak.setRoll(roll);
            streak.setRollMD5(JCasinoEncryptor.encryptMD5(roll));
            streak.setOffset(resultSet.getString("offset"));
            streak.setLines(resultSet.getString("lines"));
            streak.setBet(resultSet.getString("bet"));
            streak.setResult(resultSet.getString("result"));
            ArrayDeque<Roll> rolls = StreakLogic.buildRollList(streak);
            streak.setRolls(rolls);
            streak.setTotal(StreakLogic.countStreakTotal(rolls));
        }
        return streak;
    }

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