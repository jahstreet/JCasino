package by.sasnouskikh.jcasino.command.impl.navigation.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.logic.AdminLogic;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_VERIFICATION_LIST;

/**
 * The class provides navigating to manage verification page for admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class GotoManageVerificationCommand implements Command {

    /**
     * Saves current query to session, takes list of {@link PlayerVerification} ready for verification from Logic layer,
     * sets {@link by.sasnouskikh.jcasino.manager.ConfigConstant#ATTR_VERIFICATION_LIST} attribute to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to
     * {@link PageNavigator#FORWARD_PAGE_MANAGE_VERIFICATION}.
     *
     * @param request request from client to get parameters to work with
     * @return {@link PageNavigator} with response parameters (contains 'query' and 'response type' data for
     * {@link by.sasnouskikh.jcasino.controller.MainController})
     * @see QueryManager
     * @see AdminLogic#takeReadyForVerification()
     */
    @Override
    public PageNavigator execute(HttpServletRequest request) {
        QueryManager.saveQueryToSession(request);
        List<PlayerVerification> verificationList = AdminLogic.takeReadyForVerification();
        if (verificationList != null) {
            request.setAttribute(ATTR_VERIFICATION_LIST, verificationList);
        }
        return PageNavigator.FORWARD_PAGE_MANAGE_VERIFICATION;
    }
}