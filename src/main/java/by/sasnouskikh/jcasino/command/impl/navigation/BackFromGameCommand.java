package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.StreakService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_CURRENT_STREAK;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_DEMO_PLAY;

/**
 * The class provides navigating back from game page.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class BackFromGameCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(BackFromGameCommand.class);

    /**
     * Sets {@link by.sasnouskikh.jcasino.manager.ConfigConstant#ATTR_CURRENT_STREAK} and
     * {@link by.sasnouskikh.jcasino.manager.ConfigConstant#ATTR_DEMO_PLAY} attributes of
     * {@link javax.servlet.http.HttpSession#setAttribute(String, Object)} to null if
     * current game-mode is 'Demo' and navigates to {@link PageNavigator#REDIRECT_GOTO_INDEX}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        HttpSession session = request.getSession();
        if (session.getAttribute(ATTR_DEMO_PLAY) != null) {
            session.removeAttribute(ATTR_DEMO_PLAY);
            session.removeAttribute(ATTR_CURRENT_STREAK);
        } else {
            Streak currentStreak = (Streak) session.getAttribute(ATTR_CURRENT_STREAK);
            if (StreakService.isComplete(currentStreak)) {
                try (StreakService streakService = new StreakService()) {
                    if (streakService.updateStreak(currentStreak)) {
                        session.removeAttribute(ATTR_CURRENT_STREAK);
                    } else {
                        LOGGER.log(Level.ERROR, "Streak info wasn't saved to database: " + currentStreak);
                    }
                }
            }
        }
        return PageNavigator.REDIRECT_GOTO_INDEX;
    }
}