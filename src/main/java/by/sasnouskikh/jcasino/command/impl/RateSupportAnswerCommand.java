package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.service.QuestionService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides rating support question for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class RateSupportAnswerCommand implements Command {

    /**
     * <p>Provides rating support question for player.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_SUPPORT}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_SUPPORT}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_SUPPORT}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see MessageManager
     * @see FormValidator
     * @see QuestionService#rateAnswer(int, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        boolean valid = true;

        String questionIdString   = request.getParameter(PARAM_ID);
        String sortBySatisfaction = request.getParameter(PARAM_SORT_BY_SATISFACTION);
        int    questionId         = 0;

        if (FormValidator.validateId(questionIdString)) {
            questionId = Integer.parseInt(questionIdString);
        } else {
            valid = false;
        }

        if (!FormValidator.validateSatisfaction(sortBySatisfaction)) {
            valid = false;
        }

        if (valid) {
            try (QuestionService questionService = new QuestionService()) {
                if (questionService.rateAnswer(questionId, sortBySatisfaction)) {
                    navigator = PageNavigator.REDIRECT_GOTO_SUPPORT;
                } else {
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_RATE_SUPPORT_INTERRUPTED));
                    navigator = PageNavigator.FORWARD_PAGE_SUPPORT;
                }
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            navigator = PageNavigator.FORWARD_PAGE_SUPPORT;
        }
        return navigator;
    }
}