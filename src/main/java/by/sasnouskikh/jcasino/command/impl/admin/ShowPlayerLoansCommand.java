package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.LoanService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides showing definite player loans info for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ShowPlayerLoansCommand implements Command {

    /**
     * <p>Provides showing definite player loans info for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully saves current query to session, sets
     * {@link ConfigConstant#ATTR_LOANS} attribute to {@link HttpServletRequest#setAttribute(String, Object)} and
     * navigates to {@link PageNavigator#FORWARD_PAGE_MANAGE_LOANS}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PREV_QUERY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see LoanService#takePlayerLoans(int, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String playerIdString = request.getParameter(PARAM_ID);
        int    playerId;

        if (FormValidator.validateId(playerIdString)) {
            playerId = Integer.parseInt(playerIdString);
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        List<Loan> loans;
        try (LoanService loanService = new LoanService()) {
            loans = loanService.takePlayerLoans(playerId, null);
        }
        if (loans != null) {
            QueryManager.saveQueryToSession(request);
            request.setAttribute(ATTR_LOANS, loans);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_LOANS;
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_PLAYER_NO_LOANS));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}