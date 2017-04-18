package by.sasnouskikh.jcasino.command.ajax;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Common interface for Commands suitable to use with {@link by.sasnouskikh.jcasino.controller.AjaxController}.
 *
 * @author Sasnouskikh Aliaksandr
 */
public interface AjaxCommand {

    /**
     * Executes definite operation with data parsed from request, puts processed data and messages into
     * {@link HashMap} responseMap and returns it.
     *
     * @param request request from client to get parameters to work with
     * @return {@link HashMap} with response parameters
     */
    Map<String, Object> execute(HttpServletRequest request);
}