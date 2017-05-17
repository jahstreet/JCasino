package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.PlayerService;
import by.sasnouskikh.jcasino.service.StreakService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides navigating to game page.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class GotoGameFruitsCommand implements Command {

    /**
     * <p>Provides navigating to game page.
     * <p>Initializes if it is necessary game-mode, user balance and other attributes of
     * {@link HttpServletRequest} and {@link HttpSession} due to application service.
     * <p>Navigates to {@link PageNavigator#FORWARD_PAGE_GAME_FRUITS}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see ConfigConstant
     * @see PlayerService#updateAccountInfo(Player)
     * @see StreakService#generateStreak()
     * @see StreakService#generateStreak(int)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        Player         player         = (Player) session.getAttribute(ATTR_PLAYER);
        Streak         streak         = (Streak) session.getAttribute(ATTR_CURRENT_STREAK);
        BigDecimal     money;
        boolean demo = request.getParameter(ATTR_DEMO_PLAY) != null || player == null;

        if (!demo) {
            try (PlayerService playerService = new PlayerService();
                 StreakService streakService = new StreakService()) {
                if (playerService.updateAccountInfo(player)) {
                    money = player.getAccount().getBalance();
                } else {
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
                    return PageNavigator.FORWARD_PREV_QUERY;
                }
                if (streak == null || streak.getPlayerId() == 0) {
                    streak = streakService.generateStreak(player.getId());
                    if (streak == null) {
                        request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
                        return PageNavigator.FORWARD_PREV_QUERY;
                    }
                }
            }
        } else {
            money = DEMO_START_MONEY;
            if (streak != null && streak.getPlayerId() != 0) {
                StreakService.completeStreak(streak);
                try (StreakService streakService = new StreakService()){
                    streakService.updateStreak(streak);
                }
                streak = null;
            }
            if (streak == null) {
                streak = StreakService.generateStreak();
            }
        }

        String streakInfo;
        if (streak.getRolls().size() < BETS_IN_STREAK) {
            streakInfo = streak.getRollMD5();
        } else {
            streakInfo = streak.getRoll();
        }

        request.setAttribute(ATTR_STREAK_INFO, streakInfo);
        request.setAttribute(ATTR_MONEY_INPUT, money.toPlainString());
        if (demo) {
            session.setAttribute(ATTR_DEMO_PLAY, true);
        } else {
            session.removeAttribute(ATTR_DEMO_PLAY);
        }
        session.setAttribute(ATTR_CURRENT_STREAK, streak);
        return PageNavigator.FORWARD_PAGE_GAME_FRUITS;
    }
}