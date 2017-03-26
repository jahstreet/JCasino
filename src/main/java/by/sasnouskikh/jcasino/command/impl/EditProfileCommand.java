package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.dao.impl.PlayerDAOImpl;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.logic.UserLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;
import static by.sasnouskikh.jcasino.validator.FormValidator.*;

public class EditProfileCommand implements Command {

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = new MessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        String[]       queryParams;
        boolean        valid          = true;
        Player         player         = (Player) session.getAttribute(ATTR_PLAYER);
        String         email          = request.getParameter(PARAM_EMAIL);
        String         fname          = request.getParameter(PARAM_FNAME);
        String         mname          = request.getParameter(PARAM_MNAME);
        String         lname          = request.getParameter(PARAM_LNAME);
        String         birthDate      = request.getParameter(PARAM_BIRTHDATE);
        String         passport       = request.getParameter(PARAM_PASSPORT);
        String         question       = request.getParameter(PARAM_QUESTION);
        String         answer         = request.getParameter(PARAM_ANSWER);
        String         passwordOld    = request.getParameter(PARAM_PASSWORD_OLD);
        String         password       = request.getParameter(PARAM_PASSWORD);
        String         passwordAgain  = request.getParameter(PARAM_PASSWORD_AGAIN);

        if (email == null
            && fname == null
            && mname == null
            && lname == null
            && birthDate == null
            && passport == null
            && (question == null || answer == null)
            && (passwordOld == null || password == null || passwordAgain == null)) {
            session.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return new String[]{PAGE_ERROR_500, FORWARD};
        }

        if (email != null) {
            String currentEmail = player.getEmail();
            if (email.equals(currentEmail)) {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL_EDIT)).append(NEW_LINE_SEPARATOR);
                valid = false;
            } else {
                if (validateEmail(email)) {
                    if (!PlayerLogic.changeEmail(player, email)) {
                        errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL_INUSE)).append(NEW_LINE_SEPARATOR);
                        valid = false;
                    }
                } else {
                    errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL)).append(NEW_LINE_SEPARATOR);
                    valid = false;
                }
            }
        }

        if (password != null && passwordAgain != null && passwordOld != null) {
            if (!validatePassword(passwordOld)) {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
            if (!validatePassword(password, passwordAgain)) {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(WHITESPACE)
                            .append(MESSAGE_PASSWORD_MISMATCH).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
            if (valid && !UserLogic.changePassword(player, passwordOld, password)) {
                errorMessage.append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH_CURRENT)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
        }

        if (fname != null) {
            if (validateName(fname)) {
                PlayerLogic.changeProfileTextItem(player, fname, PlayerDAOImpl.ProfileField.FIRST_NAME);
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
        }

        if (mname != null) {
            if (validateName(mname)) {
                PlayerLogic.changeProfileTextItem(player, mname, PlayerDAOImpl.ProfileField.MIDDLE_NAME);
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
        }

        if (lname != null) {
            if (validateName(lname)) {
                PlayerLogic.changeProfileTextItem(player, lname, PlayerDAOImpl.ProfileField.LAST_NAME);
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
        }

        if (birthDate != null) {
            if (validateBirthdate(birthDate)) {
                PlayerLogic.changeBirthdate(player, birthDate);
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_BIRTHDATE)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
        }

        if (passport != null) {
            if (validatePassport(passport)) {
                PlayerLogic.changeProfileTextItem(player, passport, PlayerDAOImpl.ProfileField.PASSPORT);
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSPORT)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
        }

        if (question != null && answer != null) {
            if (!validateQuestion(question)) {

                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_QUESTION)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
            if (!validateAnswer(answer)) {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_ANSWER)).append(NEW_LINE_SEPARATOR);
                valid = false;
            }
            if (valid) {
                PlayerLogic.changeSecretQuestion(player, question, answer);
            }
        }

        if (valid) {
            queryParams = new String[]{GOTO_PROFILE, REDIRECT};
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            queryParams = new String[]{PAGE_PROFILE, FORWARD};
        }

        return queryParams;
    }
}