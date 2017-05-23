package by.sasnouskikh.jcasino.manager;

import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides helper for work with queries.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class QueryManager {

    private static final Logger LOGGER = LogManager.getLogger(QueryManager.class);

    private static final String STUB = "********";

    /**
     * Outer forbidding to create this class instances.
     */
    private QueryManager() {
    }

    /**
     * Saves query to {@link HttpSession} as {@link ConfigConstant#ATTR_PREV_QUERY} attribute.
     *
     * @param request {@link HttpServletRequest} object
     * @see #logQuery(HttpServletRequest)
     */
    public static void saveQueryToSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String      query   = buildQueryString(request);
        session.setAttribute(ATTR_PREV_QUERY, query);
    }

    /**
     * Takes saved to {@link HttpSession} query.
     *
     * @param request {@link HttpServletRequest} object
     * @return query value of previous request or query with index page path if query doesn't exist
     */
    public static String takePreviousQuery(HttpServletRequest request) {
        String prevQuery = (String) request.getSession().getAttribute(ATTR_PREV_QUERY);
        if (prevQuery == null) {
            prevQuery = PAGE_INDEX;
        }
        return prevQuery;
    }

    /**
     * Logs query built from given request.
     *
     * @param request {@link HttpServletRequest} object
     * @see #buildQueryString(HttpServletRequest)
     * @see #buildLog(HttpServletRequest)
     */
    public static void logQuery(HttpServletRequest request) {
        LOGGER.log(Level.INFO, buildLog(request));
    }

    /**
     * Logs multipart query built from given request.
     *
     * @param request {@link HttpServletRequest} object
     * @param query   query to log
     * @see #buildQueryString(HttpServletRequest)
     * @see #buildLog(String, JCasinoUser)
     */
    public static void logMultipartQuery(HttpServletRequest request, String query) {
        HttpSession session = request.getSession();
        JCasinoUser user    = (JCasinoUser) session.getAttribute(ATTR_USER);
        LOGGER.log(Level.INFO, buildLog(query, user));
    }

    /**
     * Builds log due to query and user role.
     *
     * @param request {@link HttpServletRequest} object
     * @return built log value
     * @see #buildLog(String, JCasinoUser)
     */
    public static String buildLog(HttpServletRequest request) {
        HttpSession session = request.getSession();
        JCasinoUser user    = (JCasinoUser) session.getAttribute(ATTR_USER);
        String      query   = buildQueryString(request);
        return buildLog(query, user);
    }

    /**
     * Builds log due to query and user role.
     *
     * @param query query to log
     * @param user  user who made query
     * @return built log value
     */
    private static String buildLog(String query, JCasinoUser user) {
        String log;
        if (user == null) {
            log = "guest called " + query;
        } else {
            String email = user.getEmail();
            String role  = user.getRole().getRole();
            int    id    = user.getId();
            log = role + " " + email + " (id=" + id + ") called " + query;
        }
        return log;
    }

    /**
     * Builds query by parsing request parameters.
     *
     * @param request {@link HttpServletRequest} object
     * @return built query
     */
    private static String buildQueryString(HttpServletRequest request) {
        String       uri   = request.getRequestURI();
        StringBuffer query = new StringBuffer();

        Enumeration<String> params = request.getParameterNames();

        String key;
        String value;
        while (params.hasMoreElements()) {
            key = params.nextElement();
            value = request.getParameter(key);
            if (key.equalsIgnoreCase(PARAM_PASSWORD) ||
                key.equalsIgnoreCase(PARAM_PASSWORD_AGAIN) ||
                key.equalsIgnoreCase(PARAM_PASSWORD_OLD) ||
                key.equalsIgnoreCase(PARAM_SECRET_ANSWER)) {
                value = STUB;
            }
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

}