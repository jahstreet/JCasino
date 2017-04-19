package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides recovering password.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class RecoverPasswordCommand implements Command {

    /**
     * <p>Provides recovering password.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_RECOVER_PASSWORD}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_INDEX}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_RECOVER_PASSWORD}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see PlayerLogic#recoverPassword(String, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String email = request.getParameter(PARAM_EMAIL);

        if (FormValidator.validateEmail(email)) {
            request.setAttribute(ATTR_EMAIL_INPUT, email);
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