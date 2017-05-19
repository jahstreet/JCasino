package by.sasnouskikh.jcasino.listener;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.News;
import by.sasnouskikh.jcasino.service.NewsService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.CONTEXT_NEWSLIST;

/**
 * The class provides listener of servlet container initialization and destruction.
 *
 * @author Sasnouskikh Aliaksandr
 * @see ServletContextListener
 * @see WebListener
 */
@WebListener
public class JCasinoContextListener implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger(JCasinoContextListener.class);

    /**
     * Database property-file path relative to Maven directory 'resources' servletContext initParam key.
     */
    private static final String DB_PROPERTIES = "db.props";
    /**
     * Connection pool instance to manage and work with.
     */
    private ConnectionPool pool;

    /**
     * <p>Receives notification that the web application initialization process is starting. <p>All
     * ServletContextListeners are notified of context initialization before any filters or servlets in the web
     * application are initialized.
     * <p>Inits {@link ConnectionPool} and puts {@link List} of {@link News} objects to
     * {@link javax.servlet.ServletContext#setAttribute(String, Object)}.
     *
     * @param event the ServletContextEvent containing the ServletContext that is being initialized
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        String databaseProps = event.getServletContext().getInitParameter(DB_PROPERTIES);
        try {
            pool = ConnectionPool.getInstance();
            int createdConnectionsNumber = pool.initPool(databaseProps);
            LOGGER.log(Level.INFO, "ConnectionPool was initialized with " + createdConnectionsNumber + " connections.");
        } catch (ConnectionPoolException e) {
            LOGGER.log(Level.FATAL, e);
            throw new RuntimeException(e);
        }
        List<News> news;
        try (NewsService newsService = new NewsService()) {
            news = newsService.takeNewsList();
        }
        event.getServletContext().setAttribute(CONTEXT_NEWSLIST, news);
    }

    /**
     * <p>Receives notification that the ServletContext is about to be
     * shut down.
     * <p>All servlets and filters will have been destroyed before any
     * ServletContextListeners are notified of context
     * destruction.
     * <p>Destroys {@link ConnectionPool} and calls {@link LogManager#shutdown()}.
     *
     * @param event the ServletContextEvent containing the ServletContext that is being destroyed
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        int closedConnectionsNumber = pool.destroyPool();
        LOGGER.log(Level.INFO, "ConnectionPool was destroyed. " + closedConnectionsNumber + " connections was closed.");
        LogManager.shutdown(); //to prevent exceptions and warnings on server shutdown
    }
}