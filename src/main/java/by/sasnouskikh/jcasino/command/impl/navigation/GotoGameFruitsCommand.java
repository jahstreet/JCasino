package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_PLAYER;

public class GotoGameFruitsCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        HttpSession session = request.getSession();
        Player      player  = (Player) session.getAttribute(ATTR_PLAYER);
        BigDecimal  money   = BigDecimal.ZERO;
        session.setAttribute("demo_play", null);
        if (player != null) {
            PlayerLogic.updateAccountInfo(player);
            if (player.getAccount() != null) {
                BigDecimal balance = player.getAccount().getBalance();
                if (balance != null) {
                    money = balance;
                }
            }
        } else {
            money = BigDecimal.valueOf(50.00);
            session.setAttribute("demo_play", "demo_play");
        }
        request.setAttribute("money_input", money.toPlainString());
        return PageNavigator.FORWARD_PAGE_GAME_FRUITS;
    }
}