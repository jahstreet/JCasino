package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.service.QuestionService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides sending support question for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class SendSupportCommand implements Command {

    /**
     * <p>Provides sending support question for player.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_SUPPORT}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_MAIN}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_SUPPORT}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see MessageManager
     * @see FormValidator
     * @see QuestionService#sendSupport(JCasinoUser, String, String, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        PageNavigator  navigator;

        boolean valid  = true;
        Player  player = (Player) session.getAttribute(ATTR_PLAYER);

        String email    = request.getParameter(PARAM_EMAIL);
        String topic    = request.getParameter(PARAM_TOPIC);
        String question = request.getParameter(PARAM_QUESTION);

        if (FormValidator.validateEmail(email)) {
            if (player == null || player.getEmail().equalsIgnoreCase(email)) {
                request.setAttribute(ATTR_EMAIL_INPUT, email);
            } else {
                request.setAttribute(ATTR_EMAIL_INPUT, player.getEmail());
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL_MISMATCH)).append(MESSAGE_SEPARATOR);
                valid = false;
            }
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (!FormValidator.validateTopic(topic)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_TOPIC)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (FormValidator.validateSupport(question)) {
            request.setAttribute(ATTR_QUESTION_INPUT, question);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_SUPPORT)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            try (QuestionService questionService = new QuestionService()) {
                if (questionService.sendSupport(player, email, topic, question)) {
                    navigator = PageNavigator.REDIRECT_GOTO_MAIN;
                } else {
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_SEND_SUPPORT_INTERRUPTED));
                    navigator = PageNavigator.FORWARD_PAGE_SUPPORT;
                }
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_SUPPORT;
        }
        return navigator;
    }
}