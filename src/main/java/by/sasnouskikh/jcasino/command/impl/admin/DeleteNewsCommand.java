package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.News;
import by.sasnouskikh.jcasino.logic.NewsLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class DeleteNewsCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.logQuery(request);
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
            return PageNavigator.FORWARD_PREV_QUERY;
        }
        List<News> newsList = NewsLogic.deleteNews(newsId);
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