package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
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
 * The class provides showing answered support questions for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ShowAnswersCommand implements Command {

    /**
     * <p>Provides showing answered support questions for admin.
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
     * @see QuestionService#takeAnswered(String, String, boolean, Admin, boolean)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        boolean valid = true;
        Admin   admin = (Admin) session.getAttribute(ATTR_ADMIN);

        String  month              = request.getParameter(PARAM_MONTH);
        String  topic              = request.getParameter(PARAM_TOPIC);
        boolean filterMy           = request.getParameter(PARAM_FILTER_MY) != null;
        boolean sortBySatisfaction = request.getParameter(PARAM_SORT_BY_SATISFACTION) != null;

        if (FormValidator.validateDateMonth(month)) {
            request.setAttribute(ATTR_MONTH_INPUT, month);
        } else {
            valid = false;
        }

        if (!FormValidator.validateTopic(topic)) {
            valid = false;
        }

        if (valid) {
            QueryManager.saveQueryToSession(request);
            List<Question> questionList;
            try (QuestionService questionService = new QuestionService()) {
                questionList = questionService.takeAnswered(topic, month, filterMy, admin, sortBySatisfaction);
            }
            request.setAttribute(ATTR_QUESTION_LIST, questionList);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_SUPPORT;

        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}