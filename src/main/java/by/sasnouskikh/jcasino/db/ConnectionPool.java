package by.sasnouskikh.jcasino.db;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServlet;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class provides singleton container for {@link WrappedConnection} objects.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    /**
     * Database property-file path relative to Maven directory 'resources'.
     */
    private static final String DB_PROPERTIES = "database";
    /**
     * Database property-file keys.
     */
    private static final String JDBC_URL      = "url";
    private static final String DB_LOGIN      = "user";
    private static final String DB_PASSWORD   = "password";
    private static final String POOL_SIZE     = "poolsize";
    /**
     * Class singleton instance.
     */
    private static ConnectionPool instance;
    /**
     * Marker to check if {@link #instance} is created.
     */
    private static AtomicBoolean created = new AtomicBoolean(false);
    /**
     * Class lock for {@link #getInstance()} method.
     */
    private static ReentrantLock lock    = new ReentrantLock();
    /**
     * Size of {@link #connections} to initialize and destroy pool.
     */
    private static int poolSize;

    /**
     * Collection of {@link java.sql.Connection} objects.
     */
    private ArrayBlockingQueue<WrappedConnection> connections;

    /**
     * Registers MySQL JDBC driver while constructing {@link #instance}.
     *
     * @throws RuntimeException if {@link SQLException} occurred during registering driver
     * @see DriverManager#registerDriver(Driver)
     * @see com.mysql.cj.jdbc.Driver
     */
    private ConnectionPool() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            LOGGER.log(Level.FATAL, e + " DriverManager wasn't found.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Singleton getter. Locks for first access.
     *
     * @return {@link #instance}
     */
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

    /**
     * Initializes pool due to given config data. Calls at {@link HttpServlet#init()} or
     * {@link javax.servlet.ServletContextListener#contextInitialized(ServletContextEvent)}.
     *
     * @throws ConnectionPoolException if {@link InterruptedException} occurred while putting {@link WrappedConnection}
     *                                 to {@link #connections}
     * @see ResourceBundle
     */
    public void initPool(String properties) throws ConnectionPoolException {
        ResourceBundle resourceBundle;
        String         url;
        String         login;
        String         password;
        try {
            resourceBundle = ResourceBundle.getBundle(properties);
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ERROR, "Invalid resource path to database *.properties file");
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
    }

    /**
     * Initializes pool due to default config data. Calls at {@link HttpServlet#init()} or
     * {@link javax.servlet.ServletContextListener#contextInitialized(ServletContextEvent)}.
     *
     * @throws ConnectionPoolException if {@link InterruptedException} occurred while putting {@link WrappedConnection}
     *                                 to {@link #connections}
     * @see ResourceBundle
     * @see #initPool(String)
     */
    public void initPool() throws ConnectionPoolException {
        initPool(DB_PROPERTIES);
    }

    /**
     * Takes {@link WrappedConnection} from {@link #connections} collection.
     *
     * @return taken {@link WrappedConnection}
     * @throws ConnectionPoolException if {@link InterruptedException} occurred while taking {@link WrappedConnection}
     *                                 from {@link #connections}
     */
    public WrappedConnection takeConnection() throws ConnectionPoolException {
        WrappedConnection connection;
        try {
            connection = connections.take();
        } catch (InterruptedException e) {
            throw new ConnectionPoolException("Taking-connection process was interrupted.", e);
        }
        return connection;
    }

    /**
     * Returns {@link WrappedConnection} to {@link #connections} collection. Rollbacks any transaction and sets
     * auto-commit of connection to true.
     *
     * @param connection {@link WrappedConnection} to return
     * @throws ConnectionPoolException if {@link InterruptedException} occurred while putting {@link WrappedConnection}
     *                                 to {@link #connections} or if {@link WrappedConnection} was lost, closed or
     *                                 damaged
     */
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
                throw new ConnectionPoolException("Exception while setting autoCommit to connection.");
            } catch (InterruptedException e) {
                throw new ConnectionPoolException("Putting connection back into pool was interrupted.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database access error occurred.", e);
        }
    }

    /**
     * Destroys pool. Calls at {@link HttpServlet#destroy()} or
     * {@link javax.servlet.ServletContextListener#contextDestroyed(ServletContextEvent)}.
     *
     * @see WrappedConnection
     * @see DriverManager
     */
    public void destroyPool() {
        for (int i = 0; i < poolSize; i++) {
            try {
                WrappedConnection connection = connections.poll(10, TimeUnit.SECONDS);
                if (connection != null) {
                    connection.closeConnection();
                }
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
        } finally {
            instance = null;
            created.getAndSet(false);
        }
    }
}