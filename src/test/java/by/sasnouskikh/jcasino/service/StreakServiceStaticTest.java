package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.StreakDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
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
import org.powermock.reflect.Whitebox;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.BETS_IN_STREAK;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(StreakService.class)
public class StreakServiceStaticTest {

    private static final String           ROLL          = "17-29-48_37-7-8_1-55-57_28-55-49_32-32-47_" +
                                                          "58-46-2_4-22-4_39-11-10_43-44-50_43-18-13";
    private static final String           ROLL_MD5      = "40f597f4b0d5eec67c29842e555aa3bb";
    private static final int[]            ROLL_ARRAY    = {17, 29, 48, 37, 7, 8, 1, 55, 57, 28, 55, 49, 32, 32, 47,
                                                           58, 46, 2, 4, 22, 4, 39, 11, 10, 43, 44, 50, 43, 18, 13};
    private static final String           OFFSET        = "29-2-25_13-13-41_14-50-27_19-17-41_4-22-55_" +
                                                          "11-56-3_45-10-1_7-8-8_36-36-28_33-32-23";
    private static final int[]            OFFSET_ARRAY  = {29, 2, 25, 13, 13, 41, 14, 50, 27, 19, 17, 41, 4, 22, 55,
                                                           11, 56, 3, 45, 10, 1, 7, 8, 8, 36, 36, 28, 33, 32, 23};
    private static final String           LINES         = "11111_11110_11100_11000_10000_" +
                                                          "00000_00001_00011_00111_01111";
    private static final boolean[]        LINES_ARRAY   = {true, true, true, true, true,
                                                           true, true, true, true, false,
                                                           true, true, true, false, false,
                                                           true, true, false, false, false,
                                                           true, false, false, false, false,
                                                           false, false, false, false, false,
                                                           false, false, false, false, true,
                                                           false, false, false, true, true,
                                                           false, false, true, true, true,
                                                           false, true, true, true, true};
    private static final String           BET           = "3.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9";
    private static final BigDecimal[]     BET_ARRAY     = {BigDecimal.valueOf(3.15),
                                                           BigDecimal.valueOf(2.01),
                                                           BigDecimal.valueOf(0),
                                                           BigDecimal.valueOf(0.02),
                                                           BigDecimal.valueOf(5.00),
                                                           BigDecimal.valueOf(4),
                                                           BigDecimal.valueOf(2.15),
                                                           BigDecimal.valueOf(4.9),
                                                           BigDecimal.valueOf(2.3),
                                                           BigDecimal.valueOf(0.90)};
    private static final String           RESULT        = "2.9_-2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9";
    private static final BigDecimal[]     RESULT_ARRAY  = {BigDecimal.valueOf(2.9),
                                                           BigDecimal.valueOf(-2.01),
                                                           BigDecimal.valueOf(0),
                                                           BigDecimal.valueOf(0.02),
                                                           BigDecimal.valueOf(5.00),
                                                           BigDecimal.valueOf(4),
                                                           BigDecimal.valueOf(2.15),
                                                           BigDecimal.valueOf(4.9),
                                                           BigDecimal.valueOf(2.3),
                                                           BigDecimal.valueOf(0.90)};
    private static final int              ROLL_INDEX    = 2;
    private static final int[]            ROLL_2        = {1, 55, 57};
    private static final Streak           STREAK        = new Streak();
    private static final int              STREAK_ID     = 15;
    private static final ArrayDeque<Roll> ROLLS         = new ArrayDeque<>();
    private static final BigDecimal       STREAK_RESULT = BigDecimal.valueOf(20.16);
    private static final List<Streak>     STREAK_LIST   = new ArrayList<>();
    private static final Streak           STREAK_1      = new Streak();
    private static final Streak           STREAK_2      = new Streak();
    private static final Streak           STREAK_3      = new Streak();

