package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.logic.StreakLogic;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class GotoGameFruitsCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession session = request.getSession();
        Player      player  = (Player) session.getAttribute(ATTR_PLAYER);
        Streak      streak  = (Streak) session.getAttribute(ATTR_CURRENT_STREAK);
        BigDecimal  money   = BigDecimal.ZERO;
        boolean     demo    = session.getAttribute(ATTR_DEMO_PLAY) != null;
        if (player != null) {
            PlayerLogic.updateAccountInfo(player);
            if (player.getAccount() != null) {
                BigDecimal balance = player.getAccount().getBalance();
                if (balance != null) {
                    money = balance;
                }
            }
            if (demo || streak == null) {
                streak = StreakLogic.generateStreak(player.getId());
                session.setAttribute(ATTR_DEMO_PLAY, null);
            }
        } else {
            money = DEMO_START_MONEY;
            if (streak == null) {
                streak = StreakLogic.generateStreak();
            }
            if (!demo) {
                session.setAttribute(ATTR_DEMO_PLAY, ATTR_DEMO_PLAY);
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
        session.setAttribute(ATTR_CURRENT_STREAK, streak);
        return PageNavigator.FORWARD_PAGE_GAME_FRUITS;
    }
}