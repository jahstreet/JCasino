package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.TransactionDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class TransactionServiceTest {

    private static final String DATE_PATTERN               = "anyPattern";
    private static final String MODIFIED_DATE_PATTERN      = "anyPattern%";
    private static final String MODIFIED_NULL_DATE_PATTERN = "%";
    private static final String TYPE_FILTER_WITHDRAW       = "withdRaW ";
    private static final String TYPE_FILTER_ALL            = "all ";
    private static final String TYPE_FILTER_EMPTY          = " ";
    private static final int    PLAYER_ID                  = 24;

    private static final List<Transaction> TRANSACTION_LIST = new ArrayList<>();

    @Mock
    private TransactionDAO     transactionDAO;
    @Mock
    private DAOHelper          daoHelper;
    @InjectMocks
    private TransactionService transactionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(daoHelper.getTransactionDAO()).thenReturn(transactionDAO);
    }

    @Test
    public void takePlayerTransactionsTransactionDAOTakeCallCheck() throws DAOException {
        when(transactionDAO.takePlayerTransactions(anyInt(), anyString())).thenReturn(null);
        transactionService.takePlayerTransactions(PLAYER_ID, DATE_PATTERN);

        verify(transactionDAO).takePlayerTransactions(PLAYER_ID, MODIFIED_DATE_PATTERN);
    }

    @Test
    public void takePlayerTransactionsModifyDatePatternNullCheck() throws DAOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(transactionDAO.takePlayerTransactions(anyInt(), anyString())).thenReturn(null);
        transactionService.takePlayerTransactions(PLAYER_ID, null);
        verify(transactionDAO).takePlayerTransactions(anyInt(), captor.capture());

        Assert.assertEquals(MODIFIED_NULL_DATE_PATTERN, captor.getValue());
    }

    @Test
    public void takePlayerTransactionsReturnTransactionDAOTakeResultNullCheck() throws DAOException {
        when(transactionDAO.takePlayerTransactions(anyInt(), anyString())).thenReturn(null);

        Assert.assertNull(transactionService.takePlayerTransactions(PLAYER_ID, DATE_PATTERN));
    }

    @Test
    public void takePlayerTransactionsReturnTransactionDAOTakeResultNotNullCheck() throws DAOException {
        when(transactionDAO.takePlayerTransactions(anyInt(), anyString())).thenReturn(TRANSACTION_LIST);

        Assert.assertArrayEquals(TRANSACTION_LIST.toArray(),
                                 transactionService.takePlayerTransactions(PLAYER_ID, DATE_PATTERN).toArray());
    }

    @Test
    public void takePlayerTransactionsDAOExceptionThrownReturnNullCheck() throws DAOException {
        when(transactionDAO.takePlayerTransactions(anyInt(), anyString()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(transactionService.takePlayerTransactions(PLAYER_ID, DATE_PATTERN));
    }

    @Test
    public void takeTransactionListTransactionDAOTakeCallCheck() throws DAOException {
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(null);
        transactionService.takeTransactionList(null, DATE_PATTERN, false);

        verify(transactionDAO).takeTransactionList(MODIFIED_DATE_PATTERN);
    }

    @Test
    public void takeTransactionListModifyDatePatternNullCheck() throws DAOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(null);
        transactionService.takeTransactionList(null, null, false);
        verify(transactionDAO).takeTransactionList(captor.capture());

        Assert.assertEquals(MODIFIED_NULL_DATE_PATTERN, captor.getValue());
    }

    @Test
    @PrepareForTest(TransactionService.class)
    public void takeTransactionListFilterByTypeNotInvokeTypeAllCheck() throws Exception {
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(TRANSACTION_LIST);
        PowerMockito.spy(TransactionService.class);
        PowerMockito.doNothing().when(TransactionService.class, "filterByType",
                                      any(List.class), any(Transaction.TransactionType.class));
        transactionService.takeTransactionList(TYPE_FILTER_ALL, DATE_PATTERN, false);

        PowerMockito.verifyPrivate(TransactionService.class, never())
                    .invoke("filterByType", any(List.class), any(Transaction.TransactionType.class));
    }

    @Test
    @PrepareForTest(TransactionService.class)
    public void takeTransactionListFilterByTypeInvokeTypeNullCheck() throws Exception {
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(TRANSACTION_LIST);
        PowerMockito.spy(TransactionService.class);
        PowerMockito.doNothing().when(TransactionService.class, "filterByType",
                                      any(List.class), any(Transaction.TransactionType.class));
        transactionService.takeTransactionList(null, DATE_PATTERN, false);

        PowerMockito.verifyPrivate(TransactionService.class, never())
                    .invoke("filterByType", any(List.class), any(Transaction.TransactionType.class));
    }

    @Test
    @PrepareForTest(TransactionService.class)
    public void takeTransactionListFilterByTypeInvokeTypeEmptyCheck() throws Exception {
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(null);
        PowerMockito.spy(TransactionService.class);
        PowerMockito.doNothing().when(TransactionService.class, "filterByType",
                                      any(List.class), any(Transaction.TransactionType.class));
        transactionService.takeTransactionList(TYPE_FILTER_EMPTY, DATE_PATTERN, false);

        PowerMockito.verifyPrivate(TransactionService.class, never())
                    .invoke("filterByType", any(List.class), any(Transaction.TransactionType.class));
    }

    @Test
    @PrepareForTest(TransactionService.class)
    public void takeTransactionListFilterByTypeInvokeTypeNotNullCheck() throws Exception {
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(TRANSACTION_LIST);
        PowerMockito.spy(TransactionService.class);
        PowerMockito.doNothing().when(TransactionService.class, "filterByType",
                                      any(List.class), any(Transaction.TransactionType.class));
        transactionService.takeTransactionList(TYPE_FILTER_WITHDRAW, DATE_PATTERN, false);

        PowerMockito.verifyPrivate(TransactionService.class)
                    .invoke("filterByType", TRANSACTION_LIST, Transaction.TransactionType.WITHDRAW);
    }

    @Test
    @PrepareForTest(TransactionService.class)
    public void takeTransactionListSortByAmountInvokeTrueCheck() throws Exception {
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(TRANSACTION_LIST);
        PowerMockito.spy(TransactionService.class);
        PowerMockito.doNothing().when(TransactionService.class, "sortByAmount", any(List.class), anyBoolean());
        transactionService.takeTransactionList(null, DATE_PATTERN, true);

        PowerMockito.verifyPrivate(TransactionService.class).invoke("sortByAmount", TRANSACTION_LIST, false);
    }

    @Test
    @PrepareForTest(TransactionService.class)
    public void takeTransactionListSortByAmountInvokeFalseCheck() throws Exception {
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(TRANSACTION_LIST);
        PowerMockito.spy(TransactionService.class);
        PowerMockito.doNothing().when(TransactionService.class, "sortByAmount", any(List.class), anyBoolean());
        transactionService.takeTransactionList(null, DATE_PATTERN, false);

        PowerMockito.verifyPrivate(TransactionService.class, never()).invoke("sortByAmount", TRANSACTION_LIST, false);
    }

    @Test
    public void takeTransactionListReturnTransactionDAOTakeResultNullCheck() throws DAOException {
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(null);

        Assert.assertNull(transactionService.takeTransactionList(null, DATE_PATTERN, false));
    }

    @Test
    public void takeTransactionListReturnTransactionDAOTakeResultNotNullCheck() throws DAOException {
        when(transactionDAO.takeTransactionList(anyString())).thenReturn(TRANSACTION_LIST);

        Assert.assertArrayEquals(TRANSACTION_LIST.toArray(),
                                 transactionService.takeTransactionList(null, DATE_PATTERN, false).toArray());
    }

    @Test
    public void takeTransactionListDAOExceptionThrownReturnNullCheck() throws DAOException {
        when(transactionDAO.takeTransactionList(anyString())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(transactionService.takeTransactionList(null, DATE_PATTERN, false));
    }


}