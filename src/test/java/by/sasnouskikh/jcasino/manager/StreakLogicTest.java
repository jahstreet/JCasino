package by.sasnouskikh.jcasino.manager;

import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.logic.StreakLogic;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class StreakLogicTest {
    private static final String ROLL_STRING = "24-15-88_32-19-18_77-11-10_1-2-3_3-2-1_" +
                                              "42-15-12_3-4-5_12-52-23_12-3-42_12-4-1";
    private static final int[]  ROLL        = new int[]{24, 15, 88, 32, 19, 18, 77, 11, 10, 1, 2, 3, 3, 2, 1,
                                                        42, 15, 12, 3, 4, 5, 12, 52, 23, 12, 3, 42, 12, 4, 1};

    private static final String       BET_STRING = "3.15_2.01_0_0.02_5.0_4_2.15_4.9_2.3_0.9";
    private static final BigDecimal[] BET        = new BigDecimal[]{BigDecimal.valueOf(3.15),
                                                                    BigDecimal.valueOf(2.01),
                                                                    BigDecimal.valueOf(0),
                                                                    BigDecimal.valueOf(0.02),
                                                                    BigDecimal.valueOf(5.00),
                                                                    BigDecimal.valueOf(4),
                                                                    BigDecimal.valueOf(2.15),
                                                                    BigDecimal.valueOf(4.9),
                                                                    BigDecimal.valueOf(2.3),
                                                                    BigDecimal.valueOf(0.90)};

    private static final String    LINE_STRING = "11111_11110_11100_11000_10000_" +
                                                 "00000_00001_00011_00111_01111";
    private static final boolean[] LINE        = new boolean[]{true, true, true, true, true,
                                                               true, true, true, true, false,
                                                               true, true, true, false, false,
                                                               true, true, false, false, false,
                                                               true, false, false, false, false,
                                                               false, false, false, false, false,
                                                               false, false, false, false, true,
                                                               false, false, false, true, true,
                                                               false, false, true, true, true,
                                                               false, true, true, true, true};

    @Test
    public void buildRollStringCheck() {
        String actual = StreakLogic.buildRollString(ROLL);
        Assert.assertEquals(String.format("Expected: %s\nActual: %s\n", ROLL_STRING, actual),
                            ROLL_STRING, actual);
    }

    @Test
    public void buildBetStringCheck() {
        String actual = StreakLogic.buildBetString(BET);
        Assert.assertEquals(String.format("Expected: %s\nActual: %s\n", BET_STRING, actual),
                            BET_STRING, actual);
    }

    @Test
    public void buildLineString() {
        String actual = StreakLogic.buildLineString(LINE);
        Assert.assertEquals(String.format("Expected: %s\nActual: %s\n", LINE_STRING, actual),
                            LINE_STRING, actual);
    }

    @Test
    public void parseRollArrayCheck() {
        int[] actual = StreakLogic.parseRollArray(ROLL_STRING);
        Assert.assertTrue(String.format("Expected: %s\nActual: %s\n", Arrays.toString(ROLL), Arrays.toString(actual)),
                          Arrays.equals(ROLL, actual));
    }

    @Test
    public void parseBetArrayCheck() {
        BigDecimal[] actual = StreakLogic.parseBetArray(BET_STRING);
        System.out.println(Arrays.toString(actual));
        Assert.assertTrue(String.format("Expected: %s\nActual: %s\n", Arrays.toString(BET), Arrays.toString(actual)),
                          Arrays.equals(BET, actual));
    }

    @Test
    public void parseLineArrayCheck() {
        boolean[] actual = StreakLogic.parseLineArray(LINE_STRING);
        Assert.assertTrue(String.format("Expected: %s\nActual: %s\n", Arrays.toString(LINE), Arrays.toString(actual)),
                          Arrays.equals(LINE, actual));
    }

    @Test
    public void buildRollListCheck() {
        List<Roll> actual = StreakLogic.buildRollList(ROLL_STRING, ROLL_STRING, LINE_STRING, BET_STRING, BET_STRING);
        actual.forEach(System.out::println);
    }


}