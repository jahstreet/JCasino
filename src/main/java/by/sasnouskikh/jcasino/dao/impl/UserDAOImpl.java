package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl extends AbstractDAO<Integer, JCasinoUser> {

    private static final String SQL_DEFINE_EMAIL_EXISTS = "SELECT email " +
                                                          "FROM user " +
                                                          "WHERE email=?";

    private static final String SQL_AUTH = "SELECT id, email, role, registration_date " +
                                           "FROM user " +
                                           "WHERE email=? AND password_md5=?";

    private static final String SQL_SELECT_BY_ID    = "SELECT id, email, role, registration_date " +
                                                      "FROM user " +
                                                      "WHERE id=?";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT id, email, role, registration_date " +
                                                      "FROM user " +
                                                      "WHERE email=?";
    private static final String SQL_SELECT_PASSWORD = "SELECT password_md5 " +
                                                      "FROM user " +
                                                      "WHERE id=?";

    UserDAOImpl() {
    }

    UserDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    public boolean checkEmailExist(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_EMAIL_EXISTS)) {
            statement.setString(1, email);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            throw new DAOException("Database connection error while checking is e-mail exists. " + e);
        }
    }

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

    public JCasinoUser takeUser(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildUser(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error. " + e);
        }
    }

    public JCasinoUser takeUser(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return buildUser(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error. " + e);
        }
    }

    public boolean checkPassword(int id, String password) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PASSWORD)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && password.equals(resultSet.getString("password_md5"));
        } catch (SQLException e) {
            throw new DAOException("Database connection error while checking password. " + e);
        }
    }

    private JCasinoUser buildUser(ResultSet resultSet) throws SQLException {
        JCasinoUser user = null;
        if (resultSet.next()) {
            user = new JCasinoUser();
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setRole(JCasinoUser.UserRole.valueOf(resultSet.getString("role").toUpperCase()));
            user.setRegistrationDate(resultSet.getDate("registration_date").toLocalDate());
        }
        return user;
    }
}