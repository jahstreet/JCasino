package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.PlayerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides navigating to e-mail verification page for player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class GotoEmailVerificationCommand implements Command {

    /**
     * <p>Provides navigating to e-mail verification page for player.
     * <p>Takes e-mail code from {@link by.sasnouskikh.jcasino.entity.bean.PlayerVerification} field of
     * {@link ConfigConstant#ATTR_PLAYER} attribute of {@link HttpSession#getAttribute(String)}.
     * <p>If e-mail code exists saves current query to session and navigates to
     * {@link PageNavigator#FORWARD_PAGE_EMAIL_VERIFICATION}
     * <p>If e-mail code doesn't exist sends new e-mail code at Logic layer. If Logic operation passed successfully
     * saves current query to session and navigates to {@link PageNavigator#FORWARD_PAGE_EMAIL_VERIFICATION}, else adds
     * {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to {@link HttpServletRequest#setAttribute(String, Object)}
     * and navigates to {@link PageNavigator#FORWARD_PAGE_VERIFICATION}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see PlayerService#sendEmailCode(Player, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        Player player    = (Player) session.getAttribute(ATTR_PLAYER);
        String emailCode = player.getVerification().getEmailCode();

        if (emailCode != null && !emailCode.isEmpty()) {
            QueryManager.saveQueryToSession(request);
            navigator = PageNavigator.FORWARD_PAGE_EMAIL_VERIFICATION;
        } else {
            if (PlayerService.sendEmailCode(player, locale)) {
                QueryManager.saveQueryToSession(request);
                navigator = PageNavigator.FORWARD_PAGE_EMAIL_VERIFICATION;
            } else {
                request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_CODE_NOT_SENT_ERROR));
                navigator = PageNavigator.FORWARD_PAGE_VERIFICATION;
            }
        }
        return navigator;
    }
}