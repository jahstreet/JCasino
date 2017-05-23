package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.News;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.service.NewsService;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides deleting news for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class DeleteNewsCommand implements Command {

    /**
     * <p>Provides deleting news for admin.
     * <p>Takes input parameters from {@link HttpServletRequest#getParameter(String)} and validates them.
     * <p>If any parameter is invalid adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PREV_QUERY}.
     * <p>If all the parameters are valid converts them to relevant data types and passes converted parameters further
     * to the Logic layer.
     * <p>If Logic operation passed successfully updates {@link ConfigConstant#CONTEXT_NEWSLIST} attribute of
     * {@link javax.servlet.ServletContext#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#REDIRECT_GOTO_MANAGE_NEWS}, else adds {@link ConfigConstant#ATTR_ERROR_MESSAGE} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_MANAGE_NEWS}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for {@link
     * by.sasnouskikh.jcasino.controller.MainController})
     * @see MessageManager
     * @see FormValidator
     * @see NewsService#deleteNews(int)
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String newsIdString = request.getParameter(PARAM_ID);
        int    newsId;

        if (FormValidator.validateId(newsIdString)) {
            newsId = Integer.parseInt(newsIdString);
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PAGE_MANAGE_NEWS;
        }

        List<News> newsList;
        try (NewsService newsService = new NewsService()) {
            newsList = newsService.deleteNews(newsId);
        }
        if (newsList != null) {
            request.getServletContext().setAttribute(CONTEXT_NEWSLIST, newsList);
            navigator = PageNavigator.REDIRECT_GOTO_MANAGE_NEWS;
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DATABASE_ACCESS_ERROR));
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_NEWS;
        }
        return navigator;
    }
}