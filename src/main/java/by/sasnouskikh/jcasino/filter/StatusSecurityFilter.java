package by.sasnouskikh.jcasino.filter;

import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerAccount;
import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.PlayerService;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides security filter for servlet container which prevents application banned/unactive players from
 * processing important operations.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Filter
 * @see WebFilter
 */
@WebFilter(
filterName = "StatusSecurityFilter",
servletNames = {"MainController", "AjaxController"},
dispatcherTypes = {DispatcherType.REQUEST,
                   DispatcherType.FORWARD,
                   DispatcherType.ASYNC}
)
public class StatusSecurityFilter implements Filter {

    private static final String GOTO_GAME_FRUITS       = "goto_game_fruits";
    private static final String GOTO_GAME_FRUITS_SETUP = "goto_game_fruits_setup";
    private static final String TAKE_LOAN              = "take_loan";
    private static final String WITHDRAW_MONEY         = "withdraw_money";
    private static final String SPIN                   = "spin";
    private static final String FINISH_STREAK          = "finish_streak";
    private static final String SWITCH_TO_DEMO         = "switch_to_demo";
    private static final String SWITCH_TO_REAL         = "switch_to_real";

    private static final List<String> checkedCommands = Arrays.asList(GOTO_GAME_FRUITS, GOTO_GAME_FRUITS_SETUP,
                                                                      TAKE_LOAN, WITHDRAW_MONEY,
                                                                      SPIN, FINISH_STREAK,
                                                                      SWITCH_TO_DEMO, SWITCH_TO_REAL);

    /**
     * <p>The servlet container calls the init
     * method exactly once after instantiating the filter. The init
     * method must complete successfully before the filter is asked to do any
     * filtering work.
     * <p>The web container cannot place the filter into service if the init
     * method either
     * <ol>
     * <li>Throws pressedKey ServletException
     * <li>Does not return within pressedKey time period defined by the web container
     * </ol>
     *
     * @param config a filter configuration object used by a servlet container to pass information to a filter during
     *               initialization
     * @see FilterConfig#getInitParameter(String)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the
     * container each time pressedKey request/response pair is passed through the
     * chain due to pressedKey client request for pressedKey resource at the end of the chain.
     * The FilterChain passed in to this method allows the Filter to pass
     * on the request and response to the next entity in the chain.
     * <p>A typical implementation of this method would follow the following
     * pattern:
     * <ol>
     * <li>Examine the request
     * <li>Optionally wrap the request object with pressedKey custom implementation to
     * filter content or headers for input filtering
     * <li>Optionally wrap the response object with pressedKey custom implementation to
     * filter content or headers for output filtering
     * <li>
     * <ul>
     * <li><strong>Either</strong> invoke the next entity in the chain
     * using the FilterChain object
     * (<code>chain.doFilter()</code>),
     * <li><strong>or</strong> not pass on the request/response pair to
     * the next entity in the filter chain to
     * block the request processing
     * </ul>
     * <li>Directly set headers on the response after invocation of the
     * next entity in the filter chain.
     * </ol>
     *
     * @param request  an object which provides client request information to a servlet
     * @param response an object which assists a servlet in sending a response to the client
     * @param chain    an object provided by the servlet container to the developer giving a view into the invocation
     *                 chain of a filtered request for a resource. Filters use the FilterChain to invoke the next filter
     *                 in the chain, or if the calling filter is the last filter in the chain, to invoke the resource at
     *                 the end of the chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest  httpRequest  = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession         session      = httpRequest.getSession();

        JCasinoUser.UserRole role    = (JCasinoUser.UserRole) session.getAttribute(ATTR_ROLE);
        String               command = httpRequest.getParameter(PARAM_COMMAND);
        if (role == JCasinoUser.UserRole.GUEST || role == JCasinoUser.UserRole.ADMIN ||
            role == null || !needToCheck(command)) {
            chain.doFilter(request, response);
            return;
        }

        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        Player         player         = (Player) session.getAttribute(ATTR_PLAYER);

        try (PlayerService playerService = new PlayerService()) {
            if (!playerService.updateAccountInfo(player) ||
                !playerService.updateVerificationInfo(player)) {
                forwardError(httpRequest, httpResponse, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
                return;
            }
        }

        PlayerAccount           account      = player.getAccount();
        PlayerVerification      verification = player.getVerification();
        PlayerStatus.StatusEnum statusEnum   = account.getStatus().getStatus();

        switch (command.toLowerCase().trim()) {
            case GOTO_GAME_FRUITS:
            case GOTO_GAME_FRUITS_SETUP:
            case SPIN:
            case FINISH_STREAK:
            case SWITCH_TO_DEMO:
            case SWITCH_TO_REAL:
                if (statusEnum == PlayerStatus.StatusEnum.BAN ||
                    statusEnum == PlayerStatus.StatusEnum.UNACTIVE) {
                    forwardError(httpRequest, httpResponse, messageManager.getMessage(MESSAGE_GAME_NOT_ALLOWED));
                } else {
                    chain.doFilter(request, response);
                }
                break;
            case TAKE_LOAN:
                if (statusEnum == PlayerStatus.StatusEnum.BAN ||
                    statusEnum == PlayerStatus.StatusEnum.UNACTIVE ||
                    account.getCurrentLoan() != null) {
                    forwardError(httpRequest, httpResponse, messageManager.getMessage(MESSAGE_TAKE_LOAN_NOT_ALLOWED));
                } else {
                    chain.doFilter(request, response);
                }
                break;
            case WITHDRAW_MONEY:
                if (statusEnum == PlayerStatus.StatusEnum.BAN ||
                    statusEnum == PlayerStatus.StatusEnum.UNACTIVE ||
                    account.getCurrentLoan() != null ||
                    verification.getStatus() != PlayerVerification.VerificationStatus.VERIFIED) {
                    forwardError(httpRequest, httpResponse, messageManager.getMessage(MESSAGE_WITHDRAWAL_NOT_ALLOWED));
                } else {
                    chain.doFilter(request, response);
                }
                break;
            default:
                chain.doFilter(request, response);
        }
    }

    /**
     * Called by the web container to indicate to pressedKey filter that it is being
     * taken out of service.
     * <p>This method is only called once all threads within the filter's
     * doFilter method have exited or after pressedKey timeout period has passed.
     * After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter.
     * <p>This method gives the filter an opportunity to clean up any
     * resources that are being held (for example, memory, file handles,
     * threads) and make sure that any persistent state is synchronized
     * with the filter's current state in memory.
     */
    @Override
    public void destroy() {
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
    throws ServletException, IOException {
        request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage);
        request.getRequestDispatcher(QueryManager.takePreviousQuery(request)).forward(request, response);
    }

    private boolean needToCheck(String commandName) {
        return commandName != null && checkedCommands.contains(commandName.toLowerCase().trim());
    }
}