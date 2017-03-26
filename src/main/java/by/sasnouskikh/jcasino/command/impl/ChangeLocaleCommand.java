package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ChangeLocaleCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession session = request.getSession();
        Object      locale  = request.getParameter(PARAM_LOCALE);
        if (locale != null) {
            session.setAttribute(ATTR_LOCALE, locale);
        } else {
            session.setAttribute(ATTR_LOCALE, DEFAULT_LOCALE);
        }
        String prevQuery = QueryManager.takePreviousQuery(request);
        return new String[]{prevQuery, FORWARD};
    }
}