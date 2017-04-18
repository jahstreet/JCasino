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
 * The class provides showing definite player transactions info for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ShowPlayerTransactionsCommand implements Command {

    /**
     * <p>Provides showing definite player transactions info for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully saves current query to session, sets
     * {@link ConfigConstant#ATTR_TRANSACTIONS} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_MANAGE_TRANSACTIONS}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PREV_QUERY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see TransactionLogic#takePlayerTransactions(int, String)
     */
    @SuppressWarnings("Duplicates")
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String playerIdString = request.getParameter(PARAM_ID);
        int    playerId;

        //suppressed duplicate warning code-block
        if (FormValidator.validateId(playerIdString)) {
            playerId = Integer.parseInt(playerIdString);
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        List<Transaction> transactions = TransactionLogic.takePlayerTransactions(playerId, null);
        if (transactions != null && !transactions.isEmpty()) {
            QueryManager.saveQueryToSession(request);
            request.setAttribute(ATTR_TRANSACTIONS, transactions);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_TRANSACTIONS;
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, MESSAGE_PLAYER_NO_TRANSACTIONS);
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}