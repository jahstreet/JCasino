package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.LoanService;
import by.sasnouskikh.jcasino.service.StreakService;
import by.sasnouskikh.jcasino.service.TransactionService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides showing operation history for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ShowHistoryCommand implements Command {

    /**
     * <p>Provides showing operation history for player.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)}.
     * <p>Sets list of taken data to correspondent {@link HttpSession#setAttribute(String, Object)} attribute
     * and navigates to {@link PageNavigator#FORWARD_PAGE_OPERATION_HISTORY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see TransactionService#takePlayerTransactions(int, String)
     * @see LoanService#takePlayerLoans(int, String)
     * @see StreakService#takePlayerStreaks(int, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();

        boolean valid  = true;
        Player  player = (Player) session.getAttribute(ATTR_PLAYER);
        int     id     = player.getId();

        String type  = request.getParameter(PARAM_TYPE);
        String month = request.getParameter(PARAM_MONTH);

        List<Transaction> transactions = null;
        List<Loan>        loans        = null;
        List<Streak>      streaks      = null;

        if (month != null && month.trim().isEmpty()) {
            month = null;
        }

        if (month == null || FormValidator.validateDateMonth(month)) {
            request.setAttribute(ATTR_MONTH_INPUT, month);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_DATE));
            valid = false;
        }

        if (valid) {
            QueryManager.saveQueryToSession(request);
            switch (type) {
                case ATTR_TRANSACTIONS:
                    try (TransactionService transactionService = new TransactionService()) {
                        transactions = transactionService.takePlayerTransactions(id, month);
                    }
                    break;
                case ATTR_LOANS:
                    try (LoanService loanService = new LoanService()) {
                        loans = loanService.takePlayerLoans(id, month);
                    }
                    break;
                case ATTR_STREAKS:
                    try (StreakService streakService = new StreakService()) {
                        streaks = streakService.takePlayerStreaks(id, month);
                    }
                    break;
                default:
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_HISTORY_TYPE));
            }
            request.setAttribute(ATTR_TRANSACTIONS, transactions);
            request.setAttribute(ATTR_LOANS, loans);
            request.setAttribute(ATTR_STREAKS, streaks);
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
        }
        return PageNavigator.FORWARD_PAGE_OPERATION_HISTORY;
    }
}