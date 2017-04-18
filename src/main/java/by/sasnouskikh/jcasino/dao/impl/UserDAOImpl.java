package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class provides {@link UserDAO} implementation for MySQL database.
 *
 * @author Sasnouskikh Aliaksandr
 */
class UserDAOImpl extends UserDAO {

    /**
     * Checks if user with definite e-mail exists by selecting it.
     */
    private static final String SQL_DEFINE_EMAIL_EXISTS = "SELECT email " +
                                                          "FROM user " +
                                                          "WHERE email=?";

    /**
     * Select user with definite e-mail and password encrypted by MD5 encryptor.
     */
    private static final String SQL_AUTH = "SELECT id, email, role, registration_date " +
                                           "FROM user " +
                                           "WHERE email=? AND password_md5=?";

    /**
     * Select user by its id.
     */
    private static final String SQL_SELECT_BY_ID    = "SELECT id, email, role, registration_date " +
                                                      "FROM user " +
                                                      "WHERE id=?";
    /**
     * Select user by its e-mail.
     */
    private static final String SQL_SELECT_BY_EMAIL = "SELECT id, email, role, registration_date " +
                                                      "FROM user " +
                                                      "WHERE email=?";
    /**
     * Select password encrypted by MD5 encryptor by its user id.
     */
    private static final String SQL_SELECT_PASSWORD = "SELECT password_md5 " +
                                                      "FROM user " +
                                                      "WHERE id=?";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    UserDAOImpl() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    UserDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Checks if user with definite e-mail exists.
     *
     * @param email e-mail to check if its user exists
     * @return true if user with definite e-mail exists
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public boolean checkEmailExist(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_EMAIL_EXISTS)) {
            statement.setString(1, email);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            throw new DAOException("Database connection error while checking is e-mail exists. " + e);
        }
    }

    /**
     * Takes {@link JCasinoUser} by its e-mail and password encrypted by MD5 encryptor.
     *
     * @param email    user e-mail
     * @param password user password encrypted by MD5 encryptor
     * @return taken {@link JCasinoUser} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildUser(ResultSet)
     */
    @Override
    public JCasinoUser authorizeUser(String email, String password) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_AUTH)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return buildUser(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while authorizing user. " + e);
        }
    }

    /**
     * Takes {@link JCasinoUser} by its id.
     *
     * @param id user id
     * @return taken {@link JCasinoUser} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildUser(ResultSet)
     */
    @Override
    public JCasinoUser takeUser(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildUser(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error. " + e);
        }
    }

    /**
     * Takes {@link JCasinoUser} by its e-mail.
     *
     * @param email user e-mail
     * @return taken {@link JCasinoUser} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildUser(ResultSet)
     */
    @Override
    public JCasinoUser takeUser(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return buildUser(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error. " + e);
        }
    }

    /**
     * Checks if definite {@link JCasinoUser} password matches to given password. Passwords are encrypted by MD5
     * encryptor.
     *
     * @param id       user id
     * @param password password encrypted by MD5 encryptor to match
     * @return true if passwords match
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public boolean checkPassword(int id, String password) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PASSWORD)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && password.equals(resultSet.getString(PASSWORD_MD5));
        } catch (SQLException e) {
            throw new DAOException("Database connection error while checking password. " + e);
        }
    }

    /**
     * Builds {@link JCasinoUser} object by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link JCasinoUser} object or null
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    private JCasinoUser buildUser(ResultSet resultSet) throws SQLException {
        JCasinoUser user = null;
        if (resultSet.next()) {
            user = new JCasinoUser();
            user.setId(resultSet.getInt(ID));
            user.setEmail(resultSet.getString(EMAIL));
            user.setRole(JCasinoUser.UserRole.valueOf(resultSet.getString(ROLE).toUpperCase()));
            user.setRegistrationDate(resultSet.getDate(REGISTRATION_DATE).toLocalDate());
        }
        return user;
    }
}