package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.logic.LoanLogic;
import by.sasnouskikh.jcasino.logic.StreakLogic;
import by.sasnouskikh.jcasino.logic.TransactionLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ShowHistoryCommand implements Command {

//    private static final int DEFAULT_ELEMENTS_ON_PAGE = 10;
//    private static final int FIRST_PAGE               = 1;

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();

        boolean valid  = true;
        Player  player = (Player) session.getAttribute(ATTR_PLAYER);
        int     id     = player.getId();

        String type  = request.getParameter(PARAM_TYPE);
        String month = request.getParameter(PARAM_MONTH);

        //TODO PAGINATION SUPPORT

        List<Transaction> transactions = null;
        List<Loan>        loans        = null;
        List<Streak>      streaks      = null;

        if (month != null && month.trim().isEmpty()) {
            month = null;
        }

        if (month == null || FormValidator.validateDateMonth(month)) {
            request.setAttribute(ATTR_MONTH_INPUT, month);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_DATE_OR_ALL));
            valid = false;
        }

        if (valid) {
            switch (type) {
                case ATTR_TRANSACTIONS:
                    transactions = TransactionLogic.takePlayerTransactions(id, month);
                    break;
                case ATTR_LOANS:
                    loans = LoanLogic.takePlayerLoans(id, month);
                    break;
                case ATTR_STREAKS:
                    streaks = StreakLogic.takePlayerStreaks(id, month);
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