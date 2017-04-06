package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.logic.QuestionLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class AnswerSupportCommand implements Command {

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

        String   questionIdString = request.getParameter(PARAM_ID);
        String   answer           = request.getParameter(PARAM_ANSWER);
        int      questionId;
        Question question;

        if (FormValidator.validateId(questionIdString)) {
            questionId = Integer.parseInt(questionIdString);
            question = QuestionLogic.takeQuestion(questionId);
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        if (FormValidator.validateSupport(answer)) {
            request.setAttribute(ATTR_ANSWER_INPUT, answer);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_SUPPORT));
            valid = false;
        }

        if (valid && question != null) {
            if (QuestionLogic.answerSupport(question, answer, admin, locale)) {
                navigator = PageNavigator.REDIRECT_GOTO_MANAGE_SUPPORT;
            } else {
                request.setAttribute(ATTR_QUESTION, question);
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_ANSWER_SUPPORT_ERROR));
                navigator = PageNavigator.FORWARD_PAGE_ANSWER_SUPPORT;
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}