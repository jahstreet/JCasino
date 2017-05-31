package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.WrappedConnection;
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

public abstract class AbstractDAOTest {

    private static final String DB_PROPERTIES          = "config/database.properties";
    private static final String KEY_URL                = "url";
    private static final String KEY_USER               = "user";
    private static final String KEY_PASSWORD           = "password";
    private static final String SCHEMA_NAME            = "jcasino";
    private static final String XML_PLAYER_STATUS_DATA = "by/sasnouskikh/jcasino/dao/player_status_data.xml";

    static         IDatabaseConnection   connection;
    static         IDataSet              beforeClassData;
    private static FlatXmlDataSetBuilder builder;

    IDataSet  beforeData;
    DAOHelper daoHelper;

    @BeforeClass
    public static void init() throws SQLException, IOException, DatabaseUnitException {
        Properties prop = new Properties();
        prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(DB_PROPERTIES));
        Connection conn = DriverManager.getConnection(prop.getProperty(KEY_URL),
                                                      prop.getProperty(KEY_USER),
                                                      prop.getProperty(KEY_PASSWORD));
        connection = new DatabaseConnection(conn, SCHEMA_NAME);

        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
        config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
        config.setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN, "`");

        builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);

        beforeClassData = buildDataSet(XML_PLAYER_STATUS_DATA);
        DatabaseOperation.CLEAN_INSERT.execute(connection, beforeClassData);
    }

    @AfterClass
    public static void destroy() throws SQLException, DatabaseUnitException {
        DatabaseOperation.DELETE_ALL.execute(connection, beforeClassData);
        connection.close();
        beforeClassData = null;
        builder = null;
        connection = null;
    }

    static IDataSet buildDataSet(String path) throws DataSetException {
        return builder.build(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
    }

    static void resetAutoIncrement(Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("CALL reset_all_autoincrement()");
        statement.executeUpdate();
        statement.close();
    }

    static void resetAutoIncrement(WrappedConnection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("CALL reset_all_autoincrement()");
        statement.executeUpdate();
        statement.close();
    }

    static void truncateAll(Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("CALL truncate_all()");
        statement.executeUpdate();
        statement.close();
    }

    static void truncateAll(WrappedConnection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("CALL truncate_all()");
        statement.executeUpdate();
        statement.close();
    }

    @Before
    public void setUp() throws Exception {
        daoHelper = new DAOHelper();
    }

    @After
    public void tearDown() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(connection, beforeData);
        resetAutoIncrement(connection.getConnection());
        daoHelper.close();
    }
}