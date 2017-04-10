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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        Streak  streak = (Streak) session.getAttribute("current_streak");
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
            }
        } else {
            if (!demo) {
                streak = StreakLogic.generateStreak(player.getId());
            } else {
                streak = StreakLogic.generateStreak();
            }
            session.setAttribute("current_streak", streak);
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

        System.out.println(betString);
        System.out.println(offset1String);
        System.out.println(offset2String);
        System.out.println(offset3String);
        System.out.println(line1);
        System.out.println(line2);
        System.out.println(line3);
        System.out.println(line4);
        System.out.println(line5);
        System.out.println(demo);

        if (valid) {
            BigDecimal bet     = BigDecimal.valueOf(Double.parseDouble(betString));
            int        offset1 = Integer.parseInt(offset1String);
            int        offset2 = Integer.parseInt(offset2String);
            int        offset3 = Integer.parseInt(offset3String);
            int[]      offset  = new int[]{offset1, offset2, offset3};
            boolean[]  lines   = new boolean[]{line1, line2, line3, line4, line5};
            try {
                if (!demo) {
                    streak = GameEngine.spin(streak, offset, lines, bet);
                } else {
                    streak = GameEngine.spinDemo(streak, offset, lines, bet);
                }
                List<Roll> rollList = streak.getRolls();
                Roll       roll     = rollList.get(rollList.size() - 1);
                BigDecimal result   = roll.getResult();
                int[]      realPos  = GameEngine.defineReelPos(roll.getRoll(), roll.getOffset());
                boolean[]  winLines = GameEngine.defineWinLines(realPos);

                boolean[] rollWinLines = new boolean[5];
                for (int i = 0; i < 5; i++) {
                    rollWinLines[i] = lines[i] && winLines[i];
                }

                System.out.println(streak.getRoll());
                System.out.println(Arrays.toString(roll.getRoll()));
                System.out.println(Arrays.toString(realPos));
                System.out.println(Arrays.toString(winLines));
                System.out.println(Arrays.toString(rollWinLines));

                responseMap.put("winResult", result.toPlainString());
                responseMap.put("offsets", realPos);
                responseMap.put("lines", rollWinLines);

            } catch (LogicException e) {
                responseMap.put(ATTR_ERROR_MESSAGE, "Spin error database connection etc.");
            }
        } else {
            responseMap.put(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
        }
        return responseMap;
    }
}