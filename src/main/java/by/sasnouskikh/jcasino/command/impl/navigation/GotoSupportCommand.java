package by.sasnouskikh.jcasino.command.impl.navigation;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.logic.QuestionLogic;
import by.sasnouskikh.jcasino.manager.ConfigConstant;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides navigating to support page.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class GotoSupportCommand implements Command {

    /**
     * Takes list of {@link by.sasnouskikh.jcasino.entity.bean.Question} of definite {@link Player} at Logic layer
     * if {@link ConfigConstant#ATTR_PLAYER} attribute of {@link HttpSession#getAttribute(String)} is not null
     * and sets {@link ConfigConstant#ATTR_QUESTION_LIST} and {@link ConfigConstant#ATTR_EMAIL_INPUT} attributes of
     * {@link HttpServletRequest#setAttribute(String, Object)}. Navigates to {@link PageNavigator#FORWARD_PAGE_SUPPORT}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see MessageManager
     * @see QuestionLogic#takePlayerQuestions(int)
     */
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