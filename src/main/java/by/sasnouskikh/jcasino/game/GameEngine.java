package by.sasnouskikh.jcasino.game;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.impl.DAOFactory;
import by.sasnouskikh.jcasino.dao.impl.PlayerDAOImpl;
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

import static by.sasnouskikh.jcasino.game.ReelValue.*;

public class GameEngine {
    private static final int CHERRY_MULTIPLIER     = 2;
    private static final int GRAPES_MULTIPLIER     = 4;
    private static final int LEMON_MULTIPLIER      = 12;
    private static final int APPLE_MULTIPLIER      = 15;
    private static final int ORANGE_MULTIPLIER     = 20;
    private static final int BANANA_MULTIPLIER     = 30;
    private static final int WATERMELON_MULTIPLIER = 60;

    private static final Logger LOGGER = LogManager.getLogger(GameEngine.class);

    private static final int REEL_LENGTH    = 60;
    private static final int BETS_IN_STREAK = 10;
    private static final int REEL_NUMBER    = 3;
    private static final int LINE_NUMBER    = 5;

    private static final ReelValue[] reelValues = new ReelValue[]{
    CHERRY, GRAPES, CHERRY, LEMON, CHERRY, GRAPES, CHERRY, APPLE, CHERRY, GRAPES,
    CHERRY, GRAPES, CHERRY, LEMON, CHERRY, ORANGE, CHERRY, GRAPES, CHERRY, GRAPES,
    CHERRY, APPLE, CHERRY, LEMON, CHERRY, GRAPES, CHERRY, BANANA, CHERRY, GRAPES,
    CHERRY, GRAPES, CHERRY, ORANGE, CHERRY, GRAPES, CHERRY, APPLE, CHERRY, GRAPES,
    CHERRY, WATERMELON, CHERRY, LEMON, CHERRY, GRAPES, CHERRY, BANANA, CHERRY, GRAPES,
    CHERRY, APPLE, CHERRY, LEMON, CHERRY, GRAPES, CHERRY, ORANGE, CHERRY, GRAPES
    };

    private GameEngine() {
    }

    public static Streak spin(Streak streak, int[] offset, boolean[] lines, BigDecimal bet) throws LogicException {
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            if (!playerDAO.changeBalance(streak.getPlayerId(),
                                         bet, Transaction.TransactionType.WITHDRAW)) {
                throw new LogicException("Database connection error while spinning.");
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        streak.getRolls().add(buildRoll(StreakLogic.parseCurrentRollArray(streak.getRoll(), streak.getRolls().size()), offset, lines, bet));
        return streak;
    }

    public static Streak spinDemo(Streak streak, int[] offset, boolean[] lines, BigDecimal bet) {
        if (StreakLogic.isComplete(streak)) {
            streak = StreakLogic.generateStreak();
        }
        streak.getRolls().add(buildRoll(StreakLogic.parseCurrentRollArray(streak.getRoll(), streak.getRolls().size()), offset, lines, bet));
        return streak;
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
        boolean[]  winLines = defineWinLines(reelPos);
        for (int i = 0; i < LINE_NUMBER; i++) {
            if (winLines[i] && lines[i]) {
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
        System.out.println(reelValue);
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

    private static int countPlayedLines(boolean[] lines) {
        int result = 0;
        for (boolean line : lines) {
            if (line) {
                result++;
            }
        }
        return result;
    }

    private static ReelValue getReelValue(int index) {
        int length = reelValues.length;
        while (index < 1) {
            index += length;
        }
        while (index > length) {
            index -= length;
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
}