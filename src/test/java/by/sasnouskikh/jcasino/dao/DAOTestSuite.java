package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;
import java.sql.SQLException;

@RunWith(Suite.class)
@Suite.SuiteClasses({LoanDAOTest.class, NewsDAOTest.class, PlayerDAOTest.class, QuestionDAOTest.class,
                     StreakDAOTest.class, TransactionDAOTest.class, UserDAOTest.class})
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
            AbstractDAOTest.truncateAll(conn);
            AbstractDAOTest.resetAutoIncrement(conn);
        }
    }
}