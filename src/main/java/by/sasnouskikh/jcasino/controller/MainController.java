package by.sasnouskikh.jcasino.controller;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.CommandFactory;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

@WebServlet(name = "MainController", urlPatterns = {"/controller"})
public class MainController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PageNavigator navigator;
        String        query;
        String        responseType;
        HttpSession   session = request.getSession();
        Command       command = CommandFactory.defineCommand(request);
        navigator = command.execute(request);
        if (navigator != null) {
            query = navigator.getQuery();
            if (PREV_QUERY.equals(query)) {
                query = QueryManager.takePreviousQuery(request);
            }
            responseType = navigator.getResponseType();
            switch (responseType) {
                case FORWARD:
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(query);
                    dispatcher.forward(request, response);
                    break;
                case REDIRECT:
                    response.sendRedirect(request.getContextPath() + query);
                    break;
                default:
                    query = GOTO_ERROR_500;
                    String errorMessage = (String) request.getAttribute(ATTR_ERROR_MESSAGE);
                    if (errorMessage == null) {
                        errorMessage = EMPTY_STRING;
                    } else {
                        errorMessage = errorMessage.trim() + NEW_LINE_SEPARATOR;
                    }
                    session.setAttribute(ATTR_ERROR_MESSAGE,
                                         errorMessage +
                                         "MainController error. No such responseType: " +
                                         responseType);
                    response.sendRedirect(request.getContextPath() + query);
            }
        } else {
            query = GOTO_INDEX;
            response.sendRedirect(request.getContextPath() + query);
        }
    }
}