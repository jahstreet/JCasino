package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.UserLogic;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides login process for guest.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class LoginCommand implements Command {

    /**
     * <p>Provides login process for guest.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_MAIN}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully sets to {@link HttpSession#setAttribute(String, Object)} corresponding
     * to {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole} attribute and navigates to
     * {@link PageNavigator#REDIRECT_GOTO_PROFILE}, else adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_PROFILE}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see UserLogic#authorizeUser(String, String)
     */
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