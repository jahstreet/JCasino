package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.LoanDAO;
import by.sasnouskikh.jcasino.dao.NewsDAO;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.QuestionDAO;
import by.sasnouskikh.jcasino.dao.StreakDAO;
import by.sasnouskikh.jcasino.dao.TransactionDAO;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * The class provides manager for DAO layer classes.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AutoCloseable
 */
public class DAOHelper implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger(DAOHelper.class);

    /**
     * Field used to connect to database and do queries.
     *
     * @see WrappedConnection
     */
    private WrappedConnection connection;

    /**
     * Constructs DAOHelper object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see ConnectionPool
     */
    public DAOHelper() {
        try {
            connection = ConnectionPool.getInstance().takeConnection();
        } catch (ConnectionPoolException e) {
            LOGGER.log(Level.ERROR, "Database connection error.");
        }
    }

    /**
     * Constructs DAOHelper object by assigning 'connection' field definite {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to 'connection' field
     */
    public DAOHelper(WrappedConnection connection) {
        this.connection = connection;
    }

    /**
     * Simple factory method to create {@link PlayerDAO} object.
     *
     * @return {@link PlayerDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public PlayerDAO getPlayerDAO() {
        return new PlayerDAOImpl(connection);
    }

    /**
     * Simple factory method to create {@link UserDAO} object.
     *
     * @return {@link UserDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public UserDAO getUserDAO() {
        return new UserDAOImpl(connection);
    }

    /**
     * Simple factory method to create {@link NewsDAO} object.
     *
     * @return {@link NewsDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public NewsDAO getNewsDAO() {
        return new NewsDAOImpl(connection);
    }

    /**
     * Simple factory method to create {@link TransactionDAO} object.
     *
     * @return {@link TransactionDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public TransactionDAO getTransactionDAO() {
        return new TransactionDAOImpl(connection);
    }

    /**
     * Simple factory method to create {@link StreakDAO} object.
     *
     * @return {@link StreakDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public StreakDAO getStreakDAO() {
        return new StreakDAOImpl(connection);
    }

    /**
     * Simple factory method to create {@link LoanDAO} object.
     *
     * @return {@link LoanDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public LoanDAO getLoanDAO() {
        return new LoanDAOImpl(connection);
    }

    /**
     * Simple factory method to create {@link QuestionDAO} object.
     *
     * @return {@link QuestionDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public QuestionDAO getQuestionDAO() {
        return new QuestionDAOImpl(connection);
    }

    /**
     * Starts transaction for multiple SQL queries.
     *
     * @throws SQLException if a database access error occurs,
     *                      setAutoCommit(true) is called while participating in a distributed transaction,
     *                      or this method is called on a closed connection
     * @see WrappedConnection#setAutoCommit(boolean)
     */
    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * Commits database changes made during transaction.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called while participating in a distributed transaction,
     *                      if this method is called on a closed connection or this
     *                      <code>Connection</code> object is in auto-commit mode
     * @see WrappedConnection#commit()
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Rollbacks database changes made during transaction.
     *
     * @throws SQLException if a database access error occurs,
     *                      this method is called while participating in a distributed transaction,
     *                      this method is called on a closed connection or this
     *                      <code>Connection</code> object is in auto-commit mode
     * @see WrappedConnection#rollback()
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Returns {@link #connection} to {@link ConnectionPool}.
     *
     * @throws ConnectionPoolException if if {@link InterruptedException} occurred while putting
     *                                 {@link WrappedConnection} to {@link ConnectionPool#connections} or
     *                                 if {@link WrappedConnection} was lost, closed or damaged
     */
    @Override
    public void close() throws ConnectionPoolException {
        ConnectionPool.getInstance().returnConnection(connection);
        connection = null;
    }
}