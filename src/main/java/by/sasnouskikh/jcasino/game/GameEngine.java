package by.sasnouskikh.jcasino.game;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.service.ServiceException;
import by.sasnouskikh.jcasino.service.StreakService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides main game service and interface features support for slot-machine.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class GameEngine {
    private static final Logger LOGGER = LogManager.getLogger(GameEngine.class);

    /**
     * Paytable multipliers for different reel result position combinations.
     */
    private static final int CHERRY_MULTIPLIER     = 6;
    private static final int GRAPES_MULTIPLIER     = 12;
    private static final int LEMON_MULTIPLIER      = 32;
    private static final int APPLE_MULTIPLIER      = 45;
    private static final int ORANGE_MULTIPLIER     = 60;
    private static final int BANANA_MULTIPLIER     = 90;
    private static final int WATERMELON_MULTIPLIER = 180;

    /**
     * Instance of container for the array of available reel value of slot-machine enumeration instances in order
     * they placed on reel.
     *
     * @see ReelValue
     * @see ReelValues
     */
    private static final ReelValue[] reelValues = new ReelValues().getReelValues();

    /**
     * Outer forbidding to create this class instances.
     */
    private GameEngine() {
    }

    /**
     * Provides service of 'Spin' button of slot-machine while real money play mode is chosen. Builds next {@link Roll}
     * object of current streak due to input data and adds it to given {@link Streak} roll collection. Changes player
     * balance value at database due to roll result.
     *
     * @param streak current streak object
     * @param offset current roll reel offsets
     * @param lines  current roll played lines state
     * @param bet    current roll 1 line bet
     * @return current roll result
     * @throws ServiceException if database connection error occurred during changing player balance
     * @see DAOHelper
     * @see PlayerDAO#changeBalance(int, BigDecimal, Transaction.TransactionType)
     * @see #countTotalBet(BigDecimal, boolean[])
     * @see #buildRoll(int[], int[], boolean[], BigDecimal)
     * @see StreakService#parseCurrentRollArray(String, int)
     */
    public static BigDecimal spin(Streak streak, int[] offset, boolean[] lines, BigDecimal bet) throws ServiceException {
        int        playerId = streak.getPlayerId();
        Roll       roll     = buildRoll(StreakService.parseCurrentRollArray(streak.getRoll(), streak.getRolls().size()), offset, lines, bet);
        BigDecimal result   = roll.getResult();
        if (result.compareTo(BigDecimal.ZERO) != 0) {
            try (DAOHelper daoHelper = new DAOHelper()) {
                PlayerDAO playerDAO = daoHelper.getPlayerDAO();
                if (!playerDAO.changeBalance(playerId, result, Transaction.TransactionType.REPLENISH)) {
                    throw new ServiceException("Database connection error while spinning.");
                }
            } catch (DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
                throw new ServiceException("Database connection error while spinning.");
            }
        }
        streak.getRolls().add(roll);
        return result;
    }

    /**
     * Provides service of 'Spin' button of slot-machine while demo play mode is chosen. Builds next {@link Roll}
     * object of current streak due to input data and adds it to given {@link Streak} roll collection.
     *
     * @param streak current streak object
     * @param offset current roll reel offsets
     * @param lines  current roll played lines state
     * @param bet    current roll 1 line bet
     * @return current roll result
     * @see #countTotalBet(BigDecimal, boolean[])
     * @see #buildRoll(int[], int[], boolean[], BigDecimal)
     * @see StreakService#parseCurrentRollArray(String, int)
     */
    public static BigDecimal spinDemo(Streak streak, int[] offset, boolean[] lines, BigDecimal bet) {
        Roll       roll   = buildRoll(StreakService.parseCurrentRollArray(streak.getRoll(), streak.getRolls().size()), offset, lines, bet);
        BigDecimal result = roll.getResult();
        streak.getRolls().add(roll);
        return result;
    }

    /**
     * Defines win lines array due to result reel positions of slot-machine and played lines state array.
     *
     * @param pos         result reel positions of slot-machine
     * @param playedLines played lines state array
     * @return win lines array
     * @see #defineWinLines(int[])
     */
    public static boolean[] defineWinLines(int[] pos, boolean[] playedLines) {
        boolean[] result   = new boolean[LINE_NUMBER];
        boolean[] winLines = defineWinLines(pos);
        for (int i = 0; i < LINE_NUMBER; i++) {
            result[i] = winLines[i] && playedLines[i];
        }
        return result;
    }

    /**
     * Defines result reel positions of slot-machine. Result positions are numbers from 1 to 60 which are index of reel
     * values placed on 2 line (top horizontal line) of slot-machine (see Rules).
     *
     * @param roll   roll reel values of slot-machine generated with streak
     * @param offset roll reel offsets
     * @return win lines array
     */
    public static int[] defineReelPos(int[] roll, int[] offset) {
        int[] reelPos = new int[REEL_NUMBER];
        for (int i = 0; i < REEL_NUMBER; i++) {
            reelPos[i] = roll[i] + offset[i];
            while (reelPos[i] < 1) {
                reelPos[i] += 60;
            }
            while (reelPos[i] > 60) {
                reelPos[i] -= 60;
            }
        }
        return reelPos;
    }

    /**
     * Count current roll total bet due to its 1 line bet and played lines state array values.
     *
     * @param bet         current roll 1 line bet
     * @param playedLines current roll played lines state array
     * @return current roll total bet
     * @see #countPlayedLines(boolean[])
     */
    public static BigDecimal countTotalBet(BigDecimal bet, boolean[] playedLines) {
        return bet.multiply(BigDecimal.valueOf(countPlayedLines(playedLines)));
    }

    /**
     * Builds {@link Roll} object due to given parameters.
     *
     * @param roll   current roll reel values generated with streak
     * @param offset current roll reel offsets
     * @param lines  current roll played lines state
     * @param bet    current roll 1 line bet
     * @return built {@link Roll} object
     * @see #countResult(int[], int[], boolean[], BigDecimal)
     */
    private static Roll buildRoll(int[] roll, int[] offset, boolean[] lines, BigDecimal bet) {
        Roll result = new Roll();
        result.setRoll(roll);
        result.setOffset(offset);
        result.setLines(lines);
        result.setBet(bet);
        result.setResult(countResult(roll, offset, lines, bet));
        return result;
    }

    /**
     * Counts roll result due to given parameters.
     *
     * @param roll   current roll reel values generated with streak
     * @param offset current roll reel offsets
     * @param lines  current roll played lines state
     * @param bet    current roll 1 line bet
     * @return roll result
     * @see #defineReelPos(int[], int[])
     * @see #defineWinLines(int[], boolean[])
     */
    private static BigDecimal countResult(int[] roll, int[] offset, boolean[] lines, BigDecimal bet) {
        BigDecimal result   = BigDecimal.ZERO;
        int[]      reelPos  = defineReelPos(roll, offset);
        boolean[]  winLines = defineWinLines(reelPos, lines);
        for (int i = 0; i < LINE_NUMBER; i++) {
            if (winLines[i]) {
                result = result.add(bet.multiply(defineMultiplier(reelPos[0], i + 1)));
            }
        }
        BigDecimal totalBet = countTotalBet(bet, lines);
        return result.subtract(totalBet);
    }

    /**
     * Defines win multiplier of paytable for won line due to its first value position.
     *
     * @param pos        line first value position
     * @param lineNumber number of won line
     * @return win multiplier
     * @see #getReelValue(int)
     */
    private static BigDecimal defineMultiplier(int pos, int lineNumber) {
        ReelValue reelValue = null;
        switch (lineNumber) {
            case 1:
                reelValue = getReelValue(pos + 1);
                break;
            case 2:
            case 4:
                reelValue = getReelValue(pos);
                break;
            case 3:
            case 5:
                reelValue = getReelValue(pos + 2);
                break;
        }
        if (reelValue == null) {
            return BigDecimal.ZERO;
        }
        int multiplier;
        switch (reelValue) {
            case CHERRY:
                multiplier = CHERRY_MULTIPLIER;
                break;
            case GRAPES:
                multiplier = GRAPES_MULTIPLIER;
                break;
            case LEMON:
                multiplier = LEMON_MULTIPLIER;
                break;
            case APPLE:
                multiplier = APPLE_MULTIPLIER;
                break;
            case ORANGE:
                multiplier = ORANGE_MULTIPLIER;
                break;
            case BANANA:
                multiplier = BANANA_MULTIPLIER;
                break;
            case WATERMELON:
                multiplier = WATERMELON_MULTIPLIER;
                break;
            default:
                return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(multiplier);
    }

    /**
     * Counts number of played lines in array.
     *
     * @param lines played lines state array
     * @return number of played lines
     */
    private static int countPlayedLines(boolean[] lines) {
        int result = 0;
        for (boolean line : lines) {
            if (line) {
                result++;
            }
        }
        return result;
    }

    /**
     * Takes reel value from {@link #reelValues} due to its index.
     *
     * @param index reel value index to take
     * @return taken {@link ReelValue}
     */
    private static ReelValue getReelValue(int index) {
        while (index < 1) {
            index += REEL_LENGTH;
        }
        while (index > REEL_LENGTH) {
            index -= REEL_LENGTH;
        }
        return reelValues[index - 1];
    }

    /**
     * Defines win lines array due to result reel positions of slot-machine.
     *
     * @param pos result reel positions of slot-machine
     * @return win lines array
     * @see #getReelValue(int)
     */
    private static boolean[] defineWinLines(int[] pos) {

        boolean line1 = getReelValue(pos[0] + 1) == getReelValue(pos[1] + 1)
                        && getReelValue(pos[0] + 1) == getReelValue(pos[2] + 1);

        boolean line2 = getReelValue(pos[0]) == getReelValue(pos[1])
                        && getReelValue(pos[0]) == getReelValue(pos[2]);

        boolean line3 = getReelValue(pos[0] + 2) == getReelValue(pos[1] + 2)
                        && getReelValue(pos[0] + 2) == getReelValue(pos[2] + 2);

        boolean line4 = getReelValue(pos[0]) == getReelValue(pos[1] + 1)
                        && getReelValue(pos[0]) == getReelValue(pos[2] + 2);

        boolean line5 = getReelValue(pos[0] + 2) == getReelValue(pos[1] + 1)
                        && getReelValue(pos[0] + 2) == getReelValue(pos[2]);

        return new boolean[]{line1, line2, line3, line4, line5};
    }
}