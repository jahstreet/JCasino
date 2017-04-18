package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.StreakDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.game.GameEngine;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class StreakLogic {
    private static final Logger LOGGER = LogManager.getLogger(StreakLogic.class);

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
        try (DAOHelper daoHelper = new DAOHelper()) {
            StreakDAO streakDAO = daoHelper.getStreakDAO();
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
        try (DAOHelper daoHelper = new DAOHelper()) {
            StreakDAO streakDAO = daoHelper.getStreakDAO();
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
        int              id           = streak.getId();
        ArrayDeque<Roll> rolls        = streak.getRolls();
        String           rollString   = buildRollString(rolls);
        String           offsetString = buildOffsetString(rolls);
        String           lineString   = buildLineString(rolls);
        String           betString    = buildBetString(rolls);
        String           resultString = buildResultString(rolls);
        try (DAOHelper daoHelper = new DAOHelper()) {
            StreakDAO streakDAO = daoHelper.getStreakDAO();
            return streakDAO.updateStreak(id, rollString, offsetString, lineString, betString, resultString);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static Streak generateStreak() {
        Streak streak = new Streak();
        streak.setDate(LocalDateTime.now());
        String roll = generateRollString();
        streak.setRoll(roll);
        streak.setRollMD5(JCasinoEncryptor.encryptMD5(roll));
        streak.setRolls(new ArrayDeque<>());
        return streak;
    }

    public static Streak generateStreak(int playerId) {
        Streak streak = null;
        try (DAOHelper daoHelper = new DAOHelper()) {
            StreakDAO streakDAO = daoHelper.getStreakDAO();
            daoHelper.beginTransaction();
            int streakId = streakDAO.insertStreak(playerId, generateRollString());
            streak = streakDAO.takeStreak(streakId);
            if (streak != null) {
                daoHelper.commit();
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return streak;
    }

    public static int[] parseCurrentRollArray(String roll, int rollIndex) {
        int[] rollArray = parseRollArray(roll);
        int   from      = rollIndex * REEL_NUMBER;
        int   to        = from + REEL_NUMBER;
        return Arrays.copyOfRange(rollArray, from, to);
    }

    public static ArrayDeque<Roll> buildRollList(Streak streak) {
        String roll   = streak.getRoll();
        String offset = streak.getOffset();
        String lines  = streak.getLines();
        String bet    = streak.getBet();
        String result = streak.getResult();
        return buildRollList(roll, offset, lines, bet, result);
    }

    public static BigDecimal countStreakTotal(ArrayDeque<Roll> rolls) {
        BigDecimal total = BigDecimal.ZERO;
        for (Roll roll : rolls) {
            BigDecimal result = roll.getResult();
            if (result != null) {
                total = total.add(result);
            }
        }
        return total;
    }

    public static void completeStreak(Streak streak) {
        final int[]     nullOffsetArray = new int[]{0, 0, 0, 0, 0};
        final boolean[] nullLinesArray  = new boolean[]{false, false, false, false, false};
        if (isComplete(streak)) {
            return;
        }
        ArrayDeque<Roll> rolls      = streak.getRolls();
        String           rollString = streak.getRoll();
        while (rolls.size() < BETS_IN_STREAK) {
            Roll roll = new Roll();
            roll.setRoll(parseCurrentRollArray(rollString, rolls.size()));
            roll.setOffset(nullOffsetArray);
            roll.setLines(nullLinesArray);
            roll.setBet(BigDecimal.ZERO);
            roll.setResult(BigDecimal.ZERO);
            rolls.add(roll);
        }
        streak.setOffset(buildOffsetString(rolls));
        streak.setLines(buildLineString(rolls));
        streak.setBet(buildBetString(rolls));
        streak.setResult(buildResultString(rolls));
    }

    public static boolean isComplete(Streak streak) {
        return !(streak == null || streak.getRolls() == null)
               && streak.getRolls().size() >= BETS_IN_STREAK;
    }

    static BigDecimal defineMaxBet(List<Streak> streaks) {
        BigDecimal maxBet = BigDecimal.ZERO;
        if (streaks != null) {
            for (Streak streak : streaks) {
                BigDecimal[] betArray  = parseBetArray(streak.getBet());
                boolean[]    lineArray = parseLineArray(streak.getLines());
                if (betArray == null || betArray.length < BETS_IN_STREAK
                    || lineArray == null || lineArray.length < BETS_IN_STREAK * LINE_NUMBER) {
                    continue;
                }
                for (int i = 0; i < BETS_IN_STREAK; i++) {
                    BigDecimal bet      = betArray[i];
                    int        from     = i * LINE_NUMBER;
                    int        to       = from + LINE_NUMBER;
                    boolean[]  lines    = Arrays.copyOfRange(lineArray, from, to);
                    BigDecimal totalBet = GameEngine.countTotalBet(bet, lines);
                    if (maxBet.compareTo(totalBet) < 0) {
                        maxBet = totalBet;
                    }
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
                    BigDecimal bet = roll.getBet();
                    if (bet != null) {
                        totalBet = totalBet.add(bet);
                    }
                }
            }
        }
        return totalBet;
    }

    static BigDecimal defineMaxWinRoll(List<Streak> streaks) {
        BigDecimal maxWinRoll = BigDecimal.ZERO;
        if (streaks != null) {
            for (Streak streak : streaks) {
                BigDecimal[] betArray      = parseBetArray(streak.getResult());
                BigDecimal   maxStreakRoll = BigDecimal.ZERO;
                if (betArray != null && betArray.length > 0) {
                    maxStreakRoll = Collections.max(Arrays.asList(betArray));
                }
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
                    BigDecimal result = roll.getResult();
                    if (result != null) {
                        streakResult = streakResult.add(result);
                    }
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
                    BigDecimal result = roll.getResult();
                    if (result != null && result.signum() > 0) {
                        totalWin = totalWin.add(result);
                    }
                }
            }
        }
        return totalWin;
    }

    private static String generateRollString() {
        StringBuilder streak = new StringBuilder();
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            for (int j = 0; j < REEL_NUMBER; j++) {
                streak.append(RandomGenerator.generateNumber(1, REEL_LENGTH)).append(REEL_SPLITERATOR);
            }
            streak.setCharAt(streak.length() - 1, BET_SPLITERATOR);
        }
        return streak.deleteCharAt(streak.length() - 1).toString().trim();
    }

    private static ArrayDeque<Roll> buildRollList(String stringRoll, String stringOffset, String stringLines,
                                                 String stringBet, String stringResult) {
        ArrayDeque<Roll> rollArrayDeque = new ArrayDeque<>();
        if (stringRoll == null || stringRoll.trim().isEmpty()
            || stringOffset == null || stringOffset.trim().isEmpty()
            || stringLines == null || stringLines.trim().isEmpty()
            || stringBet == null || stringBet.trim().isEmpty()
            || stringResult == null || stringResult.trim().isEmpty()) {
            return rollArrayDeque;
        }
        int[]        rolls   = parseRollArray(stringRoll);
        int[]        offsets = parseRollArray(stringOffset);
        boolean[]    lines   = parseLineArray(stringLines);
        BigDecimal[] bets    = parseBetArray(stringBet);
        BigDecimal[] results = parseBetArray(stringResult);
        if (rolls == null || rolls.length < BETS_IN_STREAK * REEL_NUMBER
            || offsets == null || offsets.length < BETS_IN_STREAK * REEL_NUMBER
            || lines == null || lines.length < BETS_IN_STREAK * LINE_NUMBER
            || bets == null || bets.length < BETS_IN_STREAK
            || results == null || results.length < BETS_IN_STREAK) {
            return rollArrayDeque;
        }
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            int  reelOffset = i * REEL_NUMBER;
            int  lineOffset = i * LINE_NUMBER;
            Roll roll       = new Roll();
            roll.setRoll(Arrays.copyOfRange(rolls, reelOffset, reelOffset + REEL_NUMBER));
            roll.setLines(Arrays.copyOfRange(lines, lineOffset, lineOffset + LINE_NUMBER));
            roll.setOffset(Arrays.copyOfRange(offsets, reelOffset, reelOffset + REEL_NUMBER));
            roll.setBet(bets[i]);
            roll.setResult(results[i]);
            rollArrayDeque.add(roll);
        }
        return rollArrayDeque;
    }

    private static String buildRollString(ArrayDeque<Roll> rolls) {
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

    private static String buildOffsetString(ArrayDeque<Roll> rolls) {
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

    private static String buildLineString(ArrayDeque<Roll> rolls) {
        StringBuilder result = new StringBuilder();
        for (Roll r : rolls) {
            boolean[] lines = r.getLines();
            for (int j = 0; j < LINE_NUMBER; j++) {
                if (lines[j]) {
                    result.append(LINE_SELECTED);
                } else {
                    result.append(LINE_NOT_SELECTED);
                }
            }
            result.append(BET_SPLITERATOR);
        }
        return result.deleteCharAt(result.length() - 1).toString().trim();
    }

    private static String buildBetString(ArrayDeque<Roll> rolls) {
        StringBuilder result = new StringBuilder();
        for (Roll r : rolls) {
            result.append(r.getBet().toPlainString()).append(BET_SPLITERATOR);
        }
        return result.deleteCharAt(result.length() - 1).toString().trim();
    }

    private static String buildResultString(ArrayDeque<Roll> rolls) {
        StringBuilder result = new StringBuilder();
        for (Roll r : rolls) {
            result.append(r.getResult().toPlainString()).append(BET_SPLITERATOR);
        }
        return result.deleteCharAt(result.length() - 1).toString().trim();
    }

    private static int[] parseRollArray(String source) {
        if (source == null || source.trim().isEmpty()) {
            return new int[]{};
        }
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

    private static BigDecimal[] parseBetArray(String source) {
        if (source == null || source.trim().isEmpty()) {
            return new BigDecimal[]{};
        }
        String[]     betArray = source.split(String.valueOf(BET_SPLITERATOR));
        BigDecimal[] result   = new BigDecimal[BETS_IN_STREAK];
        for (int i = 0; i < BETS_IN_STREAK; i++) {
            result[i] = new BigDecimal(betArray[i]);
        }
        return result;
    }

    private static boolean[] parseLineArray(String source) {
        if (source == null || source.trim().isEmpty()) {
            return new boolean[]{};
        }
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

    private static void sortByTotal(List<Streak> list, boolean ascending) {
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