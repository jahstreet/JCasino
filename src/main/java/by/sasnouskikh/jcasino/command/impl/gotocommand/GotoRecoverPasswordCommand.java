package by.sasnouskikh.jcasino.command.impl.gotocommand;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.FORWARD;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PAGE_RECOVER_PASSWORD;

public class GotoRecoverPasswordCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        return new String[]{PAGE_RECOVER_PASSWORD, FORWARD};
    }
}