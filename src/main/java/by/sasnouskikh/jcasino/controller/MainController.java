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
import java.util.HashMap;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides controller for client requests at MVC pattern of application.
 *
 * @author Sasnouskikh Aliaksandr
 * @see HttpServlet
 * @see WebServlet
 */
@WebServlet(name = "MainController", urlPatterns = {"/controller"})
public class MainController extends HttpServlet {

    /**
     * Processes request sent by POST method.
     *
     * @param request  request from client to get parameters to work with
     * @param response response to client with parameters to work with on client side
     * @see HttpServletRequest
     * @see HttpServletResponse
     * @see #processRequest(HttpServletRequest, HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processes request sent by GET method.
     *
     * @param request  request from client to get parameters to work with
     * @param response response to client with parameters to work with on client side
     * @see HttpServletRequest
     * @see HttpServletResponse
     * @see #processRequest(HttpServletRequest, HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * <p>Processes requests.
     * <p>Defines command name by {@link HttpServletRequest#getAttribute(String)} and directs request to
     * {@link Command} corresponding to it.
     * <p>Navigates to definite query corresponding to returned from processed
     * {@link Command#execute(HttpServletRequest)} {@link PageNavigator} object.
     * <p>If command execution didn't return valid {@link PageNavigator} object redirects to index page.
     * <p>If error occurs writes {@link by.sasnouskikh.jcasino.manager.ConfigConstant#ATTR_ERROR_MESSAGE} to
     * {@link HttpServletRequest#setAttribute(String, Object)} and navigates to error page.
     * {@link by.sasnouskikh.jcasino.manager.ConfigConstant#ATTR_ERROR_MESSAGE} to responding {@link HashMap}.
     *
     * @param request  request from client to get parameters to work with
     * @param response response to client with parameters to work with on client side
     * @see CommandFactory#defineCommand(HttpServletRequest)
     * @see QueryManager#takePreviousQuery(HttpServletRequest)
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                        errorMessage = errorMessage.trim() + MESSAGE_SEPARATOR;
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