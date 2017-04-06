package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.logic.TransactionLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ShowTransactionsCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        boolean valid = true;

        String  type         = request.getParameter(PARAM_TYPE);
        String  month        = request.getParameter(PARAM_MONTH);
        boolean sortByAmount = request.getParameter(PARAM_SORT_BY_AMOUNT) != null;

        if (type != null && !FormValidator.validateTransactionType(type)) {
            valid = false;
        }

        if (month == null || month.trim().isEmpty() || FormValidator.validateDateMonth(month)) {
            request.setAttribute(ATTR_MONTH_INPUT, month);
        } else {
            valid = false;
        }

        if (valid) {
            QueryManager.saveQueryToSession(request);
            List<Transaction> transactionList = TransactionLogic.takeTransactionList(type, month, sortByAmount);
            request.setAttribute(ATTR_TRANSACTIONS, transactionList);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_TRANSACTIONS;
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}