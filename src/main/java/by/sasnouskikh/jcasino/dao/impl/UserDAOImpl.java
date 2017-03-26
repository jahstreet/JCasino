package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl extends AbstractDAO<Integer, JCasinoUser> {

    private static final String SQL_DEFINE_PLAYER_NAME_BY_EMAIL = "SELECT fname " +
                                                                  "FROM player NATURAL JOIN user " +
                                                                  "WHERE email=?";
    private static final String SQL_DEFINE_EMAIL_EXISTS         = "SELECT email " +
                                                                  "FROM user " +
                                                                  "WHERE email=?";

    private static final String SQL_USER_AUTH = "SELECT * " +
                                                "FROM user " +
                                                "WHERE email=? AND password_md5=?";

    private static final String SQL_USER_SELECT_BY_EMAIL = "SELECT * " +
                                                           "FROM user " +
                                                           "WHERE email=?";
    private static final String SQL_PASSWORD_SELECT      = "SELECT password_md5 FROM user WHERE id=?";

    private static final String SQL_GUEST_QUESTION_INSERT = "INSERT INTO question (email, topic, question, q_date) " +
                                                            "VALUES (?, ?, ?, NOW())";
    private static final String SQL_USER_QUESTION_INSERT  = "INSERT INTO question (player_id, email, topic, question, q_date) " +
                                                            "VALUES (?, ?, ?, ?, NOW())";

    private static final String SQL_PASSWORD_UPDATE = "UPDATE user " +
                                                      "SET password_md5=? WHERE id=?";

    public JCasinoUser takeUser(String email) throws DAOException {
        JCasinoUser user;
        try (PreparedStatement statement = connection.prepareStatement(SQL_USER_SELECT_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            user = buildUser(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error. " + e);
        }
        return user;
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

    public JCasinoUser authorizeUser(String email, String password) throws DAOException {
        JCasinoUser user;
        try (PreparedStatement statement = connection.prepareStatement(SQL_USER_AUTH)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            user = buildUser(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error. " + e);
        }
        return user;
    }

    public boolean checkPassword(int id, String password) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_PASSWORD_SELECT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return password.equals(resultSet.getString("password_md5"));
            } else {
                throw new DAOException("No user associated with given id. Database connection error.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while checking password. " + e);
        }
    }

    public boolean changePassword(int id, String password) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_PASSWORD_UPDATE)) {
            statement.setString(1, password);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 user associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing password. " + e);
        }
    }

    public boolean checkEmailExist(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_EMAIL_EXISTS)) {
            statement.setString(1, email);
            if (statement.executeQuery().next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while checking is e-mail exists. " + e);
        }
        return false;
    }

    public String definePlayerNameByEmail(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_PLAYER_NAME_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("fname");
            } else {
                throw new DAOException("No any player associated with given e-mail.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while checking is e-mail exists. " + e);
        }
    }

    public boolean insertQuestion(String email, String topic, String question) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_GUEST_QUESTION_INSERT)) {
            statement.setString(1, email);
            statement.setString(2, topic);
            statement.setString(3, question);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while sending support. " + e);
        }
    }

    public boolean insertQuestion(int id, String email, String topic, String question) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_USER_QUESTION_INSERT)) {
            statement.setInt(1, id);
            statement.setString(2, email);
            statement.setString(3, topic);
            statement.setString(4, question);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while sending support. " + e);
        }
    }
}