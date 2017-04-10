package by.sasnouskikh.jcasino.controller;

import by.sasnouskikh.jcasino.command.ajax.AjaxCommand;
import by.sasnouskikh.jcasino.command.ajax.AjaxCommandFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "AjaxController", urlPatterns = {"/ajax"})
public class AjaxController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AjaxController.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> map     = null;
        AjaxCommand         command = AjaxCommandFactory.defineCommand(request);
        if (command != null) {
            map = command.execute(request);
        } else {
            //TODO LOG
            System.out.println("ajax controller error: command == null");
        }
        write(response, map);
    }

    private void write(HttpServletResponse response, Map map) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(map));
    }
}