package by.sasnouskikh.jcasino.db;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger LOGGER        = LogManager.getLogger(ConnectionPool.class);
    private static final String DB_PROPERTIES = "database";
    private static final String JDBC_URL      = "url";
    private static final String DB_LOGIN      = "user";
    private static final String DB_PASSWORD   = "password";
    private static final String POOL_SIZE     = "poolsize";
    private static ConnectionPool instance;
    private static AtomicBoolean created = new AtomicBoolean(false);
    private static ReentrantLock lock    = new ReentrantLock();
    private static int poolSize;

    private ArrayBlockingQueue<WrappedConnection> connections;

    private ConnectionPool() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            LOGGER.log(Level.FATAL, e + " DriverManager wasn't found.");
            throw new RuntimeException(e);
        }
    }

    public static ConnectionPool getInstance() {
        if (!created.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                    created.getAndSet(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public void initPool() throws ConnectionPoolException {
        ResourceBundle resourceBundle;
        String         url;
        String         login;
        String         password;
        try {
            resourceBundle = ResourceBundle.getBundle(DB_PROPERTIES);
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ERROR, "Invalid resource path to database.properties");
            throw new RuntimeException();
        }
        poolSize = Integer.parseInt(resourceBundle.getString(POOL_SIZE));
        connections = new ArrayBlockingQueue<>(poolSize);
        url = resourceBundle.getString(JDBC_URL);
        login = resourceBundle.getString(DB_LOGIN);
        password = resourceBundle.getString(DB_PASSWORD);
        for (int i = 0; i < poolSize; i++) {
            try {
                WrappedConnection connection = new WrappedConnection(url, login, password);
                connections.put(connection);
            } catch (InterruptedException e) {
                throw new ConnectionPoolException("ConnectionPool initializing was interrupted.", e);
            }
        }
        //TODO если не создалось нужное количество - попробовать досоздавать
    }

    public WrappedConnection takeConnection() throws ConnectionPoolException {
        WrappedConnection connection;
        try {
            connection = connections.take();
        } catch (InterruptedException e) {
            throw new ConnectionPoolException("Taking-connection process was interrupted.", e);
        }
        return connection;
    }

    public void returnConnection(WrappedConnection connection) throws ConnectionPoolException {
        try {
            if (connection.isNull() || connection.isClosed()) {
                throw new ConnectionPoolException("Connection was lost while returning.");
            }
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
                connections.put(connection);
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, "Exception while setting autoCommit.", e);
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, "Putting connection back into pool was interrupted.", e);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database access error occurred.", e);
        }
    }

    public void destroyPool() {
        //TODO blabla
        for (int i = 0; i < poolSize; i++) {
            try {
                WrappedConnection connection = connections.take(); //TODO poll timeout
                connection.closeConnection();
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, "Database access error occurred.", e);
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, "Taking connection from deque to close and destroy pool was interrupted.", e);
            }
        }
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, e + " DriverManager wasn't found.");
        }
    }
}