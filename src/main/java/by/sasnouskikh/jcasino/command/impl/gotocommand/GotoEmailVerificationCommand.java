package by.sasnouskikh.jcasino.command.impl.gotocommand;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class GotoEmailVerificationCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        HttpSession session   = request.getSession();
        String      locale    = (String) session.getAttribute(ATTR_LOCALE);
        String[]    queryParams;
        Player      player    = (Player) session.getAttribute(ATTR_PLAYER);
        String      emailCode = player.getVerification().getEmailCode();
        if (emailCode == null || emailCode.isEmpty()) {
            if (PlayerLogic.sendEmailCode(player, locale)) {
                queryParams = new String[]{PAGE_EMAIL_VERIFICATION, FORWARD};
            } else {
                queryParams = new String[]{PAGE_VERIFICATION, FORWARD};
            }
        } else {
            queryParams = new String[]{PAGE_EMAIL_VERIFICATION, FORWARD};
        }
        return queryParams;
    }
}