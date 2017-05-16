package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.PlayerService;
import by.sasnouskikh.jcasino.service.UserService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides withdrawing money for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class WithdrawMoneyCommand implements Command {

    /**
     * <p>Provides withdrawing money for player.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_WITHDRAW_MONEY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_ACCOUNT}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_WITHDRAW_MONEY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see PlayerService#makeTransaction(Player, BigDecimal, Transaction.TransactionType)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        PageNavigator  navigator;

        boolean       valid                  = true;
        Player        player                 = (Player) session.getAttribute(ATTR_PLAYER);
        PlayerAccount account                = player.getAccount();
        BigDecimal    balance                = account.getBalance();
        BigDecimal    currentMonthWithdrawal = account.getThisMonthWithdrawal();
        BigDecimal    withdrawalLimit        = account.getStatus().getWithdrawalLimit();
        BigDecimal    maxWithdrawal          = withdrawalLimit.subtract(currentMonthWithdrawal);

        String     stringAmount = request.getParameter(PARAM_AMOUNT);
        String     password     = request.getParameter(PARAM_PASSWORD);
        BigDecimal amount       = null;

        if (FormValidator.validateAmount(stringAmount)) {
            amount = BigDecimal.valueOf(Double.parseDouble(stringAmount));
            if (amount.compareTo(balance) <= 0) {
                if (amount.compareTo(maxWithdrawal) <= 0) {
                    request.setAttribute(ATTR_AMOUNT_INPUT, amount);
                } else {
                    errorMessage.append(messageManager.getMessage(MESSAGE_WITHDRAWAL_OVERLIMIT)).append(WHITESPACE)
                                .append(maxWithdrawal).append(DOT).append(MESSAGE_SEPARATOR);
                    valid = false;
                }
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_WITHDRAWAL_NOMONEY)).append(MESSAGE_SEPARATOR);
                valid = false;
            }
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_AMOUNT)).append(MESSAGE_SEPARATOR);
            valid = false;
        }
        //TODO check if user validation status allows to withdraw money

        if (!FormValidator.validatePassword(password)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        try (UserService userService = new UserService()) {
            if (!userService.checkPassword(player, password)) {
                errorMessage.append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH_CURRENT)).append(MESSAGE_SEPARATOR);
                valid = false;
            }
        }

        if (valid) {
            try (PlayerService playerService = new PlayerService()) {
                if (playerService.makeTransaction(player, amount, Transaction.TransactionType.WITHDRAW)) {
                    navigator = PageNavigator.REDIRECT_GOTO_ACCOUNT;
                } else {
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_WITHDRAWAL_INTERRUPTED));
                    navigator = PageNavigator.FORWARD_PAGE_WITHDRAW_MONEY;
                }
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_WITHDRAW_MONEY;
        }
        return navigator;
    }
}