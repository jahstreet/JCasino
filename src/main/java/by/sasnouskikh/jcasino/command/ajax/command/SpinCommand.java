package by.sasnouskikh.jcasino.command.ajax.command;

import by.sasnouskikh.jcasino.command.ajax.AjaxCommand;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Roll;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.game.GameEngine;
import by.sasnouskikh.jcasino.logic.LogicException;
import by.sasnouskikh.jcasino.logic.StreakLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class SpinCommand implements AjaxCommand {

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
        boolean demo   = player == null || request.getParameter("demo") != null;

        if (streak != null) {
            if (StreakLogic.isComplete(streak)) {
                if (!demo) {
                    if (StreakLogic.updateStreak(streak)) {
                        streak = StreakLogic.generateStreak(player.getId());
                    } else {
                        errorMessage.append("Streak generating error.").append(MESSAGE_SEPARATOR);
                        valid = false;
                    }
                } else {
                    streak = StreakLogic.generateStreak();
                }
                session.setAttribute(ATTR_CURRENT_STREAK, streak);
            }
        } else {
            if (!demo) {
                streak = StreakLogic.generateStreak(player.getId());
            } else {
                streak = StreakLogic.generateStreak();
            }
            session.setAttribute(ATTR_CURRENT_STREAK, streak);
        }
        if (streak == null) {
            errorMessage.append("Streak generation error.").append(MESSAGE_SEPARATOR);
            valid = false;
        }

        String betString     = request.getParameter("bet");
        String offset1String = request.getParameter("offset1");
        String offset2String = request.getParameter("offset2");
        String offset3String = request.getParameter("offset3");

        boolean line1 = request.getParameter("line1") != null;
        boolean line2 = request.getParameter("line2") != null;
        boolean line3 = request.getParameter("line3") != null;
        boolean line4 = request.getParameter("line4") != null;
        boolean line5 = request.getParameter("line5") != null;

        if (!FormValidator.isFloat(betString)
            || !FormValidator.isFloat(offset1String)
            || !FormValidator.isFloat(offset2String)
            || !FormValidator.isFloat(offset3String)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_JSP));
            valid = false;
        }

        if (valid) {
            BigDecimal bet     = BigDecimal.valueOf(Double.parseDouble(betString));
            int        offset1 = Integer.parseInt(offset1String);
            int        offset2 = Integer.parseInt(offset2String);
            int        offset3 = Integer.parseInt(offset3String);
            int[]      offset  = new int[]{offset1, offset2, offset3};
            boolean[]  lines   = new boolean[]{line1, line2, line3, line4, line5};

            BigDecimal totalBet = GameEngine.countTotalBet(bet, lines);
            if (!demo && player.getAccount().getBalance().compareTo(totalBet) < 0) {
                errorMessage.append("Not enough money error.");
                responseMap.put(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
                return responseMap;
            }

            try {
                if (!demo) {
                    BigDecimal total   = GameEngine.spin(streak, offset, lines, bet);
                    BigDecimal balance = player.getAccount().getBalance();
                    player.getAccount().setBalance(balance.add(total));
                } else {
                    GameEngine.spinDemo(streak, offset, lines, bet);
                }

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
                responseMap.put("streakInfo", streakInfo);
                responseMap.put("rollNumber", streakNumber);
                responseMap.put("winResult", result.toPlainString());
                responseMap.put("offsets", reelPos);
                responseMap.put("lines", winLines);

            } catch (LogicException e) {
                responseMap.put(ATTR_ERROR_MESSAGE, "Spin error database connection etc.");
            }
        } else {
            responseMap.put(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
        }
        return responseMap;
    }
}