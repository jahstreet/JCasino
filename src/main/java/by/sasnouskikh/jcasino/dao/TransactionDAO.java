package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * The class provides DAO abstraction for {@link Transaction} objects.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AbstractDAO
 */
public abstract class TransactionDAO extends AbstractDAO {

    /**
     * Column names of database table 'transaction'.
     */
    protected static final String ID        = "id";
    protected static final String PLAYER_ID = "player_id";
    protected static final String DATE      = "date";
    protected static final String AMOUNT    = "amount";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    protected TransactionDAO() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    protected TransactionDAO(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes {@link List} filled by definite player {@link Transaction} objects.
     *
     * @param playerId id of player whose transactions to take
     * @return {@link List} filled by definite player {@link Transaction} objects
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Transaction> takePlayerTransactions(int playerId) throws DAOException;

    /**
     * Takes {@link List} filled by definite player {@link Transaction} objects due to definite transaction date
     * pattern.
     *
     * @param playerId     id of player whose transactions to take
     * @param monthPattern pattern of transaction date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by definite player {@link Transaction} objects
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Transaction> takePlayerTransactions(int playerId, String monthPattern) throws DAOException;

    /**
     * Takes {@link List} filled by {@link Transaction} objects due to definite transaction date pattern.
     *
     * @param monthPattern pattern of transaction date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by {@link Transaction} objects
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Transaction> takeTransactionList(String monthPattern) throws DAOException;

    /**
     * Inserts {@link Transaction} to database.
     *
     * @param playerId id of player who does transaction
     * @param amount   amount of money transferred by transaction
     * @param type     {@link by.sasnouskikh.jcasino.entity.bean.Transaction.TransactionType} of inserting transaction
     * @return int value of inserted transaction generated id
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract int insertTransaction(int playerId, BigDecimal amount, Transaction.TransactionType type) throws DAOException;
}