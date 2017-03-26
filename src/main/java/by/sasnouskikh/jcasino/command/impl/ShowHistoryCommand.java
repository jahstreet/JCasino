package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ShowHistoryCommand implements Command {

    private static final int DEFAULT_ELEMENTS_ON_PAGE = 10;
    private static final int FIRST_PAGE               = 1;

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = new MessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        boolean        valid          = true;
        Player         player         = (Player) session.getAttribute(ATTR_PLAYER);
        int            id             = player.getId();
        String         type           = request.getParameter(PARAM_TYPE);
        String         month          = request.getParameter(PARAM_MONTH);
        String         all            = request.getParameter(PARAM_ALL);
        System.out.println(all);
        //TODO PAGINATION SUPPORT
//        String pageNumber = request.getParameter(PARAM_PAGE);
//        String elementsOnPage = request.getParameter(PARAM_ELEMENTS_ON_PAGE);
//        int onPage = DEFAULT_ELEMENTS_ON_PAGE;
//        int page = FIRST_PAGE;

//        if (FormValidator.validateStringNumber(elementsOnPage)) {
//            onPage = Integer.parseInt(elementsOnPage);
//        } else {
//            //TODO errorMessage
//        }
//
//        if (FormValidator.validateStringNumber(pageNumber)) {
//            page = Integer.parseInt(pageNumber);
//        } else {
//            //TODO errorMessage
//        }

        List<Transaction> transactions = null;
        List<Loan>        loans        = null;
        List<Streak>      streaks      = null;

        if (!FormValidator.validateDate(month) && all == null) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_DATE_OR_ALL)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }
        if (valid) {
            switch (type) {
                case ATTR_TRANSACTIONS:
                    transactions = PlayerLogic.takeTransactions(id, month, all);
                    break;
                case ATTR_LOANS:
                    loans = PlayerLogic.takeLoans(id, month, all);
                    break;
                case ATTR_STREAKS:
                    streaks = PlayerLogic.takeStreaks(id, month, all);
                    break;
                default:
                    errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_HISTORY_TYPE)).append(NEW_LINE_SEPARATOR);
                    request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
                    return new String[]{PAGE_OPERATION_HISTORY, FORWARD};
            }
            request.setAttribute(ATTR_TRANSACTIONS, transactions);
            request.setAttribute(ATTR_LOANS, loans);
            request.setAttribute(ATTR_STREAKS, streaks);
            request.setAttribute(ATTR_ERROR_MESSAGE, null);
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
        }
        return new String[]{PAGE_OPERATION_HISTORY, FORWARD};
    }
}