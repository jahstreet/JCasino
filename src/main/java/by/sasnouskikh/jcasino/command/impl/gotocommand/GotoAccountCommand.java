package by.sasnouskikh.jcasino.command.impl.gotocommand;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class GotoAccountCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = new MessageManager(locale);
        String[]       queryParams;
        Player         player         = (Player) session.getAttribute(ATTR_PLAYER);
        if (PlayerLogic.updateAccountInfo(player)) {
            queryParams = new String[]{PAGE_ACCOUNT, FORWARD};
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ERROR));
            queryParams = new String[]{PAGE_ERROR_500, FORWARD};
        }
        return queryParams;
    }
}