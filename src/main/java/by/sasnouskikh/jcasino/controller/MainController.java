package by.sasnouskikh.jcasino.controller;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.CommandFactory;

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
        String[]    pageParams;
        String      query;
        String      responseType;
        HttpSession session = request.getSession();
        Command     command = CommandFactory.defineCommand(request);
        pageParams = command.execute(request);
        if (pageParams != null && pageParams.length == 2) {
            query = pageParams[0];
            responseType = pageParams[1];
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
                        errorMessage = "";
                    } else {
                        errorMessage += "\n";
                    }
                    session.setAttribute(ATTR_ERROR_MESSAGE,
                                         errorMessage + "MainController error. No such responseType: " + responseType);
                    response.sendRedirect(request.getContextPath() + query);
            }
        } else {
            // TODO redirect to main (redirect to index in goto_main if user has no rights)
            query = GOTO_MAIN;
            // TODO log error
//            query = GOTO_ERROR_500;
//            String errorMessage = (String) session.getAttribute(ATTR_ERROR_MESSAGE);
//            if (errorMessage == null) {
//                errorMessage = "";
//            } else {
//                errorMessage += "\n";
//            }
//            session.setAttribute(ATTR_ERROR_MESSAGE,
//                    errorMessage + "MainController error. Command is null or it returned null page!");
            response.sendRedirect(request.getContextPath() + query);
        }
    }
}