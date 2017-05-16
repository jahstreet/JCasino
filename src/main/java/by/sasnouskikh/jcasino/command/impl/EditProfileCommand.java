package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.PlayerService;
import by.sasnouskikh.jcasino.service.ServiceException;
import by.sasnouskikh.jcasino.validator.FormValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;
import static by.sasnouskikh.jcasino.validator.FormValidator.*;

/**
 * The class provides editing profile info for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class EditProfileCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(EditProfileCommand.class);

    /**
     * <p>Provides editing profile info for player.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_PROFILE}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_PROFILE}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_PROFILE}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see PlayerService#changeEmail(Player, String)
     * @see PlayerService#changePassword(Player, String, String)
     * @see PlayerService#changeBirthdate(Player, String)
     * @see PlayerService#changeProfileTextItem(Player, String, PlayerService.ProfileTextField)
     * @see PlayerService#changeSecretQuestion(Player, String, String)
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

        Player player        = (Player) session.getAttribute(ATTR_PLAYER);
        String email         = request.getParameter(PARAM_EMAIL);
        String fname         = request.getParameter(PARAM_FNAME);
        String mname         = request.getParameter(PARAM_MNAME);
        String lname         = request.getParameter(PARAM_LNAME);
        String birthDate     = request.getParameter(PARAM_BIRTHDATE);
        String passport      = request.getParameter(PARAM_PASSPORT);
        String question      = request.getParameter(PARAM_QUESTION);
        String answer        = request.getParameter(PARAM_ANSWER);
        String passwordOld   = request.getParameter(PARAM_PASSWORD_OLD);
        String password      = request.getParameter(PARAM_PASSWORD);
        String passwordAgain = request.getParameter(PARAM_PASSWORD_AGAIN);

        if (email == null
            && fname == null
            && mname == null
            && lname == null
            && birthDate == null
            && passport == null
            && (question == null || answer == null)
            && (passwordOld == null || password == null || passwordAgain == null)) {
            session.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        try (PlayerService playerService = new PlayerService()) {
            if (email != null) {
                String currentEmail = player.getEmail();
                if (email.equals(currentEmail)) {
                    errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL_EDIT)).append(MESSAGE_SEPARATOR);
                    valid = false;
                } else {
                    if (validateEmail(email)) {
                        if (!playerService.changeEmail(player, email)) {
                            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL_INUSE)).append(MESSAGE_SEPARATOR);
                            valid = false;
                        }
                    } else {
                        errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_EMAIL)).append(MESSAGE_SEPARATOR);
                        valid = false;
                    }
                }
            }

            if (password != null && passwordAgain != null && passwordOld != null) {
                if (!validatePassword(passwordOld)) {
                    errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(MESSAGE_SEPARATOR);
                    valid = false;
                }
                if (!validatePassword(password, passwordAgain)) {
                    errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(WHITESPACE)
                                .append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH)).append(MESSAGE_SEPARATOR);
                    valid = false;
                }
                if (valid && !playerService.changePassword(player, passwordOld, password)) {
                    errorMessage.append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH_CURRENT)).append(MESSAGE_SEPARATOR);
                    valid = false;
                }
            }

            try {
                if (fname != null) {
                    if (validateName(fname)) {
                        playerService.changeProfileTextItem(player, fname, PlayerService.ProfileTextField.FIRST_NAME);
                    } else {
                        errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(MESSAGE_SEPARATOR);
                        valid = false;
                    }
                }

                if (mname != null) {
                    if (validateName(mname)) {
                        playerService.changeProfileTextItem(player, mname, PlayerService.ProfileTextField.MIDDLE_NAME);
                    } else {
                        errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(MESSAGE_SEPARATOR);
                        valid = false;
                    }
                }

                if (lname != null) {
                    if (validateName(lname)) {
                        playerService.changeProfileTextItem(player, lname, PlayerService.ProfileTextField.LAST_NAME);
                    } else {
                        errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NAME)).append(MESSAGE_SEPARATOR);
                        valid = false;
                    }
                }

                if (passport != null) {
                    if (validatePassport(passport)) {
                        playerService.changeProfileTextItem(player, passport, PlayerService.ProfileTextField.PASSPORT);
                    } else {
                        errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSPORT)).append(MESSAGE_SEPARATOR);
                        valid = false;
                    }
                }
            } catch (ServiceException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_JSP)).append(MESSAGE_SEPARATOR);
                valid = false;
            }

            if (birthDate != null) {
                if (validateBirthdate(birthDate)) {
                    playerService.changeBirthdate(player, birthDate);
                } else {
                    errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_BIRTHDATE)).append(MESSAGE_SEPARATOR);
                    valid = false;
                }
            }

            if (question != null && answer != null) {
                if (!validateQuestion(question)) {
                    errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_QUESTION)).append(MESSAGE_SEPARATOR);
                    valid = false;
                }
                if (!validateAnswer(answer)) {
                    errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_ANSWER)).append(MESSAGE_SEPARATOR);
                    valid = false;
                }
                if (valid) {
                    playerService.changeSecretQuestion(player, question, answer);
                }
            }
        }

        if (valid) {
            navigator = PageNavigator.REDIRECT_GOTO_PROFILE;
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_PROFILE;
        }
        return navigator;
    }
}