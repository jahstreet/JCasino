package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.manager.ConfigConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides changing locale for users.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ChangeLocaleCommand implements Command {

    /**
     * Takes {@link ConfigConstant#PARAM_LOCALE} parameter from {@link HttpServletRequest#getParameter(String)},
     * sets it as attribute to {@link HttpSession#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object      locale  = request.getParameter(PARAM_LOCALE);
        if (locale != null) {
            session.setAttribute(ATTR_LOCALE, locale);
        } else {
            session.setAttribute(ATTR_LOCALE, DEFAULT_LOCALE);
        }
        return PageNavigator.FORWARD_PREV_QUERY;
    }
}