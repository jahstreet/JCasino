package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.service.LoanService;
import by.sasnouskikh.jcasino.service.UserService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides paying loan for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class PayLoanCommand implements Command {

    /**
     * <p>Provides paying loan for player.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_PAY_LOAN}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully navigates to {@link PageNavigator#REDIRECT_GOTO_ACCOUNT}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_PAY_LOAN}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see MessageManager
     * @see FormValidator
     * @see LoanService#payLoan(Player, BigDecimal)
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

        String        stringAmount = request.getParameter(PARAM_AMOUNT);
        String        password     = request.getParameter(PARAM_PASSWORD);
        PlayerAccount account      = player.getAccount();
        BigDecimal    rest         = account.getCurrentLoan().getRest();
        BigDecimal    amount       = BigDecimal.valueOf(Double.parseDouble(stringAmount));
        BigDecimal    balance      = account.getBalance();

        if (FormValidator.validateAmount(stringAmount)) {
            if (amount.compareTo(rest) > 0) {
                amount = rest;
            }
            if (amount.compareTo(balance) > 0) {
                errorMessage.append(messageManager.getMessage(MESSAGE_PAY_LOAN_NOMONEY)).append(MESSAGE_SEPARATOR);
                valid = false;
            }
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_AMOUNT)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

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
            try (LoanService loanService = new LoanService()) {
                if (loanService.payLoan(player, amount)) {
                    navigator = PageNavigator.REDIRECT_GOTO_ACCOUNT;
                } else {
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_PAY_LOAN_INTERRUPTED));
                    navigator = PageNavigator.FORWARD_PAGE_PAY_LOAN;
                }
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_PAY_LOAN;
        }
        return navigator;
    }
}