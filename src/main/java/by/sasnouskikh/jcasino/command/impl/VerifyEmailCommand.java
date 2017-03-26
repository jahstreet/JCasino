package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class VerifyEmailCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession        session        = request.getSession();
        String             locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager     messageManager = new MessageManager(locale);
        String[]           queryParams;
        Player             player         = (Player) session.getAttribute(ATTR_PLAYER);
        String             codeInput      = request.getParameter(PARAM_EMAIL_CODE);
        PlayerVerification verification   = player.getVerification();
        String             codeSent       = verification.getEmailCode();
        if (PlayerLogic.verifyEmail(player, codeInput, codeSent)) {
            queryParams = new String[]{GOTO_VERIFICATION, REDIRECT};
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_VERIFY_EMAIL_ERROR));
            queryParams = new String[]{PAGE_EMAIL_VERIFICATION, FORWARD};
        }
        return queryParams;
    }
}