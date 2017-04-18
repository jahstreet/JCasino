package by.sasnouskikh.jcasino.command.impl.navigation.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;

/**
 * The class provides navigating to manage news page for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class GotoManageNewsCommand implements Command {

    /**
     * Saves current query to session and navigates to manage news page.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        return PageNavigator.FORWARD_PAGE_MANAGE_NEWS;
    }
}