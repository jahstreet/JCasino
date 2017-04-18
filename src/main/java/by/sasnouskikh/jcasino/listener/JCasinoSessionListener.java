package by.sasnouskikh.jcasino.listener;

import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.logic.StreakLogic;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_CURRENT_STREAK;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_PLAYER;

@WebListener
public class JCasinoSessionListener implements HttpSessionListener {
    private static final Logger LOGGER = LogManager.getLogger(JCasinoSessionListener.class);

    /**
     * Receives notification that a session has been created.
     *
     * @param event the HttpSessionEvent containing the session
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {

    }

    /**
     * Receives notification that a session is about to be invalidated.
     *
     * @param event the HttpSessionEvent containing the session
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session       = event.getSession();
        Player      player        = (Player) session.getAttribute(ATTR_PLAYER);
        Streak      currentStreak = (Streak) session.getAttribute(ATTR_CURRENT_STREAK);
        if (player != null && currentStreak != null) {
            StreakLogic.completeStreak(currentStreak);
            if (!StreakLogic.updateStreak(currentStreak)) {
                LOGGER.log(Level.ERROR, "Streak wasn't saved to database: \n" + currentStreak);
            }
        }
    }
}