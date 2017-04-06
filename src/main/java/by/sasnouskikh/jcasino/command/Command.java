package by.sasnouskikh.jcasino.command;

import javax.servlet.http.HttpServletRequest;

public interface Command {

    PageNavigator execute(HttpServletRequest request);
}