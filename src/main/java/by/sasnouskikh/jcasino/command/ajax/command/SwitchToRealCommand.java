package by.sasnouskikh.jcasino.command.ajax.command;

import by.sasnouskikh.jcasino.command.ajax.AjaxCommand;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.PlayerService;
import by.sasnouskikh.jcasino.service.StreakService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides switch to Real money play mode operation of slot-machine.
 * Is suitable to use with {@link by.sasnouskikh.jcasino.controller.AjaxController}.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AjaxCommand
 * @see by.sasnouskikh.jcasino.command.ajax.AjaxCommandFactory
 */
public class SwitchToRealCommand implements AjaxCommand {
    /**
     * Executes switch to Real money play mode operation, puts processed data and messages into {@link HashMap}
     * responseMap and returns it.
     *
     * @param request request from client to get parameters to work with
     * @return {@link HashMap} with response parameters
     */
    @Override
    public Map<String, Object> execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession         session        = request.getSession();
        String              locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager      messageManager = MessageManager.getMessageManager(locale);
        Map<String, Object> responseMap    = new HashMap<>();

        Player     player   = (Player) session.getAttribute(ATTR_PLAYER);
        int        playerId = player.getId();
        BigDecimal money    = BigDecimal.ZERO;
        Streak     streak;
        boolean    success  = true;

        //define data for real money play
        try (PlayerService playerService = new PlayerService();
             StreakService streakService = new StreakService()) {
            if (playerService.updateAccountInfo(player)) {
                money = player.getAccount().getBalance();
            } else {
                success = false;
            }
            streak = streakService.generateStreak(playerId);
            if (streak == null) {
                success = false;
            }
        }

        if (success) {
            responseMap.put(AJAX_MONEY, money.toString());
            responseMap.put(AJAX_STREAK_INFO, streak.getRollMD5());
            session.setAttribute(ATTR_CURRENT_STREAK, streak);
            session.removeAttribute(ATTR_DEMO_PLAY);
        } else {
            responseMap.put(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
        }
        return responseMap;
    }
}