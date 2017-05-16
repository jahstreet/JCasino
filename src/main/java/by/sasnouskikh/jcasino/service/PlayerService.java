package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.LoanDAO;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.StreakDAO;
import by.sasnouskikh.jcasino.dao.TransactionDAO;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.PlayerProfile;
import by.sasnouskikh.jcasino.entity.bean.PlayerStats;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.mailer.MailerException;
import by.sasnouskikh.jcasino.mailer.MailerSSL;
import by.sasnouskikh.jcasino.manager.JCasinoEncryptor;
import by.sasnouskikh.jcasino.manager.MessageManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides Service layer actions with player.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class PlayerService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger(PlayerService.class);

    /**
     * Enumeration of available {@link PlayerProfile} text fields to edit.
     */
    public enum ProfileTextField {
        FIRST_NAME, MIDDLE_NAME, LAST_NAME, PASSPORT
    }

    /**
     * Default instance constructor.
     */
    public PlayerService() {
    }

    /**
     * Constructs instance using definite {@link DAOHelper} object.
     */
    public PlayerService(DAOHelper daoHelper) {
        super(daoHelper);
    }

    /**
     * Sends new e-mail code to player e-mail and ads it to {@link PlayerVerification} object.
     *
     * @param player player to whom e-mail code is sending
     * @param locale locale string to define language of sending e-mail with e-mail code
     * @return true if operation proceeded successfully
     * @see MessageManager
     * @see RandomGenerator#generateEmailCode()
     * @see MailerSSL
     */
    public static boolean sendEmailCode(Player player, String locale) {
        String             name           = player.getProfile().getfName();
        String             emailTo        = player.getEmail();
        PlayerVerification verification   = player.getVerification();
        String             emailCode      = RandomGenerator.generateEmailCode();
        MessageManager     messageManager = MessageManager.getMessageManager(locale);
        if (name == null || name.isEmpty()) {
            name = StringUtils.capitalize(JCasinoUser.UserRole.PLAYER.toString());
        }
        try {
            String message = messageManager.getMessage(EMAIL_PATTERN_VERIFICATION_CODE) + WHITESPACE + emailCode;
            if (MailerSSL.sendEmail(name, message, emailTo)) {
                verification.setEmailCode(emailCode);
                return true;
            }
        } catch (MailerException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to fill given {@link Player} object with latest {@link PlayerProfile} data.
     *
     * @param player {@link Player} object to update data
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see PlayerDAO#takeProfile(int)
     * @see PlayerDAO#defineEmailById(int)
     */
    public boolean updateProfileInfo(Player player) {
        int       id        = player.getId();
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        try {
            PlayerProfile profile = playerDAO.takeProfile(player.getId());
            String        email   = playerDAO.defineEmailById(id);
            player.setProfile(profile);
            player.setEmail(email);
            return true;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to fill given {@link Player} object with latest {@link PlayerAccount} data.
     *
     * @param player {@link Player} object to update data
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see PlayerDAO#takeAccount(int)
     * @see LoanDAO#takeCurrentLoan(int)
     */
    public boolean updateAccountInfo(Player player) {
        int       id        = player.getId();
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        LoanDAO   loanDAO   = daoHelper.getLoanDAO();
        try {
            PlayerAccount account     = playerDAO.takeAccount(id);
            Loan          currentLoan = loanDAO.takeCurrentLoan(id);
            account.setCurrentLoan(currentLoan);
            player.setAccount(account);
            return true;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to fill given {@link Player} object with latest {@link PlayerStats} data.
     *
     * @param player {@link Player} object to update data
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see StreakDAO#takePlayerStreaks(int)
     * @see TransactionDAO#takePlayerTransactions(int)
     * @see StatsHelper#buildStats(List, List)
     */
    public boolean updateStatsInfo(Player player) {
        int            id             = player.getId();
        StreakDAO      streakDAO      = daoHelper.getStreakDAO();
        TransactionDAO transactionDAO = daoHelper.getTransactionDAO();
        try {
            List<Streak>      streaks      = streakDAO.takePlayerStreaks(id);
            List<Transaction> transactions = transactionDAO.takePlayerTransactions(id);
            PlayerStats       playerStats  = StatsHelper.buildStats(streaks, transactions);
            player.setStats(playerStats);
            return true;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to fill given {@link Player} object with latest {@link PlayerVerification} data.
     *
     * @param player {@link Player} object to update data
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see PlayerDAO#takeVerification(int)
     */
    public boolean updateVerificationInfo(Player player) {
        int       id        = player.getId();
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        try {
            PlayerVerification playerVerification = playerDAO.takeVerification(id);
            player.setVerification(playerVerification);
            return true;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Provides player registration operation. Calls DAO layer to insert all given and other needed data.
     *
     * @param email     player e-mail
     * @param password  player password
     * @param fName     player first name
     * @param mName     player middle name
     * @param lName     player last name
     * @param birthDate player birthdate
     * @param passport  player passport number
     * @param question  player secret question
     * @param answer    player answer to secret question
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see JCasinoEncryptor
     * @see PlayerDAO#insertUserPlayer(String, String)
     * @see PlayerDAO#insertPlayer(int, String, String, String, String, String, String, String)
     * @see PlayerDAO#insertEmptyVerification(int)
     */
    public boolean registerPlayer(String email, String password, String fName, String mName, String lName,
                                 String birthDate, String passport, String question, String answer) {
        email = email.trim().toLowerCase();
        password = JCasinoEncryptor.encryptMD5(password);
        if (fName != null) {
            fName = fName.trim().toUpperCase();
        }
        if (mName != null) {
            mName = mName.trim().toUpperCase();
        }
        if (lName != null) {
            lName = lName.trim().toUpperCase();
        }
        passport = passport.trim().toUpperCase();
        if (question != null) {
            question = question.trim();
            answer = JCasinoEncryptor.encryptMD5(answer.trim());
        }
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        try {
            daoHelper.beginTransaction();
            int id = playerDAO.insertUserPlayer(email, password);
            if (id != 0
                && playerDAO.insertPlayer(id, fName, mName, lName, birthDate, passport, question, answer)
                && playerDAO.insertEmptyVerification(id)) {
                daoHelper.commit();
                return true;
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    /**
     * Calls DAO layer to change player e-mail and resets player e-mail verification status.
     *
     * @param player {@link Player} object whose e-mail to change
     * @param email  new e-mail
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see UserDAO#checkEmailExist(String)
     * @see PlayerDAO#changeEmail(int, String)
     * @see PlayerDAO#changeVerificationStatus(int, byte)
     * @see VerificationService#buildNewStatus(PlayerVerification, byte, VerificationService.MaskOperation)
     */
    public boolean changeEmail(Player player, String email) {
        email = email.trim().toLowerCase();
        int       id        = player.getId();
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        UserDAO   userDAO   = daoHelper.getUserDAO();
        try {
            if (userDAO.checkEmailExist(email)) {
                return false;
            }
            daoHelper.beginTransaction();
            if (playerDAO.changeEmail(id, email)) {
                PlayerVerification verification = player.getVerification();
                byte newStatus = VerificationService.buildNewStatus(verification,
                                                                    (byte) ~EMAIL_VER_MASK,
                                                                    VerificationService.MaskOperation.AND);
                if (playerDAO.changeVerificationStatus(id, newStatus)) {
                    daoHelper.commit();
                    return true;
                }
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    /**
     * Calls DAO layer to change player password.
     *
     * @param player      {@link Player} object whose password to change
     * @param oldPassword old password
     * @param newPassword new password
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see JCasinoEncryptor
     * @see UserDAO#checkPassword(int, String)
     * @see PlayerDAO#changePassword(int, String)
     */
    public boolean changePassword(Player player, String oldPassword, String newPassword) {
        int id = player.getId();
        oldPassword = JCasinoEncryptor.encryptMD5(oldPassword);
        newPassword = JCasinoEncryptor.encryptMD5(newPassword);
        UserDAO   userDAO   = daoHelper.getUserDAO();
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        try {
            return userDAO.checkPassword(id, oldPassword)
                   && playerDAO.changePassword(id, newPassword);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to change player password and sends new generated password to user e-mail.
     *
     * @param email  user e-mail on which to send new password
     * @param locale locale string to define language of sending e-mail with new password
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see JCasinoEncryptor
     * @see MessageManager
     * @see RandomGenerator
     * @see MailerSSL
     * @see UserDAO#checkEmailExist(String)
     * @see PlayerDAO#changePassword(int, String)
     * @see PlayerDAO#defineNameByEmail(String)
     */
    public boolean recoverPassword(String email, String locale) {
        email = email.toLowerCase().trim();
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        UserDAO        userDAO        = daoHelper.getUserDAO();
        PlayerDAO      playerDAO      = daoHelper.getPlayerDAO();
        try {
            if (userDAO.checkEmailExist(email)) {
                JCasinoUser user        = userDAO.takeUser(email);
                int         id          = user.getId();
                String      newPassword = RandomGenerator.generatePassword();
                daoHelper.beginTransaction();
                if (!playerDAO.changePassword(id, JCasinoEncryptor.encryptMD5(newPassword))) {
                    return false;
                }
                String userName = playerDAO.defineNameByEmail(email);
                if (userName == null || userName.trim().isEmpty()) {
                    userName = StringUtils.capitalize(JCasinoUser.UserRole.PLAYER.toString());
                }
                String message = messageManager.getMessage(EMAIL_PATTERN_NEW_PASSWORD) + WHITESPACE + newPassword;
                if (MailerSSL.sendEmail(userName, message, email)) {
                    daoHelper.commit();
                    return true;
                }
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (MailerException e) {
            LOGGER.log(Level.ERROR, "E-mail wasn't sent while recovering password. " + e);
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    /**
     * Calls DAO layer to change player profile text item and resets player profile verification status.
     *
     * @param player player whose data is updating
     * @param text   new textfield value
     * @param field  {@link ProfileTextField} enumeration value instance
     * @return true if transaction proceeded successfully
     * @throws ServiceException if given {@link ProfileTextField} update is not supported
     * @see DAOHelper
     * @see PlayerDAO#changeFirstName(int, String)
     * @see PlayerDAO#changeMiddleName(int, String)
     * @see PlayerDAO#changeLastName(int, String)
     * @see PlayerDAO#changePassportNumber(int, String)
     * @see VerificationService#buildNewStatus(PlayerVerification, byte, VerificationService.MaskOperation)
     * @see PlayerDAO#changeVerificationStatus(int, byte)
     */
    public boolean changeProfileTextItem(Player player, String text, ProfileTextField field) throws ServiceException {
        int id = player.getId();
        if (text != null) {
            text = text.trim().toUpperCase();
        } else {
            text = EMPTY_STRING;
        }
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        try {
            daoHelper.beginTransaction();
            boolean changed;
            switch (field) {
                case FIRST_NAME:
                    changed = playerDAO.changeFirstName(id, text);
                    break;
                case MIDDLE_NAME:
                    changed = playerDAO.changeMiddleName(id, text);
                    break;
                case LAST_NAME:
                    changed = playerDAO.changeLastName(id, text);
                    break;
                case PASSPORT:
                    changed = playerDAO.changePassportNumber(id, text);
                    break;
                default:
                    throw new ServiceException("Method not realized for given profile field.");
            }
            if (changed) {
                PlayerVerification verification = player.getVerification();
                byte newStatus = VerificationService
                                 .buildNewStatus(verification, EMAIL_VER_MASK, VerificationService.MaskOperation.AND);
                if (playerDAO.changeVerificationStatus(id, newStatus)) {
                    daoHelper.commit();
                    return true;
                }
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    /**
     * Calls DAO layer to change player birthdate and resets player profile verification status.
     *
     * @param player    player whose data is updating
     * @param birthDate new birthdate value
     * @return true if transaction proceeded successfully
     * @see DAOHelper
     * @see PlayerDAO#changeBirthdate(int, String)
     * @see VerificationService#buildNewStatus(PlayerVerification, byte, VerificationService.MaskOperation)
     * @see PlayerDAO#changeVerificationStatus(int, byte)
     */
    public boolean changeBirthDate(Player player, String birthDate) {
        int                id           = player.getId();
        PlayerVerification verification = player.getVerification();
        PlayerDAO          playerDAO    = daoHelper.getPlayerDAO();
        try {
            daoHelper.beginTransaction();
            if (playerDAO.changeBirthdate(id, birthDate)) {
                byte newStatus = VerificationService
                                 .buildNewStatus(verification, EMAIL_VER_MASK, VerificationService.MaskOperation.AND);
                if (playerDAO.changeVerificationStatus(id, newStatus)) {
                    daoHelper.commit();
                    return true;
                }
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    /**
     * Calls DAO layer to change player secret question.
     *
     * @param player   player whose data is updating
     * @param question new secret question
     * @param answer   answer to new secret question
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see JCasinoEncryptor
     * @see PlayerDAO#changeSecretQuestion(int, String, String)
     */
    public boolean changeSecretQuestion(Player player, String question, String answer) {
        int id = player.getId();
        question = question.trim();
        answer = JCasinoEncryptor.encryptMD5(answer.trim());
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        try {
            return playerDAO.changeSecretQuestion(id, question, answer);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to verify player profile if it is filled.
     *
     * @param player player whose profile is verifying
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see PlayerDAO#changeVerificationStatus(int, byte)
     * @see VerificationService#buildNewStatus(PlayerVerification, byte, VerificationService.MaskOperation)
     */
    public boolean verifyProfile(Player player) {
        int                id           = player.getId();
        PlayerProfile      profile      = player.getProfile();
        PlayerVerification verification = player.getVerification();
        String             fName        = profile.getfName();
        String             lName        = profile.getlName();
        String             passport     = profile.getPassport();
        if (fName != null && !fName.isEmpty()
            && lName != null && !lName.isEmpty()
            && passport != null && !passport.isEmpty()) {
            PlayerDAO playerDAO = daoHelper.getPlayerDAO();
            try {
                byte newStatus = VerificationService
                                 .buildNewStatus(verification, PROFILE_VER_MASK, VerificationService.MaskOperation.OR);
                return playerDAO.changeVerificationStatus(id, newStatus);
            } catch (DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
            }
        }
        return false;
    }

    /**
     * Verifies player e-mail by comparing given code with {@link PlayerVerification#emailCode}.
     *
     * @param player      player whose e-mail is verifying
     * @param enteredCode code entered by player
     * @param emailCode   e-mail code to compare with code entered by player
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see PlayerDAO#changeVerificationStatus(int, byte)
     * @see VerificationService#buildNewStatus(PlayerVerification, byte, VerificationService.MaskOperation)
     */
    public boolean verifyEmail(Player player, String enteredCode, String emailCode) {
        int                id           = player.getId();
        PlayerVerification verification = player.getVerification();
        if (enteredCode != null && enteredCode.equals(emailCode)) {
            byte newStatus = VerificationService
                             .buildNewStatus(verification, EMAIL_VER_MASK, VerificationService.MaskOperation.OR);
            PlayerDAO playerDAO = daoHelper.getPlayerDAO();
            try {
                return playerDAO.changeVerificationStatus(id, newStatus);
            } catch (DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
            }
        }
        return false;
    }

    /**
     * Uploads definite player passport scan image file.
     *
     * @param player       player whose passport scan is uploading
     * @param scanFileItem {@link FileItem} image file of player passport scan
     * @param uploadPath   path to 'uploads' directory
     * @return true if operation proceeded successfully
     * @throws ServiceException if passport scan image file extension isn't supported
     * @see DAOHelper
     * @see PlayerDAO#changeScanPath(int, String)
     * @see by.sasnouskikh.jcasino.manager.ConfigConstant#AVAILABLE_SCAN_EXT
     */
    public boolean uploadPassportScan(Player player, FileItem scanFileItem, String uploadPath) throws ServiceException {
        int    id            = player.getId();
        String playerDirName = String.valueOf(id);
        String playerDirPath = uploadPath + File.separator + SCAN_UPLOAD_DIR + File.separator + playerDirName;
        File   playerDir     = new File(playerDirPath);
        if (!playerDir.exists()) {
            playerDir.mkdirs();
        }
        String fileSourceName = scanFileItem.getName();
        String fileExt        = FilenameUtils.getExtension(fileSourceName);
        if (fileExt == null || fileExt.isEmpty()
            || Arrays.binarySearch(AVAILABLE_SCAN_EXT, fileExt.toLowerCase()) == -1) {
            throw new ServiceException("Invalid extension of passport scan file.");
        }
        fileExt = fileExt.toLowerCase();
        int    fileIndex = 0;
        String fileName;
        String dbFilePath;
        String storePath;
        File   storeFile;
        do {
            fileIndex++;
            fileName = String.valueOf(fileIndex) + DOT + fileExt;
            dbFilePath = SCAN_UPLOAD_DIR + File.separator + playerDirName + File.separator + fileName;
            storePath = playerDirPath + File.separator + fileName;
            storeFile = new File(storePath);
        } while (storeFile.exists());
        try {
            scanFileItem.write(storeFile);
            PlayerDAO playerDAO = daoHelper.getPlayerDAO();
            return playerDAO.changeScanPath(id, dbFilePath);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Exception occurred while uploading passport scan. " + e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to make an account transaction of definite
     * {@link by.sasnouskikh.jcasino.entity.bean.Transaction.TransactionType}.
     *
     * @param player player who processes transaction
     * @param amount amount of money player transacts
     * @param type   type of transaction
     * @return true if transaction proceeded successfully
     * @see DAOHelper
     * @see TransactionDAO#insertTransaction(int, BigDecimal, Transaction.TransactionType)
     * @see PlayerDAO#changeBalance(int, BigDecimal, Transaction.TransactionType)
     */
    public boolean makeTransaction(Player player, BigDecimal amount, Transaction.TransactionType type) {
        int            id             = player.getId();
        PlayerDAO      playerDAO      = daoHelper.getPlayerDAO();
        TransactionDAO transactionDAO = daoHelper.getTransactionDAO();
        try {
            daoHelper.beginTransaction();
            if (transactionDAO.insertTransaction(id, amount, type) != 0
                && playerDAO.changeBalance(id, amount, type)) {
                daoHelper.commit();
                return true;
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    /**
     * Inits given {@link Player} object data.
     *
     * @param player {@link Player} object to init
     * @return true if operation proceeded successfully
     * @see #updateProfileInfo(Player)
     * @see #updateAccountInfo(Player)
     * @see #updateStatsInfo(Player)
     * @see #updateVerificationInfo(Player)
     */
    boolean initPlayerInfo(Player player) {
        return updateProfileInfo(player)
               & updateAccountInfo(player)
               & updateStatsInfo(player)
               & updateVerificationInfo(player);
    }
}