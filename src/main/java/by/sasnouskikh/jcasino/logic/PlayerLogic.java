package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.impl.DAOFactory;
import by.sasnouskikh.jcasino.dao.impl.LoanDAOImpl;
import by.sasnouskikh.jcasino.dao.impl.PlayerDAOImpl;
import by.sasnouskikh.jcasino.dao.impl.StreakDAOImpl;
import by.sasnouskikh.jcasino.dao.impl.TransactionDAOImpl;
import by.sasnouskikh.jcasino.dao.impl.UserDAOImpl;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
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

public class PlayerLogic {
    private static final Logger LOGGER = LogManager.getLogger(PlayerLogic.class);

    public enum ProfileTextField {
        FIRST_NAME, MIDDLE_NAME, LAST_NAME, PASSPORT
    }

    static boolean initPlayerInfo(Player player) {
        return updateProfileInfo(player)
               & updateAccountInfo(player)
               & updateStatsInfo(player)
               & updateVerificationInfo(player);
    }

    public static boolean updateProfileInfo(Player player) {
        int id = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            PlayerProfile profile = playerDAO.takeProfile(player.getId());
            String        email   = playerDAO.defineEmailById(id);
            player.setProfile(profile);
            player.setEmail(email);
            return true;
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean updateAccountInfo(Player player) {
        int id = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO();
             LoanDAOImpl loanDAO = DAOFactory.getLoanDAO()) {
            PlayerAccount account     = playerDAO.takeAccount(id);
            Loan          currentLoan = loanDAO.takeCurrentLoan(id);
            account.setCurrentLoan(currentLoan);
            player.setAccount(account);
            return true;
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean updateStatsInfo(Player player) {
        int id = player.getId();
        try (StreakDAOImpl streakDAO = DAOFactory.getStreakDAO();
             TransactionDAOImpl transactionDAO = DAOFactory.getTransactionDAO()) {
            List<Streak>      streaks      = streakDAO.takePlayerStreaks(id);
            List<Transaction> transactions = transactionDAO.takePlayerTransactions(id);
            PlayerStats       playerStats  = StatsHelper.buildStats(streaks, transactions);
            player.setStats(playerStats);
            return true;
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean updateVerificationInfo(Player player) {
        int id = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            PlayerVerification playerVerification = playerDAO.takeVerification(id);
            player.setVerification(playerVerification);
            return true;
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean registerPlayer(String email, String password, String fName, String mName, String lName, String birthDate, String passport, String question, String answer) {
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
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            playerDAO.beginTransaction();
            int id = playerDAO.insertUserPlayer(email, password);
            if (id != 0
                && playerDAO.insertPlayer(id, fName, mName, lName, birthDate, passport, question, answer)
                && playerDAO.insertEmptyVerification(id)) {
                playerDAO.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    public static boolean changeEmail(Player player, String email) {
        email = email.trim().toLowerCase();
        int id = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO();
             UserDAOImpl userDAO = DAOFactory.getUserDAO()) {
            if (userDAO.checkEmailExist(email)) {
                return false;
            }
            playerDAO.beginTransaction();
            if (playerDAO.changeEmail(id, email)) {
                PlayerVerification verification = player.getVerification();
                byte               newStatus    = VerificationLogic.buildNewStatus(verification, (byte) ~EMAIL_VER_MASK, VerificationLogic.MaskOperation.AND);
                if (playerDAO.changeVerificationStatus(id, newStatus)) {
                    playerDAO.commit();
                    return true;
                }
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    public static boolean changePassword(Player player, String oldPassword, String newPassword) {
        int id = player.getId();
        oldPassword = JCasinoEncryptor.encryptMD5(oldPassword);
        newPassword = JCasinoEncryptor.encryptMD5(newPassword);
        try (UserDAOImpl userDAO = DAOFactory.getUserDAO();
             PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            return userDAO.checkPassword(id, oldPassword)
                   && playerDAO.changePassword(id, newPassword);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean recoverPassword(String email, String locale) {
        email = email.toLowerCase().trim();
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        try (UserDAOImpl userDAO = DAOFactory.getUserDAO();
             PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            if (userDAO.checkEmailExist(email)) {
                JCasinoUser user        = userDAO.takeUser(email);
                int         id          = user.getId();
                String      newPassword = RandomGenerator.generatePassword();
                playerDAO.beginTransaction();
                if (!playerDAO.changePassword(id, JCasinoEncryptor.encryptMD5(newPassword))) {
                    return false;
                }
                String userName = playerDAO.defineNameByEmail(email);
                if (userName == null || userName.trim().isEmpty()) {
                    userName = StringUtils.capitalize(JCasinoUser.UserRole.PLAYER.toString());
                }
                String message = messageManager.getMessage(EMAIL_PATTERN_NEW_PASSWORD) + WHITESPACE + newPassword;
                if (MailerSSL.sendEmail(userName, message, email)) {
                    playerDAO.commit();
                    return true;
                }
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (MailerException e) {
            LOGGER.log(Level.ERROR, "E-mail wasn't sent while recovering password. " + e);
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    public static boolean changeProfileTextItem(Player player, String text, ProfileTextField field) throws LogicException {
        int id = player.getId();
        if (text != null) {
            text = text.trim().toUpperCase();
        } else {
            text = EMPTY_STRING;
        }
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            playerDAO.beginTransaction();
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
                    throw new LogicException("Method not realized for given profile field.");
            }
            if (changed) {
                PlayerVerification verification = player.getVerification();
                byte               newStatus    = VerificationLogic.buildNewStatus(verification, EMAIL_VER_MASK, VerificationLogic.MaskOperation.AND);
                if (playerDAO.changeVerificationStatus(id, newStatus)) {
                    playerDAO.commit();
                    return true;
                }
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    public static boolean changeBirthDate(Player player, String birthDate) {
        int                id           = player.getId();
        PlayerVerification verification = player.getVerification();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            playerDAO.beginTransaction();
            if (playerDAO.changeBirthdate(id, birthDate)) {
                byte newStatus = VerificationLogic.buildNewStatus(verification, EMAIL_VER_MASK, VerificationLogic.MaskOperation.AND);
                if (playerDAO.changeVerificationStatus(id, newStatus)) {
                    playerDAO.commit();
                    return true;
                }
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    public static boolean changeSecretQuestion(Player player, String question, String answer) {
        int id = player.getId();
        question = question.trim();
        answer = answer.trim();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            return playerDAO.changeSecretQuestion(id, question, answer);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean verifyProfile(Player player) {
        int                id           = player.getId();
        PlayerProfile      profile      = player.getProfile();
        PlayerVerification verification = player.getVerification();
        String             fName        = profile.getfName();
        String             lName        = profile.getlName();
        String             passport     = profile.getPassport();
        if (fName != null && !fName.isEmpty()
            && lName != null && !lName.isEmpty()
            && passport != null && !passport.isEmpty()) {
            try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
                byte newStatus = VerificationLogic.buildNewStatus(verification, PROFILE_VER_MASK, VerificationLogic.MaskOperation.OR);
                return playerDAO.changeVerificationStatus(id, newStatus);
            } catch (ConnectionPoolException | DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
            }
        }
        return false;
    }

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

    public static boolean verifyEmail(Player player, String enteredCode, String emailCode) {
        int                id           = player.getId();
        PlayerVerification verification = player.getVerification();
        if (enteredCode != null && enteredCode.equals(emailCode)) {
            byte newStatus = VerificationLogic.buildNewStatus(verification, EMAIL_VER_MASK, VerificationLogic.MaskOperation.OR);
            try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
                return playerDAO.changeVerificationStatus(id, newStatus);
            } catch (ConnectionPoolException | DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
            }
        }
        return false;
    }

    public static boolean uploadPassportScan(Player player, FileItem scanFileItem, String uploadPath) throws LogicException {
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
            throw new LogicException("Invalid extension of passport scan file.");
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
            try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
                return playerDAO.changeScanPath(id, dbFilePath);
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Exception occured while uploading passport scan. " + e.getMessage());
        }
        return false;
    }

    public static boolean replenishAccount(Player player, BigDecimal amount) {
        int id = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            TransactionDAOImpl transactionDAO = DAOFactory.getTransactionDAO(playerDAO.getConnection());
            playerDAO.beginTransaction();
            if (transactionDAO.insertTransaction(id, amount, Transaction.TransactionType.REPLENISH) != 0
                && playerDAO.changeBalance(id, amount, Transaction.TransactionType.REPLENISH)) {
                playerDAO.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    public static boolean withdrawMoney(Player player, BigDecimal amount) {
        int id = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            TransactionDAOImpl transactionDAO = DAOFactory.getTransactionDAO(playerDAO.getConnection());
            playerDAO.beginTransaction();
            if (transactionDAO.insertTransaction(id, amount, Transaction.TransactionType.WITHDRAW) != 0
                && playerDAO.changeBalance(id, amount, Transaction.TransactionType.WITHDRAW)) {
                playerDAO.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }
}