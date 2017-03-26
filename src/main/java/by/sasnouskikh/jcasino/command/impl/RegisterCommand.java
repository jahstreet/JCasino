package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;
import static by.sasnouskikh.jcasino.validator.FormValidator.*;

public class RegisterCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = new MessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        String[]       queryParams;
        boolean        valid          = true;

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
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (!validatePassword(password, passwordAgain)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(WHITESPACE)
                        .append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (validateBirthdate(birthDate)) {
            request.setAttribute(ATTR_BIRTHDATE_INPUT, birthDate);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_BIRTHDATE)).append(NEW_LINE_SEPARATOR);
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
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(NEW_LINE_SEPARATOR);
                valid = false;
                break;
            }
        }

        if (validatePassport(passport)) {
            request.setAttribute(ATTR_PASSPORT_INPUT, passport);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSPORT)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (validateQuestion(question)) {
            request.setAttribute(ATTR_QUESTION_INPUT, question);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_QUESTION)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (validateAnswer(answer)) {
            request.setAttribute(ATTR_ANSWER_INPUT, answer);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_ANSWER)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            boolean registered = PlayerLogic.registerPlayer(email, password, fName, mName, lName, birthDate, passport, question, answer);
            if (registered) {
                String query = LOGIN_COMMAND_TEMPLATE + PARAM_EMAIL + VALUE_SEPARATOR + email +
                               PARAMETER_SEPARATOR + PARAM_PASSWORD + VALUE_SEPARATOR + password;
                queryParams = new String[]{query, FORWARD};
            } else {
                if (LOCALE_EN.equals(session.getAttribute(ATTR_LOCALE))) {
                    errorMessage.append("User with email '").append(email).append("' already exists.");
                } else {
                    errorMessage.append("Пользователь с email '").append(email).append("' уже существует.");
                }
                request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
                queryParams = new String[]{PAGE_REGISTER, FORWARD};
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            queryParams = new String[]{PAGE_REGISTER, FORWARD};
        }
        return queryParams;
    }
}