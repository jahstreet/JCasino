package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.PlayerProfile;
import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.entity.bean.Transaction;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides {@link PlayerDAO} implementation for MySQL database.
 *
 * @author Sasnouskikh Aliaksandr
 */
class PlayerDAOImpl extends PlayerDAO {

    /**
     * Additional column name used in SQL queries.
     */
    private static final String BINARY_STATUS         = "bin_status";
    /**
     * Additional column name used in SQL queries.
     */
    private static final String THIS_MONTH_WITHDRAWAL = "total";

    /**
     * Selects player id by its e-mail.
     */
    private static final String SQL_DEFINE_ID_BY_EMAIL   = "SELECT id FROM user " +
                                                           "WHERE email=?";
    /**
     * Selects player e-mail by its id.
     */
    private static final String SQL_DEFINE_EMAIL_BY_ID   = "SELECT email FROM user " +
                                                           "WHERE id=?";
    /**
     * Selects player first name by its e-mail.
     */
    private static final String SQL_DEFINE_NAME_BY_EMAIL = "SELECT fname " +
                                                           "FROM player NATURAL JOIN user " +
                                                           "WHERE email=?";

    /**
     * Selects definite player profile data.
     */
    private static final String SQL_SELECT_PROFILE            = "SELECT fname, mname, lname, birthdate, passport, question " +
                                                                "FROM player " +
                                                                "WHERE id=?";
    /**
     * Selects definite player profile data.
     */
    private static final String SQL_SELECT_VERIFICATION       = "SELECT id, status AS bin_status, admin_id, commentary, date, passport " +
                                                                "FROM verification " +
                                                                "WHERE id=?";
    /**
     * Selects verification data of players who are ready to be verified by admin.
     */
    private static final String SQL_SELECT_VERIFICATION_READY = "SELECT id, status AS bin_status, admin_id, commentary, date, passport " +
                                                                "FROM verification " +
                                                                "WHERE status=0b011 AND passport IS NOT NULL";
    /**
     * Selects definite player account data.
     */
    private static final String SQL_SELECT_ACCOUNT            = "SELECT balance, status, admin_id, commentary, bet_limit, withdrawal_limit, loan_percent, max_loan_amount, " +
                                                                "IFNULL((SELECT ABS(SUM(amount)) FROM transaction " +
                                                                "WHERE player.id=player_id AND amount < 0 AND MONTH(date)=MONTH(NOW()) AND YEAR(date)=YEAR(NOW())), 0) AS total " +
                                                                "FROM player NATURAL JOIN player_status " +
                                                                "WHERE player.id=?;";
    /**
     * Selects definite player this month withdrawal amount.
     */
    private static final String SQL_SELECT_MONTH_WITHDRAWAL   = "SELECT IFNULL(ABS(SUM(amount)), 0) AS total " +
                                                                "FROM transaction " +
                                                                "WHERE player_id=? AND date LIKE ? AND amount < 0";

    /**
     * Inserts player data into 'user' table  on registration.
     */
    private static final String SQL_INSERT_USER         = "INSERT INTO user (email, password_md5, role, registration_date) " +
                                                          "VALUES (?, ?, 'player', NOW())";
    /**
     * Inserts player data into 'player' table  on registration.
     */
    private static final String SQL_INSERT_PLAYER       = "INSERT INTO player (id, fname, mname, lname, birthdate, passport, question, answer_md5) " +
                                                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    /**
     * Inserts player data into 'verification' table on registration.
     */
    private static final String SQL_INSERT_VERIFICATION = "INSERT INTO verification (id) " +
                                                          "VALUES (?)";

