package by.sasnouskikh.jcasino.command.impl.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.logic.StreakLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import by.sasnouskikh.jcasino.validator.FormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class ShowStreaksCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);
        PageNavigator  navigator;

        boolean valid = true;

        String  month       = request.getParameter(PARAM_MONTH);
        boolean sortByTotal = request.getParameter(PARAM_SORT_BY_TOTAL) != null;

        if (month == null || month.trim().isEmpty() || FormValidator.validateDateMonth(month)) {
            request.setAttribute(ATTR_MONTH_INPUT, month);
        } else {
            valid = false;
        }

        if (valid) {
            List<Streak> streaksList = StreakLogic.takeStreakList(month, sortByTotal);
            request.setAttribute(ATTR_STREAKS, streaksList);
            navigator = PageNavigator.FORWARD_PAGE_MANAGE_STREAKS;
        } else {
            QueryManager.logQuery(request);
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_INVALID_JSP));
            navigator = PageNavigator.FORWARD_PREV_QUERY;
        }
        return navigator;
    }
}