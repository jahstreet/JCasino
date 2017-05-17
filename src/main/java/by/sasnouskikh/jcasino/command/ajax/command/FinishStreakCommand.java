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

public class FinishStreakCommand implements AjaxCommand {
    /**
     * Executes definite operation with data parsed from request, puts processed data and messages into
     * {@link HashMap} responseMap and returns it.
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
        boolean demo          = session.getAttribute(ATTR_DEMO_PLAY) != null;
        boolean success       = true;

        //try to finish current streak
        if (!demo) {
            try (StreakService streakService = new StreakService()) {
                StreakService.completeStreak(currentStreak);
                success = streakService.updateStreak(currentStreak);
            }
        } else {
            StreakService.completeStreak(currentStreak);
        }

        if (success) {
            responseMap.put(AJAX_STREAK_INFO, currentStreak.getRoll());
        } else {
            responseMap.put(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
        }
        return responseMap;
    }
}