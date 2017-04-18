package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides navigating to account page for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class GotoAccountCommand implements Command {

    /**
     * <p>Provides navigating to account page for player.
     * <p>Updates {@link by.sasnouskikh.jcasino.entity.bean.PlayerAccount} field's data of
     * {@link ConfigConstant#ATTR_PLAYER} attribute of {@link HttpSession#getAttribute(String)} at Logic layer.
     * <p>If Logic operation passed successfully saves current query to session and navigates to
     * {@link PageNavigator#FORWARD_PAGE_ACCOUNT}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PREV_QUERY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see PlayerLogic#updateAccountInfo(Player)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        Player player = (Player) session.getAttribute(ATTR_PLAYER);

        if (PlayerLogic.updateAccountInfo(player)) {
            QueryManager.saveQueryToSession(request);
            navigator = PageNavigator.FORWARD_PAGE_ACCOUNT;
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}