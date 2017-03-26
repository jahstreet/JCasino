package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class RateSupportAnswerCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = new MessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        String[]       queryParams;
        boolean        valid          = true;

        String id = request.getParameter(PARAM_ID);
        if (!StringUtils.isNumeric(id)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_JSP)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }
        String satisfaction = request.getParameter(PARAM_SATISFACTION);
        if (!FormValidator.validateSatisfaction(satisfaction)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_JSP)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            if (PlayerLogic.rateSupportAnswer(Integer.parseInt(id), satisfaction)) {
                queryParams = new String[]{GOTO_SUPPORT, REDIRECT};
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_RATE_SUPPORT_INTERRUPTED));
                request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
                queryParams = new String[]{PAGE_SUPPORT, FORWARD};
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            queryParams = new String[]{PAGE_SUPPORT, FORWARD};
        }

        return queryParams;
    }
}