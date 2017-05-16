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
     * LoanDAO instance.
     */
    private LoanDAO loanDAO;

    /**
     * NewsDAO instance.
     */
    private NewsDAO newsDAO;

    /**
     * PlayerDAO instance.
     */
    private PlayerDAO playerDAO;

    /**
     * QuestionDAO instance.
     */
    private QuestionDAO questionDAO;

    /**
     * StreakDAO instance.
     */
    private StreakDAO streakDAO;

    /**
     * TransactionDAO instance.
     */
    private TransactionDAO transactionDAO;

    /**
     * UserDAO instance.
     */
    private UserDAO userDAO;

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
        if (playerDAO == null) {
            playerDAO = new PlayerDAOImpl(connection);
        }
        return playerDAO;
    }

    /**
     * Simple factory method to create {@link UserDAO} object.
     *
     * @return {@link UserDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAOImpl(connection);
        }
        return userDAO;
    }

    /**
     * Getter of {@link #newsDAO} object with lazy initialization.
     *
     * @return {@link NewsDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public NewsDAO getNewsDAO() {
        if (newsDAO == null) {
            newsDAO = new NewsDAOImpl(connection);
        }
        return newsDAO;
    }

    /**
     * Getter of {@link #transactionDAO} object with lazy initialization.
     *
     * @return {@link TransactionDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public TransactionDAO getTransactionDAO() {
        if (transactionDAO == null) {
            transactionDAO = new TransactionDAOImpl(connection);
        }
        return transactionDAO;
    }

    /**
     * Getter of {@link #streakDAO} object with lazy initialization.
     *
     * @return {@link StreakDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public StreakDAO getStreakDAO() {
        if (streakDAO == null) {
            streakDAO = new StreakDAOImpl(connection);
        }
        return streakDAO;
    }

    /**
     * Getter of {@link #loanDAO} object with lazy initialization.
     *
     * @return {@link LoanDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public LoanDAO getLoanDAO() {
        if (loanDAO == null) {
            loanDAO = new LoanDAOImpl(connection);
        }
        return loanDAO;
    }

    /**
     * Getter of {@link #questionDAO} object with lazy initialization.
     *
     * @return {@link QuestionDAO} object initialized with {@link DAOHelper#connection} field.
     * @see by.sasnouskikh.jcasino.dao.AbstractDAO#AbstractDAO(WrappedConnection)
     */
    public QuestionDAO getQuestionDAO() {
        if (questionDAO == null) {
            questionDAO = new QuestionDAOImpl(connection);
        }
        return questionDAO;
    }

    /**
     * Starts transaction for multiple SQL queries.
     *
     * @throws SQLException if a database access error occurs, setAutoCommit(true) is called while participating in a
     *                      distributed transaction, or this method is called on a closed connection
     * @see WrappedConnection#setAutoCommit(boolean)
     */
    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * Commits database changes made during transaction.
     *
     * @throws SQLException if a database access error occurs, this method is called while participating in a
     *                      distributed transaction, if this method is called on a closed connection or this
     *                      <code>Connection</code> object is in auto-commit mode
     * @see WrappedConnection#commit()
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Rollbacks database changes made during transaction.
     *
     * @throws SQLException if a database access error occurs, this method is called while participating in a
     *                      distributed transaction, this method is called on a closed connection or this
     *                      <code>Connection</code> object is in auto-commit mode
     * @see WrappedConnection#rollback()
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Returns {@link #connection} to {@link ConnectionPool}.
     */
    @Override
    public void close() {
        ConnectionPool.getInstance().returnConnection(connection);
        connection = null;
    }
}