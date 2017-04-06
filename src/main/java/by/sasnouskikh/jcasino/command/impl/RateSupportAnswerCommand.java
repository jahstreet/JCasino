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

public class RateSupportAnswerCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String questionIdString = request.getParameter(PARAM_ID);
        String satisfaction     = request.getParameter(PARAM_SATISFACTION);
        int    questionId;

        if (!FormValidator.validateId(questionIdString)) {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }
        questionId = Integer.parseInt(questionIdString);

        if (!FormValidator.validateSatisfaction(satisfaction)) {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        if (QuestionLogic.rateAnswer(questionId, satisfaction)) {
            navigator = PageNavigator.REDIRECT_GOTO_SUPPORT;
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_RATE_SUPPORT_INTERRUPTED));
            navigator = PageNavigator.FORWARD_PAGE_SUPPORT;
        }

        return navigator;
    }
}