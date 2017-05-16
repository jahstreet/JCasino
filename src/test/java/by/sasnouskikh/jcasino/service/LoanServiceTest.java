package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.LoanDAO;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class LoanServiceTest {

    private static final int           PLAYER_ID      = 112;
    private static final int           LOAN_ID        = 14;
    private static final BigDecimal    AMOUNT         = BigDecimal.TEN;
    private static final BigDecimal    COUNTED_AMOUNT = new BigDecimal(12);
    private static final BigDecimal    PERCENT        = new BigDecimal(20);
    private static final Player        PLAYER         = new Player();
    private static final PlayerAccount PLAYER_ACCOUNT = new PlayerAccount();
    private static final PlayerStatus  PLAYER_STATUS  = new PlayerStatus();
    private static final Loan          CURRENT_LOAN   = new Loan();
    private static final Loan          LOAN_1         = new Loan();
    private static final Loan          LOAN_2         = new Loan();
    private static final Loan          LOAN_3         = new Loan();
    private static final Loan          LOAN_4         = new Loan();

    static {
        PLAYER.setId(PLAYER_ID);
        CURRENT_LOAN.setId(LOAN_ID);
        PLAYER_STATUS.setLoanPercent(PERCENT);
        PLAYER_ACCOUNT.setStatus(PLAYER_STATUS);
        PLAYER_ACCOUNT.setCurrentLoan(CURRENT_LOAN);
        PLAYER.setAccount(PLAYER_ACCOUNT);

        LocalDate currentDate     = LocalDate.now();
        LocalDate overduedDate    = currentDate.minusDays(1);
        LocalDate notOverduedDate = currentDate.plusDays(1);

        LOAN_1.setId(1);
        LOAN_1.setRest(BigDecimal.TEN);
        LOAN_1.setExpire(overduedDate);

        LOAN_2.setId(2);
        LOAN_2.setRest(BigDecimal.ZERO);
        LOAN_2.setExpire(notOverduedDate);

        LOAN_3.setId(3);
        LOAN_3.setRest(BigDecimal.ONE);
        LOAN_3.setExpire(notOverduedDate);

        LOAN_4.setId(4);
        LOAN_4.setRest(BigDecimal.ZERO);
        LOAN_4.setExpire(overduedDate);
    }

    private List<Loan> loanList;

    @Mock
    private PlayerDAO   playerDAO;
    @Mock
    private LoanDAO     loanDAO;
    @Mock
    private DAOHelper   daoHelper;
    @InjectMocks
    private LoanService loanService;

    @Before
    public void setUp() {
        when(daoHelper.getLoanDAO()).thenReturn(loanDAO);
        when(daoHelper.getPlayerDAO()).thenReturn(playerDAO);
        loanList = new ArrayList<>();
        loanList.add(LOAN_1);
        loanList.add(LOAN_2);
        loanList.add(LOAN_3);
        loanList.add(LOAN_4);
    }

    @After
    public void tearDown() {
        loanList = null;
    }

    @Test
    public void takePlayerLoansLoanDAOCallCheck() throws DAOException {
        int        playerId    = 1;
        String     datePattern = "anyPattern";
        List<Loan> loanList    = new ArrayList<>();

        when(loanDAO.takePlayerLoans(anyInt(), anyString())).thenReturn(loanList);
        loanService.takePlayerLoans(playerId, datePattern);

        verify(loanDAO).takePlayerLoans(anyInt(), anyString());
    }

    @Test
    public void takePlayerLoansReturnLoanDAOResultNullCheck() throws DAOException {
        int    playerId    = 1;
        String datePattern = "anyPattern";

        when(loanDAO.takePlayerLoans(anyInt(), anyString())).thenReturn(null);

        Assert.assertNull(loanService.takePlayerLoans(playerId, datePattern));
    }

    @Test
    public void takePlayerLoansReturnLoanDAOResultNotNullCheck() throws DAOException {
        int        playerId    = 1;
        String     datePattern = "anyPattern";
        List<Loan> loanList    = new ArrayList<>();

        when(loanDAO.takePlayerLoans(anyInt(), anyString())).thenReturn(loanList);

        Assert.assertEquals(loanList, loanService.takePlayerLoans(playerId, datePattern));
    }

    @Test
    public void takePlayerLoansModifyDatePatternNotNullCheck() throws DAOException {
        int    playerId            = 1;
        String datePattern         = "anyPattern";
        String modifiedDatePattern = "anyPattern%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(loanDAO.takePlayerLoans(anyInt(), anyString())).thenReturn(null);
        loanService.takePlayerLoans(playerId, datePattern);
        verify(loanDAO).takePlayerLoans(anyInt(), captor.capture());

        Assert.assertEquals(modifiedDatePattern, captor.getValue());
    }

    @Test
    public void takePlayerLoansModifyDatePatternNullCheck() throws DAOException {
        int    playerId            = 1;
        String modifiedDatePattern = "%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(loanDAO.takePlayerLoans(anyInt(), anyString())).thenReturn(null);
        loanService.takePlayerLoans(playerId, null);
        verify(loanDAO).takePlayerLoans(anyInt(), captor.capture());

        Assert.assertEquals(modifiedDatePattern, captor.getValue());
    }

    @Test
    public void takePlayerLoansPlayerIdArgPushToLoanDAOCheck() throws DAOException {
        int playerId = 1;

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        when(loanDAO.takePlayerLoans(anyInt(), anyString())).thenReturn(null);
        loanService.takePlayerLoans(playerId, null);
        verify(loanDAO).takePlayerLoans(captor.capture(), anyString());

        Assert.assertEquals(playerId, captor.getValue().intValue());
    }

    @Test
    public void takePlayerLoansDAOExceptionThrownReturnCheck() throws DAOException {
        int playerId = 1;

        when(loanDAO.takePlayerLoans(anyInt(), anyString())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(loanService.takePlayerLoans(playerId, null));
    }

    @Test
    public void takeLoanListLoanDAOCallCheck() throws DAOException {
        String     acquirePattern = "anyPattern";
        String     expirePattern  = "anyPattern";
        List<Loan> loanList       = new ArrayList<>();

        when(loanDAO.takeLoanList(anyString(), anyString())).thenReturn(loanList);
        loanService.takeLoanList(acquirePattern, expirePattern, false, false, false);

        verify(loanDAO).takeLoanList(anyString(), anyString());
    }

    @Test
    public void takeLoanListModifyAcquirePatternNotNullCheck() throws DAOException {
        String acquirePattern         = "anyPattern";
        String modifiedAcquirePattern = "anyPattern%";
        String expirePattern          = "any";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(loanDAO.takeLoanList(anyString(), anyString())).thenReturn(null);
        loanService.takeLoanList(acquirePattern, expirePattern, false, false, false);
        verify(loanDAO).takeLoanList(captor.capture(), anyString());

        Assert.assertEquals(modifiedAcquirePattern, captor.getValue());
    }

    @Test
    public void takeLoanListModifyAcquirePatternNullCheck() throws DAOException {
        String modifiedAcquirePattern = "%";
        String expirePattern          = "anyPattern";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(loanDAO.takeLoanList(anyString(), anyString())).thenReturn(null);
        loanService.takeLoanList(null, expirePattern, false, false, false);
        verify(loanDAO).takeLoanList(captor.capture(), anyString());

        Assert.assertEquals(modifiedAcquirePattern, captor.getValue());
    }

    @Test
    public void takeLoanListModifyExpirePatternNotNullCheck() throws DAOException {
        String acquirePattern        = "any";
        String expirePattern         = "anyPattern";
        String modifiedExpirePattern = "anyPattern%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(loanDAO.takeLoanList(anyString(), anyString())).thenReturn(null);
        loanService.takeLoanList(acquirePattern, expirePattern, false, false, false);
        verify(loanDAO).takeLoanList(anyString(), captor.capture());

        Assert.assertEquals(modifiedExpirePattern, captor.getValue());
    }

    @Test
    public void takeLoanListModifyExpirePatternNullCheck() throws DAOException {
        String acquirePattern        = "anyPattern";
        String modifiedExpirePattern = "%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(loanDAO.takeLoanList(anyString(), anyString())).thenReturn(null);
        loanService.takeLoanList(acquirePattern, null, false, false, false);
        verify(loanDAO).takeLoanList(anyString(), captor.capture());

        Assert.assertEquals(modifiedExpirePattern, captor.getValue());
    }

    @Test
    public void takeLoanListDAOExceptionThrownReturnCheck() throws DAOException {
        String acquirePattern = "anyPattern";
        String expirePattern  = "anyPattern";

        when(loanDAO.takeLoanList(anyString(), anyString())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(loanService.takeLoanList(acquirePattern, expirePattern, false, false, false));
    }

    @Test
    public void privateFilterNotPaidCheck() throws Exception {
        int expectedSize = 2;

        Whitebox.invokeMethod(LoanService.class, "filterNotPaid", loanList);
        int actualSize = loanList.size();

        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void privateFilterOverduedCheck() throws Exception {
        int expectedSize = 1;

        Whitebox.invokeMethod(LoanService.class, "filterOverdued", loanList);
        int actualSize = loanList.size();

        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void privateSortByRestAscendingCheck() throws Exception {
        int expectedFirstValueId = 2;

        Whitebox.invokeMethod(LoanService.class, "sortByRest", loanList, true);
        int actualFirstValueId = loanList.get(0).getId();

        Assert.assertEquals(expectedFirstValueId, actualFirstValueId);
    }

    @Test
    public void privateSortByRestDescendingCheck() throws Exception {
        int expectedFirstValueId = 1;

        Whitebox.invokeMethod(LoanService.class, "sortByRest", loanList, false);
        int actualFirstValueId = loanList.get(0).getId();

        Assert.assertEquals(expectedFirstValueId, actualFirstValueId);
    }

    @Test
    public void takeNewLoanLoanDAOCallCheck() throws Exception {
        when(loanDAO.insertLoan(anyInt(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(LOAN_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);
        loanService.takeNewLoan(PLAYER, AMOUNT);

        verify(loanDAO).insertLoan(PLAYER_ID, COUNTED_AMOUNT, PERCENT);
    }

    @Test
    public void takeNewLoanPlayerDAOCallCheck() throws Exception {
        when(loanDAO.insertLoan(anyInt(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(LOAN_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);
        loanService.takeNewLoan(PLAYER, AMOUNT);

        verify(playerDAO).changeBalance(PLAYER_ID, AMOUNT, Transaction.TransactionType.REPLENISH);
    }

    @Test
    public void takeNewLoanLoanDAOExceptionThrownReturnFalseCheck() throws Exception {
        when(loanDAO.insertLoan(anyInt(), any(BigDecimal.class), any(BigDecimal.class)))
        .thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(loanService.takeNewLoan(PLAYER, AMOUNT));
    }

    @Test
    public void takeNewLoanPlayerDAOExceptionThrownReturnFalseCheck() throws Exception {
        when(loanDAO.insertLoan(anyInt(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(LOAN_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(loanService.takeNewLoan(PLAYER, AMOUNT));
    }

    @Test
    public void takeNewLoanBeginTransactionSQLExceptionThrownReturnFalseCheck() throws Exception {
        PowerMockito.doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();
        when(loanDAO.insertLoan(anyInt(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(LOAN_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(loanService.takeNewLoan(PLAYER, AMOUNT));
    }

    @Test
    public void takeNewLoanCommitSQLExceptionThrownReturnFalseCheck() throws Exception {
        PowerMockito.doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();
        when(loanDAO.insertLoan(anyInt(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(LOAN_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(loanService.takeNewLoan(PLAYER, AMOUNT));
    }

    @Test
    public void takeNewLoanLoanDAOReturn0Check() throws Exception {
        when(loanDAO.insertLoan(anyInt(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(0);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(loanService.takeNewLoan(PLAYER, AMOUNT));
    }

    @Test
    public void takeNewLoanPlayerDAOReturnFalseCheck() throws Exception {
        when(loanDAO.insertLoan(anyInt(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(LOAN_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(false);

        Assert.assertFalse(loanService.takeNewLoan(PLAYER, AMOUNT));
    }

    @Test
    public void takeNewLoanReturnTrueCheck() throws Exception {
        when(loanDAO.insertLoan(anyInt(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(LOAN_ID);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertTrue(loanService.takeNewLoan(PLAYER, AMOUNT));
    }

    @Test
    public void payLoanLoanDAOCallCheck() throws Exception {
        when(loanDAO.payLoan(anyInt(), any(BigDecimal.class))).thenReturn(true);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);
        loanService.payLoan(PLAYER, AMOUNT);

        verify(loanDAO).payLoan(LOAN_ID, AMOUNT);
    }

    @Test
    public void payLoanPlayerDAOCallCheck() throws Exception {
        when(loanDAO.payLoan(anyInt(), any(BigDecimal.class))).thenReturn(true);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);
        loanService.payLoan(PLAYER, AMOUNT);

        verify(playerDAO).changeBalance(PLAYER_ID, AMOUNT, Transaction.TransactionType.WITHDRAW);
    }

    @Test
    public void payLoanLoanDAOExceptionThrownReturnFalseCheck() throws Exception {
        when(loanDAO.payLoan(anyInt(), any(BigDecimal.class)))
        .thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(loanService.payLoan(PLAYER, AMOUNT));
    }

    @Test
    public void payLoanPlayerDAOExceptionThrownReturnFalseCheck() throws Exception {
        when(loanDAO.payLoan(anyInt(), any(BigDecimal.class))).thenReturn(true);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(loanService.payLoan(PLAYER, AMOUNT));
    }

    @Test
    public void payLoanBeginTransactionSQLExceptionThrownReturnFalseCheck() throws Exception {
        PowerMockito.doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();
        when(loanDAO.payLoan(anyInt(), any(BigDecimal.class))).thenReturn(true);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(loanService.payLoan(PLAYER, AMOUNT));
    }

    @Test
    public void payLoanCommitSQLExceptionThrownReturnFalseCheck() throws Exception {
        PowerMockito.doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();
        when(loanDAO.payLoan(anyInt(), any(BigDecimal.class))).thenReturn(true);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(loanService.payLoan(PLAYER, AMOUNT));
    }

    @Test
    public void payLoanLoanDAOReturnFalseCheck() throws Exception {
        when(loanDAO.payLoan(anyInt(), any(BigDecimal.class))).thenReturn(false);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertFalse(loanService.payLoan(PLAYER, AMOUNT));
    }

    @Test
    public void payLoanPlayerDAOReturnFalseCheck() throws Exception {
        when(loanDAO.payLoan(anyInt(), any(BigDecimal.class))).thenReturn(true);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(false);

        Assert.assertFalse(loanService.payLoan(PLAYER, AMOUNT));
    }

    @Test
    public void payLoanLoanReturnTrueCheck() throws Exception {
        when(loanDAO.payLoan(anyInt(), any(BigDecimal.class))).thenReturn(true);
        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertTrue(loanService.payLoan(PLAYER, AMOUNT));
    }
}