package by.sasnouskikh.jcasino.manager;

import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Enumeration;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class QueryManager {

    private static final Logger LOGGER = LogManager.getLogger(QueryManager.class);

    /**
     * Don't let anyone instantiate this class.
     */
    private QueryManager() {
    }

    /**
     * Save query to session as REV_QUERY attribute, so it could help to find previous query for
     * commands which should be redirected to the same page after processing some actions
     *
     * @param request
     */
    public static void saveQueryToSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String      query   = logQuery(request);
        session.setAttribute(ATTR_PREV_QUERY, query);
    }

    public static void saveQueryToSession(HttpSession session, String query) {
        session.setAttribute(ATTR_PREV_QUERY, query);
    }

    /**
     * Return attribute PREV_QUERY from session which contains query of previous request.
     * Check if previous query exists and returns query with index page in such case.
     *
     * @param request
     * @return query value of previous request or query with index page if query doesn't exist
     */
    public static String takePreviousQuery(HttpServletRequest request) {
        String prevQuery = (String) request.getSession().getAttribute(ATTR_PREV_QUERY);
        if (prevQuery == null) {
            prevQuery = PAGE_INDEX;
        }
        return prevQuery;
    }

    /**
     * Makes logging of query of request and returns it for following using
     *
     * @param request
     * @return value request query
     */
    public static String logQuery(HttpServletRequest request) {
        String      query   = createQueryString(request);
        HttpSession session = request.getSession();
        JCasinoUser user    = (JCasinoUser) session.getAttribute(ATTR_USER);
        return buildLog(query, user);
    }

    public static String logMultipartQuery(HttpServletRequest request, String query) {
        HttpSession session = request.getSession();
        JCasinoUser user    = (JCasinoUser) session.getAttribute(ATTR_USER);
        return buildLog(query, user);
    }

    private static String buildLog(String query, JCasinoUser user) {
        if (user == null) {
            LOGGER.log(Level.INFO, "guest called " + query);
        } else {
            String login = user.getEmail();
            String role  = user.getRole().getRole();
            int    id    = user.getId();
            LOGGER.log(Level.INFO, role + " " + login + " (id=" + id + ") called " + query);
        }
        return query;
    }

    /**
     * creates query from request
     *
     * @param request
     * @return request query
     */
    private static String createQueryString(HttpServletRequest request) {
        String       uri   = request.getRequestURI();
        StringBuffer query = new StringBuffer();

        Enumeration<String> params = request.getParameterNames();

        String key;
        String value;
        while (params.hasMoreElements()) {
            key = params.nextElement();
            value = request.getParameter(key);
            query = query.append(PARAMETER_SEPARATOR).append(key).append(VALUE_SEPARATOR).append(value);
        }

        String result;
        if (query.length() == 0) {
            result = uri;
        } else {
            query.deleteCharAt(0);
            result = uri + QUERY_START_SEPARATOR + query.toString();
        }
        return result;
    }

    /**
     * creates query from multipart request
     *
     * @param request
     * @return request query
     */
    private static String createMultipartQueryString(HttpServletRequest request) {
        String uri = request.getRequestURI();
        System.out.println(uri);
        try {
            for (Part part : request.getParts()) {
                System.out.println(part.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return uri;
    }
}