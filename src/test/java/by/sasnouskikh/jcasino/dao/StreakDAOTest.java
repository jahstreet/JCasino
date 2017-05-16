package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.service.StreakService;
import by.sasnouskikh.jcasino.manager.JCasinoEncryptor;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;

public class StreakDAOTest extends AbstractDAOTest {

    private static final String TABLE_STREAK        = "streak";
    private static final String XML_STREAK_DATA     = "by/sasnouskikh/jcasino/dao/streak_data.xml";
    private static final String XML_INSERTED_STREAK = "by/sasnouskikh/jcasino/dao/streak_data_inserted_streak.xml";
    private static final String XML_UPDATED_STREAK  = "by/sasnouskikh/jcasino/dao/streak_data_updated_streak.xml";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        beforeData = buildDataSet(XML_STREAK_DATA);
        DatabaseOperation.CLEAN_INSERT.execute(connection, beforeData);
    }

    @Test
    public void takeStreakCheck() throws DAOException {
        int           streakId = 1;
        int           playerId = 100;
        LocalDateTime date     = Timestamp.valueOf("2017-03-11 21:17:09.0").toLocalDateTime();
        String        roll     = "27-11-28_54-56-57_4-36-41_39-11-60_28-10-54_45-33-13_20-51-20_41-8-36_2-43-60_15-12-5";
        String        offset   = "5-2-59_5-2-45_5-2-36_5-3-30_4-7-30_8-6-30_11-8-34_11-8-34_10-8-34_10-8-34";
        String        lines    = "00111_00111_01001_01001_11111_11111_11111_11111_11111_11111";
        String        bet      = "0.5_0.5_0.5_4.75_4.5_4.5_5.0_5.0_4.5_4.5";
        String        result   = "0_0_3.0_0_54.0_54.0_0_30.0_0_54.0";

        Streak expected = new Streak();
        expected.setId(streakId);
        expected.setPlayerId(playerId);
        expected.setDate(date);
        expected.setRoll(roll);
        expected.setOffset(offset);
        expected.setLines(lines);
        expected.setBet(bet);
        expected.setResult(result);
        expected.setRollMD5(JCasinoEncryptor.encryptMD5(roll));
        ArrayDeque<Roll> rolls = StreakService.buildRollList(expected);
        expected.setRolls(rolls);
        expected.setTotal(StreakService.countStreakTotal(rolls));

        Streak actual = daoHelper.getStreakDAO().takeStreak(streakId);

        Assert.assertEquals(String.format("\nExpected:\t%s\nActual:\t%s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeStreakNoSuchCheck() throws DAOException {
        int streakId = 6;

        Streak actual = daoHelper.getStreakDAO().takeStreak(streakId);

        Assert.assertNull("Taken streak value expected to be null.", actual);
    }

    @Test
    public void takePlayerStreaksCheck() throws DAOException {
        int playerId = 101;

        int expectedRowNumber = 3;

        List<Streak> questionList    = daoHelper.getStreakDAO().takePlayerStreaks(playerId);
        int          actualRowNumber = questionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takePlayerStreaksNoStreaksCheck() throws DAOException {
        int playerId = 102;

        List<Streak> actual = daoHelper.getStreakDAO().takePlayerStreaks(playerId);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void takePlayerStreaksNoIdCheck() throws DAOException {
        int playerId = 103;

        List<Streak> actual = daoHelper.getStreakDAO().takePlayerStreaks(playerId);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void takePlayerStreaksDatePatternCheck() throws DAOException {
        int    playerId    = 101;
        String datePattern = "2017-04%";

        int expectedRowNumber = 2;

        List<Streak> questionList    = daoHelper.getStreakDAO().takePlayerStreaks(playerId, datePattern);
        int          actualRowNumber = questionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takePlayerStreaksAllDatePatternCheck() throws DAOException {
        int    playerId    = 101;
        String datePattern = "%";

        int expectedRowNumber = 3;

        List<Streak> questionList    = daoHelper.getStreakDAO().takePlayerStreaks(playerId, datePattern);
        int          actualRowNumber = questionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takePlayerStreaksDatePatternNoIdCheck() throws DAOException {
        int    playerId    = 103;
        String datePattern = "%";

        List<Streak> actual = daoHelper.getStreakDAO().takePlayerStreaks(playerId, datePattern);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void takePlayerStreaksDatePatternNoStreaksCheck() throws DAOException {
        int    playerId    = 101;
        String datePattern = "2017-06%";

        List<Streak> actual = daoHelper.getStreakDAO().takePlayerStreaks(playerId, datePattern);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void takeStreakListDatePatternCheck() throws DAOException {
        String datePattern = "2017-04%";

        int expectedRowNumber = 3;

        List<Streak> streakList      = daoHelper.getStreakDAO().takeStreakList(datePattern);
        int          actualRowNumber = streakList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takeStreakListAllDatePatternCheck() throws DAOException {
        String datePattern = "%";

        int expectedRowNumber = 5;

        List<Streak> streakList      = daoHelper.getStreakDAO().takeStreakList(datePattern);
        int          actualRowNumber = streakList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takeStreakListDatePatternNoStreaksCheck() throws DAOException {
        String datePattern = "2017-06%";

        List<Streak> actual = daoHelper.getStreakDAO().takeStreakList(datePattern);

        Assert.assertNull("Taken list value expected to be null.", actual);
    }

    @Test
    public void insertStreakCheck() throws DAOException, SQLException, DatabaseUnitException {
        int      playerId = 100;
        String   roll     = "12-2-9_35-12-37_56-36-14_48-22-16_39-12-13_33-1-3_20-26-3_53-35-40_53-26-37_39-25-31";
        String[] ignore   = {"date"};

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED_STREAK);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_STREAK);

        daoHelper.getStreakDAO().insertStreak(playerId, roll);
        ITable actualTable = connection.createTable(TABLE_STREAK);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void insertStreakIdCheck() throws DAOException {
        int    playerId = 100;
        String roll     = "12-2-9_35-12-37_56-36-14_48-22-16_39-12-13_33-1-3_20-26-3_53-35-40_53-26-37_39-25-31";

        int expected = 6;

        int actual = daoHelper.getStreakDAO().insertStreak(playerId, roll);

        Assert.assertEquals(String.format("Inserted streak id expected: %d, actual: %d", expected, actual),
                            expected, actual);
    }

    @Test(expected = DAOException.class)
    public void streakPlayerFkConstraintCheck() throws DAOException {
        int    playerId = 103;
        String roll     = "12-2-9_35-12-37_56-36-14_48-22-16_39-12-13_33-1-3_20-26-3_53-35-40_53-26-37_39-25-31";

        daoHelper.getStreakDAO().insertStreak(playerId, roll);

        Assert.fail("A FK constraint `jcasino`.`streak`, CONSTRAINT `fk_streak_player` FOREIGN KEY (`player_id`) " +
                    "REFERENCES `player` (`id`) should exist.");
    }

    @Test
    public void updateStreakCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      id     = 4;
        String   roll   = "31-10-20_48-40-13_52-32-36_15-21-44_45-18-58_10-30-13_25-34-26_58-59-10_36-39-42_14-50-6";
        String   offset = "10-14-30_10-14-30_10-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30";
        String   lines  = "11111_11111_11111_11111_11111_11111_11111_11111_11111_11111";
        String   bet    = "4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5";
        String   result = "0_0_81.0_0_0_0_0_0_0_27.0";
        String[] ignore = {"date"};

        IDataSet expectedDataSet = buildDataSet(XML_UPDATED_STREAK);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_STREAK);

        daoHelper.getStreakDAO().updateStreak(id, roll, offset, lines, bet, result);
        ITable actualTable = connection.createTable(TABLE_STREAK);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void updateStreakTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    id     = 4;
        String roll   = "31-10-20_48-40-13_52-32-36_15-21-44_45-18-58_10-30-13_25-34-26_58-59-10_36-39-42_14-50-6";
        String offset = "10-14-30_10-14-30_10-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30";
        String lines  = "11111_11111_11111_11111_11111_11111_11111_11111_11111_11111";
        String bet    = "4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5";
        String result = "0_0_81.0_0_0_0_0_0_0_27.0";

        boolean actual = daoHelper.getStreakDAO().updateStreak(id, roll, offset, lines, bet, result);

        Assert.assertTrue(String.format("Method should return `true`, if it proceeded successfully, but it returns: %s", actual),
                          actual);
    }

    @Test(expected = DAOException.class)
    public void updateStreakNoSuchCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    id     = 7;
        String roll   = "31-10-20_48-40-13_52-32-36_15-21-44_45-18-58_10-30-13_25-34-26_58-59-10_36-39-42_14-50-6";
        String offset = "10-14-30_10-14-30_10-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30_14-14-30";
        String lines  = "11111_11111_11111_11111_11111_11111_11111_11111_11111_11111";
        String bet    = "4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5_4.5";
        String result = "0_0_81.0_0_0_0_0_0_0_27.0";

        daoHelper.getStreakDAO().updateStreak(id, roll, offset, lines, bet, result);

        Assert.fail("DAOException should be thrown if no streak with given ID in database.");
    }
}