    /**
     * Updates definite player e-mail.
     */
    private static final String SQL_UPDATE_EMAIL                = "UPDATE user " +
                                                                  "SET email=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player password MD5 value.
     */
    private static final String SQL_UPDATE_PASSWORD             = "UPDATE user " +
                                                                  "SET password_md5=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player first name.
     */
    private static final String SQL_UPDATE_FNAME                = "UPDATE player " +
                                                                  "SET fname=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player middle name.
     */
    private static final String SQL_UPDATE_MNAME                = "UPDATE player " +
                                                                  "SET mname=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player last name.
     */
    private static final String SQL_UPDATE_LNAME                = "UPDATE player " +
                                                                  "SET lname=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player passport number.
     */
    private static final String SQL_UPDATE_PASSPORT_NUMBER      = "UPDATE player " +
                                                                  "SET passport=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player birthdate.
     */
    private static final String SQL_UPDATE_BIRTHDATE            = "UPDATE player " +
                                                                  "SET birthdate=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player secret question and its answer MD5 value.
     */
    private static final String SQL_UPDATE_SECRET_QUESTION      = "UPDATE player " +
                                                                  "SET question=?, answer_md5=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player passport scan relative path.
     *
     * @see by.sasnouskikh.jcasino.controller.ShowImageServlet
     */
    private static final String SQL_UPDATE_SCAN_PATH            = "UPDATE verification " +
                                                                  "SET passport=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player verification status binary value.
     */
    private static final String SQL_UPDATE_VER_STATUS_PLAYER    = "UPDATE verification " +
                                                                  "SET status=? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player verification status binary value by definite admin with its comments.
     */
    private static final String SQL_UPDATE_VER_STATUS_ADMIN     = "UPDATE verification " +
                                                                  "SET status=?, admin_id=?, commentary=?, date=NOW() " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player balance by adding definite value to it.
     */
    private static final String SQL_UPDATE_ACCOUNT_BALANCE      = "UPDATE player " +
                                                                  "SET balance=balance+? " +
                                                                  "WHERE id=?";
    /**
     * Updates definite player account status by definite admin with its comments.
     */
    private static final String SQL_UPDATE_ACCOUNT_STATUS_ADMIN = "UPDATE player " +
                                                                  "SET status=?, admin_id=?, commentary=? " +
                                                                  "WHERE id=?";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    PlayerDAOImpl() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    PlayerDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Defines {@link Player} id by its e-mail.
     *
     * @param email e-mail of {@link Player} whose id is defining
     * @return defined id value or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public int defineIdByEmail(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_ID_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt(ID) : 0;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while defining id by e-mail. " + e);
        }
    }

    /**
     * Defines {@link Player} e-mail by its id.
     *
     * @param id id of {@link Player} whose e-mail is defining
     * @return defined e-mail value or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public String defineEmailById(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_EMAIL_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getString(EMAIL) : null;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while defining e-mail by id. " + e);
        }
    }

    /**
     * Defines {@link Player} first name by its e-mail.
     *
     * @param email e-mail of {@link Player} whose first name is defining
     * @return defined first name value or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public String defineNameByEmail(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_NAME_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getString(FIRST_NAME) : null;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while checking is e-mail exists. " + e);
        }
    }

    /**
     * Takes {@link PlayerProfile} object of definite {@link Player}.
     *
     * @param id id of {@link Player} whose profile is taking
     * @return {@link PlayerProfile} object of definite {@link Player} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildPlayerProfile(ResultSet)
     */
    @Override
    public PlayerProfile takeProfile(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PROFILE)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildPlayerProfile(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking player profile. " + e);
        }
    }

    /**
     * Takes {@link PlayerVerification} object of definite {@link Player}.
     *
     * @param id id of {@link Player} whose verification is taking
     * @return {@link PlayerVerification} object of definite {@link Player} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildPlayerVerification(ResultSet)
     */
    @Override
    public PlayerVerification takeVerification(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_VERIFICATION)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildPlayerVerification(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking verification. " + e);
        }
    }

    /**
     * Takes {@link List} filled by {@link PlayerVerification} objects of players who are ready to be verified
     * by admin.
     *
     * @return {@link List} filled by {@link PlayerVerification} objects of players who are ready to be verified by
     * admin or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#createStatement()
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildPlayerVerificationList(ResultSet)
     */
    @Override
    public List<PlayerVerification> takeReadyForVerification() throws DAOException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_VERIFICATION_READY);
            return buildPlayerVerificationList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking ready for verification list. " + e);
        }
    }

    /**
     * Takes {@link PlayerAccount} of definite {@link Player}.
     *
     * @param id id of player whose {@link PlayerAccount} is taking
     * @return taken {@link PlayerAccount} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildPlayerAccount(ResultSet)
     */
    @Override
    public PlayerAccount takeAccount(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ACCOUNT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildPlayerAccount(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking player account. " + e);
        }
    }

    /**
     * Takes amount of money definite {@link Player} withdrawn in date due to definite pattern.
     *
     * @param playerId    id of {@link Player} whose withdrawal sum is taking
     * @param datePattern pattern of date conforming to <code>SQL LIKE</code> operator
     * @return taken given date pattern withdrawal sum or {@link BigDecimal#ZERO}
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public BigDecimal takeWithdrawalSum(int playerId, String datePattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_MONTH_WITHDRAWAL)) {
            statement.setInt(1, playerId);
            statement.setString(2, datePattern);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getBigDecimal(THIS_MONTH_WITHDRAWAL) : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking month withdrawal. " + e);
        }
    }

    /**
     * Inserts {@link Player} data into 'user' table on registration.
     *
     * @param email    email of {@link Player} whose user data is inserting
     * @param password password of {@link Player} whose user data is inserting encrypted by MD5 encryptor
     * @return id of inserted {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser} or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String, int)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public int insertUserPlayer(String email, String password) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting user player. " + e);
        }
    }

    /**
     * Inserts {@link Player} data into 'player' table on registration.
     *
     * @param id        id of {@link Player} whose data is inserting
     * @param fName     first name of {@link Player} whose data is inserting
     * @param mName     middle name of {@link Player} whose data is inserting
     * @param lName     last name of {@link Player} whose data is inserting
     * @param birthDate birthdate of {@link Player} whose data is inserting
     * @param passport  passport number of {@link Player} whose data is inserting
     * @param question  secret question of {@link Player} whose data is inserting
     * @param answer    answer to secret question of {@link Player} whose data is inserting encrypted by MD5 encryptor
     * @return true if insertion proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean insertPlayer(int id, String fName, String mName, String lName, String birthDate, String passport, String question, String answer) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_PLAYER)) {
            statement.setInt(1, id);
            statement.setString(2, fName);
            statement.setString(3, mName);
            statement.setString(4, lName);
            statement.setDate(5, Date.valueOf(birthDate));
            statement.setString(6, passport);
            statement.setString(7, question);
            statement.setString(8, answer);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting player. " + e);
        }
    }

    /**
     * Inserts {@link Player} data into 'verification' table on registration.
     *
     * @param id id of {@link Player} whose data is inserting
     * @return true if insertion proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean insertEmptyVerification(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_VERIFICATION)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting empty verification. " + e);
        }
        return true;
    }

    /**
     * Updates definite {@link Player} e-mail.
     *
     * @param id    id of {@link Player} whose data is updating
     * @param email new {@link Player} e-mail value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeEmail(int id, String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_EMAIL)) {
            statement.setString(1, email);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing email. " + e);
        }
    }

    /**
     * Updates definite {@link Player} password.
     *
     * @param id       id of {@link Player} whose data is updating
     * @param password new {@link Player} password encrypted by MD5 encryptor value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changePassword(int id, String password) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PASSWORD)) {
            statement.setString(1, password);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing password. " + e);
        }
    }

    /**
     * Updates definite {@link Player} first name.
     *
     * @param id   id of {@link Player} whose data is updating
     * @param name new {@link Player} first name value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeFirstName(int id, String name) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_FNAME)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing first name. " + e);
        }
    }

    /**
     * Updates definite {@link Player} middle name.
     *
     * @param id   id of {@link Player} whose data is updating
     * @param name new {@link Player} middle name value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeMiddleName(int id, String name) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_MNAME)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing middle name. " + e);
        }
    }

    /**
     * Updates definite {@link Player} last name.
     *
     * @param id   id of {@link Player} whose data is updating
     * @param name new {@link Player} last name value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeLastName(int id, String name) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_LNAME)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing last name. " + e);
        }
    }

    /**
     * Updates definite {@link Player} passport number.
     *
     * @param id       id of {@link Player} whose data is updating
     * @param passport new {@link Player} passport number value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changePassportNumber(int id, String passport) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PASSPORT_NUMBER)) {
            statement.setString(1, passport);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing passport. " + e);
        }
    }

    /**
     * Updates definite {@link Player} birthdate.
     *
     * @param id        id of {@link Player} whose data is updating
     * @param birthDate new {@link Player} birthdate value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeBirthdate(int id, String birthDate) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_BIRTHDATE)) {
            statement.setDate(1, Date.valueOf(birthDate));
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing birth date. " + e);
        }
    }

    /**
     * Updates definite {@link Player} secret question and its answer encrypted by MD5 encryptor.
     *
     * @param id       id of {@link Player} whose data is updating
     * @param question new {@link Player} secret question value
     * @param answer   answer to secret question encrypted by MD5 encryptor
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeSecretQuestion(int id, String question, String answer) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_SECRET_QUESTION)) {
            statement.setString(1, question);
            statement.setString(2, answer);
            statement.setInt(3, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing secret question. " + e);
        }
    }

    /**
     * Updates definite {@link Player} passport scan relative path.
     *
     * @param id   id of {@link Player} whose data is updating
     * @param path new {@link Player} passport scan relative path value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see by.sasnouskikh.jcasino.controller.ShowImageServlet
     */
    @Override
    public boolean changeScanPath(int id, String path) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_SCAN_PATH)) {
            statement.setString(1, path);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing passport scan path. " + e);
        }
    }

    /**
     * Updates definite {@link Player} verification status binary mask.
     *
     * @param id     id of {@link Player} whose data is updating
     * @param status new {@link Player} verification status binary mask value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeVerificationStatus(int id, byte status) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_VER_STATUS_PLAYER)) {
            statement.setByte(1, status);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing verification status. " + e);
        }
    }

    /**
     * Updates definite {@link Player} verification status binary mask by definite admin with commentary.
     *
     * @param playerId   id of {@link Player} whose data is updating
     * @param status     new {@link Player} verification status binary mask value
     * @param adminId    id of {@link by.sasnouskikh.jcasino.entity.bean.Admin} who processes update
     * @param commentary {@link by.sasnouskikh.jcasino.entity.bean.Admin} commentary to update
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeVerificationStatus(int playerId, byte status, int adminId, String commentary) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_VER_STATUS_ADMIN)) {
            statement.setByte(1, status);
            statement.setInt(2, adminId);
            statement.setString(3, commentary);
            statement.setInt(4, playerId);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + playerId + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing verification status. " + e);
        }
    }

    /**
     * Updates definite {@link Player} balance by adding/subtracting definite value to it.
     *
     * @param id     id of {@link Player} whose data is updating
     * @param amount amount of money to add/subtract to current balance value
     * @param type   type of balance changing
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see by.sasnouskikh.jcasino.entity.bean.Transaction.TransactionType
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeBalance(int id, BigDecimal amount, Transaction.TransactionType type) throws DAOException {
        if (type == Transaction.TransactionType.WITHDRAW) {
            amount = amount.negate();
        }
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ACCOUNT_BALANCE)) {
            statement.setBigDecimal(1, amount);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing balance. " + e);
        }
    }

    /**
     * Updates definite {@link Player} account status by definite admin with commentary.
     *
     * @param playerId   id of {@link Player} whose data is updating
     * @param adminId    id of {@link by.sasnouskikh.jcasino.entity.bean.Admin} who updates data
     * @param status     new status of {@link Player} whose data is updating
     * @param commentary admin commentary to update
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
    public boolean changeAccountStatus(int playerId, int adminId, String status, String commentary) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ACCOUNT_STATUS_ADMIN)) {
            statement.setString(1, status);
            statement.setInt(2, adminId);
            statement.setString(3, commentary);
            statement.setInt(4, playerId);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + playerId + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing account status. " + e);
        }
    }

    /**
     * Builds {@link PlayerProfile} object by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link PlayerProfile} object or null
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called
     *                      on a closed result set
     */
    private PlayerProfile buildPlayerProfile(ResultSet resultSet) throws SQLException {
        PlayerProfile profile = null;
        if (resultSet.next()) {
            profile = new PlayerProfile();
            profile.setfName(resultSet.getString(FIRST_NAME));
            profile.setmName(resultSet.getString(MIDDLE_NAME));
            profile.setlName(resultSet.getString(LAST_NAME));
            profile.setBirthDate(resultSet.getDate(BIRTHDATE).toLocalDate());
            profile.setPassport(resultSet.getString(PASSPORT));
            profile.setQuestion(resultSet.getString(SECRET_QUESTION));
        }
        return profile;
    }

    /**
     * Builds {@link PlayerVerification} object by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link PlayerVerification} object or null
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called
     *                      on a closed result set
     */
    private PlayerVerification buildPlayerVerification(ResultSet resultSet) throws SQLException {
        PlayerVerification verification = null;
        if (resultSet.next()) {
            verification = new PlayerVerification();
            verification.setPlayerId(resultSet.getInt(ID));
            byte binStatus = resultSet.getByte(BINARY_STATUS);
            verification.setProfileVerified((binStatus & PROFILE_VER_MASK) == PROFILE_VER_MASK);
            verification.setEmailVerified((binStatus & EMAIL_VER_MASK) == EMAIL_VER_MASK);
            verification.setScanVerified((binStatus & PASSPORT_VER_MASK) == PASSPORT_VER_MASK);
            if ((binStatus & FULL_VER_MASK) == FULL_VER_MASK) {
                verification.setStatus(PlayerVerification.VerificationStatus.VERIFIED);
            } else {
                verification.setStatus(PlayerVerification.VerificationStatus.NOT_VERIFIED);
            }
            verification.setAdminId(resultSet.getInt(ADMIN_ID));
            verification.setCommentary(resultSet.getString(COMMENTARY));
            Timestamp verificationDate = resultSet.getTimestamp(VERIFICATION_DATE);
            if (verificationDate != null) {
                verification.setVerificationDate(verificationDate.toLocalDateTime());
            }
            verification.setPassport(resultSet.getString(PASSPORT));
        }
        return verification;
    }

    /**
     * Builds {@link List} object filled by {@link PlayerVerification} objects by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link List} object or null
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called
     *                      on a closed result set
     * @see #buildPlayerVerification(ResultSet)
     */
    private List<PlayerVerification> buildPlayerVerificationList(ResultSet resultSet) throws SQLException {
        List<PlayerVerification> verificationList = new ArrayList<>();
        PlayerVerification       verification;
        do {
            verification = buildPlayerVerification(resultSet);
            if (verification != null) {
                verificationList.add(verification);
            }
        } while (verification != null);
        return !verificationList.isEmpty() ? verificationList : null;
    }

    /**
     * Builds {@link PlayerAccount} object by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link PlayerAccount} object or null
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called
     *                      on a closed result set
     */
    private PlayerAccount buildPlayerAccount(ResultSet resultSet) throws SQLException {
        PlayerAccount account = null;
        if (resultSet.next()) {
            account = new PlayerAccount();
            account.setBalance(resultSet.getBigDecimal(BALANCE));
            BigDecimal thisMonthWithdrawal = resultSet.getBigDecimal(THIS_MONTH_WITHDRAWAL);
            if (thisMonthWithdrawal == null) {
                thisMonthWithdrawal = BigDecimal.ZERO;
            }
            account.setThisMonthWithdrawal(thisMonthWithdrawal);
            PlayerStatus status = new PlayerStatus();
            status.setStatus(PlayerStatus.StatusEnum.valueOf(resultSet.getString(STATUS).toUpperCase()));
            status.setAdminId(resultSet.getInt(ADMIN_ID));
            status.setCommentary(resultSet.getString(COMMENTARY));
            status.setBetLimit(resultSet.getBigDecimal(BET_LIMIT));
            status.setWithdrawalLimit(resultSet.getBigDecimal(WITHDRAWAL_LIMIT));
            status.setLoanPercent(resultSet.getBigDecimal(LOAN_PERCENT));
            status.setMaxLoan(resultSet.getBigDecimal(MAX_LOAN_AMOUNT));
            account.setStatus(status);
        }
        return account;
    }
}