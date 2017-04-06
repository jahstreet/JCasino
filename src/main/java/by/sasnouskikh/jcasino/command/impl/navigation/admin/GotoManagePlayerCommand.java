package by.sasnouskikh.jcasino.command.impl.navigation.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.AdminLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class GotoManagePlayerCommand implements Command {

    @SuppressWarnings("Duplicates")
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        PageNavigator  navigator;

        int playerId;

        String stringId = request.getParameter(PARAM_ID);

        if (FormValidator.validateId(stringId)) {
            playerId = Integer.parseInt(stringId);
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        Player player = AdminLogic.takePlayer(playerId);
        if (player != null) {
            QueryManager.saveQueryToSession(request);
            request.setAttribute(ATTR_PLAYER, player);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_PLAYER;
        } else {
            QueryManager.logQuery(request);
            errorMessage.append(messageManager.getMessage(MESSAGE_PLAYER_NOT_EXIST));
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}