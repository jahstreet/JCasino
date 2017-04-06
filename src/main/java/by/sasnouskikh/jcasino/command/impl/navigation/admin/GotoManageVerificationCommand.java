package by.sasnouskikh.jcasino.command.impl.navigation.admin;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.logic.AdminLogic;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_VERIFICATION_LIST;

public class GotoManageVerificationCommand implements Command {

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