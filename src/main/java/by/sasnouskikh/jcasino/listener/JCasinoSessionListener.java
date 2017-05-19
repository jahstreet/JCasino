package by.sasnouskikh.jcasino.listener;

import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.service.StreakService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_CURRENT_STREAK;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_PLAYER;

/**
 * The class provides listener of session initialization and destruction.
 *
 * @author Sasnouskikh Aliaksandr
 * @see HttpSessionListener
 * @see WebListener
 */
@WebListener
public class JCasinoSessionListener implements HttpSessionListener {
    private static final Logger LOGGER = LogManager.getLogger(JCasinoSessionListener.class);

    /**
     * <p>Receives notification that the web application initialization process is starting. <p>All
     * ServletContextListeners are notified of context initialization before any filters or servlets in the web
     * application are initialized.
     *
     * @param event the ServletContextEvent containing the ServletContext that is being initialized
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // No need to use it yet.
    }

    /**
     * <p>Receives notification that the ServletContext is about to be
     * shut down.
     * <p>All servlets and filters will have been destroyed before any
     * ServletContextListeners are notified of context
     * destruction.
     * <p>Completes current uncompleted {@link Streak} and writes it to database.
     *
     * @param event the ServletContextEvent containing the ServletContext that is being destroyed
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session       = event.getSession();
        Player      player        = (Player) session.getAttribute(ATTR_PLAYER);
        Streak      currentStreak = (Streak) session.getAttribute(ATTR_CURRENT_STREAK);
        if (player != null && currentStreak != null && currentStreak.getPlayerId() != 0) {
            StreakService.completeStreak(currentStreak);
            try (StreakService streakService = new StreakService()) {
                if (!streakService.updateStreak(currentStreak)) {
                    LOGGER.log(Level.ERROR, "Streak wasn't saved to database: \n" + currentStreak);
                }
            }
        }
    }
}