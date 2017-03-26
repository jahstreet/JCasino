package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.logic.UserLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class TakeLoanCommand implements Command {

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

        BigDecimal maxLoan      = player.getAccount().getStatus().getMaxLoan();
        String     password     = request.getParameter(PARAM_PASSWORD);
        String     stringAmount = request.getParameter(PARAM_AMOUNT);
        BigDecimal amount       = BigDecimal.valueOf(Double.parseDouble(stringAmount));

        if (!FormValidator.validateAmount(stringAmount)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_AMOUNT)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (maxLoan.compareTo(amount) < 0) {
            errorMessage.append(messageManager.getMessage(MESSAGE_AMOUNT_LIMIT_ERROR)).append(WHITESPACE)
                        .append(maxLoan).append(DOT).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            request.setAttribute(ATTR_AMOUNT_INPUT, stringAmount);
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
            if (PlayerLogic.takeNewLoan(player, amount)) {
                queryParams = new String[]{GOTO_ACCOUNT, REDIRECT};
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_TAKE_LOAN_INTERRUPTED));
                request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
                queryParams = new String[]{PAGE_TAKE_LOAN, FORWARD};
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            queryParams = new String[]{PAGE_TAKE_LOAN, FORWARD};
        }

        return queryParams;
    }
}