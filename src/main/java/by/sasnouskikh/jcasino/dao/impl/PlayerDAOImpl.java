package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
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

public class PlayerDAOImpl extends AbstractDAO<Integer, Player> {

    private static final String SQL_DEFINE_ID_BY_EMAIL   = "SELECT id FROM user " +
                                                           "WHERE email=?";
    private static final String SQL_DEFINE_EMAIL_BY_ID   = "SELECT email FROM user " +
                                                           "WHERE id=?";
    private static final String SQL_DEFINE_NAME_BY_EMAIL = "SELECT fname " +
                                                           "FROM player NATURAL JOIN user " +
                                                           "WHERE email=?";

    private static final String SQL_SELECT_PROFILE            = "SELECT fname, mname, lname, birthdate, passport, question " +
                                                                "FROM player " +
                                                                "WHERE id=?";
    private static final String SQL_SELECT_VERIFICATION       = "SELECT id, status AS bin_status, admin_id, commentary, date, passport " +
                                                                "FROM verification " +
                                                                "WHERE id=?";
    private static final String SQL_SELECT_VERIFICATION_READY = "SELECT id, status AS bin_status, admin_id, commentary, date, passport " +
                                                                "FROM verification " +
                                                                "WHERE status=0b011 AND passport IS NOT NULL";
    private static final String SQL_SELECT_ACCOUNT            = "SELECT balance, status, admin_id, commentary, bet_limit, withdrawal_limit, loan_percent, max_loan_amount, ABS(SUM(amount)) AS total " +
                                                                "FROM player NATURAL JOIN player_status JOIN transaction ON player.id=player_id " +
                                                                "WHERE player.id=? AND MONTH(date)=MONTH(NOW()) AND YEAR(date)=YEAR(NOW()) AND amount<0";
    private static final String SQL_SELECT_MONTH_WITHDRAWAL   = "SELECT ABS(SUM(amount)) AS total " +
                                                                "FROM transaction " +
                                                                "WHERE player_id=? AND date LIKE ?";

    private static final String SQL_INSERT_USER         = "INSERT INTO user (email, password_md5, role, registration_date) " +
                                                          "VALUES (?, ?, 'player', NOW())";
    private static final String SQL_INSERT_PLAYER       = "INSERT INTO player (id, fname, mname, lname, birthdate, passport, question, answer_md5) " +
                                                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_VERIFICATION = "INSERT INTO verification (id) " +
                                                          "VALUES (?)";

    private static final String SQL_UPDATE_EMAIL                = "UPDATE user " +
                                                                  "SET email=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_PASSWORD             = "UPDATE user " +
                                                                  "SET password_md5=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_FNAME                = "UPDATE player " +
                                                                  "SET fname=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_MNAME                = "UPDATE player " +
                                                                  "SET mname=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_LNAME                = "UPDATE player " +
                                                                  "SET lname=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_PASSPORT_NUMBER      = "UPDATE player " +
                                                                  "SET passport=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_BIRTHDATE            = "UPDATE player " +
                                                                  "SET birthdate=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_SECRET_QUESTION      = "UPDATE player " +
                                                                  "SET question=?, answer_md5=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_SCAN_PATH            = "UPDATE verification " +
                                                                  "SET passport=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_VER_STATUS_PLAYER    = "UPDATE verification " +
                                                                  "SET status=? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_VER_STATUS_ADMIN     = "UPDATE verification " +
                                                                  "SET status=?, admin_id=?, commentary=?, date=NOW() " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_ACCOUNT_BALANCE      = "UPDATE player " +
                                                                  "SET balance=balance+? " +
                                                                  "WHERE id=?";
    private static final String SQL_UPDATE_ACCOUNT_STATUS_ADMIN = "UPDATE player " +
                                                                  "SET status=?, admin_id=?, commentary=? " +
                                                                  "WHERE id=?";

    PlayerDAOImpl() {
    }

    PlayerDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    public int defineIdByEmail(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_ID_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt("id") : 0;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while defining id by e-mail. " + e);
        }
    }

    public String defineEmailById(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_EMAIL_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getString("email") : null;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while defining e-mail by id. " + e);
        }
    }

    public String defineNameByEmail(String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_NAME_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getString("fname") : null;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while checking is e-mail exists. " + e);
        }
    }

    public PlayerProfile takeProfile(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PROFILE)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildProfile(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking player profile. " + e);
        }
    }

    public PlayerVerification takeVerification(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_VERIFICATION)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildPlayerVerification(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking verification. " + e);
        }
    }

    public List<PlayerVerification> takeReadyForVerification() throws DAOException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_VERIFICATION_READY);
            return buildPlayerVerificationList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking ready for verification list. " + e);
        }
    }

    public PlayerAccount takeAccount(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ACCOUNT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildPlayerAccount(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking player account. " + e);
        }
    }

    public BigDecimal takeMonthWithdrawal(int playerId, String monthPattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_MONTH_WITHDRAWAL)) {
            statement.setInt(1, playerId);
            statement.setString(2, monthPattern);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getBigDecimal("total") : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking month withdrawal. " + e);
        }
    }

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
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting player. " + e);
        }
        return true;
    }

    public boolean insertEmptyVerification(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_VERIFICATION)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting empty verification. " + e);
        }
        return true;
    }

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

    private PlayerProfile buildProfile(ResultSet resultSet) throws SQLException {
        PlayerProfile profile = null;
        if (resultSet.next()) {
            profile = new PlayerProfile();
            profile.setfName(resultSet.getString("fname"));
            profile.setmName(resultSet.getString("mname"));
            profile.setlName(resultSet.getString("lname"));
            profile.setBirthDate(resultSet.getDate("birthdate").toLocalDate());
            profile.setPassport(resultSet.getString("passport"));
            profile.setQuestion(resultSet.getString("question"));
        }
        return profile;
    }

    private PlayerVerification buildPlayerVerification(ResultSet resultSet) throws SQLException {
        PlayerVerification verification = null;
        if (resultSet.next()) {
            verification = new PlayerVerification();
            verification.setPlayerId(resultSet.getInt("id"));
            byte binStatus = resultSet.getByte("bin_status");
            verification.setProfileVerified((binStatus & PROFILE_VER_MASK) == PROFILE_VER_MASK);
            verification.setEmailVerified((binStatus & EMAIL_VER_MASK) == EMAIL_VER_MASK);
            verification.setScanVerified((binStatus & PASSPORT_VER_MASK) == PASSPORT_VER_MASK);
            if ((binStatus & FULL_VER_MASK) == FULL_VER_MASK) {
                verification.setStatus(PlayerVerification.VerificationStatus.VERIFIED);
            } else {
                verification.setStatus(PlayerVerification.VerificationStatus.NOT_VERIFIED);
            }
            verification.setAdminId(resultSet.getInt("admin_id"));
            verification.setCommentary(resultSet.getString("commentary"));
            Timestamp verificationDate = resultSet.getTimestamp("date");
            if (verificationDate != null) {
                verification.setVerificationDate(verificationDate.toLocalDateTime());
            }
            verification.setPassport(resultSet.getString("passport"));
        }
        return verification;
    }

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

    private PlayerAccount buildPlayerAccount(ResultSet resultSet) throws SQLException {
        PlayerAccount account = null;
        if (resultSet.next()) {
            account = new PlayerAccount();
            account.setBalance(resultSet.getBigDecimal("balance"));
            BigDecimal thisMonthWithdrawal = resultSet.getBigDecimal("total");
            if (thisMonthWithdrawal == null) {
                thisMonthWithdrawal = BigDecimal.ZERO;
            }
            account.setThisMonthWithdrawal(thisMonthWithdrawal);
            PlayerStatus status = new PlayerStatus();
            status.setStatus(PlayerStatus.StatusEnum.valueOf(resultSet.getString("status").toUpperCase()));
            status.setAdminId(resultSet.getInt("admin_id"));
            status.setCommentary(resultSet.getString("commentary"));
            status.setBetLimit(resultSet.getBigDecimal("bet_limit"));
            status.setWithdrawalLimit(resultSet.getBigDecimal("withdrawal_limit"));
            status.setLoanPercent(resultSet.getBigDecimal("loan_percent"));
            status.setMaxLoan(resultSet.getBigDecimal("max_loan_amount"));
            account.setStatus(status);
        }
        return account;
    }
}