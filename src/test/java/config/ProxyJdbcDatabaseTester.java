package config;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;

public class ProxyJdbcDatabaseTester extends JdbcDatabaseTester {

    /**
     * Creates a new JdbcDatabaseTester with the specified properties.
     *
     * @param driverClass   the classname of the JDBC driver to use
     * @param connectionUrl the connection url
     * @param username      a username that can has access to the database
     * @param password      the user's password
     * @throws ClassNotFoundException If the given <code>driverClass</code> was not found
     */
    public ProxyJdbcDatabaseTester(String driverClass, String connectionUrl, String username, String password) throws ClassNotFoundException {
        super(driverClass, connectionUrl, username, password);
    }

    /**
     * Creates a new JdbcDatabaseTester with the specified properties.
     *
     * @param driverClass   the classname of the JDBC driver to use
     * @param connectionUrl the connection url
     * @param username      a username that can has access to the database - can be <code>null</code>
     * @param password      the user's password - can be <code>null</code>
     * @param schema        the database schema to be tested - can be <code>null</code>
     * @throws ClassNotFoundException If the given <code>driverClass</code> was not found
     * @since 2.4.3
     */
    public ProxyJdbcDatabaseTester(String driverClass, String connectionUrl, String username, String password, String schema) throws ClassNotFoundException {
        super(driverClass, connectionUrl, username, password, schema);
    }

    @Override
    public IDatabaseConnection getConnection() throws Exception {
        IDatabaseConnection connection = super.getConnection();
        connection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
        return connection;
    }
}