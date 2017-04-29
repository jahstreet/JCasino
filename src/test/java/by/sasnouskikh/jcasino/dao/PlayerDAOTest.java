package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.PlayerProfile;
import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
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
import java.time.LocalDate;
import java.util.List;

public class PlayerDAOTest extends AbstractDAOTest {

    private static final String TABLE_PLAYER                     = "player";
    private static final String TABLE_VERIFICATION               = "verification";
    private static final String TABLE_PLAYER_STATUS              = "player_status";
    private static final String TABLE_USER                       = "user";
    private static final String XML_PLAYER_DATA                  = "by/sasnouskikh/jcasino/dao/player_data.xml";
    private static final String XML_PLAYER_DATA_NO_VERIFY        = "by/sasnouskikh/jcasino/dao/player_data_no_ready_to_verify.xml";
    private static final String XML_PLAYER_DATA_MONTH_WITHDRAWAL = "by/sasnouskikh/jcasino/dao/player_data_this_month_withdrawal.xml";
    private static final String XML_PLAYER_DATA_INSERTED_USER    = "by/sasnouskikh/jcasino/dao/player_data_inserted_user.xml";
    private static final String XML_PLAYER_DATA_INSERT_USER      = "by/sasnouskikh/jcasino/dao/player_data_insert_user.xml";
    private static final String XML_PLAYER_DATA_INSERTED_PLAYER  = "by/sasnouskikh/jcasino/dao/player_data_inserted_player.xml";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        beforeData = buildDataSet(XML_PLAYER_DATA);
        DatabaseOperation.CLEAN_INSERT.execute(connection, beforeData);
    }

    @Test
    public void defineIdByEmailCheck() throws DAOException {
        String email = "anhelina@gmail.com";

        int expected = 100;

        int actual = daoHelper.getPlayerDAO().defineIdByEmail(email);

        Assert.assertEquals(String.format("Defined ID expected: %d, actual: %d", expected, actual),
                            expected, actual);
    }

    @Test
    public void defineIdByEmailNoEmailCheck() throws DAOException {
        String email = "this@email.net";

        int expected = 0;

        int actual = daoHelper.getPlayerDAO().defineIdByEmail(email);

        Assert.assertEquals(String.format("Defined ID expected: %d, actual: %d", expected, actual),
                            expected, actual);
    }

    @Test
    public void defineEmailByIdCheck() throws DAOException {
        int playerId = 101;

        String expected = "anhel@gmail.com";

        String actual = daoHelper.getPlayerDAO().defineEmailById(playerId);

        Assert.assertEquals(String.format("Defined e-mail expected: %s, actual: %s", expected, actual),
                            expected, actual);
    }

    @Test
    public void defineEmailByIdNoIdCheck() throws DAOException {
        int playerId = 103;

        String actual = daoHelper.getPlayerDAO().defineEmailById(playerId);

        Assert.assertEquals(String.format("Defined e-mail expected: %s, actual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void defineNameByEmailCheck() throws DAOException {
        String email = "anhelina@gmail.com";

        String expected = "ALIAKSANDER";

        String actual = daoHelper.getPlayerDAO().defineNameByEmail(email);

        Assert.assertEquals(String.format("Defined name expected: %s, actual: %s", expected, actual),
                            expected, actual);
    }

    @Test
    public void defineNameByEmailNoEmailCheck() throws DAOException {
        String email = "this@email.net";

        String actual = daoHelper.getPlayerDAO().defineNameByEmail(email);

        Assert.assertEquals(String.format("Defined name expected: %s, actual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void defineNameByEmailNoNameCheck() throws DAOException {
        String email = "anhel@gmail.com";

        String actual = daoHelper.getPlayerDAO().defineNameByEmail(email);

        Assert.assertEquals(String.format("Defined name expected: %s, actual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takeProfileCheck() throws DAOException {
        int    playerId  = 100;
        String fName     = "ALIAKSANDER";
        String lName     = "SASNOUSKIKH";
        String birthdate = "1991-09-24";
        String passport  = "KH1731245";
        String question  = "Вопрос?";

        PlayerProfile expected = new PlayerProfile();
        expected.setfName(fName);
        expected.setmName(null);
        expected.setlName(lName);
        expected.setBirthDate(LocalDate.parse(birthdate));
        expected.setPassport(passport);
        expected.setQuestion(question);

        PlayerProfile actual = daoHelper.getPlayerDAO().takeProfile(playerId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeProfileNoSuchCheck() throws DAOException {
        int playerId = 103;

        PlayerProfile actual = daoHelper.getPlayerDAO().takeProfile(playerId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takeVerificationCheck() throws DAOException {
        int                                   playerId         = 100;
        PlayerVerification.VerificationStatus status           = PlayerVerification.VerificationStatus.NOT_VERIFIED;
        String                                verificationDate = "2017-04-06 21:01:24.0";
        int                                   adminId          = 1;
        String                                passport         = "scan/2/6.png";

        PlayerVerification expected = new PlayerVerification();
        expected.setPlayerId(playerId);
        expected.setStatus(status);
        expected.setProfileVerified(true);
        expected.setEmailVerified(true);
        expected.setScanVerified(false);
        expected.setVerificationDate(Timestamp.valueOf(verificationDate).toLocalDateTime());
        expected.setAdminId(adminId);
        expected.setCommentary(null);
        expected.setPassport(passport);

        PlayerVerification actual = daoHelper.getPlayerDAO().takeVerification(playerId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeVerificationNoSuchCheck() throws DAOException {
        int playerId = 103;

        PlayerVerification actual = daoHelper.getPlayerDAO().takeVerification(playerId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takeReadyForVerificationCheck() throws DAOException {
        int expected = 2;

        List<PlayerVerification> verificationList = daoHelper.getPlayerDAO().takeReadyForVerification();
        int                      actual           = verificationList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeReadyForVerificationNoSuchCheck() throws DAOException, DatabaseUnitException, SQLException {
        DatabaseOperation.CLEAN_INSERT.execute(connection, buildDataSet(XML_PLAYER_DATA_NO_VERIFY));
        List<PlayerVerification> actual = daoHelper.getPlayerDAO().takeReadyForVerification();

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takeAccountCheck() throws DAOException, DatabaseUnitException, SQLException {
        DatabaseOperation.CLEAN_INSERT.execute(connection, buildDataSet(XML_PLAYER_DATA_MONTH_WITHDRAWAL));
        int                     playerId        = 100;
        PlayerStatus.StatusEnum status          = PlayerStatus.StatusEnum.BASIC;
        BigDecimal              balance         = new BigDecimal(0);
        BigDecimal              monthWithdrawal = new BigDecimal(120);
        BigDecimal              betLimit        = new BigDecimal(5);
        BigDecimal              withdrawalLimit = new BigDecimal(300);
        BigDecimal              maxLoan         = new BigDecimal(25);
        BigDecimal              percent         = new BigDecimal(20);

        PlayerAccount expected     = new PlayerAccount();
        PlayerStatus  playerStatus = new PlayerStatus();
        playerStatus.setStatus(status);
        playerStatus.setLoanPercent(percent);
        playerStatus.setBetLimit(betLimit);
        playerStatus.setWithdrawalLimit(withdrawalLimit);
        playerStatus.setMaxLoan(maxLoan);
        expected.setStatus(playerStatus);
        expected.setBalance(balance);
        expected.setThisMonthWithdrawal(monthWithdrawal);

        PlayerAccount actual = daoHelper.getPlayerDAO().takeAccount(playerId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeAccountNoSuchCheck() throws DAOException, DatabaseUnitException, SQLException {
        int playerId = 104;

        PlayerAccount actual = daoHelper.getPlayerDAO().takeAccount(playerId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takeMonthWithdrawalCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId     = 100;
        String monthPattern = "2017-04%";
        double expected     = new BigDecimal(40).doubleValue();

        double actual = daoHelper.getPlayerDAO().takeWithdrawalSum(playerId, monthPattern).doubleValue();

        Assert.assertEquals(String.format("Month withdrawal expected: %s, actual: %s", expected, actual),
                            expected, actual, 1e-4);
    }

    @Test
    public void takeMonthWithdrawalAllCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId     = 100;
        String monthPattern = "%";

        double expected = new BigDecimal(125).doubleValue();

        double actual = daoHelper.getPlayerDAO().takeWithdrawalSum(playerId, monthPattern).doubleValue();

        Assert.assertEquals(String.format("Total withdrawal expected: %s, actual: %s", expected, actual),
                            expected, actual, 1e-4);
    }

    @Test
    public void takeMonthWithdrawalNoCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId     = 100;
        String monthPattern = "2016%";
        double expected     = BigDecimal.ZERO.doubleValue();

        double actual = daoHelper.getPlayerDAO().takeWithdrawalSum(playerId, monthPattern).doubleValue();

        Assert.assertEquals(String.format("Result expected: %s, actual: %s", expected, actual),
                            expected, actual, 1e-4);
    }

    @Test
    public void insertUserPlayerCheck() throws DatabaseUnitException, DAOException, SQLException {
        String   email    = "newuser@jcasino.by";
        String   password = "5d3c47fddf8cbac13c1687c612f331d8";
        String[] ignore   = {"id", "registration_date"};

        IDataSet expectedDataSet = buildDataSet(XML_PLAYER_DATA_INSERTED_USER);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_USER);

        daoHelper.getPlayerDAO().insertUserPlayer(email, password);
        ITable actualTable = connection.createTable(TABLE_USER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void insertUserPlayerIdCheck() throws DAOException {
        String email    = "newuser@jcasino.by";
        String password = "5d3c47fddf8cbac13c1687c612f331d8";

        int expected = 103;

        int actual = daoHelper.getPlayerDAO().insertUserPlayer(email, password);

        Assert.assertEquals(String.format("Inserted user id expected: %d, actual: %d", expected, actual),
                            expected, actual);
    }

    @Test(expected = DAOException.class)
    public void insertUserPlayerUniqueEmailCheck() throws DAOException {
        String email    = "jahstreetlove@gmail.com";
        String password = "5d3c47fddf8cbac13c1687c612f331d8";

        daoHelper.getPlayerDAO().insertUserPlayer(email, password);

        Assert.fail("Field `user`.`email` should be unique.");
    }

    @Test
    public void insertPlayerCheck() throws DatabaseUnitException, DAOException, SQLException {
        DatabaseOperation.INSERT.execute(connection, buildDataSet(XML_PLAYER_DATA_INSERT_USER));
        int      playerId  = 103;
        String   fName     = "MATVEI";
        String   lName     = "ZUEVSKI";
        String   birthDate = "1993-02-22";
        String   passport  = "MP8473622";
        String[] ignore    = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_PLAYER_DATA_INSERTED_PLAYER);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().insertPlayer(playerId, fName, null, lName, birthDate, passport, null, null);
        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void insertPlayerTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        DatabaseOperation.INSERT.execute(connection, buildDataSet(XML_PLAYER_DATA_INSERT_USER));
        int    playerId  = 103;
        String fName     = "MATVEI";
        String lName     = "ZUEVSKI";
        String birthDate = "1993-02-22";
        String passport  = "MP8473622";

        boolean actual = daoHelper.getPlayerDAO().insertPlayer(playerId, fName, null, lName, birthDate, passport, null, null);

        Assert.assertTrue(String.format("Method should return true, if it proceeded successfully, but it returns: %s", actual),
                          actual);
    }
}