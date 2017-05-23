package by.sasnouskikh.jcasino.command.ajax.command;

import by.sasnouskikh.jcasino.command.ajax.AjaxCommand;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.service.StreakService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides finish streak operation of slot-machine.
 * Is suitable to use with {@link by.sasnouskikh.jcasino.controller.AjaxController}.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AjaxCommand
 * @see by.sasnouskikh.jcasino.command.ajax.AjaxCommandFactory
 */
public class FinishStreakCommand implements AjaxCommand {
    /**
     * Executes finish streak operation, puts processed data and messages into {@link HashMap} responseMap and returns
     * it.
     *
     * @param request request from client to get parameters to work with
     * @return {@link HashMap} with response parameters
     */
    @Override
    public Map<String, Object> execute(HttpServletRequest request) {
        HttpSession         session        = request.getSession();
        String              locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager      messageManager = MessageManager.getMessageManager(locale);
        Map<String, Object> responseMap    = new HashMap<>();

        Streak  currentStreak = (Streak) session.getAttribute(ATTR_CURRENT_STREAK);
        boolean demo          = session.getAttribute(ATTR_DEMO_PLAY) != null;
        boolean success       = true;

        //try to finish current streak
        if (currentStreak != null) {
            if (!demo) {
                try (StreakService streakService = new StreakService()) {
                    StreakService.completeStreak(currentStreak);
                    success = streakService.updateStreak(currentStreak);
                }
            } else {
                StreakService.completeStreak(currentStreak);
            }
        } else {
            responseMap.put(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_NULL_STREAK_COMPLETE_ERROR));
            return responseMap;
        }

        if (success) {
            responseMap.put(AJAX_STREAK_INFO, currentStreak.getRoll());
        } else {
            responseMap.put(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
        }
        return responseMap;
    }
}