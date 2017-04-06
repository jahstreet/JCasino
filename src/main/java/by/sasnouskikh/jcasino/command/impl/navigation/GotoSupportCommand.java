package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.logic.QuestionLogic;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class GotoSupportCommand implements Command {

    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        HttpSession session = request.getSession();

        Player player = (Player) session.getAttribute(ATTR_PLAYER);

        if (player != null) {
            List<Question> questionList = QuestionLogic.takePlayerQuestions(player.getId());
            request.setAttribute(ATTR_EMAIL_INPUT, player.getEmail());
            request.setAttribute(ATTR_QUESTION_LIST, questionList);
        }
        return PageNavigator.FORWARD_PAGE_SUPPORT;
    }
}