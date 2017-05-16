package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.LoanDAO;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.StreakDAO;
import by.sasnouskikh.jcasino.dao.TransactionDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.PlayerProfile;
import by.sasnouskikh.jcasino.entity.bean.PlayerStats;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private static final int    LOAN_ID        = 24;
    private static final int    STREAK_ID      = 12;
    private static final int    TRANSACTION_ID = 45;
    private static final int    PLAYER_ID      = 7;
    private static final String EMAIL          = "any@mail.net";
    private static final String FIRST_NAME     = "anyFirstName";

    static {
        PROFILE.setfName(FIRST_NAME);

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


}