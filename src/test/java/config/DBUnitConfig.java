package config;

import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DBUnitConfig {
    private static final Logger LOGGER = LogManager.getLogger(DBUnitConfig.class);

    private static final String DB_PROPERTIES = "config/database.properties";
    private static final String KEY_URL       = "url";
    private static final String KEY_USER      = "user";
    private static final String KEY_PASSWORD  = "password";
    private static final String SCHEMA_NAME   = "jcasino";

    protected IDatabaseConnection connection;
    protected IDataSet            beforeData;
    protected DAOHelper           daoHelper;

    public DBUnitConfig() {
        Properties prop = new Properties();
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(DB_PROPERTIES));
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            Connection conn = DriverManager.getConnection(prop.getProperty(KEY_URL),
                                                          prop.getProperty(KEY_USER),
                                                          prop.getProperty(KEY_PASSWORD));
            connection = new DatabaseConnection(conn, SCHEMA_NAME);
            DatabaseConfig config = connection.getConfig();
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
            config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
        } catch (IOException | SQLException | DatabaseUnitException e) {
            LOGGER.log(Level.FATAL, "Test case initialization error.");
            throw new RuntimeException();
        }
    }

    @BeforeClass
    public static void init() throws ConnectionPoolException {
        ConnectionPool.getInstance().initPool();
    }

    @AfterClass
    public static void destroy() {
        ConnectionPool.getInstance().destroyPool();
    }

    @Before
    public void setUp() throws Exception {
        daoHelper = new DAOHelper();
    }

    @After
    public void tearDown() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(connection, beforeData);
        resetAutoIncrement();
        daoHelper.close();
    }

    protected IDataSet buildDataSet(String path) throws DataSetException {
        return new FlatXmlDataSetBuilder().build(getClass().getClassLoader().getResourceAsStream(path));
    }

    private void resetAutoIncrement() throws SQLException {
        CallableStatement statement = connection.getConnection().prepareCall("CALL reset_all_autoincrement()");
        statement.executeUpdate();
        statement.close();
    }
}