package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.NewsDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.News;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides Service layer actions with news.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class NewsService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger(NewsService.class);

    /**
     * Default instance constructor.
     */
    public NewsService() {
    }

    /**
     * Constructs instance using definite {@link DAOHelper} object.
     */
    public NewsService(DAOHelper daoHelper) {
        super(daoHelper);
    }

    /**
     * Calls DAO layer to take {@link List} collection of all {@link News} objects.
     *
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see NewsDAO#takeNews()
     */
    public List<News> takeNewsList() {
        List<News> newsList = null;
        NewsDAO    newsDAO  = daoHelper.getNewsDAO();
        try {
            newsList = newsDAO.takeNews();
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return newsList;
    }

    /**
     * Calls DAO layer to add new news and uploads news image.
     *
     * @param header    news header
     * @param text      news text
     * @param newsImage news {@link FileItem} image
     * @param admin     admin who adds news
     * @param uploadDir path to directory 'uploads'
     * @return added {@link News} object
     * @throws ServiceException if image file has unsupported file extension
     * @see DAOHelper
     * @see NewsDAO#insertNews(int, String, String, String)
     * @see NewsDAO#takeNews(int)
     * @see #updateNewsImage(int, FileItem, String)
     */
    public News addNews(String header, String text, FileItem newsImage, String locale, Admin admin, String uploadDir) throws ServiceException {
        int     adminId = admin.getId();
        NewsDAO newsDAO = daoHelper.getNewsDAO();
        locale = locale != null && !locale.trim().isEmpty() ?
                 locale.trim().toLowerCase() :
                 News.NewsLocale.RU.toString().toLowerCase();
        try {
            daoHelper.beginTransaction();
            int newsId = newsDAO.insertNews(adminId, header, text, locale);
            if (newsId != 0) {
                News news = newsDAO.takeNews(newsId);
                if (news != null && updateNewsImage(newsId, newsImage, uploadDir)) {
                    daoHelper.commit();
                    return news;
                }
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return null;
    }

    /**
     * Calls DAO layer to edit news data and updates news image.
     *
     * @param newsId    editing news id
     * @param header    new news header
     * @param newsImage new news {@link FileItem} image
     * @param text      new news text
     * @param admin     admin who edits news
     * @param uploadDir path to directory 'uploads'
     * @return updated {@link News} object
     * @throws ServiceException if image file has unsupported file extension
     * @see DAOHelper
     * @see NewsDAO#changeNewsHeader(int, String, int)
     * @see NewsDAO#changeNewsText(int, String, int)
     * @see NewsDAO#takeNews(int)
     * @see #updateNewsImage(int, FileItem, String)
     */
    public News editNews(int newsId, String header, FileItem newsImage, String text, Admin admin, String uploadDir) throws ServiceException {
        News    news          = null;
        int     adminId       = admin.getId();
        boolean changedText   = true;
        boolean changedHeader = true;
        boolean changedImage  = true;
        NewsDAO newsDAO       = daoHelper.getNewsDAO();
        try {
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
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return news;
    }

    /**
     * Calls DAO layer to delete news.
     *
     * @param id id of deleting news
     * @return {@link List} collection of rest {@link News} objects
     * @see DAOHelper
     * @see NewsDAO#deleteNews(int)
     * @see NewsDAO#takeNews()
     */
    public List<News> deleteNews(int id) {
        List<News> newsList = null;
        NewsDAO    newsDAO  = daoHelper.getNewsDAO();
        try {
            if (newsDAO.deleteNews(id)) {
                newsList = newsDAO.takeNews();
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return newsList;
    }

    /**
     * Updates news image.
     *
     * @param newsId    news id whose image is updating
     * @param newsImage new news {@link FileItem} image
     * @param uploadDir path to directory 'uploads'
     * @return true if operation proceeded successfully
     * @throws ServiceException if image file has unsupported file extension
     * @see by.sasnouskikh.jcasino.manager.ConfigConstant#AVAILABLE_NEWS_IMAGE_EXT
     * @see FileItem#write(File)
     */
    private static boolean updateNewsImage(int newsId, FileItem newsImage, String uploadDir) throws ServiceException {
        String newsDirPath = uploadDir + File.separator + NEWS_UPLOAD_DIR;
        File   newsDir     = new File(newsDirPath);
        if (!newsDir.exists()) {
            newsDir.mkdirs();
        }
        String fileSourceName = newsImage.getName();
        String fileExt        = FilenameUtils.getExtension(fileSourceName);
        System.out.println(fileExt);
        if (fileExt == null || fileExt.isEmpty()
            || Arrays.binarySearch(AVAILABLE_NEWS_IMAGE_EXT, fileExt.toLowerCase()) == -1) {
            throw new ServiceException("Invalid file extension.");
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
}