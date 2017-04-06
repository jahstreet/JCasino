package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.logic.StreakLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StreakDAOImpl extends AbstractDAO<Integer, Streak> {

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

    StreakDAOImpl() {
    }

    StreakDAOImpl(WrappedConnection connection) {
        super(connection);
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

    private Streak buildStreak(ResultSet resultSet) throws SQLException {
        Streak streak = null;
        if (resultSet.next()) {
            streak = new Streak();
            streak.setId(resultSet.getInt("id"));
            streak.setPlayerId(resultSet.getInt("player_id"));
            streak.setDate(resultSet.getTimestamp("date").toLocalDateTime());
            streak.setRoll(resultSet.getString("roll"));
            streak.setOffset(resultSet.getString("offset"));
            streak.setLines(resultSet.getString("lines"));
            streak.setBet(resultSet.getString("bet"));
            streak.setResult(resultSet.getString("result"));
            List<Roll> rolls = StreakLogic.buildRollList(streak);
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