    static {
        for (int i = 0; i < 10; i++) {
            Roll roll = new Roll();
            roll.setRoll(Arrays.copyOfRange(ROLL_ARRAY, i * 3, i * 3 + 3));
            roll.setOffset(Arrays.copyOfRange(OFFSET_ARRAY, i * 3, i * 3 + 3));
            roll.setLines(Arrays.copyOfRange(LINES_ARRAY, i * 5, i * 5 + 5));
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

        STREAK_1.setTotal(BigDecimal.valueOf(2));
        STREAK_2.setTotal(null);
        STREAK_3.setTotal(BigDecimal.valueOf(-2));
        STREAK_LIST.addAll(Arrays.asList(STREAK_1, STREAK_2, STREAK_3));
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
        PowerMockito.spy(StreakService.class);
        when(daoHelper.getStreakDAO()).thenReturn(streakDAO);
    }

    @Test
    public void generateStreakCheck() throws Exception {
        PowerMockito.doReturn(ROLL).when(StreakService.class, "generateRollString");

        Streak streak = StreakService.generateStreak();
        boolean result = streak != null &&
                         ROLL.equals(streak.getRoll()) &&
                         ROLL_MD5.equals(streak.getRollMD5()) &&
                         streak.getDate() != null &&
                         streak.getRolls() != null &&
                         streak.getRolls().isEmpty() &&
                         streak.getRolls().getClass() == ArrayDeque.class;

        Assert.assertTrue(result);
    }

    @Test
    public void parseCurrentRollArrayCheck() throws Exception {
        PowerMockito.doReturn(ROLL_ARRAY).when(StreakService.class, "parseRollArray", anyString());

        int[] result = StreakService.parseCurrentRollArray(ROLL, ROLL_INDEX);

        Assert.assertArrayEquals(ROLL_2, result);
    }

    @Test
    public void buildRollListForwardToPrivateCallCheck() throws Exception {
        PowerMockito.doReturn(null).when(StreakService.class, "buildRollList",
                                         anyString(), anyString(), anyString(), anyString(), anyString());
        StreakService.buildRollList(STREAK);

        PowerMockito.verifyPrivate(StreakService.class)
                    .invoke("buildRollList", eq(ROLL), eq(OFFSET), eq(LINES), eq(BET), eq(RESULT));
    }

    @Test
    public void buildRollListReturnPrivateResultCheck() throws Exception {
        PowerMockito.doReturn(ROLLS).when(StreakService.class, "buildRollList",
                                          anyString(), anyString(), anyString(), anyString(), anyString());
        ArrayDeque<Roll> result = StreakService.buildRollList(STREAK);
        if (result == null) {
            Assert.fail("Result expected not to be null.");
        }
        Assert.assertArrayEquals(ROLLS.toArray(), result.toArray());
    }

    @Test
    public void countStreakTotalCheck() {
        Assert.assertEquals(STREAK_RESULT.doubleValue(), StreakService.countStreakTotal(ROLLS).doubleValue(), 1e-4);
    }

    @Test
    public void completeStreakNotCompletedCheck() throws CloneNotSupportedException {
        Streak streakClone = STREAK.clone();
        StreakService.completeStreak(streakClone);

        Assert.assertTrue(StreakService.isComplete(streakClone));
    }

    @Test
    public void completeStreakCompletedCheck() throws CloneNotSupportedException {
        Streak streakClone = STREAK.clone();
        StreakService.completeStreak(streakClone);
        StreakService.completeStreak(streakClone);

        Assert.assertTrue(StreakService.isComplete(streakClone));
    }

    @Test
    public void isCompleteTrueCheck() {
        Streak           streak = new Streak();
        ArrayDeque<Roll> rolls  = new ArrayDeque<>();
        streak.setRolls(rolls);
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            rolls.add(new Roll());
        }

        Assert.assertTrue(StreakService.isComplete(streak));
    }

    @Test
    public void isCompleteNullFalseCheck() {
        Assert.assertFalse(StreakService.isComplete(null));
    }

    @Test
    public void isCompleteEmptyFalseCheck() {
        Streak           streak = new Streak();
        ArrayDeque<Roll> rolls  = new ArrayDeque<>();
        streak.setRolls(rolls);

        Assert.assertFalse(StreakService.isComplete(streak));
    }

    @Test
    public void isCompleteNotFilledFalseCheck() {
        Streak streak = new Streak();
        streak.setRolls(new ArrayDeque<>());

        Assert.assertFalse(StreakService.isComplete(streak));
    }

    @Test
    @SuppressWarnings("all")
    public void takeStreakListSortByTotalResultNullCheck() throws Exception {
        List<Streak> streakList = null;

        Assert.assertNull(Whitebox.invokeMethod(StreakService.class, "sortByTotal", streakList, false));
    }

    @Test
    public void takeStreakListSortByTotalResultAcsendingCheck() throws Exception {
        List<Streak> expected = new ArrayList<>(Arrays.asList(STREAK_2, STREAK_3, STREAK_1));

        List<Streak> actual = new ArrayList<>(STREAK_LIST);
        Whitebox.invokeMethod(StreakService.class, "sortByTotal", actual, true);

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void takeStreakListSortByTotalResultDescendingCheck() throws Exception {
        List<Streak> expected = new ArrayList<>(Arrays.asList(STREAK_1, STREAK_3, STREAK_2));

        List<Streak> actual = new ArrayList<>(STREAK_LIST);
        Whitebox.invokeMethod(StreakService.class, "sortByTotal", actual, false);

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}