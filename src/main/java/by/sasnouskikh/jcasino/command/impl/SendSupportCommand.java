package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.QuestionLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class SendSupportCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
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
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL_MISMATCH)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (!FormValidator.validateTopic(topic)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_TOPIC)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (FormValidator.validateSupport(question)) {
            request.setAttribute(ATTR_QUESTION_INPUT, question);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_SUPPORT)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            if (QuestionLogic.sendSupport(player, email, topic, question)) {
                navigator = PageNavigator.REDIRECT_GOTO_MAIN;
            } else {
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_SEND_SUPPORT_INTERRUPTED));
                navigator = PageNavigator.FORWARD_PAGE_SUPPORT;
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_SUPPORT;
        }
        return navigator;
    }
}