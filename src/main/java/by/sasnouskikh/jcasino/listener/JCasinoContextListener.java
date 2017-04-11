package by.sasnouskikh.jcasino.listener;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.News;
import by.sasnouskikh.jcasino.logic.NewsLogic;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.CONTEXT_NEWSLIST;

@WebListener
public class JCasinoContextListener implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger(JCasinoContextListener.class);

    private ConnectionPool pool;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            pool = ConnectionPool.getInstance();
            pool.initPool();
            LOGGER.log(Level.INFO, "ConnectionPool was initialized.");
        } catch (ConnectionPoolException e) {
            LOGGER.log(Level.FATAL, e);
            throw new RuntimeException(e);
        }
        List<News> news = NewsLogic.takeNewsList();
        event.getServletContext().setAttribute(CONTEXT_NEWSLIST, news);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        pool.destroyPool();
        LOGGER.log(Level.INFO, "ConnectionPool was destroyed.");
        LogManager.shutdown(); //to prevent exceptions and warnings on server shutdown
    }
}