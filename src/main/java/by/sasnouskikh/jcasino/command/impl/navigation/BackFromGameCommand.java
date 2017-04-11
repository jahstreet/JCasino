package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_CURRENT_STREAK;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_DEMO_PLAY;

public class BackFromGameCommand implements Command {
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        HttpSession session       = request.getSession();
        boolean     demo          = session.getAttribute(ATTR_DEMO_PLAY) != null;
        Streak      currentStreak = (Streak) session.getAttribute(ATTR_CURRENT_STREAK);

        if (demo) {
            session.setAttribute(ATTR_DEMO_PLAY, null);
            if (currentStreak != null) {
                session.setAttribute(ATTR_CURRENT_STREAK, null);
            }
        }
        return PageNavigator.REDIRECT_GOTO_INDEX;
    }
}