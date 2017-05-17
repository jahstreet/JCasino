package by.sasnouskikh.jcasino.command.ajax.command;

import by.sasnouskikh.jcasino.command.ajax.AjaxCommand;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.game.GameEngine;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.ServiceException;
import by.sasnouskikh.jcasino.service.StreakService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides spin operation of slot-machine.
 * Is suitable to use with {@link by.sasnouskikh.jcasino.controller.AjaxController}.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AjaxCommand
 * @see by.sasnouskikh.jcasino.command.ajax.AjaxCommandFactory
 */
public class SpinCommand implements AjaxCommand {

    /**
     * <p>Provides spin operation of slot-machine.
     * <p>Defines if game mode is 'Demo' ({@link HttpSession#getAttribute(String)} has attribute
     * {@link ConfigConstant#ATTR_PLAYER} or {@link HttpServletRequest#getParameter(String)} has parameter
     * {@link ConfigConstant#PARAM_DEMO}).
     * <p>Takes {@link ConfigConstant#ATTR_CURRENT_STREAK} from {@link HttpSession#getAttribute(String)} and checks if
     * {@link Streak} is valid for continue using. If no - generates new one and adds it to
     * {@link HttpSession#setAttribute(String, Object)}.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If all the parameters are valid converts them to relevant data types. (If play mode is 'Non-demo' checks if
     * {@link PlayerAccount#getBalance()} {@literal >}= totalBet counted using input data.)
     * <p>If everything is ok passes converted parameters further to the Logic layer. Then builds {@link HashMap}
     * responseMap corresponding to modified by Logic layer {@link Streak} object data.
     * <p>If there was any error while validating operations returning {@link HashMap} would contain key
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} with {@link String} value.
     *
     * @param request request from client to get parameters to work with
     * @return {@link HashMap} with response parameters
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see StreakService#generateStreak(int)
     * @see StreakService#generateStreak()
     * @see GameEngine#spin(Streak, int[], boolean[], BigDecimal)
     * @see GameEngine#spinDemo(Streak, int[], boolean[], BigDecimal)
     */
    @Override
    public Map<String, Object> execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession         session        = request.getSession();
        String              locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager      messageManager = MessageManager.getMessageManager(locale);
        StringBuilder       errorMessage   = new StringBuilder();
        boolean             valid          = true;
        Map<String, Object> responseMap    = new HashMap<>();

        Player  player = (Player) session.getAttribute(ATTR_PLAYER);
        Streak  streak = (Streak) session.getAttribute(ATTR_CURRENT_STREAK);
        boolean demo   = session.getAttribute(ATTR_DEMO_PLAY) != null;

        //validate/generate streak
        if (!demo) {
            if (streak == null || StreakService.isComplete(streak)) {
                try (StreakService streakService = new StreakService()) {
                    if (streakService.updateStreak(streak)) {
                        streak = streakService.generateStreak(player.getId());
                        session.setAttribute(ATTR_CURRENT_STREAK, streak);
                    } else {
                        errorMessage.append(messageManager.getMessage(MESSAGE_STREAK_GENERATION_ERROR))
                                    .append(MESSAGE_SEPARATOR);
                        valid = false;
                    }
                }
            }
        } else {
            if (streak == null || StreakService.isComplete(streak)) {
                streak = StreakService.generateStreak();
                session.setAttribute(ATTR_CURRENT_STREAK, streak);
            }
        }

        String betString     = request.getParameter(PARAM_BET);
        String offset1String = request.getParameter(PARAM_OFFSET1);
        String offset2String = request.getParameter(PARAM_OFFSET2);
        String offset3String = request.getParameter(PARAM_OFFSET3);

        boolean line1 = request.getParameter(PARAM_LINE1) != null;
        boolean line2 = request.getParameter(PARAM_LINE2) != null;
        boolean line3 = request.getParameter(PARAM_LINE3) != null;
        boolean line4 = request.getParameter(PARAM_LINE4) != null;
        boolean line5 = request.getParameter(PARAM_LINE5) != null;

        if (!FormValidator.validateFloatAmount(betString)
            || !FormValidator.validateFloatAmount(offset1String)
            || !FormValidator.validateFloatAmount(offset2String)
            || !FormValidator.validateFloatAmount(offset3String)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_JSP));
            valid = false;
        }

        if (valid) {

            //convert data to corresponding data types
            BigDecimal bet     = BigDecimal.valueOf(Double.parseDouble(betString));
            int        offset1 = Integer.parseInt(offset1String);
            int        offset2 = Integer.parseInt(offset2String);
            int        offset3 = Integer.parseInt(offset3String);
            int[]      offset  = new int[]{offset1, offset2, offset3};
            boolean[]  lines   = new boolean[]{line1, line2, line3, line4, line5};

            //check balance
            BigDecimal totalBet = GameEngine.countTotalBet(bet, lines);
            if (!demo && player != null && player.getAccount().getBalance().compareTo(totalBet) < 0) {
                errorMessage.append(messageManager.getMessage(MESSAGE_NO_MONEY));
                responseMap.put(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
                return responseMap;
            }

            try {
                //pass to Logic layer
                if (!demo) {
                    BigDecimal total = GameEngine.spin(streak, offset, lines, bet);
                } else {
                    GameEngine.spinDemo(streak, offset, lines, bet);
                }

                //build response parameters map
                ArrayDeque<Roll> rollArrayDeque = streak.getRolls();
                Roll             lastRoll       = rollArrayDeque.getLast();
                BigDecimal       result         = lastRoll.getResult();
                int[]            reelPos        = GameEngine.defineReelPos(lastRoll.getRoll(), offset);
                boolean[]        winLines       = GameEngine.defineWinLines(reelPos, lines);
                int              streakNumber   = rollArrayDeque.size();
                String           streakInfo;
                if (streakNumber < BETS_IN_STREAK) {
                    streakInfo = streak.getRollMD5();
                } else {
                    streakInfo = streak.getRoll();
                }
                responseMap.put(AJAX_STREAK_INFO, streakInfo);
                responseMap.put(AJAX_ROLL_NUMBER, streakNumber);
                responseMap.put(AJAX_WIN_RESULT, result.toPlainString());
                responseMap.put(AJAX_OFFSETS, reelPos);
                responseMap.put(AJAX_LINES, winLines);

            } catch (ServiceException e) {
                responseMap.put(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
            }
        } else {
            responseMap.put(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
        }
        return responseMap;
    }
}