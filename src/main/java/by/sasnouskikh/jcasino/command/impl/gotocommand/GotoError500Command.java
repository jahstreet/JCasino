package by.sasnouskikh.jcasino.command.impl.gotocommand;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.FORWARD;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PAGE_ERROR_500;

public class GotoError500Command implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        return new String[]{PAGE_ERROR_500, FORWARD};
    }
}