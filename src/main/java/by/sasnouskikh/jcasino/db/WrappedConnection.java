package by.sasnouskikh.jcasino.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class provides wrapper for {@link Connection} object.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AutoCloseable
 */
public class WrappedConnection implements AutoCloseable {

    /**
     * {@link Connection} which is wrapped.
     */
    private Connection connection;

    /**
     * Constructs {@link WrappedConnection} object due to config data.
     *
     * @param url      database url
     * @param login    database user login
     * @param password database user password
     * @see DriverManager#getConnection(String, String, String)
     */
    WrappedConnection(String url, String login, String password) {
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wraps {@link #connection#getAutoCommit()} method.
     */
    boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    /**
     * Wraps {@link #connection#setAutoCommit(boolean)} method.
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    /**
     * Wraps {@link #connection#createStatement()} method.
     */
    public Statement createStatement() throws SQLException {
        if (connection != null) {
            Statement statement = connection.createStatement();
            if (statement != null) {
                return statement;
            }
        }
        throw new SQLException("Connection or statement is null.");
    }

    /**
     * Wraps {@link #connection#close()} method.
     */
    void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Wraps {@link #connection#isClosed()} method.
     */
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    /**
     * Checks if wrapped {@link #connection} is null;
     *
     * @return true if wrapped {@link #connection} is null
     */
    public boolean isNull() {
        return connection == null;
    }

    /**
     * Wraps {@link #connection#prepareStatement(String)} method.
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    /**
     * Wraps {@link #connection#prepareStatement(String, int)} method.
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * Wraps {@link #connection#commit()} method.
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Wraps {@link #connection#rollback()} method.
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Returns this {@link WrappedConnection} to {@link ConnectionPool}.
     *
     * @throws ConnectionPoolException if if {@link InterruptedException} occurred while putting
     *                                 {@link WrappedConnection} to {@link ConnectionPool#connections} or
     *                                 if {@link WrappedConnection} was lost, closed or damaged
     */
    @Override
    public void close() throws ConnectionPoolException {
        ConnectionPool.getInstance().returnConnection(this);
    }
}