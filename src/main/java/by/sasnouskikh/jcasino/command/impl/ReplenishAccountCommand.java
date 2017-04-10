package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
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

public class ReplenishAccountCommand implements Command {

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
            if (PlayerLogic.replenishAccount(player, amount)) {
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