package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.PlayerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides navigating to withdraw money page for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class GotoWithdrawMoneyCommand implements Command {

    /**
     * Saves current query to session and navigates to withdraw money page for player.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        Player player = (Player) session.getAttribute(ATTR_PLAYER);

        try (PlayerService playerService = new PlayerService()) {
            if (playerService.updateAccountInfo(player)) {
                QueryManager.saveQueryToSession(request);
                navigator = PageNavigator.FORWARD_PAGE_WITHDRAW_MONEY;
            } else {
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
                navigator = PageNavigator.FORWARD_PREV_QUERY;
            }
        }
        return navigator;
    }
}