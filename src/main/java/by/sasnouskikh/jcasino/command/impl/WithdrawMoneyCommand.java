package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.logic.UserLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class WithdrawMoneyCommand implements Command {

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

        String        stringAmount           = request.getParameter(PARAM_AMOUNT);
        String        password               = request.getParameter(PARAM_PASSWORD);
        PlayerAccount account                = player.getAccount();
        BigDecimal    amount                 = BigDecimal.valueOf(Double.parseDouble(stringAmount));
        BigDecimal    balance                = account.getBalance();
        BigDecimal    currentMonthWithdrawal = account.getThisMonthWithdrawal();
        BigDecimal    monthWirhdrawalLimit   = account.getStatus().getWithdrawalLimit();
        BigDecimal    maxWithdrawal          = monthWirhdrawalLimit.subtract(currentMonthWithdrawal);

        if (FormValidator.validateAmount(stringAmount)) {
            if (amount.compareTo(balance) > 0) {
                errorMessage.append(messageManager.getMessage(MESSAGE_WITHDRAWAL_NOMONEY)).append(NEW_LINE_SEPARATOR);
                valid = false;
            } else {
                if (amount.compareTo(maxWithdrawal) > 0) {
                    errorMessage.append(messageManager.getMessage(MESSAGE_WITHDRAWAL_OVERLIMIT)).append(WHITESPACE)
                                .append(maxWithdrawal).append(DOT).append(NEW_LINE_SEPARATOR);
                    valid = false;
                } else {
                    request.setAttribute(ATTR_AMOUNT_INPUT, amount);
                }
            }
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_AMOUNT)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (!FormValidator.validatePassword(password)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_PASSWORD)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (!UserLogic.checkPassword(player, password)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_PASSWORD_MISMATCH_CURRENT)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            if (PlayerLogic.withdrawMoney(player, amount)) {
                queryParams = new String[]{GOTO_ACCOUNT, REDIRECT};
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_WITHDRAWAL_INTERRUPTED)).append(NEW_LINE_SEPARATOR);
                request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
                queryParams = new String[]{PAGE_WITHDRAW_MONEY, FORWARD};
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            queryParams = new String[]{PAGE_WITHDRAW_MONEY, FORWARD};
        }

        return queryParams;
    }
}