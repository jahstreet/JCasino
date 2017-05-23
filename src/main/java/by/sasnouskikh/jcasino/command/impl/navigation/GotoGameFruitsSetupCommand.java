package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.controller.MainController;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;

/**
 * The class provides navigating to game setup page.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class GotoGameFruitsSetupCommand implements Command {

    /**
     * <p>Provides navigating to game setup page.
     * <p>Navigates to {@link PageNavigator#FORWARD_PAGE_GAME_FRUITS_SETUP}.
     *
     * @param request - request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * MainController})
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        return PageNavigator.FORWARD_PAGE_GAME_FRUITS_SETUP;
    }
}