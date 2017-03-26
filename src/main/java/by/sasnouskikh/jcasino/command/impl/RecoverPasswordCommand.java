package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.logic.UserLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class RecoverPasswordCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = new MessageManager(locale);
        String[]       queryParams;
        String         email          = request.getParameter(PARAM_EMAIL);

        if (FormValidator.validateEmail(email)) {
            if (UserLogic.recoverPassword(email, locale)) {
                queryParams = new String[]{GOTO_INDEX, REDIRECT};
            } else {
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_RECOVER_PASSWORD_ERROR));
                queryParams = new String[]{PAGE_RECOVER_PASSWORD, FORWARD};
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_EMAIL));
            queryParams = new String[]{PAGE_RECOVER_PASSWORD, FORWARD};
        }
        return queryParams;
    }
}