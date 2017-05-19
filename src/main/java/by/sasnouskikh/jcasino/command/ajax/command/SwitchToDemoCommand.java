package by.sasnouskikh.jcasino.command.ajax.command;

import by.sasnouskikh.jcasino.command.ajax.AjaxCommand;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.StreakService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides switch to Demo-play mode operation of slot-machine.
 * Is suitable to use with {@link by.sasnouskikh.jcasino.controller.AjaxController}.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AjaxCommand
 * @see by.sasnouskikh.jcasino.command.ajax.AjaxCommandFactory
 */
public class SwitchToDemoCommand implements AjaxCommand {
    /**
     * Executes switch to Demo-play mode operation, puts processed data and messages into {@link HashMap} responseMap
     * and returns it.
     *
     * @param request request from client to get parameters to work with
     * @return {@link HashMap} with response parameters
     */
    @Override
    public Map<String, Object> execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession         session        = request.getSession();
        String              locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager      messageManager = MessageManager.getMessageManager(locale);
        Map<String, Object> responseMap    = new HashMap<>();

        Streak  currentStreak = (Streak) session.getAttribute(ATTR_CURRENT_STREAK);
        boolean success;

        //try to finish current streak
        if (currentStreak != null) {
            try (StreakService streakService = new StreakService()) {
                StreakService.completeStreak(currentStreak);
                success = streakService.updateStreak(currentStreak);
            }
        } else {
            responseMap.put(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_NULL_STREAK_COMPLETE_ERROR));
            return responseMap;
        }

        if (success) {
            Streak streak = StreakService.generateStreak();
            responseMap.put(AJAX_MONEY, DEMO_START_MONEY.toString());
            responseMap.put(AJAX_STREAK_INFO, streak.getRollMD5());
            session.setAttribute(ATTR_CURRENT_STREAK, streak);
            session.setAttribute(ATTR_DEMO_PLAY, true);
        } else {
            responseMap.put(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
        }
        return responseMap;
    }
}