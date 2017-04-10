package by.sasnouskikh.jcasino.command.impl.navigation.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;

public class GotoAdminCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        // TODO updateStreak tasks list???
        return PageNavigator.FORWARD_PAGE_ADMIN;
    }
}