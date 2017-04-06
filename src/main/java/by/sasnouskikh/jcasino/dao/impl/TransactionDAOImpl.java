package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import com.mysql.cj.api.jdbc.Statement;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl extends AbstractDAO<Integer, Transaction> {

    private static final String SQL_SELECT_BY_PLAYER_ID      = "SELECT id, player_id, date, amount " +
                                                               "FROM transaction " +
                                                               "WHERE player_id=? " +
                                                               "ORDER BY date DESC";
    private static final String SQL_SELECT_PLAYER_LIKE_MONTH = "SELECT id, player_id, date, amount " +
                                                               "FROM transaction " +
                                                               "WHERE player_id=? AND date LIKE ? " +
                                                               "ORDER BY date DESC";
    private static final String SQL_SELECT_LIKE_MONTH        = "SELECT id, player_id, date, amount " +
                                                               "FROM transaction " +
                                                               "WHERE date LIKE ? " +
                                                               "ORDER BY date DESC";
    private static final String SQL_TRANSACTION_INSERT       = "INSERT INTO transaction (player_id, date, amount) " +
                                                               "VALUES (?, NOW(), ?)";

    TransactionDAOImpl() {
    }

    TransactionDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    public List<Transaction> takePlayerTransactions(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PLAYER_ID)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildTransactionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking transactions. " + e);
        }
    }

    public List<Transaction> takePlayerTransactions(int playerId, String monthPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PLAYER_LIKE_MONTH)) {
            statement.setInt(1, playerId);
            statement.setString(2, monthPattern);
            ResultSet resultSet = statement.executeQuery();
            return buildTransactionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking transactions. " + e);
        }
    }

    public List<Transaction> takeTransactionList(String monthPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_LIKE_MONTH)) {
            statement.setString(1, monthPattern);
            ResultSet resultSet = statement.executeQuery();
            return buildTransactionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking transactions. " + e);
        }
    }

    public int insertTransaction(int playerId, BigDecimal amount, Transaction.TransactionType type) throws DAOException {
        if (type == Transaction.TransactionType.WITHDRAW) {
            amount = amount.negate();
        }
        try (PreparedStatement statement = connection.prepareStatement(SQL_TRANSACTION_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, playerId);
            statement.setBigDecimal(2, amount);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting transaction. " + e);
        }
    }

    private Transaction buildTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = null;
        if (resultSet.next()) {
            transaction = new Transaction();
            transaction.setId(resultSet.getInt("id"));
            transaction.setPlayerId(resultSet.getInt("player_id"));
            transaction.setDate(resultSet.getTimestamp("date").toLocalDateTime());
            BigDecimal amount = resultSet.getBigDecimal("amount");
            transaction.setAmount(amount.abs());
            if (amount.signum() == -1) {
                transaction.setType(Transaction.TransactionType.WITHDRAW);
            } else {
                transaction.setType(Transaction.TransactionType.REPLENISH);
            }
        }
        return transaction;
    }

    private List<Transaction> buildTransactionList(ResultSet resultSet) throws SQLException {
        List<Transaction> transactionList = new ArrayList<>();
        Transaction       transaction;
        do {
            transaction = buildTransaction(resultSet);
            if (transaction != null) {
                transactionList.add(transaction);
            }
        } while (transaction != null);
        return !transactionList.isEmpty() ? transactionList : null;
    }


}