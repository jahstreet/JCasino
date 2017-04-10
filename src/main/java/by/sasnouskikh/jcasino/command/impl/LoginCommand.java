package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.UserLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class LoginCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        PageNavigator  navigator;

        boolean valid    = true;
        String  email    = request.getParameter(PARAM_EMAIL);
        String  password = request.getParameter(PARAM_PASSWORD);

        if (!FormValidator.validateEmail(email)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL)).append(MESSAGE_SEPARATOR);
            valid = false;
        }
        if (!FormValidator.validatePassword(password)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            JCasinoUser user = UserLogic.authorizeUser(email, password);
            if (user != null) {
                if (user.getClass() == Player.class) {
                    Player player = (Player) user;
                    session.setAttribute(ATTR_PLAYER, player);
                } else {
                    Admin admin = (Admin) user;
                    session.setAttribute(ATTR_ADMIN, admin);
                }
                session.setAttribute(ATTR_USER, user);
                session.setAttribute(ATTR_ROLE, user.getRole());
                navigator = PageNavigator.REDIRECT_GOTO_MAIN;
            } else {
                request.setAttribute(ATTR_EMAIL_INPUT, email);
                errorMessage.append(messageManager.getMessage(MESSAGE_LOGIN_MISMATCH)).append(MESSAGE_SEPARATOR);
                request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
                navigator = PageNavigator.FORWARD_PAGE_MAIN;
            }
        } else {
            request.setAttribute(ATTR_EMAIL_INPUT, email);
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_MAIN;
        }
        return navigator;
    }
}