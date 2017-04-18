package by.sasnouskikh.jcasino.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Common interface for Commands suitable to use with {@link by.sasnouskikh.jcasino.controller.MainController}.
 *
 * @author Sasnouskikh Aliaksandr
 */
public interface Command {

    /**
     * Executes definite operation with data parsed from request and returns {@link PageNavigator} data.
     *
     * @param request - request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     */
    PageNavigator execute(HttpServletRequest request);
}