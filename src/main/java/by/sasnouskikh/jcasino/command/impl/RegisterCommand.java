package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;
import static by.sasnouskikh.jcasino.validator.FormValidator.*;

public class RegisterCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        PageNavigator  navigator;

        boolean valid = true;

        String email         = request.getParameter(PARAM_EMAIL);
        String password      = request.getParameter(PARAM_PASSWORD);
        String passwordAgain = request.getParameter(PARAM_PASSWORD_AGAIN);
        String birthDate     = request.getParameter(PARAM_BIRTHDATE);
        String fName         = request.getParameter(PARAM_FNAME);
        String mName         = request.getParameter(PARAM_MNAME);
        String lName         = request.getParameter(PARAM_LNAME);
        String passport      = request.getParameter(PARAM_PASSPORT);
        String question      = request.getParameter(PARAM_QUESTION);
        String answer        = request.getParameter(PARAM_ANSWER);

        if (validateEmail(email)) {
            request.setAttribute(ATTR_EMAIL_INPUT, email);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (!validatePassword(password, passwordAgain)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(WHITESPACE)
                        .append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (validateBirthdate(birthDate)) {
            request.setAttribute(ATTR_BIRTHDATE_INPUT, birthDate);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_BIRTHDATE)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        boolean fNameValid = validateName(fName);
        boolean mNameValid = validateName(mName);
        boolean lNameValid = validateName(lName);
        if (fNameValid) {
            request.setAttribute(ATTR_FNAME_INPUT, fName);
        }
        if (mNameValid) {
            request.setAttribute(ATTR_MNAME_INPUT, mName);
        }
        if (lNameValid) {
            request.setAttribute(ATTR_LNAME_INPUT, lName);
        }
        for (boolean nameValid : new boolean[]{fNameValid, mNameValid, lNameValid}) {
            if (!nameValid) {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(MESSAGE_SEPARATOR);
                valid = false;
                break;
            }
        }

        if (validatePassport(passport)) {
            request.setAttribute(ATTR_PASSPORT_INPUT, passport);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSPORT)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (validateQuestion(question)) {
            request.setAttribute(ATTR_QUESTION_INPUT, question);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_QUESTION)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (validateAnswer(answer)) {
            request.setAttribute(ATTR_ANSWER_INPUT, answer);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_ANSWER)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            boolean registered = PlayerLogic.registerPlayer(email, password, fName, mName, lName, birthDate, passport, question, answer);
            if (registered) {
                navigator = PageNavigator.REDIRECT_GOTO_INDEX;
            } else {
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_PLAYER_EMAIL_EXIST));
                navigator = PageNavigator.FORWARD_PAGE_REGISTER;
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_REGISTER;
        }
        return navigator;
    }
}