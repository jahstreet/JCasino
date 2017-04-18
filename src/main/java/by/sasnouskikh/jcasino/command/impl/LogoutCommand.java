package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The class provides logout process for user.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class LogoutCommand implements Command {

    /**
     * Invalidates {@link HttpSession#invalidate()} and navigates to {@link PageNavigator#REDIRECT_GOTO_INDEX}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        request.getSession().invalidate();
        return PageNavigator.REDIRECT_GOTO_INDEX;
    }
}