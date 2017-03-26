package by.sasnouskikh.jcasino.manager;

import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StreakManager {

    private static final int    BETS_IN_STREAK    = 10;
    private static final int    REEL_NUMBER       = 3;
    private static final int    LINE_NUMBER       = 5;
    private static final char   LINE_NOT_SELECTED = '0';
    private static final char   LINE_SELECTED     = '1';
    private static final String BET_SPLITERATOR   = "_";
    private static final String REEL_SPLITERATOR  = "-";

    private StreakManager() {
    }

    public static String buildRollString(int[] source) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            int offset = i * REEL_NUMBER;
            for (int j = 0; j < REEL_NUMBER; j++) {
                builder.append(source[offset + j]);
                if (j < REEL_NUMBER - 1) {
                    builder.append(REEL_SPLITERATOR);
                }
            }
            builder.append(BET_SPLITERATOR);
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 1);
    }

    public static String buildBetString(BigDecimal[] source) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            builder.append(source[i]).append(BET_SPLITERATOR);
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 1);
    }

    public static String buildLineString(boolean[] source) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            int offset = i * LINE_NUMBER;
            for (int j = 0; j < LINE_NUMBER; j++) {
                if (source[offset + j]) {
                    builder.append(LINE_SELECTED);
                } else {
                    builder.append(LINE_NOT_SELECTED);
                }
            }
            builder.append(BET_SPLITERATOR);
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 1);
    }

    public static int[] parseRollArray(String source) {
        String[] betArray = source.split(BET_SPLITERATOR);
        int[]    result   = new int[BETS_IN_STREAK * REEL_NUMBER];
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            int      offset     = i * REEL_NUMBER;
            String[] reelValues = betArray[i].split(REEL_SPLITERATOR);
            for (int j = 0; j < REEL_NUMBER; j++) {
                result[offset + j] = Integer.parseInt(reelValues[j]);
            }
        }
        return result;
    }

    public static BigDecimal[] parseBetArray(String source) {
        String[]     betArray = source.split(BET_SPLITERATOR);
        BigDecimal[] result   = new BigDecimal[BETS_IN_STREAK];
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            result[i] = new BigDecimal(betArray[i]);
        }
        return result;
    }

    public static boolean[] parseLineArray(String source) {
        String[]  betArray = source.split(BET_SPLITERATOR);
        boolean[] result   = new boolean[BETS_IN_STREAK * LINE_NUMBER];
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            int offset = i * LINE_NUMBER;
            for (int j = 0; j < LINE_NUMBER; j++) {
                result[offset + j] = betArray[i].charAt(j) == LINE_SELECTED;
            }
        }
        return result;
    }

    public static List<Roll> buildRollList(Streak streak) {
        String roll   = streak.getRoll();
        String offset = streak.getOffset();
        String lines  = streak.getLines();
        String bet    = streak.getBet();
        String result = streak.getResult();
        return buildRollList(roll, offset, lines, bet, result);
    }

    public static List<Roll> buildRollList(String stringRoll, String stringOffset, String stringLines,
                                          String stringBet, String stringResult) {
        List<Roll>   rollList = new ArrayList<>();
        int[]        rolls    = parseRollArray(stringRoll);
        int[]        offsets  = parseRollArray(stringOffset);
        boolean[]    lines    = parseLineArray(stringLines);
        BigDecimal[] bets     = parseBetArray(stringBet);
        BigDecimal[] results  = parseBetArray(stringResult);
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            int  reelOffset = i * REEL_NUMBER;
            int  lineOffset = i * LINE_NUMBER;
            Roll roll       = new Roll();
            roll.setLines(Arrays.copyOfRange(lines, lineOffset, lineOffset + LINE_NUMBER));
            roll.setRoll(Arrays.copyOfRange(rolls, reelOffset, reelOffset + REEL_NUMBER));
            roll.setOffset(Arrays.copyOfRange(offsets, reelOffset, reelOffset + REEL_NUMBER));
            roll.setBet(bets[i]);
            roll.setResult(results[i]);
            rollList.add(roll);
        }
        return rollList;
    }

    public static BigDecimal defineMaxBet(List<Streak> streaks) {
        BigDecimal maxBet = BigDecimal.ZERO;
        for (Streak streak : streaks) {
            BigDecimal maxStreakBet = Collections.max(Arrays.asList(parseBetArray(streak.getBet())));
            if (maxBet.compareTo(maxStreakBet) < 0) {
                maxBet = maxStreakBet;
            }
        }
        return maxBet;
    }

    public static BigDecimal countTotalBet(List<Streak> streaks) {
        BigDecimal totalBet = BigDecimal.ZERO;
        for (Streak streak : streaks) {
            for (Roll roll : streak.getRolls()) {
                totalBet = totalBet.add(roll.getBet());
            }
        }
        return totalBet;
    }

    public static BigDecimal defineMaxWinRoll(List<Streak> streaks) {
        BigDecimal maxWinRoll = BigDecimal.ZERO;
        for (Streak streak : streaks) {
            BigDecimal maxStreakRoll = Collections.max(Arrays.asList(parseBetArray(streak.getResult())));
            if (maxWinRoll.compareTo(maxStreakRoll) < 0) {
                maxWinRoll = maxStreakRoll;
            }
        }
        return maxWinRoll;
    }

    public static BigDecimal defineMaxWinStreak(List<Streak> streaks) {
        BigDecimal maxWinStreak = BigDecimal.ZERO;
        for (Streak streak : streaks) {
            BigDecimal streakResult = BigDecimal.ZERO;
            for (Roll roll : streak.getRolls()) {
                streakResult = streakResult.add(roll.getResult());
            }
            if (maxWinStreak.compareTo(streakResult) < 0) {
                maxWinStreak = streakResult;
            }
        }
        return maxWinStreak;
    }

    public static BigDecimal countTotalWin(List<Streak> streaks) {
        BigDecimal totalWin = BigDecimal.ZERO;
        for (Streak streak : streaks) {
            for (Roll roll : streak.getRolls()) {
                totalWin = totalWin.add(roll.getResult());
            }
        }
        return totalWin;
    }
}