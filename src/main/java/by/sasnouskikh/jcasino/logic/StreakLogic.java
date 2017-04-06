package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.impl.DAOFactory;
import by.sasnouskikh.jcasino.dao.impl.StreakDAOImpl;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.EMPTY_STRING;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PERCENT;

public class StreakLogic {
    private static final Logger LOGGER = LogManager.getLogger(StreakLogic.class);

    private static final int    BETS_IN_STREAK    = 10;
    private static final int    REEL_NUMBER       = 3;
    private static final int    LINE_NUMBER       = 5;
    private static final char   LINE_NOT_SELECTED = '0';
    private static final char   LINE_SELECTED     = '1';
    private static final String BET_SPLITERATOR   = "_";
    private static final String REEL_SPLITERATOR  = "-";

    private StreakLogic() {
    }

    public static List<Streak> takePlayerStreaks(int id, String month) {
        String monthPattern;
        if (month != null) {
            monthPattern = month;
        } else {
            monthPattern = EMPTY_STRING;
        }
        monthPattern = monthPattern.trim() + PERCENT;
        List<Streak> streakList = null;
        try (StreakDAOImpl streakDAO = DAOFactory.getStreakDAO()) {
            streakList = streakDAO.takePlayerStreaks(id, monthPattern);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return streakList;
    }

    public static List<Streak> takeStreakList(String month, boolean sortByTotal) {
        List<Streak> streakList = null;
        String       monthPattern;
        if (month != null && !month.trim().isEmpty()) {
            monthPattern = month;
        } else {
            monthPattern = EMPTY_STRING;
        }
        monthPattern = monthPattern.trim() + PERCENT;
        try (StreakDAOImpl streakDAO = DAOFactory.getStreakDAO()) {
            streakList = streakDAO.takeStreakList(monthPattern);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        if (sortByTotal) {
            StreakLogic.sortByTotal(streakList, false);
        }
        return streakList;
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

    public static BigDecimal countStreakTotal(List<Roll> rolls) {
        BigDecimal total = BigDecimal.ZERO;
        for (Roll roll : rolls) {
            total = total.add(roll.getResult());
        }
        return total;
    }

    static BigDecimal defineMaxBet(List<Streak> streaks) {
        BigDecimal maxBet = BigDecimal.ZERO;
        if (streaks != null) {
            for (Streak streak : streaks) {
                BigDecimal maxStreakBet = Collections.max(Arrays.asList(parseBetArray(streak.getBet())));
                if (maxBet.compareTo(maxStreakBet) < 0) {
                    maxBet = maxStreakBet;
                }
            }
        }
        return maxBet;
    }

    static BigDecimal countTotalBet(List<Streak> streaks) {
        BigDecimal totalBet = BigDecimal.ZERO;
        if (streaks != null) {
            for (Streak streak : streaks) {
                for (Roll roll : streak.getRolls()) {
                    totalBet = totalBet.add(roll.getBet());
                }
            }
        }
        return totalBet;
    }

    static BigDecimal defineMaxWinRoll(List<Streak> streaks) {
        BigDecimal maxWinRoll = BigDecimal.ZERO;
        if (streaks != null) {
            for (Streak streak : streaks) {
                BigDecimal maxStreakRoll = Collections.max(Arrays.asList(parseBetArray(streak.getResult())));
                if (maxWinRoll.compareTo(maxStreakRoll) < 0) {
                    maxWinRoll = maxStreakRoll;
                }
            }
        }
        return maxWinRoll;
    }

    static BigDecimal defineMaxWinStreak(List<Streak> streaks) {
        BigDecimal maxWinStreak = BigDecimal.ZERO;
        if (streaks != null) {
            for (Streak streak : streaks) {
                BigDecimal streakResult = BigDecimal.ZERO;
                for (Roll roll : streak.getRolls()) {
                    streakResult = streakResult.add(roll.getResult());
                }
                if (maxWinStreak.compareTo(streakResult) < 0) {
                    maxWinStreak = streakResult;
                }
            }
        }
        return maxWinStreak;
    }

    static BigDecimal countTotalWin(List<Streak> streaks) {
        BigDecimal totalWin = BigDecimal.ZERO;
        if (streaks != null) {
            for (Streak streak : streaks) {
                for (Roll roll : streak.getRolls()) {
                    totalWin = totalWin.add(roll.getResult());
                }
            }
        }
        return totalWin;
    }

    static void sortByTotal(List<Streak> list, boolean ascending) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Comparator<Streak> comparator = new TotalComparator();
        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(list, comparator);
    }

    private static class TotalComparator implements Comparator<Streak> {

        /**
         * Compares its two arguments for order.  Returns a negative integer,
         * zero, or a positive integer as the first argument is less than, equal
         * to, or greater than the second.<p>
         * <p>
         * In the foregoing description, the notation
         * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
         * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
         * <tt>0</tt>, or <tt>1</tt> according to whether the value of
         * <i>expression</i> is negative, zero or positive.<p>
         * <p>
         * The implementor must ensure that <tt>sgn(compare(x, y)) ==
         * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
         * implies that <tt>compare(x, y)</tt> must throw an exception if and only
         * if <tt>compare(y, x)</tt> throws an exception.)<p>
         * <p>
         * The implementor must also ensure that the relation is transitive:
         * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
         * <tt>compare(x, z)&gt;0</tt>.<p>
         * <p>
         * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
         * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
         * <tt>z</tt>.<p>
         * <p>
         * It is generally the case, but <i>not</i> strictly required that
         * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
         * any comparator that violates this condition should clearly indicate
         * this fact.  The recommended language is "Note: this comparator
         * imposes orderings that are inconsistent with equals."
         *
         * @param o1 the first object to be compared.
         * @param o2 the second object to be compared.
         * @return a negative integer, zero, or a positive integer as the
         * first argument is less than, equal to, or greater than the
         * second.
         * @throws NullPointerException if an argument is null and this
         *                              comparator does not permit null arguments
         * @throws ClassCastException   if the arguments' types prevent them from
         *                              being compared by this comparator.
         */
        @Override
        public int compare(Streak o1, Streak o2) {
            return o1.getTotal().compareTo(o2.getTotal());
        }
    }
}