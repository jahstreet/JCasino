package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class GotoEmailVerificationCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        Player player    = (Player) session.getAttribute(ATTR_PLAYER);
        String emailCode = player.getVerification().getEmailCode();

        if (emailCode != null && !emailCode.isEmpty()) {
            QueryManager.saveQueryToSession(request);
            navigator = PageNavigator.FORWARD_PAGE_EMAIL_VERIFICATION;
        } else {
            if (PlayerLogic.sendEmailCode(player, locale)) {
                QueryManager.saveQueryToSession(request);
                navigator = PageNavigator.FORWARD_PAGE_EMAIL_VERIFICATION;
            } else {
                QueryManager.logQuery(request);
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_CODE_NOT_SENT_ERROR));
                navigator = PageNavigator.FORWARD_PAGE_VERIFICATION;
            }
        }
        return navigator;
    }
}