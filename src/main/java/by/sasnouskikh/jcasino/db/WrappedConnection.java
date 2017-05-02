package by.sasnouskikh.jcasino.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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

    public Connection getConnection() {
        return connection;
    }

    /**
     * Wraps {@link Connection#getAutoCommit()} method.
     *
     * @return the current state of wrapped <code>Connection</code> object's auto-commit mode
     * @throws SQLException if a database access error occurs wrapped connection is closed
     */
    boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    /**
     * Wraps {@link Connection#setAutoCommit(boolean)} method.
     *
     * @param autoCommit auto-commit state of connection
     * @throws SQLException if a database access error occurs, setAutoCommit(true) is called while participating in a
     *                      distributed transaction, wrapped connection is closed
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    /**
     * Wraps {@link Connection#createStatement()} method.
     *
     * @return a new default <code>Statement</code> object
     * @throws SQLException if a database access error occurs or wrapped connection is closed
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
     * Wraps {@link Connection#close()} method.
     *
     * @throws SQLException if a database access error occurs
     */
    void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Wraps {@link Connection#isClosed()} method.
     *
     * @return <code>true</code> if wrapped <code>Connection</code> object is closed; <code>false</code> if it is still
     * open
     * @throws SQLException if a database access error occurs
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
     * Returns true if the connection has not been closed and is still valid.
     * The driver shall submit a query on the connection or use some other
     * mechanism that positively verifies the connection is still valid when
     * this method is called.
     *
     * @param timeout The time in seconds to wait for the database operation used to validate the connection to
     *                complete.  If the timeout period expires before the operation completes, this method returns
     *                false.  A value of 0 indicates a timeout is not applied to the database operation.
     * @return true if the connection is valid, false otherwise
     * @throws SQLException if the value supplied for <code>timeout</code> is less then 0
     * @see DatabaseMetaData#getClientInfoProperties
     * @since 1.6
     */
    public boolean isValid(int timeout) throws SQLException {
        return connection.isValid(timeout);
    }

    /**
     * Wraps {@link Connection#prepareStatement(String)} method.
     *
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @return a new default <code>PreparedStatement</code> object containing the pre-compiled SQL statement
     * @throws SQLException if a database access error occurs wrapped connection is closed
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    /**
     * Wraps {@link Connection#prepareStatement(String, int)} method.
     *
     * @param sql               an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param autoGeneratedKeys a flag indicating whether auto-generated keys should be returned; one of
     *                          <code>Statement.RETURN_GENERATED_KEYS</code> or <code>Statement.NO_GENERATED_KEYS</code>
     * @return a new <code>PreparedStatement</code> object, containing the pre-compiled SQL statement, that will have
     * the capability of returning auto-generated keys
     * @throws SQLException                    if a database access error occurs, wrapped connection is closed or the
     *                                         given parameter is not a <code>Statement</code> constant indicating
     *                                         whether auto-generated keys should be returned
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method with a constant of
     *                                         Statement.RETURN_GENERATED_KEYS
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * Creates a <code>CallableStatement</code> object for calling
     * database stored procedures.
     * The <code>CallableStatement</code> object provides
     * methods for setting up its IN and OUT parameters, and
     * methods for executing the call to a stored procedure.
     *
     * @param sql an SQL statement that may contain one or more '?' parameter placeholders. Typically this statement is
     *            specified using JDBC call escape syntax.
     * @return a new default <code>CallableStatement</code> object containing the pre-compiled SQL statement
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    /**
     * Wraps {@link Connection#commit()} method.
     *
     * @throws SQLException if a database access error occurs, this method is called while participating in a
     *                      distributed transaction, wrapped connection is closed or wrapped <code>Connection</code>
     *                      object is in auto-commit mode
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Wraps {@link Connection#rollback()} method.
     *
     * @throws SQLException if a database access error occurs, this method is called while participating in a
     *                      distributed transaction, wrapped connection is closed or wrapped <code>Connection</code>
     *                      object is in auto-commit mode
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Returns this {@link WrappedConnection} to {@link ConnectionPool}.
     *
     * @throws ConnectionPoolException if if {@link InterruptedException} occurred while putting {@link
     *                                 WrappedConnection} to {@link ConnectionPool#connections} or if {@link
     *                                 WrappedConnection} was lost, closed or damaged
     */
    @Override
    public void close() throws ConnectionPoolException {
        ConnectionPool.getInstance().returnConnection(this);
    }
}