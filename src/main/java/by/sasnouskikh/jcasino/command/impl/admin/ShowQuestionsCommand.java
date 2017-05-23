package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.QuestionService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides showing unanswered support questions info for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ShowQuestionsCommand implements Command {

    /**
     * <p>Provides showing unanswered support questions info for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer, saves current query to session, sets {@link ConfigConstant#ATTR_QUESTION_LIST} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_MANAGE_SUPPORT}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see QuestionService#takeUnanswered(String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        boolean valid = true;

        String topic = request.getParameter(PARAM_TOPIC);

        if (!FormValidator.validateTopic(topic)) {
            valid = false;
        }

        if (valid) {
            QueryManager.saveQueryToSession(request);
            List<Question> questionList;
            try (QuestionService questionService = new QuestionService()) {
                questionList = questionService.takeUnanswered(topic);
            }
            request.setAttribute(ATTR_QUESTION_LIST, questionList);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_SUPPORT;
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }

        return navigator;
    }
}