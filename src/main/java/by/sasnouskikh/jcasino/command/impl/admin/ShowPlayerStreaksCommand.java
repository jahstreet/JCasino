package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.StreakService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides showing definite player streaks info for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ShowPlayerStreaksCommand implements Command {

    /**
     * <p>Provides showing definite player streaks info for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully saves current query to session, sets
     * {@link ConfigConstant#ATTR_STREAKS} attribute to {@link HttpServletRequest#setAttribute(String, Object)} and
     * navigates to {@link PageNavigator#FORWARD_PAGE_MANAGE_STREAKS}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PREV_QUERY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see StreakService#takePlayerStreaks(int, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String playerIdString = request.getParameter(PARAM_ID);
        int    playerId;

        if (FormValidator.validateId(playerIdString)) {
            playerId = Integer.parseInt(playerIdString);
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        List<Streak> streaks;
        try (StreakService streakService = new StreakService()) {
            streaks = streakService.takePlayerStreaks(playerId, null);
        }
        if (streaks != null) {
            QueryManager.saveQueryToSession(request);
            request.setAttribute(ATTR_STREAKS, streaks);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_STREAKS;
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_PLAYER_NO_STREAKS));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}