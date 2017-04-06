package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.logic.QuestionLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ShowAnswersCommand implements Command {
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        boolean valid = true;
        Admin   admin = (Admin) session.getAttribute(ATTR_ADMIN);

        String  month            = request.getParameter(PARAM_MONTH);
        String  topic            = request.getParameter(PARAM_TOPIC);
        boolean showMy           = request.getParameter(PARAM_SHOW_MY) != null;
        boolean satisfactionSort = request.getParameter(PARAM_SATISFACTION) != null;

        if (month == null || month.isEmpty() || FormValidator.validateDateMonth(month)) {
            request.setAttribute(ATTR_MONTH_INPUT, month);
        } else {
            valid = false;
        }

        if (topic != null && !topic.trim().isEmpty() && !FormValidator.validateTopic(topic)) {
            valid = false;
        }

        if (valid) {
            QueryManager.saveQueryToSession(request);
            List<Question> questionList = QuestionLogic.takeAnswered(topic, month, showMy, admin, satisfactionSort);
            request.setAttribute(ATTR_QUESTION_LIST, questionList);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_SUPPORT;
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}