package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;

import java.sql.SQLException;

/**
 * The class provides DAO abstraction for {@link JCasinoUser} objects.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AbstractDAO
 */
public abstract class UserDAO extends AbstractDAO {

    /**
     * Column names of database table 'user'.
     */
    protected static final String ID                = "id";
    protected static final String PASSWORD_MD5      = "password_md5";
    protected static final String EMAIL             = "email";
    protected static final String ROLE              = "role";
    protected static final String REGISTRATION_DATE = "registration_date";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    protected UserDAO() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    protected UserDAO(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Checks if user with definite e-mail exists.
     *
     * @param email e-mail to check if its user exists
     * @return true if user with definite e-mail exists
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean checkEmailExist(String email) throws DAOException;

    /**
     * Takes {@link JCasinoUser} by its e-mail and password encrypted by MD5 encryptor.
     *
     * @param email    user e-mail
     * @param password user password encrypted by MD5 encryptor
     * @return taken {@link JCasinoUser}
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract JCasinoUser authorizeUser(String email, String password) throws DAOException;

    /**
     * Takes {@link JCasinoUser} by its id.
     *
     * @param id user id
     * @return taken {@link JCasinoUser}
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract JCasinoUser takeUser(int id) throws DAOException;

    /**
     * Takes {@link JCasinoUser} by its e-mail.
     *
     * @param email user e-mail
     * @return taken {@link JCasinoUser} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract JCasinoUser takeUser(String email) throws DAOException;

    /**
     * Checks if definite {@link JCasinoUser} password matches to given password. Passwords are encrypted by MD5
     * encryptor.
     *
     * @param id       user id
     * @param password password encrypted by MD5 encryptor to match
     * @return true if passwords match
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean checkPassword(int id, String password) throws DAOException;
}