package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.service.PlayerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides verifying e-mail for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class VerifyEmailCommand implements Command {

    /**
     * <p>Provides verifying e-mail for player.
     * <p>Takes {@link ConfigConstant#PARAM_EMAIL_CODE} parameter from {@link HttpServletRequest#getParameter(String)}
     * and passes it further to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_VERIFICATION}, else
     * adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_EMAIL_VERIFICATION}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see MessageManager
     * @see PlayerService#verifyEmail(Player, String, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        Player             player       = (Player) session.getAttribute(ATTR_PLAYER);
        PlayerVerification verification = player.getVerification();
        String             codeSent     = verification.getEmailCode();

        String codeInput = request.getParameter(PARAM_EMAIL_CODE);

        try (PlayerService playerService = new PlayerService()) {
            if (playerService.verifyEmail(player, codeInput, codeSent)) {
                navigator = PageNavigator.REDIRECT_GOTO_VERIFICATION;
            } else {
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_VERIFY_EMAIL_ERROR));
                navigator = PageNavigator.FORWARD_PAGE_EMAIL_VERIFICATION;
            }
        }
        return navigator;
    }
}