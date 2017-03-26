package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.impl.DAOFactory;
import by.sasnouskikh.jcasino.dao.impl.PlayerDAOImpl;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.*;
import by.sasnouskikh.jcasino.mailer.MailerException;
import by.sasnouskikh.jcasino.mailer.MailerSSL;
import by.sasnouskikh.jcasino.manager.LoanManager;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.StatsManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
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

    static boolean initPlayerInfo(Player player) {
        return updateProfileInfo(player)
               & updateAccountInfo(player)
               & updateStatsInfo(player)
               & updateVerificationInfo(player);
    }

    public static boolean updateProfileInfo(Player player) {
        boolean updated = false;
        int     id      = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            PlayerProfile profile = playerDAO.takeProfile(player.getId());
            String        email   = playerDAO.defineEmailById(id);
            player.setProfile(profile);
            player.setEmail(email);
            updated = true;
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return updated;
    }

    public static boolean updateAccountInfo(Player player) {
        boolean updated = false;
        int     id      = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            PlayerAccount account     = playerDAO.takeAccount(id);
            Loan          currentLoan = playerDAO.takeCurrentLoan(id);
            account.setCurrentLoan(currentLoan);
            player.setAccount(account);
            updated = true;
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return updated;
    }

    public static boolean updateStatsInfo(Player player) {
        boolean updated = false;
        int     id      = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            List<Streak>      streaks      = playerDAO.takeStreaks(id);
            List<Transaction> transactions = playerDAO.takeTransactions(id);
            PlayerStats       playerStats  = StatsManager.buildStats(streaks, transactions);
            player.setStats(playerStats);
            updated = true;
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return updated;
    }

    public static boolean updateVerificationInfo(Player player) {
        boolean updated = false;
        int     id      = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            PlayerVerification playerVerification = playerDAO.takeVerification(id);
            player.setVerification(playerVerification);
            updated = true;
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return updated;
    }

    public static boolean updateQuestionsInfo(Player player) {
        int            id = player.getId();
        List<Question> questions;
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            questions = playerDAO.takeQuestions(id);
            player.setQuestions(questions);
            return true;
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static List<Transaction> takeTransactions(int id, String stringMonth, String all) {
        List<Transaction> transactionList = null;
        String[]          pair            = stringMonth.split(MONTH_SEPARATOR);
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            if (all != null) {
                transactionList = playerDAO.takeTransactions(id);
            } else {
                int year  = Integer.parseInt(pair[0]);
                int month = Integer.parseInt(pair[1]);
                transactionList = playerDAO.takeTransactions(id, month, year);
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return transactionList;
    }

    public static List<Loan> takeLoans(int id, String stringMonth, String all) {
        List<Loan> loanList = null;
        String[]   pair     = stringMonth.split(MONTH_SEPARATOR);
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            if (all != null) {
                loanList = playerDAO.takeLoans(id);
            } else {
                int year  = Integer.parseInt(pair[0]);
                int month = Integer.parseInt(pair[1]);
                loanList = playerDAO.takeLoans(id, month, year);
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return loanList;
    }

    public static List<Streak> takeStreaks(int id, String stringMonth, String all) {
        List<Streak> streakList = null;
        String[]     pair       = stringMonth.split(MONTH_SEPARATOR);
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            if (all != null) {
                streakList = playerDAO.takeStreaks(id);
            } else {
                int year  = Integer.parseInt(pair[0]);
                int month = Integer.parseInt(pair[1]);
                streakList = playerDAO.takeStreaks(id, month, year);
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return streakList;
    }

    public static boolean registerPlayer(String email, String password, String fName, String mName, String lName, String birthDate, String passport, String question, String answer) {
        email = email.trim().toLowerCase();
        password = JCasinoEncryptor.encryptMD5(password);
        fName = fName.trim().toUpperCase();
        mName = mName.trim().toUpperCase();
        lName = lName.trim().toUpperCase();
        passport = passport.toUpperCase();
        question = question.trim();
        answer = JCasinoEncryptor.encryptMD5(answer.trim());
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            WrappedConnection connection = playerDAO.getConnection();
            connection.setAutoCommit(false);
            if (playerDAO.insertUser(email, password)) {
                int id = playerDAO.defineIdByEmail(email);
                if (playerDAO.insertPlayer(id, fName, mName, lName, birthDate, passport, question, answer)
                    && playerDAO.insertEmptyVerification(id)) {
                    connection.commit();
                    return true;
                }
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error. " + e);
        }
        return false;
    }

    public static boolean changeProfileTextItem(Player player, String name, PlayerDAOImpl.ProfileField field) {
        name = name.trim().toUpperCase();
        PlayerVerification verification = player.getVerification();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            WrappedConnection connection = playerDAO.getConnection();
            connection.setAutoCommit(false);
            if (playerDAO.changeProfileTextItem(player, name, field)
                && playerDAO.changeVerificationStatus(verification, EMAIL_VER_MASK, PlayerDAOImpl.MaskOperation.AND)) {
                connection.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error. " + e);
        }
        return false;
    }

    public static boolean changeSecretQuestion(Player player, String question, String answer) {
        question = question.trim();
        answer = answer.trim();
        PlayerVerification verification = player.getVerification();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            WrappedConnection connection = playerDAO.getConnection();
            connection.setAutoCommit(false);
            if (playerDAO.changeSecretQuestion(player, question, answer)
                && playerDAO.changeVerificationStatus(verification, EMAIL_VER_MASK, PlayerDAOImpl.MaskOperation.AND)) {
                connection.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error. " + e);
        }
        return false;
    }

    public static boolean changeBirthdate(Player player, String birthDate) {
        PlayerVerification verification = player.getVerification();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            WrappedConnection connection = playerDAO.getConnection();
            connection.setAutoCommit(false);
            if (playerDAO.changeBirthdate(player, birthDate)
                && playerDAO.changeVerificationStatus(verification, EMAIL_VER_MASK, PlayerDAOImpl.MaskOperation.AND)) {
                connection.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error. " + e);
        }
        return false;
    }

    public static boolean changeEmail(Player player, String email) {
        email = email.trim().toLowerCase();
        PlayerVerification verification = player.getVerification();
        int                id           = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            if (playerDAO.defineIdByEmail(email) == 0) {
                return false;
            }
            WrappedConnection connection = playerDAO.getConnection();
            connection.setAutoCommit(false);
            if (playerDAO.changeEmail(id, email)
                && playerDAO.changeVerificationStatus(verification, (byte) ~EMAIL_VER_MASK, PlayerDAOImpl.MaskOperation.AND)) {
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error. " + e);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }

        return false;
    }

    public static boolean verifyProfile(Player player) {
        boolean            verified     = false;
        PlayerProfile      profile      = player.getProfile();
        PlayerVerification verification = player.getVerification();
        String             fName        = profile.getfName();
        String             lName        = profile.getlName();
        if (fName != null && !fName.isEmpty() && lName != null && !lName.isEmpty()) {
            try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
                verified = playerDAO.changeVerificationStatus(verification, PROFILE_VER_MASK, PlayerDAOImpl.MaskOperation.OR);
            } catch (ConnectionPoolException | DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
            }
        }
        return verified;
    }

    public static boolean sendEmailCode(Player player, String locale) {
        MessageManager     messageManager = new MessageManager(locale);
        String             emailTo        = player.getEmail();
        String             emailCode      = RandomGenerator.generateEmailCode();
        String             name           = player.getProfile().getfName();
        PlayerVerification verification   = player.getVerification();
        if (name == null || name.isEmpty()) {
            name = JCasinoUser.UserRole.PLAYER.toString();
        }
        try {
            String message = messageManager.getMessage(EMAIL_PATTERN_VERIFICATION_CODE) + WHITESPACE + emailCode;
            if (MailerSSL.sendEmail(name, message, emailTo)) {
                verification.setEmailCode(emailCode);
                return true;
            }
            return false;
        } catch (MailerException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean verifyEmail(Player player, String enteredCode, String sentCode) {
        PlayerVerification verification = player.getVerification();
        if (enteredCode != null && enteredCode.equals(sentCode)) {
            try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
                return playerDAO.changeVerificationStatus(verification, EMAIL_VER_MASK, PlayerDAOImpl.MaskOperation.OR);
            } catch (ConnectionPoolException | DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
            }
        }
        return false;
    }

    public static boolean replenishAccount(Player player, String stringAmount) {
        int        id     = player.getId();
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(stringAmount));
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            WrappedConnection connection = playerDAO.getConnection();
            connection.setAutoCommit(false);
            if (playerDAO.insertTransaction(id, amount, PlayerDAOImpl.TransactionType.REPLENISH)
                && playerDAO.changeBalance(id, amount, PlayerDAOImpl.TransactionType.REPLENISH)) {
                connection.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error. " + e);
        }
        return false;
    }

    public static boolean payLoan(Player player, BigDecimal amount) {
        int id     = player.getId();
        int loanId = player.getAccount().getCurrentLoan().getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            WrappedConnection connection = playerDAO.getConnection();
            connection.setAutoCommit(false);
            if (playerDAO.changeBalance(id, amount, PlayerDAOImpl.TransactionType.WITHDRAW)
                && playerDAO.payLoan(loanId, amount)) {
                connection.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error. " + e);
        }
        return false;
    }

    public static boolean takeNewLoan(Player player, BigDecimal amount) {
        int        id      = player.getId();
        BigDecimal percent = player.getAccount().getStatus().getLoanPercent();
        amount = LoanManager.countLoan(amount, percent);
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            WrappedConnection connection = playerDAO.getConnection();
            connection.setAutoCommit(false);
            if (playerDAO.insertLoan(id, amount, percent)
                && playerDAO.changeBalance(id, amount, PlayerDAOImpl.TransactionType.REPLENISH)) {
                connection.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error. " + e);
        }
        return false;
    }

    public static boolean withdrawMoney(Player player, BigDecimal amount) {
        int id = player.getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            WrappedConnection connection = playerDAO.getConnection();
            connection.setAutoCommit(false);
            if (playerDAO.insertTransaction(id, amount, PlayerDAOImpl.TransactionType.WITHDRAW)
                && playerDAO.changeBalance(id, amount, PlayerDAOImpl.TransactionType.WITHDRAW)) {
                connection.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error. " + e);
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
            throw new LogicException("Invalid file extension.");
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
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean rateSupportAnswer(int id, String satisfaction) {
        satisfaction = satisfaction.toLowerCase();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            return playerDAO.changeQuestionSatisfaction(id, satisfaction);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean resetSupportAnswerRating(int id) {
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            return playerDAO.changeQuestionSatisfaction(id, null);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }
}