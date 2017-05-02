package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(Suite.class)
@Suite.SuiteClasses({PlayerDAOTest.class})//LoanDAOTest.class, NewsDAOTest.class,
public class DAOTestSuite {

    private static final String DB_PROPERTIES = "database";
    private static ConnectionPool instance;

    @BeforeClass
    public static void setUp() throws SQLException, IOException, ConnectionPoolException {
        instance = ConnectionPool.getInstance();
        instance.initPool(DB_PROPERTIES);
        resetDatabase();
    }

    @AfterClass
    public static void tearDown() throws SQLException, ConnectionPoolException {
        resetDatabase();
        instance.destroyPool();
        instance = null;
    }

    private static void resetDatabase() throws ConnectionPoolException, SQLException {
        try (WrappedConnection conn = instance.takeConnection()) {
            Connection connection = conn.getConnection();
            AbstractDAOTest.truncateAll(connection);
            AbstractDAOTest.resetAutoIncrement(connection);
        }
    }
}