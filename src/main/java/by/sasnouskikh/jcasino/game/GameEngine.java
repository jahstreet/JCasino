package by.sasnouskikh.jcasino.game;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.logic.LogicException;
import by.sasnouskikh.jcasino.logic.StreakLogic;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class GameEngine {
    private static final Logger LOGGER = LogManager.getLogger(GameEngine.class);

    private static final int CHERRY_MULTIPLIER     = 6;
    private static final int GRAPES_MULTIPLIER     = 12;
    private static final int LEMON_MULTIPLIER      = 32;
    private static final int APPLE_MULTIPLIER      = 45;
    private static final int ORANGE_MULTIPLIER     = 60;
    private static final int BANANA_MULTIPLIER     = 90;
    private static final int WATERMELON_MULTIPLIER = 180;

    private static final ReelValue[] reelValues = new ReelValues().getReelValues();

    private GameEngine() {
    }

    public static BigDecimal spin(Streak streak, int[] offset, boolean[] lines, BigDecimal bet) throws LogicException {
        int        playerId = streak.getPlayerId();
        BigDecimal totalBet = countTotalBet(bet, lines);
        Roll       roll     = buildRoll(StreakLogic.parseCurrentRollArray(streak.getRoll(), streak.getRolls().size()), offset, lines, bet);
        BigDecimal result   = roll.getResult();
        BigDecimal total    = result.subtract(totalBet);
        try (DAOHelper daoHelper = new DAOHelper()) {
            PlayerDAO playerDAO = daoHelper.getPlayerDAO();
            if (!playerDAO.changeBalance(playerId, total, Transaction.TransactionType.REPLENISH)) {
                throw new LogicException("Database connection error while spinning.");
            }
            streak.getRolls().add(roll);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return total;
    }

    public static BigDecimal spinDemo(Streak streak, int[] offset, boolean[] lines, BigDecimal bet) {
        BigDecimal totalBet = countTotalBet(bet, lines);
        Roll       roll     = buildRoll(StreakLogic.parseCurrentRollArray(streak.getRoll(), streak.getRolls().size()), offset, lines, bet);
        BigDecimal result   = roll.getResult();
        BigDecimal total    = result.subtract(totalBet);
        streak.getRolls().add(roll);
        return total;
    }

    private static Roll buildRoll(int[] roll, int[] offset, boolean[] lines, BigDecimal bet) {
        Roll result = new Roll();
        result.setRoll(roll);
        result.setOffset(offset);
        result.setLines(lines);
        result.setBet(bet);
        result.setResult(countResult(roll, offset, lines, bet));
        return result;
    }

    private static BigDecimal countResult(int[] roll, int[] offset, boolean[] lines, BigDecimal bet) {
        BigDecimal result   = BigDecimal.ZERO;
        int[]      reelPos  = defineReelPos(roll, offset);
        boolean[]  winLines = defineWinLines(reelPos, lines);
        for (int i = 0; i < LINE_NUMBER; i++) {
            if (winLines[i]) {
                result = result.add(bet.multiply(defineMultiplier(reelPos[0], i + 1)));
            }
        }
        return result;
    }

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

    public static int countPlayedLines(boolean[] lines) {
        int result = 0;
        for (boolean line : lines) {
            if (line) {
                result++;
            }
        }
        return result;
    }

    private static ReelValue getReelValue(int index) {
        while (index < 1) {
            index += REEL_LENGTH;
        }
        while (index > REEL_LENGTH) {
            index -= REEL_LENGTH;
        }
        return reelValues[index - 1];
    }

    public static boolean[] defineWinLines(int[] pos) {

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

    public static boolean[] defineWinLines(int[] pos, boolean[] playedLines) {
        boolean[] result   = new boolean[LINE_NUMBER];
        boolean[] winLines = defineWinLines(pos);
        for (int i = 0; i < LINE_NUMBER; i++) {
            result[i] = winLines[i] && playedLines[i];
        }
        return result;
    }

    public static boolean[] defineWinLines(int[] roll, int[] offset, boolean[] playedLines) {
        int[] reelPos = defineReelPos(roll, offset);
        return defineWinLines(reelPos, playedLines);
    }

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

    public static BigDecimal countTotalBet(BigDecimal bet, boolean[] playedLines) {
        return bet.multiply(BigDecimal.valueOf(countPlayedLines(playedLines)));
    }
}