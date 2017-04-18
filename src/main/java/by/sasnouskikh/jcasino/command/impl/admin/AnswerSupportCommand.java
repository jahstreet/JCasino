package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.logic.QuestionLogic;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides answering support questions operation for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class AnswerSupportCommand implements Command {

    /**
     * <p>Provides answering support questions operation for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_MANAGE_SUPPORT}, else
     * adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_ANSWER_SUPPORT}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see QuestionLogic#takeQuestion(int)
     * @see QuestionLogic#answerSupport(Question, String, Admin, String)
     */
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
        Question question         = null;
        int      questionId;

        if (FormValidator.validateId(questionIdString)) {
            questionId = Integer.parseInt(questionIdString);
            question = QuestionLogic.takeQuestion(questionId);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_JSP)).append(MESSAGE_SEPARATOR);
            valid = false;
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