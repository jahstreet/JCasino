package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.News;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.service.AdminService;
import by.sasnouskikh.jcasino.service.NewsService;
import by.sasnouskikh.jcasino.service.PlayerService;
import by.sasnouskikh.jcasino.service.ServiceException;
import by.sasnouskikh.jcasino.validator.FormValidator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides multipart/form-data request processing.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class MultipartFormCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(MultipartFormCommand.class);

    private static final int    MEMORY_THRESHOLD  = 1024 * 1024 * 3;  //  3 MB
    private static final int    MAX_FILE_SIZE     = 1024 * 1024 * 16; // 16 MB
    private static final int    MAX_REQUEST_SIZE  = 1024 * 1024 * 20; // 20 MB
    private static final String TEMP_REPOSITORY   = "java.io.tmpdir";
    private static final String MULTIPART_QUERY   = "multipart_form_command";
    private static final String UPLOAD_PASSPORT   = "upload_passport";
    private static final String UPLOAD_NEWS_LOG   = "upload_news";
    private static final String EDIT_NEWS_LOG     = "edit_news";
    private static final String UTF_8_ENCODING    = "UTF-8";
    private static final String ADD_NEWS_COMMAND  = "add_news";
    private static final String EDIT_NEWS_COMMAND = "edit_news";

    /**
     * <p>Provides multipart/form-data request processing.
     * <p>Takes input parameters from {@link HttpServletRequest} due to org.apache.commons.fileupload library
     * documentation, validates them and passes further to Logic layer.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see NewsService#addNews(String, String, FileItem, String, Admin, String)
     * @see NewsService#editNews(int, String, FileItem, String, Admin, String)
     * @see PlayerService#uploadPassportScan(Player, FileItem, String)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logMultipartQuery(request, MULTIPART_QUERY);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);

        String uploadDir = request.getServletContext().getInitParameter(INIT_PARAM_UPLOADS);

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty(TEMP_REPOSITORY)));
        ServletFileUpload uploader = new ServletFileUpload(factory);
        uploader.setFileSizeMax(MAX_FILE_SIZE);
        uploader.setSizeMax(MAX_REQUEST_SIZE);

        //upload passport scan
        Player player = (Player) session.getAttribute(ATTR_PLAYER);

        //upload/edit news
        Admin admin = (Admin) session.getAttribute(ATTR_ADMIN);

        try {
            List<FileItem> formItems = uploader.parseRequest(request);
            if (formItems != null && !formItems.isEmpty()) {
                if (player != null) {
                    QueryManager.logMultipartQuery(request, UPLOAD_PASSPORT);
                    try (PlayerService playerService = new PlayerService()) {
                        if (playerService.uploadPassportScan(formItems, player, uploadDir)) {
                            return PageNavigator.REDIRECT_GOTO_VERIFICATION;
                        }
                    }
                } else if (admin != null) {
                    String command = null;
                    News   news    = null;
                    for (FileItem item : formItems) {
                        if (PARAM_COMMAND.equals(item.getFieldName())) {
                            command = item.getString(UTF_8_ENCODING);
                            break;
                        }
                    }
                    if (ADD_NEWS_COMMAND.equals(command)) {
                        QueryManager.logMultipartQuery(request, UPLOAD_NEWS_LOG);
                        news = AdminService.addNews(formItems, admin, uploadDir);
                    } else if (EDIT_NEWS_COMMAND.equals(command)) {
                        QueryManager.logMultipartQuery(request, EDIT_NEWS_LOG);
                        news = AdminService.editNews(formItems, admin, uploadDir);
                    }
                    if (news != null) {
                        try (NewsService newsService = new NewsService()) {
                            request.getServletContext().setAttribute(CONTEXT_NEWSLIST, newsService.takeNewsList());
                        }
                        return PageNavigator.REDIRECT_GOTO_MANAGE_NEWS;
                    }
                }
            }
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_MULTIPART_UPLOAD_ERROR));
            return PageNavigator.FORWARD_PREV_QUERY;
        } catch (FileUploadException | ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_MULTIPART_UPLOAD_ERROR));
            return PageNavigator.FORWARD_PREV_QUERY;
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.ERROR, "UTF-8 request encoding not supported in multipart forms. " + e.getMessage());
            request.getSession().setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_MULTIPART_UPLOAD_ERROR));
            return PageNavigator.REDIRECT_GOTO_ERROR_500;
        }
    }
}