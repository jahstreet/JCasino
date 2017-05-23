package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.service.VerificationService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides canceling passport scan verification of player for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class CancelScanVerificationCommand implements Command {

    /**
     * <p>Provides canceling passport scan verification of player for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_PREV_QUERY}, else
     * adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see MessageManager
     * @see FormValidator
     * @see VerificationService#cancelScanVerification(int, Admin, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        PageNavigator  navigator;

        boolean valid = true;
        Admin   admin = (Admin) session.getAttribute(ATTR_ADMIN);

        String playerIdString = request.getParameter(PARAM_ID);
        String commentary     = request.getParameter(PARAM_COMMENTARY);
        int    playerId       = 0;

        if (FormValidator.validateId(playerIdString)) {
            playerId = Integer.parseInt(playerIdString);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_JSP)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (FormValidator.validateSupport(commentary)) {
            request.setAttribute(ATTR_COMMENTARY_INPUT, commentary);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_SUPPORT));
            valid = false;
        }

        if (valid) {
            try (VerificationService verificationService = new VerificationService()) {
                if (verificationService.cancelScanVerification(playerId, admin, commentary)) {
                    navigator = PageNavigator.REDIRECT_PREV_QUERY;
                } else {
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
                    navigator = PageNavigator.FORWARD_PREV_QUERY;
                }
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}