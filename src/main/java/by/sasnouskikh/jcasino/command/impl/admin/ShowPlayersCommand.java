package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.AdminService;
import by.sasnouskikh.jcasino.service.StreakService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides showing players list for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class ShowPlayersCommand implements Command {

    private static final String DEFAULT_ID_VALUE = "0";

    /**
     * <p>Provides showing definite player streaks info for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully saves current query to session, sets
     * {@link ConfigConstant#ATTR_STREAKS} attribute to {@link HttpServletRequest#setAttribute(String, Object)} and
     * navigates to {@link PageNavigator#FORWARD_PAGE_MANAGE_STREAKS}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PREV_QUERY}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see StreakService#takePlayerStreaks(int, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String  playerIdString             = request.getParameter(PARAM_ID);
        String  statusFilter               = request.getParameter(PARAM_STATUS);
        String  verificationFilter         = request.getParameter(PARAM_VERIFICATION);
        String  monthWithdrawalRangeString = request.getParameter(PARAM_WITHDRAWAL);
        String  balanceRangeString         = request.getParameter(PARAM_BALANCE);
        boolean ignoreRanges              = request.getParameter(PARAM_DISABLE_RANGES) != null;

        int     playerId = 0;
        boolean valid    = true;

        playerIdString = playerIdString == null || playerIdString.trim().isEmpty() ?
                         DEFAULT_ID_VALUE :
                         playerIdString.trim();

        if (playerIdString.equalsIgnoreCase(DEFAULT_ID_VALUE) || FormValidator.validateId(playerIdString)) {
            playerId = Integer.parseInt(playerIdString);
        } else {
            valid = false;
        }

        if (statusFilter != null && !statusFilter.trim().isEmpty() &&
            !FormValidator.validateAccountStatus(statusFilter)) {
            valid = false;
        }

        if (verificationFilter != null && !verificationFilter.trim().isEmpty() &&
            !FormValidator.validateVerificationStatus(verificationFilter)) {
            valid = false;
        }

        if (!FormValidator.validateNumberRange(monthWithdrawalRangeString)) {
            valid = false;
        }

        if (!FormValidator.validateNumberRange(balanceRangeString)) {
            valid = false;
        }

        if (valid) {
            List<Player> players;
            try (AdminService adminService = new AdminService()) {
                players = adminService.takePlayerList(playerId, statusFilter, verificationFilter,
                                                      monthWithdrawalRangeString, balanceRangeString, ignoreRanges);
            }
            if (players != null) {
                if (!players.isEmpty()) {
                    QueryManager.saveQueryToSession(request);
                    request.setAttribute(ATTR_PLAYERS, players);
                    navigator = PageNavigator.FORWARD_PAGE_MANAGE_PLAYERS;
                } else {
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_NO_PLAYERS));
                    navigator = PageNavigator.FORWARD_PREV_QUERY;
                }
            } else {
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
                navigator = PageNavigator.FORWARD_PREV_QUERY;
            }
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}