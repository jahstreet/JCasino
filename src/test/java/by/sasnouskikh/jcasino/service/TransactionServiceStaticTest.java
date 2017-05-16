package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class TransactionServiceStaticTest {

    private static final List<Transaction> TRANSACTION_LIST = new ArrayList<>();
    private static final Transaction       TRANSACTION_1    = new Transaction();
    private static final Transaction       TRANSACTION_2    = new Transaction();
    private static final Transaction       TRANSACTION_3    = new Transaction();
    private static final Transaction       TRANSACTION_4    = new Transaction();
    private static final BigDecimal        MAX_PAYMENT      = BigDecimal.valueOf(7.28);
    private static final BigDecimal        MAX_WITHDRAWAL   = BigDecimal.TEN;
    private static final BigDecimal        TOTAL_PAYMENT    = BigDecimal.valueOf(8.28);
    private static final BigDecimal        TOTAL_WITHDRAWAL = BigDecimal.valueOf(14.15);

    static {
        LocalDateTime date      = LocalDateTime.now();
        LocalDateTime datePlus  = date.plusDays(2);
        LocalDateTime dateMinus = date.minusDays(2);

        TRANSACTION_1.setId(1);
        TRANSACTION_1.setPlayerId(12);
        TRANSACTION_1.setType(Transaction.TransactionType.WITHDRAW);
        TRANSACTION_1.setAmount(MAX_WITHDRAWAL);
        TRANSACTION_1.setDate(date);

        TRANSACTION_2.setId(2);
        TRANSACTION_2.setPlayerId(12);
        TRANSACTION_2.setType(Transaction.TransactionType.REPLENISH);
        TRANSACTION_2.setAmount(BigDecimal.ONE);
        TRANSACTION_2.setDate(datePlus);

        TRANSACTION_3.setId(3);
        TRANSACTION_3.setPlayerId(7);
        TRANSACTION_3.setType(Transaction.TransactionType.WITHDRAW);
        TRANSACTION_3.setAmount(BigDecimal.valueOf(4.15));
        TRANSACTION_3.setDate(dateMinus);

        TRANSACTION_4.setId(4);
        TRANSACTION_4.setPlayerId(7);
        TRANSACTION_4.setType(Transaction.TransactionType.REPLENISH);
        TRANSACTION_4.setAmount(MAX_PAYMENT);
        TRANSACTION_4.setDate(dateMinus);

        TRANSACTION_LIST.addAll(Arrays.asList(TRANSACTION_1, TRANSACTION_2, TRANSACTION_3, TRANSACTION_4));
    }

    @Before
    public void init() {
        PowerMockito.spy(TransactionService.class);
    }

    @Test
    public void defineMaxPaymentCheck() throws Exception {
        BigDecimal actual = Whitebox.invokeMethod(TransactionService.class, "defineMaxPayment", TRANSACTION_LIST);

        Assert.assertEquals(MAX_PAYMENT.doubleValue(), actual.doubleValue(), 1e-4);
    }

    @Test
    public void defineMaxPaymentNullCheck() throws Exception {
        BigDecimal expected = BigDecimal.ZERO;

        BigDecimal actual = Whitebox.invokeMethod(TransactionService.class, "defineMaxPayment", (Object) null);

        Assert.assertEquals(expected.doubleValue(), actual.doubleValue(), 1e-4);
    }

    @Test
    public void countTotalPaymentCheck() throws Exception {
        BigDecimal actual = Whitebox.invokeMethod(TransactionService.class, "countTotalPayment", TRANSACTION_LIST);

        Assert.assertEquals(TOTAL_PAYMENT.doubleValue(), actual.doubleValue(), 1e-4);
    }

    @Test
    public void countTotalPaymentNullCheck() throws Exception {
        BigDecimal expected = BigDecimal.ZERO;

        BigDecimal actual = Whitebox.invokeMethod(TransactionService.class, "countTotalPayment", (Object) null);

        Assert.assertEquals(expected.doubleValue(), actual.doubleValue(), 1e-4);
    }

    @Test
    public void defineMaxWithdrawalCheck() throws Exception {
        BigDecimal actual = Whitebox.invokeMethod(TransactionService.class, "defineMaxWithdrawal", TRANSACTION_LIST);

        Assert.assertEquals(MAX_WITHDRAWAL.doubleValue(), actual.doubleValue(), 1e-4);
    }

    @Test
    public void defineMaxWithdrawalNullCheck() throws Exception {
        BigDecimal expected = BigDecimal.ZERO;

        BigDecimal actual = Whitebox.invokeMethod(TransactionService.class, "defineMaxWithdrawal", (Object) null);

        Assert.assertEquals(expected.doubleValue(), actual.doubleValue(), 1e-4);
    }

    @Test
    public void countTotalWithdrawalCheck() throws Exception {
        BigDecimal actual = Whitebox.invokeMethod(TransactionService.class, "countTotalWithdrawal", TRANSACTION_LIST);

        Assert.assertEquals(TOTAL_WITHDRAWAL.doubleValue(), actual.doubleValue(), 1e-4);
    }

    @Test
    public void countTotalWithdrawalNullCheck() throws Exception {
        BigDecimal expected = BigDecimal.ZERO;

        BigDecimal actual = Whitebox.invokeMethod(TransactionService.class, "countTotalWithdrawal", (Object) null);

        Assert.assertEquals(expected.doubleValue(), actual.doubleValue(), 1e-4);
    }

    @Test
    public void filterByTypeCheck() throws Exception {
        List<Transaction> expected = new ArrayList<>(Arrays.asList(TRANSACTION_2, TRANSACTION_4));

        List<Transaction> actual = new ArrayList<>(TRANSACTION_LIST);
        Whitebox.invokeMethod(TransactionService.class, "filterByType", actual, Transaction.TransactionType.REPLENISH);

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    @SuppressWarnings("all")
    public void filterByTypeNullCheck() throws Exception {
        List<Transaction> actual = null;

        Whitebox.invokeMethod(TransactionService.class, "filterByType", actual, Transaction.TransactionType.WITHDRAW);

        Assert.assertNull(actual);
    }

    @Test
    public void sortByAmountAscendingCheck() throws Exception {
        List<Transaction> expected = new ArrayList<>(Arrays.asList(TRANSACTION_2, TRANSACTION_3,
                                                                   TRANSACTION_4, TRANSACTION_1));

        List<Transaction> actual = new ArrayList<>(TRANSACTION_LIST);
        Whitebox.invokeMethod(TransactionService.class, "sortByAmount", actual, true);

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void sortByAmountDescendingCheck() throws Exception {
        List<Transaction> expected = new ArrayList<>(Arrays.asList(TRANSACTION_1, TRANSACTION_4,
                                                                   TRANSACTION_3, TRANSACTION_2));

        List<Transaction> actual = new ArrayList<>(TRANSACTION_LIST);
        Whitebox.invokeMethod(TransactionService.class, "sortByAmount", actual, false);

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}