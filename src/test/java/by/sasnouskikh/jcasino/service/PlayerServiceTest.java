package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.LoanDAO;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.StreakDAO;
import by.sasnouskikh.jcasino.dao.TransactionDAO;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyByte;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class PlayerServiceTest {

    private static final PlayerProfile      PROFILE          = new PlayerProfile();
    private static final PlayerAccount      ACCOUNT          = new PlayerAccount();
    private static final Loan               CURRENT_LOAN     = new Loan();
    private static final List<Streak>       STREAK_LIST      = new ArrayList<>();
    private static final Streak             STREAK           = new Streak();
    private static final List<Transaction>  TRANSACTION_LIST = new ArrayList<>();
    private static final Transaction        TRANSACTION      = new Transaction();
    private static final PlayerStats        STATS            = new PlayerStats();
    private static final PlayerVerification VERIFICATION     = new PlayerVerification();

    private static final int    LOAN_ID            = 24;
    private static final int    STREAK_ID          = 12;
    private static final int    TRANSACTION_ID     = 45;
    private static final int    PLAYER_ID          = 7;
    private static final String EMAIL              = "any@MAil.net";
    private static final String PASSWORD           = "anyPa55w0rd";
    private static final String PASSWORD_MD5       = "1d79d14d00506d66f0ec1ea9847776a0";
    private static final String OLD_PASSWORD       = "anyOldPa55w0rd";
    private static final String OLD_PASSWORD_MD5   = "4c19d7c3aaac6afd04b0b586585a9315";
    private static final String NAME               = "anyName";
    private static final String BIRTHDATE          = "1991-24-09";
    private static final String PASSPORT           = "KH1731245";
    private static final String QUESTION           = "anyQuestion";
    private static final String ANSWER             = "anyAnswer";
    private static final String ANSWER_MD5         = "947ce5aa67c9b59556a70ac5ac4fee1d";
    private static final String LOCALE             = "anyLocale";
    private static final String EMAIL_CODE         = "anyCode";
    private static final String EMAIL_CODE_INVALID = "anyInvalidCode";


    static {
        PROFILE.setfName(NAME);

        ACCOUNT.setCurrentLoan(CURRENT_LOAN);
        ACCOUNT.setBalance(BigDecimal.TEN);

        CURRENT_LOAN.setId(LOAN_ID);

        STREAK.setId(STREAK_ID);
        TRANSACTION.setId(TRANSACTION_ID);

        STREAK_LIST.add(STREAK);
        TRANSACTION_LIST.add(TRANSACTION);

        STATS.setTotalBet(BigDecimal.ONE);

        VERIFICATION.setPlayerId(PLAYER_ID);
    }

    private Player player;

    @Mock
    private PlayerDAO      playerDAO;
    @Mock
    private UserDAO        userDAO;
    @Mock
    private LoanDAO        loanDAO;
    @Mock
    private StreakDAO      streakDAO;
    @Mock
    private TransactionDAO transactionDAO;
    @Mock
    private DAOHelper      daoHelper;
    @InjectMocks
    private PlayerService  playerService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(daoHelper.getPlayerDAO()).thenReturn(playerDAO);
        when(daoHelper.getUserDAO()).thenReturn(userDAO);
        when(daoHelper.getLoanDAO()).thenReturn(loanDAO);
        when(daoHelper.getStreakDAO()).thenReturn(streakDAO);
        when(daoHelper.getTransactionDAO()).thenReturn(transactionDAO);
        player = new Player();
        player.setId(PLAYER_ID);
    }

    @After
    public void tearDown() {
        player = null;
    }

    @Test
    public void updateProfileInfoPlayerDAOTakeProfileCallCheck() throws DAOException {
        when(playerDAO.takeProfile(anyInt())).thenReturn(PROFILE);
        when(playerDAO.defineEmailById(anyInt())).thenReturn(EMAIL);
        playerService.updateProfileInfo(player);

        verify(playerDAO).takeProfile(PLAYER_ID);
    }

    @Test
    public void updateProfileInfoPlayerDAODefineEmailCallCheck() throws DAOException {
        when(playerDAO.takeProfile(anyInt())).thenReturn(PROFILE);
        when(playerDAO.defineEmailById(anyInt())).thenReturn(EMAIL);
        playerService.updateProfileInfo(player);

        verify(playerDAO).defineEmailById(PLAYER_ID);
    }

    @Test
    public void updateProfileInfoUpdateEffectCheck() throws DAOException {
        player.setProfile(PROFILE);
        player.setEmail(EMAIL);

        Player actual = new Player();
        actual.setId(PLAYER_ID);

        when(playerDAO.takeProfile(anyInt())).thenReturn(PROFILE);
        when(playerDAO.defineEmailById(anyInt())).thenReturn(EMAIL);
        playerService.updateProfileInfo(actual);

        Assert.assertEquals(player, actual);
    }

    @Test
    public void updateProfileInfoReturnTrueCheck() throws DAOException {
        when(playerDAO.takeProfile(anyInt())).thenReturn(PROFILE);
        when(playerDAO.defineEmailById(anyInt())).thenReturn(EMAIL);

        Assert.assertTrue(playerService.updateProfileInfo(player));
    }

    @Test
    public void updateProfileInfoDAOExceptionThrownOnTakeProfileReturnFalseCheck() throws DAOException {
        when(playerDAO.takeProfile(anyInt())).thenThrow(new DAOException("Database connection error."));
        when(playerDAO.defineEmailById(anyInt())).thenReturn(EMAIL);

        Assert.assertFalse(playerService.updateProfileInfo(player));
    }

    @Test
    public void updateProfileInfoDAOExceptionThrownOnDefineEmailReturnFalseCheck() throws DAOException {
        when(playerDAO.takeProfile(anyInt())).thenReturn(PROFILE);
        when(playerDAO.defineEmailById(anyInt())).thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.updateProfileInfo(player));
    }

    @Test
    public void updateAccountInfoPlayerDAOTakeAccountCallCheck() throws DAOException {
        when(playerDAO.takeAccount(anyInt())).thenReturn(ACCOUNT);
        when(loanDAO.takeCurrentLoan(anyInt())).thenReturn(CURRENT_LOAN);
        playerService.updateAccountInfo(player);

        verify(playerDAO).takeAccount(PLAYER_ID);
    }

    @Test
    public void updateAccountInfoLoanDAOTakeCurrentLoanCallCheck() throws DAOException {
        when(playerDAO.takeAccount(anyInt())).thenReturn(ACCOUNT);
        when(loanDAO.takeCurrentLoan(anyInt())).thenReturn(CURRENT_LOAN);
        playerService.updateAccountInfo(player);

        verify(loanDAO).takeCurrentLoan(PLAYER_ID);
    }

    @Test
    public void updateAccountInfoUpdateEffectCheck() throws DAOException {
        player.setAccount(ACCOUNT);

        Player actual = new Player();
        actual.setId(PLAYER_ID);

        when(playerDAO.takeAccount(anyInt())).thenReturn(ACCOUNT);
        when(loanDAO.takeCurrentLoan(anyInt())).thenReturn(CURRENT_LOAN);
        playerService.updateAccountInfo(actual);

        Assert.assertEquals(player, actual);
    }

    @Test
    public void updateAccountInfoReturnTrueCheck() throws DAOException {
        when(playerDAO.takeAccount(anyInt())).thenReturn(ACCOUNT);
        when(loanDAO.takeCurrentLoan(anyInt())).thenReturn(CURRENT_LOAN);

        Assert.assertTrue(playerService.updateAccountInfo(player));
    }

    @Test
    public void updateAccountInfoDAOExceptionThrownOnTakeAccountReturnFalseCheck() throws DAOException {
        when(playerDAO.takeAccount(anyInt())).thenThrow(new DAOException("Database connection error."));
        when(loanDAO.takeCurrentLoan(anyInt())).thenReturn(CURRENT_LOAN);

        Assert.assertFalse(playerService.updateAccountInfo(player));
    }

    @Test
    public void updateAccountInfoDAOExceptionThrownOnTakeCurrentLoanReturnFalseCheck() throws DAOException {
        when(playerDAO.takeAccount(anyInt())).thenReturn(ACCOUNT);
        when(loanDAO.takeCurrentLoan(anyInt())).thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.updateAccountInfo(player));
    }

    @Test
    @PrepareForTest(StatsHelper.class)
    public void updateStatsInfoStreakDAOTakePlayerStreaksCallCheck() throws Exception {
        when(streakDAO.takePlayerStreaks(anyInt())).thenReturn(STREAK_LIST);
        when(transactionDAO.takePlayerTransactions(anyInt())).thenReturn(TRANSACTION_LIST);
        PowerMockito.mockStatic(StatsHelper.class);
        PowerMockito.when(StatsHelper.class, "buildStats", any(List.class), any(List.class)).thenReturn(STATS);
        playerService.updateStatsInfo(player);

        verify(streakDAO).takePlayerStreaks(PLAYER_ID);
    }

    @Test
    @PrepareForTest(StatsHelper.class)
    public void updateStatsInfoTransactionDAOTakePlayerTransactionsCallCheck() throws Exception {
        when(streakDAO.takePlayerStreaks(anyInt())).thenReturn(STREAK_LIST);
        when(transactionDAO.takePlayerTransactions(anyInt())).thenReturn(TRANSACTION_LIST);
        PowerMockito.mockStatic(StatsHelper.class);
        PowerMockito.when(StatsHelper.class, "buildStats", any(List.class), any(List.class)).thenReturn(STATS);
        playerService.updateStatsInfo(player);

        verify(transactionDAO).takePlayerTransactions(PLAYER_ID);
    }

    @Test
    @PrepareForTest(StatsHelper.class)
    public void updateStatsInfoTransactionHelperBuildStatsCallCheck() throws Exception {
        when(streakDAO.takePlayerStreaks(anyInt())).thenReturn(STREAK_LIST);
        when(transactionDAO.takePlayerTransactions(anyInt())).thenReturn(TRANSACTION_LIST);
        PowerMockito.mockStatic(StatsHelper.class);
        PowerMockito.when(StatsHelper.class, "buildStats", any(List.class), any(List.class)).thenReturn(STATS);
        playerService.updateStatsInfo(player);

        PowerMockito.verifyPrivate(StatsHelper.class).invoke("buildStats", STREAK_LIST, TRANSACTION_LIST);
    }

    @Test
    @PrepareForTest(StatsHelper.class)
    public void updateStatsInfoUpdateEffectCheck() throws Exception {
        player.setStats(STATS);

        Player actual = new Player();
        actual.setId(PLAYER_ID);

        when(streakDAO.takePlayerStreaks(anyInt())).thenReturn(STREAK_LIST);
        when(transactionDAO.takePlayerTransactions(anyInt())).thenReturn(TRANSACTION_LIST);
        PowerMockito.mockStatic(StatsHelper.class);
        PowerMockito.when(StatsHelper.class, "buildStats", any(List.class), any(List.class)).thenReturn(STATS);
        playerService.updateStatsInfo(actual);

        Assert.assertEquals(player, actual);
    }

    @Test
    @PrepareForTest(StatsHelper.class)
    public void updateStatsInfoReturnTrueCheck() throws Exception {
        when(streakDAO.takePlayerStreaks(anyInt())).thenReturn(STREAK_LIST);
        when(transactionDAO.takePlayerTransactions(anyInt())).thenReturn(TRANSACTION_LIST);
        PowerMockito.mockStatic(StatsHelper.class);
        PowerMockito.when(StatsHelper.class, "buildStats", any(List.class), any(List.class)).thenReturn(STATS);

        Assert.assertTrue(playerService.updateStatsInfo(player));
    }

    @Test
    @PrepareForTest(StatsHelper.class)
    public void updateStatsInfoDAOExceptionThrownOnTakePlayerStreaksReturnFalseCheck() throws Exception {
        when(streakDAO.takePlayerStreaks(anyInt())).thenThrow(new DAOException("Database connection error."));
        when(transactionDAO.takePlayerTransactions(anyInt())).thenReturn(TRANSACTION_LIST);
        PowerMockito.mockStatic(StatsHelper.class);
        PowerMockito.when(StatsHelper.class, "buildStats", any(List.class), any(List.class)).thenReturn(STATS);

        Assert.assertFalse(playerService.updateStatsInfo(player));
    }

    @Test
    @PrepareForTest(StatsHelper.class)
    public void updateStatsInfoDAOExceptionThrownOnTakePlayerTransactionsReturnFalseCheck() throws Exception {
        when(streakDAO.takePlayerStreaks(anyInt())).thenReturn(STREAK_LIST);
        when(transactionDAO.takePlayerTransactions(anyInt())).thenThrow(new DAOException("Database connection error."));
        PowerMockito.mockStatic(StatsHelper.class);
        PowerMockito.when(StatsHelper.class, "buildStats", any(List.class), any(List.class)).thenReturn(STATS);

        Assert.assertFalse(playerService.updateStatsInfo(player));
    }

    @Test
    public void updateVerificationInfoPlayerDAOTakeVerificationCallCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION);
        playerService.updateVerificationInfo(player);

        verify(playerDAO).takeVerification(PLAYER_ID);
    }

    @Test
    public void updateVerificationInfoUpdateEffectCheck() throws DAOException {
        player.setVerification(VERIFICATION);

        Player actual = new Player();
        actual.setId(PLAYER_ID);

        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION);
        playerService.updateVerificationInfo(actual);

        Assert.assertEquals(player, actual);
    }

    @Test
    public void updateVerificationInfoReturnTrueCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION);

        Assert.assertTrue(playerService.updateVerificationInfo(player));
    }

    @Test
    public void updateVerificationInfoDAOExceptionThrownReturnFalseCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.updateVerificationInfo(player));
    }

    @Test
    public void registerPlayerPlayerDAOInsertUserPlayerCallCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(true);
        playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE, PASSPORT, QUESTION, ANSWER);

        verify(playerDAO).insertUserPlayer(EMAIL.toLowerCase(), PASSWORD_MD5);
    }

    @Test
    public void registerPlayerPlayerDAOInsertPlayerCallCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(true);
        playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE, PASSPORT, QUESTION, ANSWER);

        verify(playerDAO).insertPlayer(PLAYER_ID, NAME.toUpperCase(), NAME.toUpperCase(), NAME.toUpperCase(),
                                       BIRTHDATE, PASSPORT.toUpperCase(), QUESTION, ANSWER_MD5);
    }

    @Test
    public void registerPlayerPlayerDAOInsertEmptyVerificationCallCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(true);
        playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE, PASSPORT, QUESTION, ANSWER);

        verify(playerDAO).insertEmptyVerification(PLAYER_ID);
    }

    @Test
    public void registerPlayerReturnTrueCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(true);

        Assert.assertTrue(playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE,
                                                       PASSPORT, QUESTION, ANSWER));
    }

    @Test
    public void registerPlayerPlayerDAOInsertUserPlayerReturnZeroCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(0);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(true);

        Assert.assertFalse(playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE,
                                                        PASSPORT, QUESTION, ANSWER));
    }

    @Test
    public void registerPlayerPlayerDAOInsertPlayerReturnFalseCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(false);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(true);

        Assert.assertFalse(playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE,
                                                        PASSPORT, QUESTION, ANSWER));
    }

    @Test
    public void registerPlayerPlayerDAOInsertEmptyVerificationReturnFalseCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(false);

        Assert.assertFalse(playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE,
                                                        PASSPORT, QUESTION, ANSWER));
    }

    @Test
    public void registerPlayerDAOExceptionThrownOnInsertUserPlayerReturnFalseCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString()))
        .thenThrow(new DAOException("Database connection error."));
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(false);

        Assert.assertFalse(playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE,
                                                        PASSPORT, QUESTION, ANSWER));
    }

    @Test
    public void registerPlayerDAOExceptionThrownOnInsertPlayerReturnFalseCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString()))
        .thenThrow(new DAOException("Database connection error."));
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(false);

        Assert.assertFalse(playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE,
                                                        PASSPORT, QUESTION, ANSWER));
    }

    @Test
    public void registerPlayerDAOExceptionThrownOnInsertEmptyVerificationReturnFalseCheck() throws DAOException {
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE,
                                                        PASSPORT, QUESTION, ANSWER));
    }

    @Test
    public void registerPlayerSQLExceptionThrownOnDAOHelperBeginTransactionReturnFalseCheck() throws DAOException,
                                                                                                     SQLException {
        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(true);

        Assert.assertFalse(playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE,
                                                        PASSPORT, QUESTION, ANSWER));
    }

    @Test
    public void registerPlayerSQLExceptionThrownOnDAOHelperCommitReturnFalseCheck() throws DAOException, SQLException {
        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();
        when(playerDAO.insertUserPlayer(anyString(), anyString())).thenReturn(PLAYER_ID);
        when(playerDAO.insertPlayer(anyInt(), anyString(), anyString(), anyString(), anyString(),
                                    anyString(), anyString(), anyString())).thenReturn(true);
        when(playerDAO.insertEmptyVerification(anyInt())).thenReturn(true);

        Assert.assertFalse(playerService.registerPlayer(EMAIL, PASSWORD, NAME, NAME, NAME, BIRTHDATE,
                                                        PASSPORT, QUESTION, ANSWER));
    }

    @Test
    public void changeEmailUserDAOCheckEmailExistCallCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeEmail(player, EMAIL);

        verify(userDAO).checkEmailExist(EMAIL.toLowerCase());
    }

    @Test
    public void changeEmailPlayerDAOChangeEmailCallCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeEmail(player, EMAIL);

        verify(playerDAO).changeEmail(PLAYER_ID, EMAIL.toLowerCase());
    }

    @Test
    public void changeEmailPlayerDAOChangeVerificationStatusCallCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeEmail(player, EMAIL);

        verify(playerDAO).changeVerificationStatus(PLAYER_ID, (byte) 0b001);
    }

    @Test
    public void changeEmailReturnTrueCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertTrue(playerService.changeEmail(player, EMAIL));
    }

    @Test
    public void changeEmailUserDAOCheckEmailExistReturnTrueCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeEmail(player, EMAIL));
    }

    @Test
    public void changeEmailPlayerDAOChangeEmailReturnFalseCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(false);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeEmail(player, EMAIL));
    }

    @Test
    public void changeEmailPlayerDAOChangeVerificationStatusReturnFalseCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(false);

        Assert.assertFalse(playerService.changeEmail(player, EMAIL));
    }

    @Test
    public void changeEmailDAOExceptionThrownOnCheckEmailExistReturnFalseCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeEmail(player, EMAIL));
    }

    @Test
    public void changeEmailDAOExceptionThrownOnChangeEmailReturnFalseCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeEmail(player, EMAIL));
    }

    @Test
    public void changeEmailDAOExceptionThrownOnChangeVerificationStatusReturnFalseCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.changeEmail(player, EMAIL));
    }

    @Test
    public void changeEmailSQLExceptionThrownOnDAOHelperBeginTransactionReturnFalseCheck() throws DAOException,
                                                                                                  SQLException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();
        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeEmail(player, EMAIL));
    }

    @Test
    public void changeEmailSQLExceptionThrownOnDAOHelperCommitReturnFalseCheck() throws DAOException, SQLException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();
        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(playerDAO.changeEmail(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeEmail(player, EMAIL));
    }

    @Test
    public void changePasswordUserDAOCheckPasswordCallCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        playerService.changePassword(player, OLD_PASSWORD, PASSWORD);

        verify(userDAO).checkPassword(PLAYER_ID, OLD_PASSWORD_MD5);
    }

    @Test
    public void changePasswordPlayerDAOChangePasswordCallCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        playerService.changePassword(player, OLD_PASSWORD, PASSWORD);

        verify(playerDAO).changePassword(PLAYER_ID, PASSWORD_MD5);
    }

    @Test
    public void changePasswordReturnTrueCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);

        Assert.assertTrue(playerService.changePassword(player, OLD_PASSWORD, PASSWORD));
    }

    @Test
    public void changePasswordCheckPasswordReturnFalseCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenReturn(false);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.changePassword(player, OLD_PASSWORD, PASSWORD));
    }

    @Test
    public void changePasswordChangePasswordReturnFalseCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(false);

        Assert.assertFalse(playerService.changePassword(player, OLD_PASSWORD, PASSWORD));
    }

    @Test
    public void changeEmailDAOExceptionThrownOnCheckPasswordReturnFalseCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.changePassword(player, OLD_PASSWORD, PASSWORD));
    }

    @Test
    public void changeEmailDAOExceptionThrownOnChangePasswordReturnFalseCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changePassword(anyInt(), anyString())).thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.changePassword(player, OLD_PASSWORD, PASSWORD));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordUserDAOCheckEmailExistCallCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        playerService.recoverPassword(EMAIL, LOCALE);

        verify(userDAO).checkEmailExist(EMAIL.toLowerCase());
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordUserDAOTakeUserCallCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        playerService.recoverPassword(EMAIL, LOCALE);

        verify(userDAO).takeUser(EMAIL.toLowerCase());
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordPlayerDAOChangePasswordCallCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        playerService.recoverPassword(EMAIL, LOCALE);

        verify(playerDAO).changePassword(eq(PLAYER_ID), anyString());
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordPlayerDAODefineNameByEmailCallCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        playerService.recoverPassword(EMAIL, LOCALE);

        verify(playerDAO).defineNameByEmail(EMAIL.toLowerCase());
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordMailerSSLSendEmailCallCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        playerService.recoverPassword(EMAIL, LOCALE);

        PowerMockito.verifyStatic();
        MailerSSL.sendEmail(eq(NAME), anyString(), eq(EMAIL.toLowerCase()));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordReturnTrueCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertTrue(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordCheckEmailExistReturnFalseCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(false);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordTakeUserReturnNullCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(null);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordChangePasswordReturnFalseCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(false);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordDefineNameByEmailReturnNullCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(null);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertTrue(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordSendEmailReturnFalseCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(false);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordDAOExceptionThrownOnCheckEmailExistReturnFalseCheck() throws DAOException,
                                                                                            MailerException {
        when(userDAO.checkEmailExist(anyString())).thenThrow(new DAOException("Database connection error."));
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordDAOExceptionThrownOnTakeUserReturnFalseCheck() throws DAOException,
                                                                                     MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordDAOExceptionThrownOnChangePasswordReturnFalseCheck() throws DAOException,
                                                                                           MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenThrow(new DAOException("Database connection error."));
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordDAOExceptionThrownOnDefineNameByEmailReturnFalseCheck() throws DAOException,
                                                                                              MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenThrow(new DAOException("Database connection error."));
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordMailerExceptionThrownOnSendEmailReturnFalseCheck() throws DAOException, MailerException {
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString()))
                    .thenThrow(new MailerException("Error occurred while sending e-mail."));

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordSQLExceptionThrownOnDAOHelperBeginTransactionFalseCheck()
    throws DAOException, MailerException, SQLException {
        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    @PrepareForTest(MailerSSL.class)
    public void recoverPasswordSQLExceptionThrownOnDAOHelperCommitFalseCheck()
    throws DAOException, MailerException, SQLException {
        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();
        when(userDAO.checkEmailExist(anyString())).thenReturn(true);
        when(userDAO.takeUser(anyString())).thenReturn(player);
        when(playerDAO.changePassword(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.defineNameByEmail(anyString())).thenReturn(NAME);
        PowerMockito.mockStatic(MailerSSL.class);
        PowerMockito.when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        Assert.assertFalse(playerService.recoverPassword(EMAIL, LOCALE));
    }

    @Test
    public void changeProfileTextItemPlayerDAOChangeFirstNameCallCheck() throws DAOException, ServiceException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeFirstName(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeProfileTextItem(player, NAME, PlayerService.ProfileTextField.FIRST_NAME);

        verify(playerDAO).changeFirstName(PLAYER_ID, NAME.toUpperCase());
    }

    @Test
    public void changeProfileTextItemPlayerDAOChangeMiddleNameCallCheck() throws DAOException, ServiceException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeMiddleName(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeProfileTextItem(player, NAME, PlayerService.ProfileTextField.MIDDLE_NAME);

        verify(playerDAO).changeMiddleName(PLAYER_ID, NAME.toUpperCase());
    }

    @Test
    public void changeProfileTextItemPlayerDAOChangeLastNameCallCheck() throws DAOException, ServiceException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeLastName(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeProfileTextItem(player, NAME, PlayerService.ProfileTextField.LAST_NAME);

        verify(playerDAO).changeLastName(PLAYER_ID, NAME.toUpperCase());
    }

    @Test
    public void changeProfileTextItemPlayerDAOChangePassportNumberCallCheck() throws DAOException, ServiceException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changePassportNumber(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeProfileTextItem(player, PASSPORT, PlayerService.ProfileTextField.PASSPORT);

        verify(playerDAO).changePassportNumber(PLAYER_ID, PASSPORT.toUpperCase());
    }

    @Test
    public void changeProfileTextItemPlayerDAOChangeVerificationStatusCallCheck() throws DAOException, ServiceException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeFirstName(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeProfileTextItem(player, NAME, PlayerService.ProfileTextField.FIRST_NAME);

        verify(playerDAO).changeVerificationStatus(PLAYER_ID, (byte) 0b010);
    }

    @Test
    public void changeProfileTextItemDAOExceptionThrownOnChangeFirstNameReturnFalseCheck()
    throws DAOException, ServiceException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeFirstName(anyInt(), anyString())).thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeProfileTextItem(player, NAME, PlayerService.ProfileTextField.FIRST_NAME));
    }

    @Test
    public void changeProfileTextItemDAOExceptionThrownOnChangeVerificationStatusReturnFalseCheck()
    throws DAOException, ServiceException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeFirstName(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.changeProfileTextItem(player, NAME, PlayerService.ProfileTextField.FIRST_NAME));
    }

    @Test
    public void changeProfileTextItemReturnTrueCheck() throws DAOException, ServiceException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeFirstName(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertTrue(playerService.changeProfileTextItem(player, NAME, PlayerService.ProfileTextField.FIRST_NAME));
    }

    @Test
    public void changeProfileTextItemSQLExceptionThrownOnDAOHelperBeginTransactionReturnFalseCheck()
    throws DAOException, ServiceException, SQLException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();
        when(playerDAO.changeFirstName(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeProfileTextItem(player, NAME, PlayerService.ProfileTextField.FIRST_NAME));
    }

    @Test
    public void changeProfileTextItemSQLExceptionThrownOnDAOHelperCommitReturnFalseCheck()
    throws DAOException, ServiceException, SQLException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();
        when(playerDAO.changeFirstName(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeProfileTextItem(player, NAME, PlayerService.ProfileTextField.FIRST_NAME));
    }

    @Test
    public void changeBirthdatePlayerDAOChangeBirthdateCallCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeBirthdate(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeBirthdate(player, BIRTHDATE);

        verify(playerDAO).changeBirthdate(PLAYER_ID, BIRTHDATE);
    }

    @Test
    public void changeBirthdatePlayerDAOChangeVerificationStatusCallCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeBirthdate(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.changeBirthdate(player, BIRTHDATE);

        verify(playerDAO).changeVerificationStatus(PLAYER_ID, (byte) 0b010);
    }

    @Test
    public void changeBirthdateReturnTrueCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeBirthdate(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertTrue(playerService.changeBirthdate(player, BIRTHDATE));
    }

    @Test
    public void changeBirthdatePlayerDAOChangeBirthdateReturnFalseCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeBirthdate(anyInt(), anyString())).thenReturn(false);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeBirthdate(player, BIRTHDATE));
    }

    @Test
    public void changeBirthdatePlayerDAOChangeVerificationStatusReturnFalseCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeBirthdate(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(false);

        Assert.assertFalse(playerService.changeBirthdate(player, BIRTHDATE));
    }

    @Test
    public void changeBirthdateDAOExceptionThrownOnChangeBirthdateReturnFalseCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeBirthdate(anyInt(), anyString()))
        .thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeBirthdate(player, BIRTHDATE));
    }

    @Test
    public void changeBirthdateDAOExceptionThrownOnChangeVerificationStatusReturnFalseCheck() throws DAOException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        when(playerDAO.changeBirthdate(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.changeBirthdate(player, BIRTHDATE));
    }

    @Test
    public void changeBirthdateSQLExceptionThrownOnDAOHelperBeginTransactionReturnFalseCheck()
    throws DAOException, SQLException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();
        when(playerDAO.changeBirthdate(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeBirthdate(player, BIRTHDATE));
    }

    @Test
    public void changeBirthdateSQLExceptionThrownOnDAOHelperCommitReturnFalseCheck()
    throws DAOException, SQLException {
        PlayerVerification verification = new PlayerVerification();
        verification.setProfileVerified(true);
        verification.setEmailVerified(true);
        verification.setPlayerId(PLAYER_ID);
        player.setVerification(verification);

        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();
        when(playerDAO.changeBirthdate(anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.changeBirthdate(player, BIRTHDATE));
    }

    @Test
    public void changeSecretQuestionPlayerDAOChangeSecretQuestionCallCheck() throws DAOException {
        when(playerDAO.changeSecretQuestion(anyInt(), anyString(), anyString())).thenReturn(true);
        playerService.changeSecretQuestion(player, QUESTION, ANSWER);

        verify(playerDAO).changeSecretQuestion(PLAYER_ID, QUESTION, ANSWER_MD5);
    }

    @Test
    public void changeSecretQuestionPlayerDAOChangeSecretQuestionReturnTrueCheck() throws DAOException {
        when(playerDAO.changeSecretQuestion(anyInt(), anyString(), anyString())).thenReturn(true);

        Assert.assertTrue(playerService.changeSecretQuestion(player, QUESTION, ANSWER));
    }

    @Test
    public void changeSecretQuestionPlayerDAOChangeSecretQuestionReturnFalseCheck() throws DAOException {
        when(playerDAO.changeSecretQuestion(anyInt(), anyString(), anyString())).thenReturn(false);

        Assert.assertFalse(playerService.changeSecretQuestion(player, QUESTION, ANSWER));
    }

    @Test
    public void changeSecretQuestionDAOExceptionThrownReturnFalseCheck() throws DAOException {
        when(playerDAO.changeSecretQuestion(anyInt(), anyString(), anyString()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.changeSecretQuestion(player, QUESTION, ANSWER));
    }

    @Test
    public void verifyProfilePlayerDAOChangeVerificationStatusCallCheck() throws DAOException {
        PlayerProfile profile = new PlayerProfile();
        profile.setfName(NAME);
        profile.setlName(NAME);
        profile.setBirthDate(LocalDate.now().minusYears(19));
        profile.setPassport(PASSPORT);
        player.setVerification(VERIFICATION);
        player.setProfile(profile);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.verifyProfile(player);

        verify(playerDAO).changeVerificationStatus(PLAYER_ID, (byte) 0b001);
    }

    @Test
    public void verifyProfilePlayerDAOChangeVerificationStatusReturnTrueCheck() throws DAOException {
        PlayerProfile profile = new PlayerProfile();
        profile.setfName(NAME);
        profile.setlName(NAME);
        profile.setPassport(PASSPORT);
        player.setVerification(VERIFICATION);
        player.setProfile(profile);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertTrue(playerService.verifyProfile(player));
    }

    @Test
    public void verifyProfilePlayerDAOChangeVerificationStatusReturnFalseCheck() throws DAOException {
        PlayerProfile profile = new PlayerProfile();
        profile.setfName(NAME);
        profile.setmName(NAME);
        profile.setlName(NAME);
        profile.setPassport(PASSPORT);
        player.setVerification(VERIFICATION);
        player.setProfile(profile);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(false);

        Assert.assertFalse(playerService.verifyProfile(player));
    }

    @Test
    public void verifyProfilePlayerDAOChangeVerificationStatusNoDataReturnFalseCheck() throws DAOException {
        PlayerProfile profile = new PlayerProfile();
        profile.setfName(NAME);
        profile.setlName(NAME);
        player.setVerification(VERIFICATION);
        player.setProfile(profile);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(false);

        Assert.assertFalse(playerService.verifyProfile(player));
    }

    @Test
    public void verifyProfileDAOExceptionThrownReturnFalseCheck() throws DAOException {
        PlayerProfile profile = new PlayerProfile();
        profile.setfName(NAME);
        profile.setlName(NAME);
        profile.setPassport(PASSPORT);
        player.setVerification(VERIFICATION);
        player.setProfile(profile);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.verifyProfile(player));
    }

    @Test
    public void verifyEmailPlayerDAOChangeVerificationStatusCallCheck() throws DAOException {
        player.setVerification(VERIFICATION);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);
        playerService.verifyEmail(player, EMAIL_CODE, EMAIL_CODE);

        verify(playerDAO).changeVerificationStatus(PLAYER_ID, (byte) 0b010);
    }

    @Test
    public void verifyEmailPlayerDAOChangeVerificationStatusReturnTrueCheck() throws DAOException {
        player.setVerification(VERIFICATION);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertTrue(playerService.verifyEmail(player, EMAIL_CODE, EMAIL_CODE));
    }

    @Test
    public void verifyEmailPlayerDAOInvalidCodeReturnFalseCheck() throws DAOException {
        player.setVerification(VERIFICATION);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(true);

        Assert.assertFalse(playerService.verifyEmail(player, EMAIL_CODE, EMAIL_CODE_INVALID));
    }

    @Test
    public void verifyEmailPlayerDAOChangeVerificationStatusReturnFalseCheck() throws DAOException {
        player.setVerification(VERIFICATION);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte())).thenReturn(false);

        Assert.assertFalse(playerService.verifyEmail(player, EMAIL_CODE, EMAIL_CODE));
    }

    @Test
    public void verifyEmailDAOExceptionThrownReturnFalseCheck() throws DAOException {
        player.setVerification(VERIFICATION);

        when(playerDAO.changeVerificationStatus(anyInt(), anyByte()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.verifyEmail(player, EMAIL_CODE, EMAIL_CODE));
    }

    @Test
    public void makeTransactionTransactionDAOInsertTransactionCallCheck() throws DAOException {
        when(transactionDAO.insertTransaction(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(TRANSACTION_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);
        playerService.makeTransaction(player, BigDecimal.TEN, Transaction.TransactionType.REPLENISH);

        verify(transactionDAO).insertTransaction(PLAYER_ID, BigDecimal.TEN, Transaction.TransactionType.REPLENISH);
    }

    @Test
    public void makeTransactionPlayerDAOChangeBalanceCallCheck() throws DAOException {
        when(transactionDAO.insertTransaction(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(TRANSACTION_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);
        playerService.makeTransaction(player, BigDecimal.TEN, Transaction.TransactionType.REPLENISH);

        verify(playerDAO).changeBalance(PLAYER_ID, BigDecimal.TEN, Transaction.TransactionType.REPLENISH);
    }

    @Test
    public void makeTransactionReturnTrueCheck() throws DAOException {
        when(transactionDAO.insertTransaction(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(TRANSACTION_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertTrue(playerService.makeTransaction(player, BigDecimal.TEN, Transaction.TransactionType.REPLENISH));
    }

    @Test
    public void makeTransactionTransactionDAOInsertTransactionReturnZeroCheck() throws DAOException {
        when(transactionDAO.insertTransaction(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(0);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(playerService.makeTransaction(player, BigDecimal.TEN, Transaction.TransactionType.REPLENISH));
    }

    @Test
    public void makeTransactionPlayerDAOChangeBalanceReturnFalseCheck() throws DAOException {
        when(transactionDAO.insertTransaction(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(TRANSACTION_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(false);

        Assert.assertFalse(playerService.makeTransaction(player, BigDecimal.TEN, Transaction.TransactionType.REPLENISH));
    }

    @Test
    public void makeTransactionDAOExceptionThrownOnInsertTransactionReturnFalseCheck() throws DAOException {
        when(transactionDAO.insertTransaction(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(playerService.makeTransaction(player, BigDecimal.TEN, Transaction.TransactionType.REPLENISH));
    }

    @Test
    public void makeTransactionDAOExceptionThrownOnChangeBalanceReturnFalseCheck() throws DAOException {
        when(transactionDAO.insertTransaction(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(TRANSACTION_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(playerService.makeTransaction(player, BigDecimal.TEN, Transaction.TransactionType.REPLENISH));
    }

    @Test
    public void makeTransactionSQLExceptionThrownOnDAOHelperBeginTransactionReturnFalseCheck()
    throws DAOException, SQLException {
        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();
        when(transactionDAO.insertTransaction(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(TRANSACTION_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(playerService.makeTransaction(player, BigDecimal.TEN, Transaction.TransactionType.REPLENISH));
    }

    @Test
    public void makeTransactionSQLExceptionThrownOnDAOHelperCommitReturnFalseCheck() throws DAOException, SQLException {
        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();
        when(transactionDAO.insertTransaction(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(TRANSACTION_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(playerService.makeTransaction(player, BigDecimal.TEN, Transaction.TransactionType.REPLENISH));
    }


}