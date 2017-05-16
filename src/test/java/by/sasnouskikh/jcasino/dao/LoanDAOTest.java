package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.entity.bean.Loan;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoanDAOTest extends AbstractDAOTest {

    private static final String TABLE_LOAN    = "loan";
    private static final String XML_LOAN_DATA = "by/sasnouskikh/jcasino/dao/loan_data.xml";
    private static final String XML_PAID      = "by/sasnouskikh/jcasino/dao/loan_data_paid.xml";
    private static final String XML_INSERTED  = "by/sasnouskikh/jcasino/dao/loan_data_inserted.xml";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        beforeData = buildDataSet(XML_LOAN_DATA);
        DatabaseOperation.CLEAN_INSERT.execute(connection, beforeData);
    }

    @Test
    public void takeLoanCheck() throws DAOException {
        int        loanId   = 2;
        int        playerId = 100;
        String     acquire  = "2017-02-24";
        String     expire   = "2017-03-24";
        BigDecimal amount   = new BigDecimal(200.0);
        BigDecimal rest     = new BigDecimal(170.0);
        BigDecimal percent  = new BigDecimal(20.0);

        Loan expected = new Loan();
        expected.setId(loanId);
        expected.setPlayerId(playerId);
        expected.setAcquire(LocalDate.parse(acquire));
        expected.setExpire(LocalDate.parse(expire));
        expected.setAmount(amount);
        expected.setPercent(percent);
        expected.setRest(rest);

        Loan actual = daoHelper.getLoanDAO().takeLoan(loanId);

        Assert.assertEquals(String.format("\nExpected:\t%s\nActual:\t%s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeLoanNoSuchCheck() throws DAOException {
        int loanId = 7;

        Loan actual = daoHelper.getLoanDAO().takeLoan(loanId);

        Assert.assertNull("Taken loan object value expected to be null.", actual);
    }

    @Test
    public void takeCurrentLoanCheck() throws DAOException {
        int        loanId   = 2;
        int        playerId = 100;
        String     acquire  = "2017-02-24";
        String     expire   = "2017-03-24";
        BigDecimal amount   = new BigDecimal(200.0);
        BigDecimal rest     = new BigDecimal(170.0);
        BigDecimal percent  = new BigDecimal(20.0);

        Loan expected = new Loan();
        expected.setId(loanId);
        expected.setPlayerId(playerId);
        expected.setAcquire(LocalDate.parse(acquire));
        expected.setExpire(LocalDate.parse(expire));
        expected.setAmount(amount);
        expected.setPercent(percent);
        expected.setRest(rest);

        Loan actual = daoHelper.getLoanDAO().takeCurrentLoan(playerId);

        Assert.assertEquals(String.format("\nExpected:\t%s\nActual:\t%s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeCurrentLoanNoIdCheck() throws DAOException {
        int playerId = 103;

        Loan actual = daoHelper.getLoanDAO().takeCurrentLoan(playerId);

        Assert.assertNull("Taken loan object value expected to be null.", actual);
    }

    @Test
    public void takeCurrentLoanNoSuchCheck() throws DAOException {
        int playerId = 102;

        Loan actual = daoHelper.getLoanDAO().takeCurrentLoan(playerId);

        Assert.assertNull("Taken loan object value expected to be null.", actual);
    }

    @Test
    public void takePlayerLoansCheck() throws DAOException {
        int playerId = 100;

        int expectedSize = 3;

        List<Loan> loanList   = daoHelper.getLoanDAO().takePlayerLoans(playerId);
        int        actualSize = loanList.size();

        Assert.assertEquals(String.format("Number of rows expected: %d, actual: %d", expectedSize, actualSize),
                            expectedSize, actualSize);
    }

    @Test
    public void takePlayerLoansNoIdCheck() throws DAOException {
        int playerId = 103;

        List<Loan> actual = daoHelper.getLoanDAO().takePlayerLoans(playerId);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void takePlayerLoansAcquirePatternCheck() throws DAOException {
        int    playerId       = 100;
        String acquirePattern = "2017-02%";

        int expectedSize = 2;

        List<Loan> loanList   = daoHelper.getLoanDAO().takePlayerLoans(playerId, acquirePattern);
        int        actualSize = loanList.size();

        Assert.assertEquals(String.format("Number of rows expected: %d, actual: %d", expectedSize, actualSize),
                            expectedSize, actualSize);
    }

    @Test
    public void takePlayerLoansNoPatternCheck() throws DAOException {
        int    playerId       = 100;
        String acquirePattern = "%";

        int expectedSize = 3;

        List<Loan> loanList   = daoHelper.getLoanDAO().takePlayerLoans(playerId, acquirePattern);
        int        actualSize = loanList.size();

        Assert.assertEquals(String.format("Number of rows expected: %d, actual: %d", expectedSize, actualSize),
                            expectedSize, actualSize);
    }

    @Test
    public void takeLoanListAcquireExpirePatternCheck() throws DAOException {
        String acquirePattern = "2017-03%";
        String expirePattern  = "2017-04%";

        int expectedSize = 2;

        List<Loan> loanList   = daoHelper.getLoanDAO().takeLoanList(acquirePattern, expirePattern);
        int        actualSize = loanList.size();

        Assert.assertEquals(String.format("Number of rows expected: %d, actual: %d", expectedSize, actualSize),
                            expectedSize, actualSize);
    }

    @Test
    public void takeLoanListOnlyAcquireCheck() throws DAOException {
        String acquirePattern = "2017-03%";
        String expirePattern  = "%";

        int expectedSize = 2;

        List<Loan> loanList   = daoHelper.getLoanDAO().takeLoanList(acquirePattern, expirePattern);
        int        actualSize = loanList.size();

        Assert.assertEquals(String.format("Number of rows expected: %d, actual: %d", expectedSize, actualSize),
                            expectedSize, actualSize);
    }

    @Test
    public void takeLoanListOnlyExpireCheck() throws DAOException {
        String acquirePattern = "%";
        String expirePattern  = "2017-04%";

        int expectedSize = 2;

        List<Loan> loanList   = daoHelper.getLoanDAO().takeLoanList(acquirePattern, expirePattern);
        int        actualSize = loanList.size();

        Assert.assertEquals(String.format("Number of rows expected: %d, actual: %d", expectedSize, actualSize),
                            expectedSize, actualSize);
    }

    @Test
    public void takeLoanListNoPatternCheck() throws DAOException {
        String acquirePattern = "%";
        String expirePattern  = "%";

        int expectedSize = 6;

        List<Loan> loanList   = daoHelper.getLoanDAO().takeLoanList(acquirePattern, expirePattern);
        int        actualSize = loanList.size();

        Assert.assertEquals(String.format("Number of rows expected: %d, actual: %d", expectedSize, actualSize),
                            expectedSize, actualSize);
    }

    @Test
    public void insertLoanIdCheck() throws DAOException {
        int playerId = 100;

        int expectedLoanId = 7;

        int actualLoanId = daoHelper.getLoanDAO().insertLoan(playerId, new BigDecimal(200), new BigDecimal(20));

        Assert.assertEquals(String.format("Inserted loan id expected: %d, actual: %d", expectedLoanId, actualLoanId),
                            expectedLoanId, actualLoanId);
    }

    @Test
    public void insertLoanCheck() throws DAOException, DatabaseUnitException, SQLException {
        int        playerId = 101;
        BigDecimal amount   = new BigDecimal(200);
        BigDecimal percent  = new BigDecimal(20);
        String[]   ignore   = {"id", "acquire", "expire"};

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_LOAN);

        daoHelper.getLoanDAO().insertLoan(playerId, amount, percent);
        ITable actualTable = connection.createTable(TABLE_LOAN);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void payLoanCheck() throws DatabaseUnitException, DAOException, SQLException {
        int        loanId    = 2;
        BigDecimal payAmount = new BigDecimal(50.0);

        IDataSet expectedDataSet = buildDataSet(XML_PAID);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_LOAN);

        daoHelper.getLoanDAO().payLoan(loanId, payAmount);

        ITable actualTable = connection.createTable(TABLE_LOAN);
        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable,
                                                                              expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, filteredActualTable);
    }

    @Test
    public void payLoanTrueCheck() throws DatabaseUnitException, DAOException, SQLException {
        int        loanId    = 2;
        BigDecimal payAmount = new BigDecimal(50.0);

        boolean actual = daoHelper.getLoanDAO().payLoan(loanId, payAmount);

        Assert.assertTrue(String.format("Method should return `true`, if it proceeded successfully, but it returns: %s", actual),
                          actual);
    }

    @Test(expected = DAOException.class)
    public void playerIdFkConstraintCheck() throws DAOException {
        int        playerId = 103;
        BigDecimal amount   = new BigDecimal(200);
        BigDecimal percent  = new BigDecimal(20);

        daoHelper.getLoanDAO().insertLoan(playerId, amount, percent);

        Assert.fail("A FK constraint `jcasino`.`loan`, CONSTRAINT `fk_player_loan` FOREIGN KEY (`player_id`) " +
                    "REFERENCES `player` (`id`) should exist.");

    }
}