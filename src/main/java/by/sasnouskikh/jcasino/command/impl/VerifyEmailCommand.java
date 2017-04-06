package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
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
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        Player             player       = (Player) session.getAttribute(ATTR_PLAYER);
        PlayerVerification verification = player.getVerification();
        String             codeSent     = verification.getEmailCode();

        String codeInput = request.getParameter(PARAM_EMAIL_CODE);

        if (PlayerLogic.verifyEmail(player, codeInput, codeSent)) {
            navigator = PageNavigator.REDIRECT_GOTO_VERIFICATION;
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_VERIFY_EMAIL_ERROR));
            navigator = PageNavigator.FORWARD_PAGE_EMAIL_VERIFICATION;
        }
        return navigator;
    }
}