package by.sasnouskikh.jcasino.filter;

import by.sasnouskikh.jcasino.mailer.MailerException;
import by.sasnouskikh.jcasino.mailer.MailerSSL;
import by.sasnouskikh.jcasino.manager.QueryManager;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * The class provides security filter for servlet container which bans users who try to make js injection attack and
 * notifies casino administration about it via e-mail messages and logs.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Filter
 * @see WebFilter
 */
@WebFilter(
filterName = "JSInjectSecurityFilter",
servletNames = {"MainController"}
)
public class JSInjectSecurityFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(JSInjectSecurityFilter.class);

    private static final String[] warnValues           = new String[]{"<script>"};
    private static final String   securityEmailSubject = "JS injection attempt";
    private static final String   rootAdminEmail       = "jahstreetlove@gmail.com";

    /**
     * <p>The servlet container calls the init
     * method exactly once after instantiating the filter. The init
     * method must complete successfully before the filter is asked to do any
     * filtering work.
     * <p>The web container cannot place the filter into service if the init
     * method either
     * <ol>
     * <li>Throws pressedKey ServletException
     * <li>Does not return within pressedKey time period defined by the web container
     * </ol>
     *
     * @param config a filter configuration object used by a servlet container to pass information to a filter during
     *               initialization
     * @see FilterConfig#getInitParameter(String)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the
     * container each time pressedKey request/response pair is passed through the
     * chain due to pressedKey client request for pressedKey resource at the end of the chain.
     * The FilterChain passed in to this method allows the Filter to pass
     * on the request and response to the next entity in the chain.
     * <p>A typical implementation of this method would follow the following
     * pattern:
     * <ol>
     * <li>Examine the request
     * <li>Optionally wrap the request object with pressedKey custom implementation to
     * filter content or headers for input filtering
     * <li>Optionally wrap the response object with pressedKey custom implementation to
     * filter content or headers for output filtering
     * <li>
     * <ul>
     * <li><strong>Either</strong> invoke the next entity in the chain
     * using the FilterChain object
     * (<code>chain.doFilter()</code>),
     * <li><strong>or</strong> not pass on the request/response pair to
     * the next entity in the filter chain to
     * block the request processing
     * </ul>
     * <li>Directly set headers on the response after invocation of the
     * next entity in the filter chain.
     * </ol>
     *
     * @param request  an object which provides client request information to a servlet
     * @param response an object which assists a servlet in sending a response to the client
     * @param chain    an object provided by the servlet container to the developer giving a view into the invocation
     *                 chain of a filtered request for a resource. Filters use the FilterChain to invoke the next filter
     *                 in the chain, or if the calling filter is the last filter in the chain, to invoke the resource at
     *                 the end of the chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (!ServletFileUpload.isMultipartContent(httpRequest)) {
            Map<String, String[]> parameterMap = httpRequest.getParameterMap();
            for (Map.Entry<String, String[]> pair : parameterMap.entrySet()) {
                String[] values = pair.getValue();
                for (String value : values) {
                    for (String warnValue : warnValues) {
                        if (value != null && value.toLowerCase().contains(warnValue)) {
                            String log = QueryManager.buildLog(httpRequest);
                            new Thread(() -> {
                                String text = "User tried to inject JS script. Log data: " + log;
                                try {
                                    MailerSSL.sendEmail(securityEmailSubject, text, rootAdminEmail);
                                } catch (MailerException e) {
                                    LOGGER.log(Level.WARN, "Root admin e-mail sending error occurred. E-mail text: "
                                                           + text + ". Thrown exception details: " + e.getMessage());
                                }
                            }).start();
                        }
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Called by the web container to indicate to pressedKey filter that it is being
     * taken out of service.
     * <p>This method is only called once all threads within the filter's
     * doFilter method have exited or after pressedKey timeout period has passed.
     * After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter.
     * <p>This method gives the filter an opportunity to clean up any
     * resources that are being held (for example, memory, file handles,
     * threads) and make sure that any persistent state is synchronized
     * with the filter's current state in memory.
     */
    @Override
    public void destroy() {
    }
}