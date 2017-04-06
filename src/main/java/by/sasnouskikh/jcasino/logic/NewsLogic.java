package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.impl.DAOFactory;
import by.sasnouskikh.jcasino.dao.impl.NewsDAOImpl;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.News;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class NewsLogic {
    private static final Logger LOGGER = LogManager.getLogger(NewsLogic.class);

    private NewsLogic() {
    }

    public static List<News> takeNewsList() {
        List<News> newsList = null;
        try (NewsDAOImpl newsDAO = DAOFactory.getNewsDAO()) {
            newsList = newsDAO.takeNews();
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return newsList;
    }

    public static News addNews(String header, String text, FileItem newsImage, Admin admin, String uploadDir) throws LogicException {
        header = StringUtils.capitalize(header);
        text = StringUtils.capitalize(text);
        int adminId = admin.getId();
        try (NewsDAOImpl newsDAO = DAOFactory.getNewsDAO()) {
            newsDAO.beginTransaction();
            int newsId = newsDAO.insertNews(adminId, header, text);
            if (newsId != 0) {
                News news = newsDAO.takeNews(newsId);
                if (news != null && updateNewsImage(newsId, newsImage, uploadDir)) {
                    newsDAO.commit();
                    return news;
                }
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return null;
    }

    public static News editNews(int newsId, String header, FileItem newsImage, String text, Admin admin, String uploadDir) throws LogicException {
        News news = null;
        header = StringUtils.capitalize(header);
        text = StringUtils.capitalize(text);
        int     adminId       = admin.getId();
        boolean changedText   = true;
        boolean changedHeader = true;
        boolean changedImage  = true;
        try (NewsDAOImpl newsDAO = DAOFactory.getNewsDAO()) {
            if (header != null) {
                changedText = newsDAO.changeNewsHeader(newsId, header, adminId);
            }
            if (text != null) {
                changedHeader = newsDAO.changeNewsText(newsId, text, adminId);
            }
            if (newsImage != null) {
                changedImage = updateNewsImage(newsId, newsImage, uploadDir);
            }
            if (changedText && changedHeader && changedImage) {
                news = newsDAO.takeNews(newsId);
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return news;
    }

    private static boolean updateNewsImage(int newsId, FileItem newsImage, String uploadDir) throws LogicException {
        String newsDirPath = uploadDir + File.separator + NEWS_UPLOAD_DIR;
        File   newsDir     = new File(newsDirPath);
        if (!newsDir.exists()) {
            newsDir.mkdirs();
        }
        String fileSourceName = newsImage.getName();
        String fileExt        = FilenameUtils.getExtension(fileSourceName);
        if (fileExt == null || fileExt.isEmpty()
            || Arrays.binarySearch(AVAILABLE_NEWS_IMAGE_EXT, fileExt.toLowerCase()) == -1) {
            throw new LogicException("Invalid file extension.");
        }
        fileExt = fileExt.toLowerCase();
        String fileName  = NEWS_IMAGE_NAME_PATTERN + newsId + DOT + fileExt;
        String storePath = newsDirPath + File.separator + fileName;
        File   storeFile = new File(storePath);
        try {
            newsImage.write(storeFile);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static List<News> deleteNews(int id) {
        List<News> newsList = null;
        try (NewsDAOImpl newsDAO = DAOFactory.getNewsDAO()) {
            if (newsDAO.deleteNews(id)) {
                newsList = newsDAO.takeNews();
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return newsList;
    }
}