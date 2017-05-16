package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.StreakDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class StreakServiceTest {

    private static final String       DATE_PATTERN               = "anyPattern";
    private static final String       MODIFIED_DATE_PATTERN      = "anyPattern%";
    private static final String       MODIFIED_NULL_DATE_PATTERN = "%";
    private static final int          PLAYER_ID                  = 8;
    private static final List<Streak> STREAK_LIST                = new ArrayList<>();

    private static final ArrayDeque<Roll> ROLLS        = new ArrayDeque<>();
    private static final Streak           STREAK       = new Streak();
    private static final int              STREAK_ID    = 15;
    private static final String           ROLL         = "17-29-48_37-7-8_1-55-57_28-55-49_32-32-47_" +
                                                         "58-46-2_4-22-4_39-11-10_43-44-50_43-18-13";
    private static final String           ROLL_MD5     = "40f597f4b0d5eec67c29842e555aa3bb";
    private static final int[]            ROLL_ARRAY   = {17, 29, 48, 37, 7, 8, 1, 55, 57, 28, 55, 49, 32, 32, 47,
                                                          58, 46, 2, 4, 22, 4, 39, 11, 10, 43, 44, 50, 43, 18, 13};
    private static final String           OFFSET       = "29-2-25_13-13-41_14-50-27_19-17-41_4-22-55_" +
                                                         "11-56-3_45-10-1_7-8-8_36-36-28_33-32-23";
    private static final int[]            OFFSET_ARRAY = {29, 2, 25, 13, 13, 41, 14, 50, 27, 19, 17, 41, 4, 22, 55,
                                                          11, 56, 3, 45, 10, 1, 7, 8, 8, 36, 36, 28, 33, 32, 23};
    private static final String           LINES        = "11111_11110_11100_11000_10000_" +
                                                         "00000_00001_00011_00111_01111";
    private static final boolean[]        LINES_ARRAY  = {true, true, true, true, true,
                                                          true, true, true, true, false,
                                                          true, true, true, false, false,
                                                          true, true, false, false, false,
                                                          true, false, false, false, false,
                                                          false, false, false, false, false,
                                                          false, false, false, false, true,
                                                          false, false, false, true, true,
                                                          false, false, true, true, true,
                                                          false, true, true, true, true};
    private static final String           BET          = "3.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9";
    private static final BigDecimal[]     BET_ARRAY    = {BigDecimal.valueOf(3.15),
                                                          BigDecimal.valueOf(2.01),
                                                          BigDecimal.valueOf(0),
                                                          BigDecimal.valueOf(0.02),
                                                          BigDecimal.valueOf(5.00),
                                                          BigDecimal.valueOf(4),
                                                          BigDecimal.valueOf(2.15),
                                                          BigDecimal.valueOf(4.9),
                                                          BigDecimal.valueOf(2.3),
                                                          BigDecimal.valueOf(0.90)};
    private static final String           RESULT       = "2.9_-2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9";
    private static final BigDecimal[]     RESULT_ARRAY = {BigDecimal.valueOf(2.9),
                                                          BigDecimal.valueOf(-2.01),
                                                          BigDecimal.valueOf(0),
                                                          BigDecimal.valueOf(0.02),
                                                          BigDecimal.valueOf(5.00),
                                                          BigDecimal.valueOf(4),
                                                          BigDecimal.valueOf(2.15),
                                                          BigDecimal.valueOf(4.9),
                                                          BigDecimal.valueOf(2.3),
                                                          BigDecimal.valueOf(0.90)};

    static {
        for (int i = 0; i < 10; i++) {
            Roll roll = new Roll();
            roll.setRoll(Arrays.copyOfRange(ROLL_ARRAY, i * 3, (i + 1) * 3));
            roll.setOffset(Arrays.copyOfRange(OFFSET_ARRAY, i * 3, (i + 1) * 3));
            roll.setLines(Arrays.copyOfRange(LINES_ARRAY, i * 5, (i + 1) * 5));
            roll.setBet(BET_ARRAY[i]);
            roll.setResult(RESULT_ARRAY[i]);
            ROLLS.add(roll);
        }

        STREAK.setId(STREAK_ID);
        STREAK.setRoll(ROLL);
        STREAK.setOffset(OFFSET);
        STREAK.setLines(LINES);
        STREAK.setBet(BET);
        STREAK.setResult(RESULT);
        STREAK.setRollMD5(ROLL_MD5);
        STREAK.setRolls(ROLLS);

        STREAK_LIST.addAll(Arrays.asList(new Streak(), new Streak(), new Streak()));
    }

    @Mock
    private StreakDAO     streakDAO;
    @Mock
    private DAOHelper     daoHelper;
    @InjectMocks
    private StreakService streakService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(daoHelper.getStreakDAO()).thenReturn(streakDAO);
    }

    @Test
    public void takePlayerStreaksStreakDAOTakeCallCheck() throws DAOException {
        when(streakDAO.takePlayerStreaks(anyInt(), anyString())).thenReturn(null);
        streakService.takePlayerStreaks(PLAYER_ID, DATE_PATTERN);

        verify(streakDAO).takePlayerStreaks(PLAYER_ID, MODIFIED_DATE_PATTERN);
    }

    @Test
    public void takePlayerStreaksModifyDatePatternNullCheck() throws DAOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(streakDAO.takePlayerStreaks(anyInt(), anyString())).thenReturn(null);
        streakService.takePlayerStreaks(PLAYER_ID, null);
        verify(streakDAO).takePlayerStreaks(anyInt(), captor.capture());

        Assert.assertEquals(MODIFIED_NULL_DATE_PATTERN, captor.getValue());
    }

    @Test
    public void takePlayerStreaksReturnStreakDAOTakeResultNullCheck() throws DAOException {
        when(streakDAO.takePlayerStreaks(anyInt(), anyString())).thenReturn(null);

        Assert.assertNull(streakService.takePlayerStreaks(PLAYER_ID, DATE_PATTERN));
    }

    @Test
    public void takePlayerStreaksReturnStreakDAOTakeResultNotNullCheck() throws DAOException {
        when(streakDAO.takePlayerStreaks(anyInt(), anyString())).thenReturn(STREAK_LIST);

        Assert.assertArrayEquals(STREAK_LIST.toArray(),
                                 streakService.takePlayerStreaks(PLAYER_ID, DATE_PATTERN).toArray());
    }

    @Test
    public void takePlayerStreaksDAOExceptionThrownReturnNullCheck() throws DAOException {
        when(streakDAO.takePlayerStreaks(anyInt(), anyString()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(streakService.takePlayerStreaks(PLAYER_ID, DATE_PATTERN));
    }

    @Test
    public void takeStreakListStreakDAOTakeCallCheck() throws DAOException {
        when(streakDAO.takeStreakList(anyString())).thenReturn(null);
        streakService.takeStreakList(DATE_PATTERN, false);

        verify(streakDAO).takeStreakList(MODIFIED_DATE_PATTERN);
    }

    @Test
    public void takeStreakListModifyDatePatternNullCheck() throws DAOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(streakDAO.takeStreakList(anyString())).thenReturn(null);
        streakService.takeStreakList(null, false);
        verify(streakDAO).takeStreakList(captor.capture());

        Assert.assertEquals(MODIFIED_NULL_DATE_PATTERN, captor.getValue());
    }

    @Test
    public void takeStreakListReturnStreakDAOTakeResultNullCheck() throws DAOException {
        when(streakDAO.takeStreakList(anyString())).thenReturn(null);

        Assert.assertNull(streakService.takeStreakList(DATE_PATTERN, false));
    }

    @Test
    public void takeStreakListReturnStreakDAOTakeResultNotNullCheck() throws DAOException {
        when(streakDAO.takeStreakList(anyString())).thenReturn(STREAK_LIST);

        Assert.assertArrayEquals(STREAK_LIST.toArray(),
                                 streakService.takeStreakList(DATE_PATTERN, false).toArray());
    }

    @Test
    public void takeStreakListDAOExceptionThrownReturnNullCheck() throws DAOException {
        when(streakDAO.takeStreakList(anyString())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(streakService.takeStreakList(DATE_PATTERN, false));
    }

    @Test
    @PrepareForTest(StreakService.class)
    public void takeStreakListSortByTotalInvokeCheck() throws Exception {
        when(streakDAO.takeStreakList(anyString())).thenReturn(STREAK_LIST);

        PowerMockito.spy(StreakService.class);
        PowerMockito.doNothing().when(StreakService.class, "sortByTotal", any(List.class), anyBoolean());
        streakService.takeStreakList(DATE_PATTERN, true);

        PowerMockito.verifyPrivate(StreakService.class).invoke("sortByTotal", eq(STREAK_LIST), anyBoolean());
    }

    @Test
    public void updateStreakStreakDAOUpdateCallCheck() throws DAOException {
        when(streakDAO.updateStreak(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(true);
        streakService.updateStreak(STREAK);

        verify(streakDAO).updateStreak(STREAK_ID, ROLL, OFFSET, LINES, BET, RESULT);
    }

    @Test
    public void updateStreakReturnStreakDAOResultTrueCheck() throws DAOException {
        when(streakDAO.updateStreak(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(true);

        Assert.assertTrue(streakService.updateStreak(STREAK));
    }

    @Test
    public void updateStreakReturnStreakDAOResultFalseCheck() throws DAOException {
        when(streakDAO.updateStreak(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(false);

        Assert.assertFalse(streakService.updateStreak(STREAK));
    }

    @Test
    public void updateStreakDAOExceptionThrownReturnFalseCheck() throws DAOException {
        when(streakDAO.updateStreak(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(streakService.updateStreak(STREAK));
    }

    @Test
    @PrepareForTest(StreakService.class)
    public void generateStreakStreakDAOInsertCallCheck() throws Exception {
        Streak streak = new Streak();
        streak.setPlayerId(PLAYER_ID);
        streak.setRoll(ROLL);
        streak.setRollMD5(ROLL_MD5);
        streak.setRolls(new ArrayDeque<>());

        PowerMockito.spy(StreakService.class);
        PowerMockito.doReturn(ROLL).when(StreakService.class, "generateRollString");
        when(streakDAO.insertStreak(anyInt(), anyString())).thenReturn(STREAK_ID);
        when(streakDAO.takeStreak(anyInt())).thenReturn(streak);
        streakService.generateStreak(PLAYER_ID);

        verify(streakDAO).insertStreak(PLAYER_ID, ROLL);
    }

    @Test
    @PrepareForTest(StreakService.class)
    public void generateStreakStreakDAOTakeCallCheck() throws Exception {
        Streak streak = new Streak();
        streak.setPlayerId(PLAYER_ID);
        streak.setRoll(ROLL);
        streak.setRollMD5(ROLL_MD5);
        streak.setRolls(new ArrayDeque<>());

        PowerMockito.spy(StreakService.class);
        PowerMockito.doReturn(ROLL).when(StreakService.class, "generateRollString");
        when(streakDAO.insertStreak(anyInt(), anyString())).thenReturn(STREAK_ID);
        when(streakDAO.takeStreak(anyInt())).thenReturn(streak);
        streakService.generateStreak(PLAYER_ID);

        verify(streakDAO).takeStreak(STREAK_ID);
    }

    @Test
    @PrepareForTest(StreakService.class)
    public void generateStreakReturnStreakDAOTakeResultNullCheck() throws Exception {
        PowerMockito.spy(StreakService.class);
        PowerMockito.doReturn(ROLL).when(StreakService.class, "generateRollString");
        when(streakDAO.insertStreak(anyInt(), anyString())).thenReturn(STREAK_ID);
        when(streakDAO.takeStreak(anyInt())).thenReturn(null);

        Assert.assertNull(streakService.generateStreak(PLAYER_ID));
    }

    @Test
    @PrepareForTest(StreakService.class)
    public void generateStreakReturnStreakDAOTakeResultNotNullCheck() throws Exception {
        PowerMockito.spy(StreakService.class);
        PowerMockito.doReturn(ROLL).when(StreakService.class, "generateRollString");
        when(streakDAO.insertStreak(anyInt(), anyString())).thenReturn(STREAK_ID);
        when(streakDAO.takeStreak(anyInt())).thenReturn(STREAK);

        Assert.assertEquals(STREAK, streakService.generateStreak(PLAYER_ID));
    }

    @Test
    @PrepareForTest(StreakService.class)
    public void generateStreakDAOExceptionThrownOnInsertReturnNullCheck() throws Exception {
        PowerMockito.spy(StreakService.class);
        PowerMockito.doReturn(ROLL).when(StreakService.class, "generateRollString");
        when(streakDAO.insertStreak(anyInt(), anyString())).thenThrow(new DAOException("Database connection error."));
        when(streakDAO.takeStreak(anyInt())).thenReturn(STREAK);

        Assert.assertNull(streakService.generateStreak(PLAYER_ID));
    }

    @Test
    @PrepareForTest(StreakService.class)
    public void generateStreakDAOExceptionThrownOnTakeReturnNullCheck() throws Exception {
        PowerMockito.spy(StreakService.class);
        PowerMockito.doReturn(ROLL).when(StreakService.class, "generateRollString");
        when(streakDAO.insertStreak(anyInt(), anyString())).thenReturn(STREAK_ID);
        when(streakDAO.takeStreak(anyInt())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(streakService.generateStreak(PLAYER_ID));
    }

    @Test
    @PrepareForTest(StreakService.class)
    public void generateStreakSQLExceptionThrownOnDAOHelperBeginTransactionReturnNullCheck() throws Exception {
        PowerMockito.spy(StreakService.class);
        PowerMockito.doReturn(ROLL).when(StreakService.class, "generateRollString");
        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();
        when(streakDAO.insertStreak(anyInt(), anyString())).thenReturn(STREAK_ID);
        when(streakDAO.takeStreak(anyInt())).thenReturn(STREAK);

        Assert.assertNull(streakService.generateStreak(PLAYER_ID));
    }

    @Test
    @PrepareForTest(StreakService.class)
    public void generateStreakSQLExceptionThrownOnDAOHelperCommitReturnNullCheck() throws Exception {
        PowerMockito.spy(StreakService.class);
        PowerMockito.doReturn(ROLL).when(StreakService.class, "generateRollString");
        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();
        when(streakDAO.insertStreak(anyInt(), anyString())).thenReturn(STREAK_ID);
        when(streakDAO.takeStreak(anyInt())).thenReturn(STREAK);

        Assert.assertNull(streakService.generateStreak(PLAYER_ID));
    }


}