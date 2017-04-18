package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class provides root abstraction for DAO layer classes.
 *
 * @author Sasnouskikh Aliaksandr
 */
public abstract class AbstractDAO {
    private static final Logger LOGGER = LogManager.getLogger(AbstractDAO.class);

    /**
     * Field used to connect to database and do queries.
     *
     * @see WrappedConnection
     */
    protected WrappedConnection connection;

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     */
    protected AbstractDAO() {
        try {
            connection = ConnectionPool.getInstance().takeConnection();
        } catch (ConnectionPoolException e) {
            LOGGER.log(Level.ERROR, "Database connection error.");
        }
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     */
    protected AbstractDAO(WrappedConnection connection) {
        this.connection = connection;
    }

    /**
     * {@link AbstractDAO#connection} field getter.
     *
     * @return {@link #connection}
     */
    protected WrappedConnection getConnection() {
        return connection;
    }

    /**
     * {@link AbstractDAO#connection} field setter.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     */
    protected void setConnection(WrappedConnection connection) {
        this.connection = connection;
    }
}