package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides navigating back from error page.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class BackFromErrorCommand implements Command {

    /**
     * Sets {@link by.sasnouskikh.jcasino.manager.ConfigConstant#ATTR_ERROR_MESSAGE} attribute of
     * {@link javax.servlet.http.HttpSession#setAttribute(String, Object)} to null and navigates to
     * {@link PageNavigator#REDIRECT_PREV_QUERY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(ATTR_ERROR_MESSAGE);
        if (session.getAttribute(ATTR_DEMO_PLAY) != null) {
            session.removeAttribute(ATTR_DEMO_PLAY);
            session.removeAttribute(ATTR_CURRENT_STREAK);
        }
        return PageNavigator.REDIRECT_PREV_QUERY;
    }
}