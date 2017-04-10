package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.logic.LoanLogic;
import by.sasnouskikh.jcasino.logic.UserLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class PayLoanCommand implements Command {

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

        if (!UserLogic.checkPassword(player, password)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH_CURRENT)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            if (LoanLogic.payLoan(player, amount)) {
                navigator = PageNavigator.REDIRECT_GOTO_ACCOUNT;
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_PAY_LOAN_INTERRUPTED)).append(MESSAGE_SEPARATOR);
                request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
                navigator = PageNavigator.FORWARD_PAGE_PAY_LOAN;
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            navigator = PageNavigator.FORWARD_PAGE_PAY_LOAN;
        }
        return navigator;
    }
}