package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.logic.AdminLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ChangeAccountStatusCommand implements Command {
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        PageNavigator  navigator;

        boolean valid = true;
        Admin   admin = (Admin) session.getAttribute(ATTR_ADMIN);

        String playerIdString = request.getParameter(PARAM_ID);
        String status         = request.getParameter(PARAM_STATUS);
        String commentary     = request.getParameter(PARAM_COMMENTARY);
        int    playerId;

        if (FormValidator.validateId(playerIdString)) {
            playerId = Integer.parseInt(playerIdString);
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        if (commentary != null && commentary.trim().isEmpty()) {
            commentary = null;
        }

        if (commentary == null || FormValidator.validateSupport(commentary)) {
            request.setAttribute(ATTR_COMMENTARY_INPUT, commentary);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_SUPPORT));
            valid = false;
        }

        if (valid && AdminLogic.changeAccountStatus(playerId, admin, status, commentary)) {
            navigator = PageNavigator.REDIRECT_PREV_QUERY;
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}