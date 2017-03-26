package by.sasnouskikh.jcasino.command.impl.gotocommand;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.FORWARD;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PAGE_MAIN;

public class GotoMainCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        return new String[]{PAGE_MAIN, FORWARD};
    }
}