package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.logic.TransactionLogic;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides showing transactions info for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ShowTransactionsCommand implements Command {

    /**
     * <p>provides showing transactions info for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer, saves current query to session, sets {@link ConfigConstant#ATTR_TRANSACTIONS} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_MANAGE_TRANSACTIONS}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see TransactionLogic#takeTransactionList(String, String, boolean)
     */
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