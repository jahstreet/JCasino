package by.sasnouskikh.jcasino.command.impl.gotocommand;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class GotoSupportCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = new MessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        Player         player         = (Player) session.getAttribute(ATTR_PLAYER);
        if (player != null) {
            if (PlayerLogic.updateQuestionsInfo(player)) {
                request.setAttribute(ATTR_EMAIL_INPUT, player.getEmail());
                return new String[]{PAGE_SUPPORT, FORWARD};
            } else {
                errorMessage.append(messageManager.getMessage("take.questions.interrupted"));
                request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString());
                return new String[]{PAGE_ERROR_500, FORWARD};
            }

        }
        return new String[]{PAGE_SUPPORT, FORWARD};
    }
}