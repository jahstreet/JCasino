package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.logic.LoanLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ShowLoansCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        PageNavigator  navigator;

        boolean valid = true;

        String  acquire        = request.getParameter(PARAM_MONTH_ACQUIRE);
        String  expire         = request.getParameter(PARAM_MONTH_EXPIRE);
        boolean sortByRest     = request.getParameter(PARAM_SORT_BY_REST) != null;
        boolean filterNotPaid  = request.getParameter(PARAM_FILTER_NOT_PAID) != null;
        boolean filterOverdued = request.getParameter(PARAM_FILTER_OVERDUED) != null;

        if (acquire == null || acquire.trim().isEmpty() || FormValidator.validateDateMonth(acquire)) {
            request.setAttribute(ATTR_MONTH_ACQUIRE_INPUT, acquire);
        } else {
            valid = false;
        }

        if (expire == null || expire.trim().isEmpty() || FormValidator.validateDateMonth(expire)) {
            request.setAttribute(ATTR_MONTH_EXPIRE_INPUT, expire);
        } else {
            valid = false;
        }

        if (valid) {
            QueryManager.saveQueryToSession(request);
            List<Loan> loanList = LoanLogic.takeLoanList(acquire, expire, sortByRest, filterNotPaid, filterOverdued);
            request.setAttribute(ATTR_LOANS, loanList);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_LOANS;
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}