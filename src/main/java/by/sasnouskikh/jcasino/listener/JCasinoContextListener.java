package by.sasnouskikh.jcasino.listener;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.impl.DAOFactory;
import by.sasnouskikh.jcasino.dao.impl.NewsDAOImpl;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.News;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.CONTEXT_NEWSLIST;

@WebListener
public class JCasinoContextListener implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger(JCasinoContextListener.class);

    private ConnectionPool pool;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            pool = ConnectionPool.getInstance();
            pool.initPool();
            LOGGER.log(Level.INFO, "ConnectionPool was initialized.");
        } catch (ConnectionPoolException e) {
            LOGGER.log(Level.FATAL, e);
            throw new RuntimeException(e);
        }
        try (NewsDAOImpl newsDAO = DAOFactory.getNewsDAO()) {
            ArrayList<News> news = newsDAO.takeNews();
            servletContextEvent.getServletContext().setAttribute(CONTEXT_NEWSLIST, news);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.FATAL, e);
            throw new RuntimeException();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        pool.destroyPool();
        LOGGER.log(Level.INFO, "ConnectionPool was destroyed.");
        LogManager.shutdown(); //to prevent exceptions and warnings on server shutdown
    }
}