package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOTest extends AbstractDAOTest {

    private static final String TABLE_TRANSACION       = "transaction";
    private static final String XML_TRANSACTION_DATA   = "by/sasnouskikh/jcasino/dao/transaction_data.xml";
    private static final String XML_INSERTED_WITHDRAW  = "by/sasnouskikh/jcasino/dao/transaction_data_inserted_withdraw.xml";
    private static final String XML_INSERTED_REPLENISH = "by/sasnouskikh/jcasino/dao/transaction_data_inserted_replenish.xml";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        beforeData = buildDataSet(XML_TRANSACTION_DATA);
        DatabaseOperation.CLEAN_INSERT.execute(connection, beforeData);
    }

    @Test
    public void takePlayerTransactionsCheck() throws DAOException {
        int playerId = 101;

        int           transactionId1 = 13;
        BigDecimal    amount1        = new BigDecimal(5);
        LocalDateTime date1          = Timestamp.valueOf("2017-04-13 13:32:03.0").toLocalDateTime();

        int           transactionId2 = 15;
        BigDecimal    amount2        = new BigDecimal(20);
        LocalDateTime date2          = Timestamp.valueOf("2017-06-15 15:12:33.0").toLocalDateTime();

        Transaction transaction1 = new Transaction();
        transaction1.setId(transactionId1);
        transaction1.setPlayerId(playerId);
        transaction1.setDate(date1);
        transaction1.setType(Transaction.TransactionType.WITHDRAW);
        transaction1.setAmount(amount1);

        Transaction transaction2 = new Transaction();
        transaction2.setId(transactionId2);
        transaction2.setPlayerId(playerId);
        transaction2.setDate(date2);
        transaction2.setType(Transaction.TransactionType.REPLENISH);
        transaction2.setAmount(amount2);

        List<Transaction> expected = new ArrayList<>();
        expected.add(transaction1);
        expected.add(transaction2);

        List<Transaction> actual = daoHelper.getTransactionDAO().takePlayerTransactions(playerId);

        Assert.assertTrue(String.format("\nExpected list content:\t%s\nActual list content:\t%s", expected, actual),
                          expected.containsAll(actual) && expected.size() == actual.size());
    }

    @Test
    public void takePlayerTransactionsNoTransactionsCheck() throws DAOException {
        int playerId = 102;

        List<Transaction> actual = daoHelper.getTransactionDAO().takePlayerTransactions(playerId);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void takePlayerTransactionsNoIdCheck() throws DAOException {
        int playerId = 103;

        List<Transaction> actual = daoHelper.getTransactionDAO().takePlayerTransactions(playerId);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void takePlayerTransactionsDatePatternCheck() throws DAOException {
        int    playerId    = 100;
        String datePattern = "2017-04%";

        int expectedRowNumber = 2;

        List<Transaction> transactionList = daoHelper.getTransactionDAO().takePlayerTransactions(playerId, datePattern);
        int               actualRowNumber = transactionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takePlayerTransactionsAllDatePatternCheck() throws DAOException {
        int    playerId    = 100;
        String datePattern = "%";

        int expectedRowNumber = 4;

        List<Transaction> transactionList = daoHelper.getTransactionDAO().takePlayerTransactions(playerId, datePattern);
        int               actualRowNumber = transactionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takePlayerTransactionsDatePatternNoTransactionsCheck() throws DAOException {
        int    playerId    = 102;
        String datePattern = "%";

        List<Transaction> actual = daoHelper.getTransactionDAO().takePlayerTransactions(playerId, datePattern);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void takePlayerTransactionsDatePatternNoIdCheck() throws DAOException {
        int    playerId    = 103;
        String datePattern = "2017-04%";

        List<Transaction> actual = daoHelper.getTransactionDAO().takePlayerTransactions(playerId, datePattern);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void takeTransactionListDatePatternCheck() throws DAOException {
        String datePattern = "2017-04%";

        int expectedRowNumber = 3;

        List<Transaction> transactionList = daoHelper.getTransactionDAO().takeTransactionList(datePattern);
        int               actualRowNumber = transactionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takeTransactionListAllDatePatternCheck() throws DAOException {
        String datePattern = "%";

        int expectedRowNumber = 6;

        List<Transaction> transactionList = daoHelper.getTransactionDAO().takeTransactionList(datePattern);
        int               actualRowNumber = transactionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takeTransactionListDatePatternNoTransactionsCheck() throws DAOException {
        String datePattern = "2017-07%";

        List<Transaction> actual = daoHelper.getTransactionDAO().takeTransactionList(datePattern);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void insertTransactionReplenishCheck() throws DAOException, SQLException, DatabaseUnitException {
        int                         playerId = 101;
        BigDecimal                  amount   = new BigDecimal(400);
        Transaction.TransactionType type     = Transaction.TransactionType.REPLENISH;
        String[]                    ignore   = {"date"};

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED_REPLENISH);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_TRANSACION);

        daoHelper.getTransactionDAO().insertTransaction(playerId, amount, type);
        ITable actualTable = connection.createTable(TABLE_TRANSACION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void insertTransactionWithdrawCheck() throws DAOException, SQLException, DatabaseUnitException {
        int                         playerId = 101;
        BigDecimal                  amount   = new BigDecimal(200);
        Transaction.TransactionType type     = Transaction.TransactionType.WITHDRAW;
        String[]                    ignore   = {"date"};

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED_WITHDRAW);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_TRANSACION);

        daoHelper.getTransactionDAO().insertTransaction(playerId, amount, type);
        ITable actualTable = connection.createTable(TABLE_TRANSACION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void insertTransactionIdCheck() throws DAOException {
        int                         playerId = 101;
        BigDecimal                  amount   = new BigDecimal(200);
        Transaction.TransactionType type     = Transaction.TransactionType.WITHDRAW;

        int expected = 16;

        int actual = daoHelper.getTransactionDAO().insertTransaction(playerId, amount, type);

        Assert.assertEquals(String.format("Inserted transaction id expected: %d, actual: %d", expected, actual),
                            expected, actual);
    }

    @Test(expected = DAOException.class)
    public void transactionPlayerFkConstraintCheck() throws DAOException {
        int                         playerId = 103;
        BigDecimal                  amount   = new BigDecimal(200);
        Transaction.TransactionType type     = Transaction.TransactionType.WITHDRAW;

        daoHelper.getTransactionDAO().insertTransaction(playerId, amount, type);

        Assert.fail("A FK constraint `jcasino`.`transaction`, CONSTRAINT `fk_transaction_player` FOREIGN KEY (`player_id`) " +
                    "REFERENCES `player` (`id`) should exist.");
    }
}