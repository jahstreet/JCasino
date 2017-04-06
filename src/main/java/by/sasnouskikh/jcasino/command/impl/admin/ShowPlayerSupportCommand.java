package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.logic.QuestionLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ShowPlayerSupportCommand implements Command {

    @SuppressWarnings("Duplicates")
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        String playerIdString = request.getParameter(PARAM_ID);
        int    playerId;

        if (FormValidator.validateId(playerIdString)) {
            playerId = Integer.parseInt(playerIdString);
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            return PageNavigator.FORWARD_PREV_QUERY;
        }

        List<Question> questions = QuestionLogic.takePlayerQuestions(playerId);
        if (questions != null) {
            QueryManager.saveQueryToSession(request);
            request.setAttribute(ATTR_QUESTION_LIST, questions);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_SUPPORT;
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, MESSAGE_PLAYER_NO_QUESTIONS);
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}