package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.logic.QuestionLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ResetSupportAnswerRatingCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        PageNavigator  navigator;

        String questionIdString = request.getParameter(PARAM_ID);
        int    questionId;

        if (FormValidator.validateId(questionIdString)) {
            questionId = Integer.parseInt(questionIdString);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PAGE_SUPPORT;
        }

        if (QuestionLogic.resetAnswerRating(questionId)) {
            navigator = PageNavigator.REDIRECT_GOTO_SUPPORT;
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_RATE_SUPPORT_INTERRUPTED));
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_SUPPORT;
        }
        return navigator;
    }
}