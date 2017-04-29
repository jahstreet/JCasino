package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.PlayerProfile;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.entity.bean.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * The class provides DAO abstraction for {@link Player} objects.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AbstractDAO
 */
public abstract class PlayerDAO extends AbstractDAO {

    /**
     * Column names of database table 'player'.
     */
    protected static final String ID              = "id";
    protected static final String FIRST_NAME      = "fname";
    protected static final String MIDDLE_NAME     = "mname";
    protected static final String LAST_NAME       = "lname";
    protected static final String BIRTHDATE       = "birthdate";
    protected static final String PASSPORT        = "passport";
    protected static final String SECRET_QUESTION = "question";
    protected static final String ANSWER_MD5      = "answer_md5";
    protected static final String BALANCE         = "balance";
    protected static final String STATUS          = "status";
    protected static final String ADMIN_ID        = "admin_id";
    protected static final String COMMENTARY      = "commentary";

    /**
     * Additional column names of database table 'user'.
     */
    protected static final String EMAIL = "email";

    /**
     * Additional column names of database table 'verification'.
     */
    protected static final String VERIFICATION_DATE = "date";

    /**
     * Additional column names of database table 'player_status'.
     */
    protected static final String BET_LIMIT        = "bet_limit";
    protected static final String WITHDRAWAL_LIMIT = "withdrawal_limit";
    protected static final String LOAN_PERCENT     = "loan_percent";
    protected static final String MAX_LOAN_AMOUNT  = "max_loan_amount";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    protected PlayerDAO() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    protected PlayerDAO(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Defines {@link Player} id by its e-mail.
     *
     * @param email e-mail of {@link Player} whose id is defining
     * @return defined id value or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract int defineIdByEmail(String email) throws DAOException;

    /**
     * Defines {@link Player} e-mail by its id.
     *
     * @param id id of {@link Player} whose e-mail is defining
     * @return defined e-mail value or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract String defineEmailById(int id) throws DAOException;

    /**
     * Defines {@link Player} first name by its e-mail.
     *
     * @param email e-mail of {@link Player} whose first name is defining
     * @return defined first name value or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract String defineNameByEmail(String email) throws DAOException;

    /**
     * Takes {@link PlayerProfile} object of definite {@link Player}.
     *
     * @param id id of {@link Player} whose profile is taking
     * @return {@link PlayerProfile} object of definite {@link Player} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract PlayerProfile takeProfile(int id) throws DAOException;

    /**
     * Takes {@link PlayerVerification} object of definite {@link Player}.
     *
     * @param id id of {@link Player} whose verification is taking
     * @return {@link PlayerVerification} object of definite {@link Player} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract PlayerVerification takeVerification(int id) throws DAOException;

    /**
     * Takes {@link List} filled by {@link PlayerVerification} objects of players who are ready to be verified
     * by admin.
     *
     * @return {@link List} filled by {@link PlayerVerification} objects of players who are ready to be verified by
     * admin or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<PlayerVerification> takeReadyForVerification() throws DAOException;

    /**
     * Takes {@link PlayerAccount} of definite {@link Player}.
     *
     * @param id id of player whose {@link PlayerAccount} is taking
     * @return taken {@link PlayerAccount} or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract PlayerAccount takeAccount(int id) throws DAOException;

    /**
     * Takes amount of money definite {@link Player} withdrawn in date due to definite pattern.
     *
     * @param playerId    id of {@link Player} whose withdrawal sum is taking
     * @param datePattern pattern of date conforming to <code>SQL LIKE</code> operator
     * @return taken given date pattern withdrawal sum or {@link BigDecimal#ZERO}
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract BigDecimal takeWithdrawalSum(int playerId, String datePattern) throws DAOException;

    /**
     * Inserts {@link Player} data into 'user' table on registration.
     *
     * @param email    email of {@link Player} whose user data is inserting
     * @param password password of {@link Player} whose user data is inserting encrypted by MD5 encryptor
     * @return id of inserted {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser} or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract int insertUserPlayer(String email, String password) throws DAOException;

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
     */
    public abstract boolean insertPlayer(int id, String fName, String mName, String lName, String birthDate, String passport, String question, String answer) throws DAOException;

    /**
     * Inserts {@link Player} data into 'verification' table on registration.
     *
     * @param id id of {@link Player} whose data is inserting
     * @return true if insertion proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean insertEmptyVerification(int id) throws DAOException;

    /**
     * Updates definite {@link Player} e-mail.
     *
     * @param id    id of {@link Player} whose data is updating
     * @param email new {@link Player} e-mail value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeEmail(int id, String email) throws DAOException;

    /**
     * Updates definite {@link Player} password.
     *
     * @param id       id of {@link Player} whose data is updating
     * @param password new {@link Player} password encrypted by MD5 encryptor value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changePassword(int id, String password) throws DAOException;

    /**
     * Updates definite {@link Player} first name.
     *
     * @param id   id of {@link Player} whose data is updating
     * @param name new {@link Player} first name value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeFirstName(int id, String name) throws DAOException;

    /**
     * Updates definite {@link Player} middle name.
     *
     * @param id   id of {@link Player} whose data is updating
     * @param name new {@link Player} middle name value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeMiddleName(int id, String name) throws DAOException;

    /**
     * Updates definite {@link Player} last name.
     *
     * @param id   id of {@link Player} whose data is updating
     * @param name new {@link Player} last name value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeLastName(int id, String name) throws DAOException;

    /**
     * Updates definite {@link Player} passport number.
     *
     * @param id       id of {@link Player} whose data is updating
     * @param passport new {@link Player} passport number value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changePassportNumber(int id, String passport) throws DAOException;

    /**
     * Updates definite {@link Player} birthdate.
     *
     * @param id        id of {@link Player} whose data is updating
     * @param birthDate new {@link Player} birthdate value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeBirthdate(int id, String birthDate) throws DAOException;

    /**
     * Updates definite {@link Player} secret question and its answer encrypted by MD5 encryptor.
     *
     * @param id       id of {@link Player} whose data is updating
     * @param question new {@link Player} secret question value
     * @param answer   answer to secret question encrypted by MD5 encryptor
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeSecretQuestion(int id, String question, String answer) throws DAOException;

    /**
     * Updates definite {@link Player} passport scan relative path.
     *
     * @param id   id of {@link Player} whose data is updating
     * @param path new {@link Player} passport scan relative path value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeScanPath(int id, String path) throws DAOException;

    /**
     * Updates definite {@link Player} verification status binary mask.
     *
     * @param id     id of {@link Player} whose data is updating
     * @param status new {@link Player} verification status binary mask value
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeVerificationStatus(int id, byte status) throws DAOException;

    /**
     * Updates definite {@link Player} verification status binary mask by definite admin with commentary.
     *
     * @param playerId   id of {@link Player} whose data is updating
     * @param status     new {@link Player} verification status binary mask value
     * @param adminId    id of {@link by.sasnouskikh.jcasino.entity.bean.Admin} who processes update
     * @param commentary {@link by.sasnouskikh.jcasino.entity.bean.Admin} commentary to update
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeVerificationStatus(int playerId, byte status, int adminId, String commentary) throws DAOException;

    /**
     * Updates definite {@link Player} balance by adding/subtracting definite value to it.
     *
     * @param id     id of {@link Player} whose data is updating
     * @param amount amount of money to add/subtract to current balance value
     * @param type   type of balance changing
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see by.sasnouskikh.jcasino.entity.bean.Transaction.TransactionType
     */
    public abstract boolean changeBalance(int id, BigDecimal amount, Transaction.TransactionType type) throws DAOException;

    /**
     * Updates definite {@link Player} account status by definite admin with commentary.
     *
     * @param playerId   id of {@link Player} whose data is updating
     * @param adminId    id of {@link by.sasnouskikh.jcasino.entity.bean.Admin} who updates data
     * @param status     new status of {@link Player} whose data is updating
     * @param commentary admin commentary to update
     * @return true if update proceeded successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeAccountStatus(int playerId, int adminId, String status, String commentary) throws DAOException;
}