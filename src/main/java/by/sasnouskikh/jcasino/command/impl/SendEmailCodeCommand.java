package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides sending e-mail code for e-mail verification for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class SendEmailCodeCommand implements Command {

    /**
     * <p>Provides sending e-mail code for e-mail verification for player.
     * <p>Passes sending e-mail code to the Logic layer. If Logic operation passed successfully navigates to
     * {@link PageNavigator#REDIRECT_GOTO_EMAIL_VERIFICATION}, else adds {@link ConfigConstant#ATTR_ERROR_MESSAGE}
     * attribute to {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_EMAIL_VERIFICATION}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see PlayerLogic#sendEmailCode(Player, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        Player player = (Player) session.getAttribute(ATTR_PLAYER);

        if (PlayerLogic.sendEmailCode(player, locale)) {
            navigator = PageNavigator.REDIRECT_GOTO_EMAIL_VERIFICATION;
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_EMAILCODE_SEND_ERROR));
            navigator = PageNavigator.FORWARD_PAGE_EMAIL_VERIFICATION;
        }
        return navigator;
    }
}