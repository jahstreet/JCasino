package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.PlayerProfile;
import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
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
import java.time.LocalDate;
import java.util.List;

public class PlayerDAOTest extends AbstractDAOTest {

    private static final String TABLE_PLAYER                 = "player";
    private static final String TABLE_VERIFICATION           = "verification";
    private static final String TABLE_USER                   = "user";
    private static final String XML_PLAYER_DATA              = "by/sasnouskikh/jcasino/dao/player_data.xml";
    private static final String XML_NO_VERIFY                = "by/sasnouskikh/jcasino/dao/player_data_no_ready_to_verify.xml";
    private static final String XML_MONTH_WITHDRAWAL         = "by/sasnouskikh/jcasino/dao/player_data_this_month_withdrawal.xml";
    private static final String XML_INSERTED_USER            = "by/sasnouskikh/jcasino/dao/player_data_inserted_user.xml";
    private static final String XML_INSERT_USER              = "by/sasnouskikh/jcasino/dao/player_data_insert_user.xml";
    private static final String XML_INSERT_PLAYER            = "by/sasnouskikh/jcasino/dao/player_data_insert_player.xml";
    private static final String XML_INSERTED_PLAYER          = "by/sasnouskikh/jcasino/dao/player_data_inserted_player.xml";
    private static final String XML_INSERTED_VERIFICATION    = "by/sasnouskikh/jcasino/dao/player_data_inserted_verification.xml";
    private static final String XML_CHANGED_EMAIL            = "by/sasnouskikh/jcasino/dao/player_data_changed_email.xml";
    private static final String XML_CHANGED_PASSWORD         = "by/sasnouskikh/jcasino/dao/player_data_changed_password.xml";
    private static final String XML_CHANGED_FNAME            = "by/sasnouskikh/jcasino/dao/player_data_changed_fname.xml";
    private static final String XML_CHANGED_MNAME            = "by/sasnouskikh/jcasino/dao/player_data_changed_mname.xml";
    private static final String XML_CHANGED_LNAME            = "by/sasnouskikh/jcasino/dao/player_data_changed_lname.xml";
    private static final String XML_CHANGED_PASSPORT         = "by/sasnouskikh/jcasino/dao/player_data_changed_passport.xml";
    private static final String XML_CHANGED_BIRTHDATE        = "by/sasnouskikh/jcasino/dao/player_data_changed_birthdate.xml";
    private static final String XML_CHANGED_QUESTION         = "by/sasnouskikh/jcasino/dao/player_data_changed_question.xml";
    private static final String XML_CHANGED_VER_STATUS       = "by/sasnouskikh/jcasino/dao/player_data_changed_ver_status.xml";
    private static final String XML_CHANGED_VER_STATUS_COMM  = "by/sasnouskikh/jcasino/dao/player_data_changed_ver_status_with_commentary.xml";
    private static final String XML_CHANGED_SCAN             = "by/sasnouskikh/jcasino/dao/player_data_changed_scan.xml";
    private static final String XML_CHANGED_BALANCE_PAY      = "by/sasnouskikh/jcasino/dao/player_data_changed_balance_pay.xml";
    private static final String XML_CHANGED_BALANCE_WITHDRAW = "by/sasnouskikh/jcasino/dao/player_data_changed_balance_withdraw.xml";
    private static final String XML_CHANGED_ACCOUNT_STATUS   = "by/sasnouskikh/jcasino/dao/player_data_changed_account_status.xml";

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
        DatabaseOperation.CLEAN_INSERT.execute(connection, buildDataSet(XML_NO_VERIFY));
        List<PlayerVerification> actual = daoHelper.getPlayerDAO().takeReadyForVerification();

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takeAccountCheck() throws DAOException, DatabaseUnitException, SQLException {
        DatabaseOperation.CLEAN_INSERT.execute(connection, buildDataSet(XML_MONTH_WITHDRAWAL));
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

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED_USER);
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
        DatabaseOperation.INSERT.execute(connection, buildDataSet(XML_INSERT_USER));
        int      playerId  = 103;
        String   fName     = "MATVEI";
        String   lName     = "ZUEVSKI";
        String   birthDate = "1993-02-22";
        String   passport  = "MP8473622";
        String[] ignore    = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED_PLAYER);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().insertPlayer(playerId, fName, null, lName, birthDate, passport, null, null);
        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void insertPlayerTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        DatabaseOperation.INSERT.execute(connection, buildDataSet(XML_INSERT_USER));
        int    playerId  = 103;
        String fName     = "MATVEI";
        String lName     = "ZUEVSKI";
        String birthDate = "1993-02-22";
        String passport  = "MP8473622";

        boolean actual = daoHelper.getPlayerDAO().insertPlayer(playerId, fName, null, lName, birthDate, passport, null, null);

        Assert.assertTrue(String.format("Method should return `true`, if it proceeded successfully, but it returns: %s", actual),
                          actual);
    }

    @Test(expected = DAOException.class)
    public void insertPlayerIdExistsCheck() throws DAOException {
        int    playerId  = 100;
        String birthDate = "1993-02-22";
        String passport  = "MP8473622";

        daoHelper.getPlayerDAO().insertPlayer(playerId, null, null, null, birthDate, passport, null, null);

        Assert.fail("DAOException should be thrown because of existing row with given id.");
    }

    @Test(expected = DAOException.class)
    public void playerUserIdFkConstraintCheck() throws DAOException {
        int    playerId  = 103;
        String birthDate = "1993-02-22";
        String passport  = "MP8473622";

        daoHelper.getPlayerDAO().insertPlayer(playerId, null, null, null, birthDate, passport, null, null);

        Assert.fail("A FK constraint `jcasino`.`player`, CONSTRAINT `fk_player_user` FOREIGN KEY (`id`) " +
                    "REFERENCES `user` (`id`) should exist.");
    }

    @Test
    public void insertEmptyVerificationCheck() throws DAOException, DatabaseUnitException, SQLException {
        DatabaseOperation.INSERT.execute(connection, buildDataSet(XML_INSERT_PLAYER));
        int      playerId = 103;
        String[] ignore   = {"commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED_VERIFICATION);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_VERIFICATION);

        daoHelper.getPlayerDAO().insertEmptyVerification(playerId);

        ITable actualTable = connection.createTable(TABLE_VERIFICATION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);

    }

    @Test
    public void insertEmptyVerificationTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        DatabaseOperation.INSERT.execute(connection, buildDataSet(XML_INSERT_PLAYER));
        int playerId = 103;

        boolean actual = daoHelper.getPlayerDAO().insertEmptyVerification(playerId);

        Assert.assertTrue(String.format("Method should return `true`, if it proceeded successfully, but it returns: %s", actual),
                          actual);
    }

    @Test(expected = DAOException.class)
    public void insertEmptyVerificationExistsCheck() throws DAOException, DatabaseUnitException, SQLException {
        int playerId = 102;

        daoHelper.getPlayerDAO().insertEmptyVerification(playerId);

        Assert.fail("DAOException should be thrown because of existing row with given id.");
    }

    @Test(expected = DAOException.class)
    public void playerVerificationIdFkConstraintCheck() throws DAOException {
        int playerId = 103;

        daoHelper.getPlayerDAO().insertEmptyVerification(playerId);

        Assert.fail("A FK constraint `jcasino`.`verification`, CONSTRAINT `fk_verification_player` FOREIGN KEY (`id`) " +
                    "REFERENCES `player` (`id`) should exist.");
    }

    @Test
    public void changeEmailCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId = 100;
        String   newEmail = "newmail@mail.test";
        String[] ignore   = {"registration_date"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_EMAIL);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_USER);

        daoHelper.getPlayerDAO().changeEmail(playerId, newEmail);

        ITable actualTable = connection.createTable(TABLE_USER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeEmailTrueCheck() throws DAOException {
        int    playerId = 100;
        String newEmail = "newmail@mail.test";

        boolean actual = daoHelper.getPlayerDAO().changeEmail(playerId, newEmail);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeEmailExistsCheck() throws DAOException {
        int    playerId = 100;
        String newEmail = "anhel@gmail.com";

        daoHelper.getPlayerDAO().changeEmail(playerId, newEmail);

        Assert.fail("New e-mail value shouldn't be equals to existent.");
    }

    @Test(expected = DAOException.class)
    public void changeEmailNoIdCheck() throws DAOException {
        int    playerId = 103;
        String newEmail = "anhel@gmail.com";

        daoHelper.getPlayerDAO().changeEmail(playerId, newEmail);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changePasswordCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId        = 100;
        String   newPasswordHash = "7fa3b767c460b5jd8be4d49030b349c7";
        String[] ignore          = {"registration_date"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_PASSWORD);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_USER);

        daoHelper.getPlayerDAO().changePassword(playerId, newPasswordHash);

        ITable actualTable = connection.createTable(TABLE_USER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changePasswordTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId        = 100;
        String newPasswordHash = "7fa3b767c460b5jd8be4d49030b349c7";

        boolean actual = daoHelper.getPlayerDAO().changePassword(playerId, newPasswordHash);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changePasswordNoIdCheck() throws DAOException {
        int    playerId        = 103;
        String newPasswordHash = "7fa3b767c460b5jd8be4d49030b349c7";

        daoHelper.getPlayerDAO().changePassword(playerId, newPasswordHash);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeFirstNameCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId     = 100;
        String   newFirstName = "RICHARD";
        String[] ignore       = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_FNAME);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().changeFirstName(playerId, newFirstName);

        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeFirstNameTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId     = 100;
        String newFirstName = "RICHARD";

        boolean actual = daoHelper.getPlayerDAO().changeFirstName(playerId, newFirstName);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeFirstNameNoIdCheck() throws DAOException {
        int    playerId     = 103;
        String newFirstName = "RICHARD";

        daoHelper.getPlayerDAO().changeFirstName(playerId, newFirstName);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeMiddleNameCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId      = 100;
        String   newMiddleName = "ALBERTOVICH";
        String[] ignore        = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_MNAME);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().changeMiddleName(playerId, newMiddleName);

        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeMiddleNameTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId      = 100;
        String newMiddleName = "ALBERTOVICH";

        boolean actual = daoHelper.getPlayerDAO().changeMiddleName(playerId, newMiddleName);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeMiddleNameNoIdCheck() throws DAOException {
        int    playerId      = 103;
        String newMiddleName = "ALBERTOVICH";

        daoHelper.getPlayerDAO().changeMiddleName(playerId, newMiddleName);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeLastNameCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId    = 100;
        String   newLastName = "MOLODCOV";
        String[] ignore      = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_LNAME);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().changeLastName(playerId, newLastName);

        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeLastNameTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId    = 100;
        String newLastName = "MOLODCOV";

        boolean actual = daoHelper.getPlayerDAO().changeLastName(playerId, newLastName);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeLastNameNoIdCheck() throws DAOException {
        int    playerId    = 103;
        String newLastName = "MOLODCOV";

        daoHelper.getPlayerDAO().changeLastName(playerId, newLastName);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changePassportNumberCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId          = 100;
        String   newPassportNumber = "ML2938821";
        String[] ignore            = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_PASSPORT);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().changePassportNumber(playerId, newPassportNumber);

        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changePassportNumberTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId          = 100;
        String newPassportNumber = "ML2938821";

        boolean actual = daoHelper.getPlayerDAO().changePassportNumber(playerId, newPassportNumber);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changePassportNumberNoIdCheck() throws DAOException {
        int    playerId          = 103;
        String newPassportNumber = "ML2938821";

        daoHelper.getPlayerDAO().changePassportNumber(playerId, newPassportNumber);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeBirthdateCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId  = 100;
        String   birthdate = "1994-12-25";
        String[] ignore    = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_BIRTHDATE);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().changeBirthdate(playerId, birthdate);

        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeBirthdateTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId  = 100;
        String birthdate = "1994-12-25";

        boolean actual = daoHelper.getPlayerDAO().changeBirthdate(playerId, birthdate);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeBirthdateNoIdCheck() throws DAOException {
        int    playerId  = 103;
        String birthdate = "1994-12-25";

        daoHelper.getPlayerDAO().changeBirthdate(playerId, birthdate);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeSecretQuestionCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId   = 100;
        String   question   = "New question?";
        String   answerHash = "7fa3b767c460b22a2be4d49030b349c7";
        String[] ignore     = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_QUESTION);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().changeSecretQuestion(playerId, question, answerHash);

        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeSecretQuestionTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId   = 100;
        String question   = "New question?";
        String answerHash = "7fa3b767c460b22a2be4d49030b349c7";

        boolean actual = daoHelper.getPlayerDAO().changeSecretQuestion(playerId, question, answerHash);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeSecretQuestionNoIdCheck() throws DAOException {
        int    playerId   = 103;
        String question   = "New question?";
        String answerHash = "7fa3b767c460b22a2be4d49030b349c7";

        daoHelper.getPlayerDAO().changeSecretQuestion(playerId, question, answerHash);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeVerStatusCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId  = 100;
        byte     newStatus = 1;
        String[] ignore    = {"commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_VER_STATUS);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_VERIFICATION);

        daoHelper.getPlayerDAO().changeVerificationStatus(playerId, newStatus);

        ITable actualTable = connection.createTable(TABLE_VERIFICATION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeVerStatusTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int  playerId  = 100;
        byte newStatus = 1;

        boolean actual = daoHelper.getPlayerDAO().changeVerificationStatus(playerId, newStatus);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeVerStatusNoIdCheck() throws DAOException {
        int  playerId  = 103;
        byte newStatus = 1;

        daoHelper.getPlayerDAO().changeVerificationStatus(playerId, newStatus);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeVerStatusCommCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId   = 100;
        byte     newStatus  = 1;
        int      adminId    = 1;
        String   commentary = "Test commentary.";
        String[] ignore     = {"date"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_VER_STATUS_COMM);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_VERIFICATION);

        daoHelper.getPlayerDAO().changeVerificationStatus(playerId, newStatus, adminId, commentary);

        ITable actualTable = connection.createTable(TABLE_VERIFICATION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeVerStatusCommTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId   = 100;
        byte   newStatus  = 1;
        int    adminId    = 1;
        String commentary = "Test commentary.";

        boolean actual = daoHelper.getPlayerDAO().changeVerificationStatus(playerId, newStatus, adminId, commentary);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeVerStatusCommNoIdCheck() throws DAOException {
        int    playerId   = 103;
        byte   newStatus  = 1;
        int    adminId    = 1;
        String commentary = "Test commentary.";

        daoHelper.getPlayerDAO().changeVerificationStatus(playerId, newStatus, adminId, commentary);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeScanPathCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId    = 100;
        String   newScanPath = "scan/2/7.png";
        String[] ignore      = {"commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_SCAN);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_VERIFICATION);

        daoHelper.getPlayerDAO().changeScanPath(playerId, newScanPath);

        ITable actualTable = connection.createTable(TABLE_VERIFICATION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeScanPathTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId    = 100;
        String newScanPath = "scan/2/7.png";

        boolean actual = daoHelper.getPlayerDAO().changeScanPath(playerId, newScanPath);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeScanPathNoIdCheck() throws DAOException {
        int    playerId    = 103;
        String newScanPath = "scan/2/7.png";

        daoHelper.getPlayerDAO().changeScanPath(playerId, newScanPath);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeBalancePaymentCheck() throws DAOException, DatabaseUnitException, SQLException {
        int        playerId = 100;
        BigDecimal payment  = new BigDecimal(200);
        String[]   ignore   = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_BALANCE_PAY);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().changeBalance(playerId, payment, Transaction.TransactionType.REPLENISH);

        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeBalanceWithdrawalCheck() throws DAOException, DatabaseUnitException, SQLException {
        int        playerId   = 101;
        BigDecimal withdrawal = new BigDecimal(100);
        String[]   ignore     = {"admin_id", "commentary"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_BALANCE_WITHDRAW);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().changeBalance(playerId, withdrawal, Transaction.TransactionType.WITHDRAW);

        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeBalanceTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int        playerId = 100;
        BigDecimal payment  = new BigDecimal(200);

        boolean actual = daoHelper.getPlayerDAO().changeBalance(playerId, payment, Transaction.TransactionType.REPLENISH);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeBalanceNoIdCheck() throws DAOException {
        int        playerId = 103;
        BigDecimal payment  = new BigDecimal(200);

        daoHelper.getPlayerDAO().changeBalance(playerId, payment, Transaction.TransactionType.REPLENISH);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test
    public void changeAccountStatusCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId   = 100;
        int    adminId    = 1;
        String status     = "vip";
        String commentary = "Test commentary.";

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_ACCOUNT_STATUS);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_PLAYER);

        daoHelper.getPlayerDAO().changeAccountStatus(playerId, adminId, status, commentary);

        ITable actualTable = connection.createTable(TABLE_PLAYER);

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    public void changeAccountStatusTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    playerId   = 100;
        int    adminId    = 1;
        String status     = "vip";
        String commentary = "Test commentary.";

        boolean actual = daoHelper.getPlayerDAO().changeAccountStatus(playerId, adminId, status, commentary);

        Assert.assertTrue("Method should return `true`, if it proceeded successfully, but it returns: %s", actual);
    }

    @Test(expected = DAOException.class)
    public void changeAccountStatusNoIdCheck() throws DAOException {
        int    playerId   = 103;
        int    adminId    = 1;
        String status     = "vip";
        String commentary = "Test commentary.";

        daoHelper.getPlayerDAO().changeAccountStatus(playerId, adminId, status, commentary);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test(expected = DAOException.class)
    public void playerStatusEnumCheck() throws DAOException {
        int    playerId   = 100;
        int    adminId    = 1;
        String status     = "unknown";
        String commentary = "Test commentary.";

        daoHelper.getPlayerDAO().changeAccountStatus(playerId, adminId, status, commentary);

        Assert.fail("Value of `status` should match definite status-enum values.");
    }


}