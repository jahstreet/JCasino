package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_ERROR_MESSAGE;

public class BackFromErrorCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        request.getSession().setAttribute(ATTR_ERROR_MESSAGE, null);
        return PageNavigator.REDIRECT_PREV_QUERY;
    }
}