package by.sasnouskikh.jcasino.controller;

import by.sasnouskikh.jcasino.command.ajax.AjaxCommand;
import by.sasnouskikh.jcasino.command.ajax.AjaxCommandFactory;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_ERROR_MESSAGE;

/**
 * The class provides controller for AJAX requests at MVC pattern of application.
 *
 * @author Sasnouskikh Aliaksandr
 * @see HttpServlet
 * @see WebServlet
 */
@WebServlet(name = "AjaxController", urlPatterns = {"/ajax"})
public class AjaxController extends HttpServlet {

    /**
     * Processes request sent by POST method.
     *
     * @param request  request from client to get parameters to work with
     * @param response response to client with parameters to work with on client side
     * @throws IOException      if an input or output error is detected when the servlet handles the request
     * @throws ServletException if the request could not be handled
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
     * @throws IOException      if an input or output error is detected when the servlet handles the request
     * @throws ServletException if the request could not be handled
     * @see HttpServletRequest
     * @see HttpServletResponse
     * @see #processRequest(HttpServletRequest, HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * <p>Processes requests.
     * <p>Defines AJAX command name by {@link HttpServletRequest#getAttribute(String)} and directs request to
     * {@link AjaxCommand} corresponding to it.
     * <p>Writes {@link String} object in JSON format parsed from {@link java.util.HashMap} returned from
     * {@link AjaxCommand#execute(HttpServletRequest)} processing using {@link Gson#toJson(Object)} and writes it
     * to {@link HttpServletResponse}.
     * <p>If {@link AjaxCommand} wasn't defined puts
     * {@link by.sasnouskikh.jcasino.manager.ConfigConstant#ATTR_ERROR_MESSAGE} to responding {@link HashMap}.
     *
     * @param request  request from client to get parameters to work with
     * @param response response to client with parameters to work with on client side
     * @throws IOException      if an input or output error is detected when the servlet handles the request
     * @throws ServletException if the request could not be handled
     * @see AjaxCommandFactory#defineCommand(HttpServletRequest)
     * @see #write(HttpServletResponse, Map)
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> map     = new HashMap<>();
        AjaxCommand         command = AjaxCommandFactory.defineCommand(request);
        if (command != null) {
            map = command.execute(request);
        } else {
            //TODO message manager to message
            map.put(ATTR_ERROR_MESSAGE, "Unknown AJAX command.");
        }
        write(response, map);
    }

    /**
     * Service method to convert {@link HashMap} object to {@link String} in JSON format and write it to
     * {@link HttpServletResponse}.
     *
     * @param response response to client with parameters to work with on client side
     * @param map      {@link HashMap} to convert and write
     * @throws IOException if an input or output exception occurred while getting response writer
     */
    private void write(HttpServletResponse response, Map map) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(map));
    }
}