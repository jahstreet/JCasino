package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.TransactionDAO;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import com.mysql.cj.api.jdbc.Statement;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class provides {@link TransactionDAO} implementation for MySQL database.
 *
 * @author Sasnouskikh Aliaksandr
 */
class TransactionDAOImpl extends TransactionDAO {

    /**
     * Selects definite player transactions and orders them by date in descending order.
     */
    private static final String SQL_SELECT_BY_PLAYER_ID      = "SELECT id, player_id, date, amount " +
                                                               "FROM transaction " +
                                                               "WHERE player_id=? " +
                                                               "ORDER BY date DESC";
    /**
     * Selects definite player transactions where date is like definite pattern and orders them by date
     * in descending order.
     */
    private static final String SQL_SELECT_PLAYER_LIKE_MONTH = "SELECT id, player_id, date, amount " +
                                                               "FROM transaction " +
                                                               "WHERE player_id=? AND date LIKE ? " +
                                                               "ORDER BY date DESC";
    /**
     * Selects transactions where date is like definite pattern and orders them by date in descending order.
     */
    private static final String SQL_SELECT_LIKE_MONTH        = "SELECT id, player_id, date, amount " +
                                                               "FROM transaction " +
                                                               "WHERE date LIKE ? " +
                                                               "ORDER BY date DESC";
    /**
     * Inserts transaction to database.
     */
    private static final String SQL_TRANSACTION_INSERT       = "INSERT INTO transaction (player_id, date, amount) " +
                                                               "VALUES (?, NOW(), ?)";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    TransactionDAOImpl() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    TransactionDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes {@link List} filled by definite player {@link Transaction} objects.
     *
     * @param playerId id of player whose transactions to take
     * @return {@link List} filled by definite player {@link Transaction} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildTransactionList(ResultSet)
     */
    @Override
    public List<Transaction> takePlayerTransactions(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PLAYER_ID)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildTransactionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking transactions. " + e);
        }
    }

    /**
     * Takes {@link List} filled by definite player {@link Transaction} objects due to definite transaction date
     * pattern.
     *
     * @param playerId     id of player whose transactions to take
     * @param monthPattern pattern of transaction date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by definite player {@link Transaction} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildTransactionList(ResultSet)
     */
    @Override
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

    /**
     * Takes {@link List} filled by {@link Transaction} objects due to definite transaction date pattern.
     *
     * @param monthPattern pattern of transaction date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by {@link Transaction} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildTransactionList(ResultSet)
     */
    @Override
    public List<Transaction> takeTransactionList(String monthPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_LIKE_MONTH)) {
            statement.setString(1, monthPattern);
            ResultSet resultSet = statement.executeQuery();
            return buildTransactionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking transactions. " + e);
        }
    }

    /**
     * Inserts {@link Transaction} to database.
     *
     * @param playerId id of player who does transaction
     * @param amount   amount of money transferred by transaction
     * @param type     {@link by.sasnouskikh.jcasino.entity.bean.Transaction.TransactionType} of inserting transaction
     * @return int value of inserted transaction generated id or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String, int)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
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

    /**
     * Builds {@link Transaction} object by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link Transaction} object or null
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called
     *                      on a closed result set
     */
    private Transaction buildTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = null;
        if (resultSet.next()) {
            transaction = new Transaction();
            transaction.setId(resultSet.getInt(ID));
            transaction.setPlayerId(resultSet.getInt(PLAYER_ID));
            transaction.setDate(resultSet.getTimestamp(DATE).toLocalDateTime());
            BigDecimal amount = resultSet.getBigDecimal(AMOUNT);
            transaction.setType(amount.signum() == -1 ?
                                Transaction.TransactionType.WITHDRAW :
                                Transaction.TransactionType.REPLENISH);
            transaction.setAmount(amount.abs());
        }
        return transaction;
    }

    /**
     * Builds {@link List} object filled by {@link Transaction} objects by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link List} object or null
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called
     *                      on a closed result set
     * @see #buildTransaction(ResultSet)
     */
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