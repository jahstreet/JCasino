package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.GOTO_INDEX;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.REDIRECT;

public class LogoutCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        request.getSession().invalidate();
        return new String[]{GOTO_INDEX, REDIRECT};
    }
}