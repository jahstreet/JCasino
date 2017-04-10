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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.EMPTY_STRING;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PERCENT;

public class StreakLogic {
    private static final Logger LOGGER = LogManager.getLogger(StreakLogic.class);

    private static final int  REEL_LENGTH       = 60;
    private static final int  BETS_IN_STREAK    = 10;
    private static final int  REEL_NUMBER       = 3;
    private static final int  LINE_NUMBER       = 5;
    private static final char LINE_NOT_SELECTED = '0';
    private static final char LINE_SELECTED     = '1';
    private static final char BET_SPLITERATOR   = '_';
    private static final char REEL_SPLITERATOR  = '-';

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

    public static boolean updateStreak(Streak streak) {
        int        id           = streak.getId();
        List<Roll> rolls        = streak.getRolls();
        String     rollString   = buildRollString(rolls);
        String     offsetString = buildOffsetString(rolls);
        String     lineString   = buildLineString(rolls);
        String     betString    = buildBetString(rolls);
        String     resultString = buildResultString(rolls);
        try (StreakDAOImpl streakDAO = DAOFactory.getStreakDAO()) {
            return streakDAO.updateStreak(id, rollString, offsetString, lineString, betString, resultString);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static String generateRollString() {
        StringBuilder streak = new StringBuilder();
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            for (int j = 0; j < REEL_NUMBER; j++) {
                streak.append(RandomGenerator.generateNumber(1, REEL_LENGTH)).append(REEL_SPLITERATOR);
            }
            streak.setCharAt(streak.length() - 1, BET_SPLITERATOR);
        }
        return streak.deleteCharAt(streak.length() - 1).toString().trim();
    }

    public static int[] generateRollArray() {
        return parseRollArray(generateRollString());
    }

    public static Streak generateStreak() {
        Streak streak = new Streak();
        streak.setDate(LocalDateTime.now());
        String roll = generateRollString();
        streak.setRoll(roll);
        streak.setRollMD5(JCasinoEncryptor.encryptMD5(roll));
        streak.setRolls(new ArrayList<>());
        return streak;
    }

    public static Streak generateStreak(int playerId) {
        Streak streak = null;
        try (StreakDAOImpl streakDAO = DAOFactory.getStreakDAO()) {
            streakDAO.beginTransaction();
            int streakId = streakDAO.insertStreak(playerId, generateRollString());
            streak = streakDAO.takeStreak(streakId);
            if (streak != null) {
                streakDAO.commit();
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return streak;
    }

    public static int[] parseCurrentRollArray(String roll, int rollsPlayed) {
        int[] rollArray = parseRollArray(roll);
        int   from      = (rollsPlayed) * REEL_NUMBER;
        int   to        = from + REEL_NUMBER;
        return Arrays.copyOfRange(rollArray, from, to);
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

    public static String buildRollString(List<Roll> rolls) {
        StringBuilder result = new StringBuilder();
        for (Roll r : rolls) {
            int[] roll = r.getRoll();
            for (int i : roll) {
                result.append(i).append(REEL_SPLITERATOR);
            }
            result.setCharAt(result.length() - 1, BET_SPLITERATOR);
        }
        return result.deleteCharAt(result.length() - 1).toString().trim();
    }

    public static String buildOffsetString(List<Roll> rolls) {
        StringBuilder result = new StringBuilder();
        for (Roll r : rolls) {
            int[] offset = r.getOffset();
            for (int i : offset) {
                result.append(i).append(REEL_SPLITERATOR);
            }
            result.setCharAt(result.length() - 1, BET_SPLITERATOR);
        }
        return result.deleteCharAt(result.length() - 1).toString().trim();
    }

    public static String buildLineString(List<Roll> rolls) {
        StringBuilder result = new StringBuilder();
        for (Roll r : rolls) {
            boolean[] lines = r.getLines();
            for (boolean line : lines) {
                if (line) {
                    result.append(1);
                } else {
                    result.append(0);
                }
                result.append(BET_SPLITERATOR);
            }
        }
        return result.deleteCharAt(result.length() - 1).toString().trim();
    }

    public static String buildBetString(List<Roll> rolls) {
        StringBuilder result = new StringBuilder();
        for (Roll r : rolls) {
            result.append(r.getBet().toPlainString()).append(BET_SPLITERATOR);
        }
        return result.deleteCharAt(result.length() - 1).toString().trim();
    }

    public static String buildResultString(List<Roll> rolls) {
        StringBuilder result = new StringBuilder();
        for (Roll r : rolls) {
            result.append(r.getResult().toPlainString()).append(BET_SPLITERATOR);
        }
        return result.deleteCharAt(result.length() - 1).toString().trim();
    }

    public static int[] parseRollArray(String source) {
        String[] betArray = source.split(String.valueOf(BET_SPLITERATOR));
        int[]    result   = new int[BETS_IN_STREAK * REEL_NUMBER];
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            int      offset     = i * REEL_NUMBER;
            String[] reelValues = betArray[i].split(String.valueOf(REEL_SPLITERATOR));
            for (int j = 0; j < REEL_NUMBER; j++) {
                result[offset + j] = Integer.parseInt(reelValues[j]);
            }
        }
        return result;
    }

    public static BigDecimal[] parseBetArray(String source) {
        String[]     betArray = source.split(String.valueOf(BET_SPLITERATOR));
        BigDecimal[] result   = new BigDecimal[BETS_IN_STREAK];
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            result[i] = new BigDecimal(betArray[i]);
        }
        return result;
    }

    public static boolean[] parseLineArray(String source) {
        String[]  betArray = source.split(String.valueOf(BET_SPLITERATOR));
        boolean[] result   = new boolean[BETS_IN_STREAK * LINE_NUMBER];
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            int offset = i * LINE_NUMBER;
            for (int j = 0; j < LINE_NUMBER; j++) {
                result[offset + j] = betArray[i].charAt(j) == LINE_SELECTED;
            }
        }
        return result;
    }

    //TODO need?
    public static String buildRollString(int[] source) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            int offset = i * REEL_NUMBER;
            for (int j = 0; j < REEL_NUMBER; j++) {
                builder.append(source[offset + j]).append(REEL_SPLITERATOR);
            }
            builder.setCharAt(builder.length() - 1, BET_SPLITERATOR);
        }
        return builder.deleteCharAt(builder.length() - 1).toString().trim();
    }

    //TODO need?
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
        return builder.deleteCharAt(builder.length() - 1).toString().trim();
    }

    //TODO need?
    public static String buildBetString(BigDecimal[] source) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            builder.append(source[i]).append(BET_SPLITERATOR);
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
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

    public static boolean isComplete(Streak streak) {
        return !(streak == null || streak.getRolls() == null)
               && streak.getRolls().size() >= BETS_IN_STREAK;
    }

    private static class TotalComparator implements Comparator<Streak> {

        /**
         * Compares its two arguments for order.  Returns pressedKey negative integer,
         * zero, or pressedKey positive integer as the first argument is less than, equal
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
         * @return pressedKey negative integer, zero, or pressedKey positive integer as the
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