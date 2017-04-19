package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.logic.UserLogic;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides replenishing account for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ReplenishAccountCommand implements Command {

    /**
     * <p>Provides replenishing account for player.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_REPLENISH_ACCOUNT}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_ACCOUNT}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_REPLENISH_ACCOUNT}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see PlayerLogic#makeTransaction(Player, BigDecimal, Transaction.TransactionType)
     */
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

        String     password     = request.getParameter(PARAM_PASSWORD);
        String     stringAmount = request.getParameter(PARAM_AMOUNT);
        BigDecimal amount       = null;

        if (FormValidator.validateAmount(stringAmount)) {
            amount = BigDecimal.valueOf(Double.parseDouble(stringAmount));
            request.setAttribute(ATTR_AMOUNT_INPUT, stringAmount);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_AMOUNT)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (!FormValidator.validatePassword(password)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (!UserLogic.checkPassword(player, password)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH_CURRENT)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            if (PlayerLogic.makeTransaction(player, amount, Transaction.TransactionType.REPLENISH)) {
                navigator = PageNavigator.REDIRECT_GOTO_ACCOUNT;
            } else {
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_REPLENISH_INTERRUPTED));
                navigator = PageNavigator.FORWARD_PAGE_REPLENISH_ACCOUNT;
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_REPLENISH_ACCOUNT;
        }
        return navigator;
    }
}