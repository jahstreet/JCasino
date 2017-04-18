package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.News;
import by.sasnouskikh.jcasino.logic.LogicException;
import by.sasnouskikh.jcasino.logic.NewsLogic;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;
import org.apache.commons.fileupload.FileItem;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides editing news for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class EditNewsCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(EditNewsCommand.class);

    /**
     * <p>Provides editing news for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_MANAGE_NEWS}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully updates {@link ConfigConstant#CONTEXT_NEWSLIST} attribute of
     * {@link javax.servlet.ServletContext#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#REDIRECT_GOTO_MANAGE_NEWS}, else adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_MANAGE_NEWS}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see FormValidator
     * @see NewsLogic#editNews(int, String, FileItem, String, Admin, String)
     * @see NewsLogic#takeNewsList()
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();

        boolean valid = true;
        Admin   admin = (Admin) session.getAttribute(ATTR_ADMIN);

        String newsIdString = request.getParameter(PARAM_ID);
        String header       = request.getParameter(PARAM_HEADER);
        String text         = request.getParameter(PARAM_TEXT);
        int    newsId       = 0;

        if (FormValidator.validateId(newsIdString)) {
            newsId = Integer.parseInt(newsIdString);
        } else {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_JSP)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (!FormValidator.validateNewsHeader(header)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NEWS_HEADER)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (!FormValidator.validateNewsText(text)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NEWS_TEXT)).append(MESSAGE_SEPARATOR);
            valid = false;
        }

        if (valid && newsId > 0) {
            try {
                if (NewsLogic.editNews(newsId, header, null, text, admin, null) != null) {
                    List<News> newsList = NewsLogic.takeNewsList();
                    if (newsList != null) {
                        request.getServletContext().setAttribute(CONTEXT_NEWSLIST, newsList);
                        return PageNavigator.REDIRECT_GOTO_MANAGE_NEWS;
                    }
                }
                errorMessage.append(messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
            } catch (LogicException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_FILE_EXTENSION));
            }
        }
        request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
        return PageNavigator.FORWARD_PAGE_MANAGE_NEWS;
    }
}