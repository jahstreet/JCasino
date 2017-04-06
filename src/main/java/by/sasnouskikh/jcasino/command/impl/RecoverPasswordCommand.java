package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class RecoverPasswordCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String email = request.getParameter(PARAM_EMAIL);

        if (FormValidator.validateEmail(email)) {
            if (PlayerLogic.recoverPassword(email, locale)) {
                navigator = PageNavigator.REDIRECT_GOTO_INDEX;
            } else {
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_RECOVER_PASSWORD_ERROR));
                navigator = PageNavigator.FORWARD_PAGE_RECOVER_PASSWORD;
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_EMAIL));
            navigator = PageNavigator.FORWARD_PAGE_RECOVER_PASSWORD;
        }
        return navigator;
    }
}