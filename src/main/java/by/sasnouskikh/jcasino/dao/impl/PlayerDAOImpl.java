package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.PlayerProfile;
import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.manager.StreakManager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class PlayerDAOImpl extends AbstractDAO<Integer, Player> {

    private static final String SQL_DEFINE_ID_BY_EMAIL = "SELECT id FROM user " +
                                                         "WHERE email=?";
    private static final String SQL_DEFINE_EMAIL_BY_ID = "SELECT email FROM user " +
                                                         "WHERE id=?";

    private static final String SQL_PLAYER_PROFILE_SELECT   = "SELECT fname, mname, lname, birthdate, passport, question " +
                                                              "FROM player " +
                                                              "WHERE id=?";
    private static final String SQL_PLAYER_ACCOUNT_SELECT   = "SELECT balance, status, bet_limit, withdrawal_limit, loan_percent, max_loan_amount, ABS(SUM(amount)) AS total " +
                                                              "FROM player NATURAL JOIN player_status JOIN transaction ON player.id=player_id " +
                                                              "WHERE player.id=? AND MONTH(date)=MONTH(NOW()) AND YEAR(date)=YEAR(NOW()) AND amount<0";
    private static final String SQL_CURRENT_LOAN_SELECT     = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                              "FROM loan " +
                                                              "WHERE (amount>amount_paid) AND (player_id=?)";
    private static final String SQL_MONTH_WITHDRAWAL_SELECT = "SELECT ABS(SUM(amount)) AS total " +
                                                              "FROM transaction " +
                                                              "WHERE MONTH(date)=? AND YEAR(date)=? AND player_id=?";
    private static final String SQL_VERIFICATION_SELECT     = "SELECT id, status AS bin_status, admin_id, date, passport " +
                                                              "FROM verification " +
                                                              "WHERE id=?";
    private static final String SQL_QUESTIONS_SELECT        = "SELECT id, player_id, topic, question, q_date, answer, a_date, satisfaction " +
                                                              "FROM question " +
                                                              "WHERE player_id=?";

    private static final String SQL_TRANSACTIONS_SELECT_BY_ID    = "SELECT * FROM transaction " +
                                                                   "WHERE player_id=?";
    private static final String SQL_TRANSACTIONS_SELECT_BY_MONTH = "SELECT * FROM transaction " +
                                                                   "WHERE player_id=? AND MONTH(date)=? AND YEAR(date)=?";
    private static final String SQL_LOANS_SELECT_BY_ID           = "SELECT * FROM loan " +
                                                                   "WHERE player_id=?";
    private static final String SQL_LOANS_SELECT_BY_MONTH        = "SELECT * FROM loan " +
                                                                   "WHERE player_id=? AND MONTH(acquire)=? AND YEAR(acquire)=?";
    private static final String SQL_STREAKS_SELECT_BY_ID         = "SELECT * FROM streak " +
                                                                   "WHERE player_id=?";
    private static final String SQL_STREAKS_SELECT_BY_MONTH      = "SELECT * FROM streak " +
                                                                   "WHERE player_id=? AND MONTH(date)=? AND YEAR(date)=?";

    private static final String SQL_USER_INSERT         = "INSERT INTO user (email, password_md5, role, registration_date) " +
                                                          "VALUES (?, ?, 'player', NOW())";
    private static final String SQL_PLAYER_INSERT       = "INSERT INTO player (id, fname, mname, lname, birthdate, passport, question, answer_md5) " +
                                                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_VERIFICATION_INSERT = "INSERT INTO verification (id) " +
                                                          "VALUES (?)";
    private static final String SQL_LOAN_INSERT         = "INSERT INTO loan (player_id, amount, acquire, expire, percent) " +
                                                          "VALUES (?, ?, NOW(), ?, ?)";
    private static final String SQL_TRANSACTION_INSERT  = "INSERT INTO transaction (player_id, date, amount ) " +
                                                          "VALUES (?, NOW(), ?)";

    private static final String SQL_FNAME_UPDATE           = "UPDATE player " +
                                                             "SET fname=? " +
                                                             "WHERE id=?";
    private static final String SQL_MNAME_UPDATE           = "UPDATE player " +
                                                             "SET mname=? " +
                                                             "WHERE id=?";
    private static final String SQL_LNAME_UPDATE           = "UPDATE player " +
                                                             "SET lname=? " +
                                                             "WHERE id=?";
    private static final String SQL_PASSPORT_UPDATE        = "UPDATE player " +
                                                             "SET passport=? " +
                                                             "WHERE id=?";
    private static final String SQL_QUESTION_UPDATE        = "UPDATE player " +
                                                             "SET question=?, answer=? " +
                                                             "WHERE id=?";
    private static final String SQL_BIRTHDATE_UPDATE       = "UPDATE player " +
                                                             "SET birthdate=? " +
                                                             "WHERE id=?";
    private static final String SQL_EMAIL_UPDATE           = "UPDATE user " +
                                                             "SET email=? " +
                                                             "WHERE id=?";
    private static final String SQL_SCAN_PATH_UPDATE       = "UPDATE verification " +
                                                             "SET passport=? " +
                                                             "WHERE id=?";
    private static final String SQL_VER_STATUS_UPDATE      = "UPDATE verification " +
                                                             "SET status=? " +
                                                             "WHERE id=?";
    private static final String SQL_ACCOUNT_BALANCE_UPDATE = "UPDATE player " +
                                                             "SET balance=balance+? " +
                                                             "WHERE id=?";
    private static final String SQL_LOAN_UPDATE            = "UPDATE loan " +
                                                             "SET amount_paid=amount_paid+? " +
                                                             "WHERE id=?";
    private static final String SQL_SATISFACTION_UPDATE    = "UPDATE question " +
                                                             "SET satisfaction=? " +
                                                             "WHERE id=?";

    public enum ProfileField {
        FIRST_NAME, MIDDLE_NAME, LAST_NAME, PASSPORT
    }

    public enum MaskOperation {
        AND, OR
    }

    public enum TransactionType {
        REPLENISH, WITHDRAW
    }

    public int defineIdByEmail(String email) throws DAOException {
        int id;
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_ID_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            } else {
                throw new DAOException("No id associated with given email. Database error.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while defining id by e-mail. " + e);
        }
        return id;
    }

    public String defineEmailById(int id) throws DAOException {
        String email;
        try (PreparedStatement statement = connection.prepareStatement(SQL_DEFINE_EMAIL_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                email = resultSet.getString("email");
            } else {
                throw new DAOException("No user associated with given id. Database error.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while defining e-mail. " + e);
        }
        return email;
    }

    public PlayerProfile takeProfile(int id) throws DAOException {
        PlayerProfile profile;
        try (PreparedStatement statement = connection.prepareStatement(SQL_PLAYER_PROFILE_SELECT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                profile = new PlayerProfile();
                profile.setfName(resultSet.getString("fname"));
                profile.setmName(resultSet.getString("mname"));
                profile.setlName(resultSet.getString("lname"));
                profile.setBirthDate(resultSet.getDate("birthdate").toLocalDate());
                profile.setPassport(resultSet.getString("passport"));
                profile.setQuestion(resultSet.getString("question"));
            } else {
                throw new DAOException("No any profile associated with given id. Database error.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking player profile. " + e);
        }
        return profile;
    }

    public PlayerAccount takeAccount(int id) throws DAOException {
        PlayerAccount account;
        try (PreparedStatement statement = connection.prepareStatement(SQL_PLAYER_ACCOUNT_SELECT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
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
                status.setBetLimit(resultSet.getBigDecimal("bet_limit"));
                status.setWithdrawalLimit(resultSet.getBigDecimal("withdrawal_limit"));
                status.setLoanPercent(resultSet.getBigDecimal("loan_percent"));
                status.setMaxLoan(resultSet.getBigDecimal("max_loan_amount"));
                account.setStatus(status);
            } else {
                throw new DAOException("No any account associated with given id. Database error.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking player account. " + e);
        }
        return account;
    }

    public Loan takeCurrentLoan(int id) throws DAOException {
        Loan currentLoan = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_CURRENT_LOAN_SELECT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                currentLoan = new Loan();
                currentLoan.setPlayerId(id);
                currentLoan.setId(resultSet.getInt("id"));
                currentLoan.setPlayerId(resultSet.getInt("player_id"));
                currentLoan.setAmount(resultSet.getBigDecimal("amount"));
                currentLoan.setAcquire(resultSet.getDate("acquire").toLocalDate());
                currentLoan.setExpire(resultSet.getDate("expire").toLocalDate());
                currentLoan.setPercent(resultSet.getBigDecimal("percent"));
                currentLoan.setRest(resultSet.getBigDecimal("rest"));
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking current loan. " + e);
        }
        return currentLoan;
    }

    public BigDecimal takeMonthWithdrawal(int id, int month, int year) throws DAOException {
        BigDecimal monthWithdrawal = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL_MONTH_WITHDRAWAL_SELECT)) {
            statement.setInt(1, month);
            statement.setInt(2, year);
            statement.setInt(3, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                monthWithdrawal = resultSet.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking current month withdrawal. " + e);
        }
        return monthWithdrawal;
    }

    public PlayerVerification takeVerification(int id) throws DAOException {
        PlayerVerification playerVerification;
        try (PreparedStatement statement = connection.prepareStatement(SQL_VERIFICATION_SELECT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                playerVerification = buildPlayerVerification(resultSet);
            } else {
                throw new DAOException("No any verification associated with given id. Database error.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking verification. " + e);
        }
        return playerVerification;
    }

    private PlayerVerification buildPlayerVerification(ResultSet resultSet) throws SQLException {
        PlayerVerification verification = new PlayerVerification();
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
        Timestamp verificationDate = resultSet.getTimestamp("date");
        if (verificationDate != null) {
            verification.setVerificationDate(verificationDate.toLocalDateTime());
        }
        verification.setPassport(resultSet.getString("passport"));
        return verification;
    }

    public List<Transaction> takeTransactions(int id) throws DAOException {
        List<Transaction> transactionList;
        try (PreparedStatement statement = connection.prepareStatement(SQL_TRANSACTIONS_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            transactionList = buildTransactionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking transactions. " + e);
        }
        return transactionList;
    }

    public List<Transaction> takeTransactions(int id, int month, int year) throws DAOException {
        List<Transaction> transactionList;
        try (PreparedStatement statement = connection.prepareStatement(SQL_TRANSACTIONS_SELECT_BY_MONTH)) {
            statement.setInt(1, id);
            statement.setInt(2, month);
            statement.setInt(3, year);
            ResultSet resultSet = statement.executeQuery();
            transactionList = buildTransactionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking transactions. " + e);
        }
        return transactionList;
    }

    private List<Transaction> buildTransactionList(ResultSet resultSet) throws SQLException {
        List<Transaction> transactionList = new ArrayList<>();
        while (resultSet.next()) {
            Transaction transaction = new Transaction();
            transaction.setId(resultSet.getInt("id"));
            transaction.setPlayerId(resultSet.getInt("player_id"));
            transaction.setDate(resultSet.getTimestamp("date").toLocalDateTime());
            transaction.setAmount(resultSet.getBigDecimal("amount"));
            transactionList.add(transaction);
        }
        return transactionList;
    }

    public List<Loan> takeLoans(int id) throws DAOException {
        List<Loan> loanList;
        try (PreparedStatement statement = connection.prepareStatement(SQL_LOANS_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            loanList = buildLoanList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking loans. " + e);
        }
        return loanList;
    }

    public List<Loan> takeLoans(int id, int month, int year) throws DAOException {
        List<Loan> loanList;
        try (PreparedStatement statement = connection.prepareStatement(SQL_LOANS_SELECT_BY_MONTH)) {
            statement.setInt(1, id);
            statement.setInt(2, month);
            statement.setInt(3, year);
            ResultSet resultSet = statement.executeQuery();
            loanList = buildLoanList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking loans. " + e);
        }
        return loanList;
    }

    private List<Loan> buildLoanList(ResultSet resultSet) throws SQLException {
        List<Loan> loanList = new ArrayList<>();
        while (resultSet.next()) {
            Loan loan = new Loan();
            loan.setId(resultSet.getInt("id"));
            loan.setPlayerId(resultSet.getInt("player_id"));
            loan.setPercent(resultSet.getBigDecimal("percent"));
            loan.setAcquire(resultSet.getDate("acquire").toLocalDate());
            loan.setExpire(resultSet.getDate("expire").toLocalDate());
            BigDecimal amount  = resultSet.getBigDecimal("amount");
            BigDecimal paid    = resultSet.getBigDecimal("amount_paid");
            loan.setAmount(amount);
            loan.setRest(amount.subtract(paid));
            loanList.add(loan);
        }
        return loanList;
    }

    public List<Streak> takeStreaks(int id) throws DAOException {
        List<Streak> streakList;
        try (PreparedStatement statement = connection.prepareStatement(SQL_STREAKS_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            streakList = buildStreakList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking streaks. " + e);
        }
        return streakList;
    }

    public List<Streak> takeStreaks(int id, int month, int year) throws DAOException {
        List<Streak> streakList;
        try (PreparedStatement statement = connection.prepareStatement(SQL_STREAKS_SELECT_BY_MONTH)) {
            statement.setInt(1, id);
            statement.setInt(2, month);
            statement.setInt(3, year);
            ResultSet resultSet = statement.executeQuery();
            streakList = buildStreakList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking streaks. " + e);
        }
        return streakList;
    }

    private List<Streak> buildStreakList(ResultSet resultSet) throws SQLException {
        List<Streak> streakList = new ArrayList<>();
        while (resultSet.next()) {
            Streak streak = new Streak();
            streak.setId(resultSet.getInt("id"));
            streak.setPlayerId(resultSet.getInt("player_id"));
            streak.setDate(resultSet.getTimestamp("date").toLocalDateTime());
            streak.setRoll(resultSet.getString("roll"));
            streak.setRollMD5(resultSet.getString("roll_md5"));
            streak.setOffset(resultSet.getString("offset"));
            streak.setLines(resultSet.getString("lines"));
            streak.setBet(resultSet.getString("bet"));
            streak.setResult(resultSet.getString("result"));
            streak.setRolls(StreakManager.buildRollList(streak));
            streakList.add(streak);
        }
        return streakList;
    }

    public List<Question> takeQuestions(int id) throws DAOException {
        List<Question> questions;
        try (PreparedStatement statement = connection.prepareStatement(SQL_QUESTIONS_SELECT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            questions = buildQuestionList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking questions. " + e);
        }
        return questions;
    }

    private List<Question> buildQuestionList(ResultSet resultSet) throws SQLException {
        List<Question> questions = new ArrayList<>();
        while (resultSet.next()) {
            Question question = new Question();
            question.setId(resultSet.getInt("id"));
            question.setPlayerId(resultSet.getInt("player_id"));
            question.setTopic(Question.QuestionTopic.valueOf(resultSet.getString("topic").toUpperCase()));
            question.setQuestionDate(resultSet.getTimestamp("q_date").toLocalDateTime());
            question.setQuestion(resultSet.getString("question"));
            Timestamp answerDate = resultSet.getTimestamp("a_date");
            if (answerDate != null) {
                question.setAnswerDate(answerDate.toLocalDateTime());
            }
            question.setAnswer(resultSet.getString("answer"));
            String satisfaction = resultSet.getString("satisfaction");
            if (satisfaction != null) {
                question.setSatisfaction(Question.Satisfaction.valueOf(satisfaction.toUpperCase()));
            }
            questions.add(question);
        }
        return questions;
    }

    public boolean insertUser(String email, String password) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_USER_INSERT)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting user. " + e);
        }
        return true;
    }

    public boolean insertPlayer(int id, String fName, String mName, String lName, String birthDate, String passport, String question, String answer) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_PLAYER_INSERT)) {
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
        try (PreparedStatement statement = connection.prepareStatement(SQL_VERIFICATION_INSERT)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting empty verification. " + e);
        }
        return true;
    }

    public boolean insertTransaction(int id, BigDecimal amount, TransactionType type) throws DAOException {
        if (type == TransactionType.WITHDRAW) {
            amount = amount.negate();
        }
        try (PreparedStatement statement = connection.prepareStatement(SQL_TRANSACTION_INSERT)) {
            statement.setInt(1, id);
            statement.setBigDecimal(2, amount);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting transaction. " + e);
        }
    }

    public boolean insertLoan(int id, BigDecimal amount, BigDecimal percent) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_LOAN_INSERT)) {
            statement.setInt(1, id);
            statement.setBigDecimal(2, amount);
            statement.setDate(3, Date.valueOf(LocalDate.now().plusMonths(LOAN_TERM_MONTH)));
            statement.setBigDecimal(4, percent);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting loan. " + e);
        }
    }

    public boolean changeProfileTextItem(Player player, String text, ProfileField field) throws DAOException {
        String sql;
        switch (field) {
            case FIRST_NAME:
                sql = SQL_FNAME_UPDATE;
                break;
            case MIDDLE_NAME:
                sql = SQL_MNAME_UPDATE;
                break;
            case LAST_NAME:
                sql = SQL_LNAME_UPDATE;
                break;
            case PASSPORT:
                sql = SQL_PASSPORT_UPDATE;
                break;
            default:
                throw new DAOException("Method not realized for given ProfileField parameter.");
        }
        int id = player.getId();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, text);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + player.getId() + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing profile text item. " + e);
        }
    }

    public boolean changeSecretQuestion(Player player, String question, String answer) throws DAOException {
        int id = player.getId();
        try (PreparedStatement statement = connection.prepareStatement(SQL_QUESTION_UPDATE)) {
            statement.setString(1, question);
            statement.setString(2, answer);
            statement.setInt(3, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + player.getId() + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing secret question. " + e);
        }
    }

    public boolean changeBirthdate(Player player, String birthdate) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_BIRTHDATE_UPDATE)) {
            statement.setDate(1, Date.valueOf(birthdate));
            statement.setInt(2, player.getId());
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + player.getId() + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing birthdate. " + e);
        }
    }

    public boolean changeEmail(int id, String email) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EMAIL_UPDATE)) {
            statement.setString(1, email);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 user associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing email. " + e);
        }
    }

    public boolean changeVerificationStatus(PlayerVerification verification, byte changeMask, MaskOperation operation) throws DAOException {
        byte status = 0;
        int  id     = verification.getPlayerId();

        if (verification.getStatus() == PlayerVerification.VerificationStatus.VERIFIED) {
            status = FULL_VER_MASK;
        } else {
            if (verification.isProfileVerified()) {
                status |= PROFILE_VER_MASK;
            }
            if (verification.isEmailVerified()) {
                status |= EMAIL_VER_MASK;
            }
            if (verification.isScanVerified()) {
                status |= PASSPORT_VER_MASK;
            }
        }
        byte newStatus;
        if (operation == MaskOperation.AND) {
            newStatus = (byte) (status & changeMask);
        } else {
            newStatus = (byte) (status | changeMask);
        }
        try (PreparedStatement statement = connection.prepareStatement(SQL_VER_STATUS_UPDATE)) {
            statement.setByte(1, newStatus);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 player associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while updating verification status. " + e);
        }
    }

    public boolean changeBalance(int id, BigDecimal amount, TransactionType type) throws DAOException {
        if (type == TransactionType.WITHDRAW) {
            amount = amount.negate();
        }
        try (PreparedStatement statement = connection.prepareStatement(SQL_ACCOUNT_BALANCE_UPDATE)) {
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

    public boolean payLoan(int loanId, BigDecimal amount) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_LOAN_UPDATE)) {
            statement.setBigDecimal(1, amount);
            statement.setInt(2, loanId);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 loan associated with given id: '" + loanId + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while paying loan. " + e);
        }
    }

    public boolean changeScanPath(int id, String path) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SCAN_PATH_UPDATE)) {
            statement.setString(1, path);
            statement.setInt(2, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing passport scan path. " + e);
        }
    }

    public boolean changeQuestionSatisfaction(int id, String satisfaction) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SATISFACTION_UPDATE)) {
            statement.setString(1, satisfaction);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 question associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing question satisfaction. " + e);
        }
    }
}