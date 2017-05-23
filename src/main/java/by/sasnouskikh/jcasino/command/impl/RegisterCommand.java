package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.service.PlayerService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;
import static by.sasnouskikh.jcasino.validator.FormValidator.*;

/**
 * The class provides registering player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class RegisterCommand implements Command {

    /**
     * <p>Provides registering player.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_REGISTER}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_INDEX}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_REGISTER}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see MessageManager
     * @see FormValidator
     * @see PlayerService#registerPlayer(String, String, String, String, String, String, String, String, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String errorMessage = validateRequestParams(request);

        if (errorMessage.isEmpty()) {
            try (PlayerService playerService = new PlayerService()) {
                String email     = request.getParameter(PARAM_EMAIL);
                String password  = request.getParameter(PARAM_PASSWORD);
                String birthDate = request.getParameter(PARAM_BIRTHDATE);
                String fName     = request.getParameter(PARAM_FNAME);
                String mName     = request.getParameter(PARAM_MNAME);
                String lName     = request.getParameter(PARAM_LNAME);
                String passport  = request.getParameter(PARAM_PASSPORT);
                String question  = request.getParameter(PARAM_QUESTION);
                String answer    = request.getParameter(PARAM_SECRET_ANSWER);
                if (playerService.registerPlayer(email, password, fName, mName, lName, birthDate, passport,
                                                 question, answer)) {
                    navigator = PageNavigator.REDIRECT_GOTO_INDEX;
                } else {
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_PLAYER_EMAIL_EXIST));
                    navigator = PageNavigator.FORWARD_PAGE_REGISTER;
                }
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage);
            navigator = PageNavigator.FORWARD_PAGE_REGISTER;
        }
        return navigator;
    }

    /**
     * Provides request parameters validation
     *
     * @param request request from client to get parameters to validate
     * @return error message built while validating or empty string
     */
    private static String validateRequestParams(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();

        String email         = request.getParameter(PARAM_EMAIL);
        String password      = request.getParameter(PARAM_PASSWORD);
        String passwordAgain = request.getParameter(PARAM_PASSWORD_AGAIN);
        String birthDate     = request.getParameter(PARAM_BIRTHDATE);
        String fName         = request.getParameter(PARAM_FNAME);
        String mName         = request.getParameter(PARAM_MNAME);
        String lName         = request.getParameter(PARAM_LNAME);
        String passport      = request.getParameter(PARAM_PASSPORT);
        String question      = request.getParameter(PARAM_QUESTION);
        String answer        = request.getParameter(PARAM_SECRET_ANSWER);

        if (validateEmail(email)) {
            request.setAttribute(ATTR_EMAIL_INPUT, email);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL)).append(MESSAGE_SEPARATOR);
        }

        if (!validatePassword(password, passwordAgain)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(WHITESPACE)
                        .append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH)).append(MESSAGE_SEPARATOR);
        }

        if (validateBirthdate(birthDate)) {
            request.setAttribute(ATTR_BIRTHDATE_INPUT, birthDate);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_BIRTHDATE)).append(MESSAGE_SEPARATOR);
        }

        if (validateName(fName)) {
            request.setAttribute(ATTR_FNAME_INPUT, fName);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(MESSAGE_SEPARATOR);
        }
        if (validateName(mName)) {
            request.setAttribute(ATTR_MNAME_INPUT, mName);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(MESSAGE_SEPARATOR);
        }
        if (validateName(lName)) {
            request.setAttribute(ATTR_LNAME_INPUT, lName);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(MESSAGE_SEPARATOR);
        }

        if (validatePassport(passport)) {
            request.setAttribute(ATTR_PASSPORT_INPUT, passport);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSPORT)).append(MESSAGE_SEPARATOR);
        }

        if (validateQuestion(question)) {
            request.setAttribute(ATTR_QUESTION_INPUT, question);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_QUESTION)).append(MESSAGE_SEPARATOR);
        }

        if (validateAnswer(answer)) {
            request.setAttribute(ATTR_ANSWER_INPUT, answer);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_ANSWER)).append(MESSAGE_SEPARATOR);
        }

        return errorMessage.toString().trim();
    }
}