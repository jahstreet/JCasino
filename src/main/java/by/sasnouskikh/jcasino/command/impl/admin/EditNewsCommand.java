package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.News;
import by.sasnouskikh.jcasino.logic.LogicException;
import by.sasnouskikh.jcasino.logic.NewsLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class EditNewsCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(EditNewsCommand.class);

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
        int    newsId;

        if (FormValidator.validateId(newsIdString)) {
            newsId = Integer.parseInt(newsIdString);
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        if (!FormValidator.validateNewsHeader(header)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NEWS_HEADER)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (!FormValidator.validateNewsText(text)) {
            errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_NEWS_TEXT)).append(NEW_LINE_SEPARATOR);
            valid = false;
        }

        if (valid) {
            try {
                if (NewsLogic.editNews(newsId, header, null, text, admin, null) != null) {
                    List<News> newsList = NewsLogic.takeNewsList();
                    if (newsList != null) {
                        request.getServletContext().setAttribute(CONTEXT_NEWSLIST, newsList);
                    }
                    return PageNavigator.REDIRECT_GOTO_MANAGE_NEWS;
                } else {
                    request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_EDIT_NEWS_ERROR));
                    return PageNavigator.FORWARD_PAGE_MANAGE_NEWS;
                }
            } catch (LogicException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
            }
        }
        request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
        return PageNavigator.FORWARD_PAGE_MANAGE_NEWS;
    }
}