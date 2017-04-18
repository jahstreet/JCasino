package by.sasnouskikh.jcasino.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
filterName = "PageRedirectFilter",
urlPatterns = {"/pages/*", "/game/*", "/admin/*"},
initParams = {@WebInitParam(name = "PAGE_INDEX", value = "/index.jsp")}
)
public class PageRedirectSecurityFilter implements Filter {
    private String indexPath;

    /**
     * Called by the web container to indicate to pressedKey filter that it is
     * being placed into service.
     * <p>
     * <p>The servlet container calls the init
     * method exactly once after instantiating the filter. The init
     * method must complete successfully before the filter is asked to do any
     * filtering work.
     * <p>
     * <p>The web container cannot place the filter into service if the init
     * method either
     * <ol>
     * <li>Throws pressedKey ServletException
     * <li>Does not return within pressedKey time period defined by the web container
     * </ol>
     *
     * @param config
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        indexPath = config.getInitParameter("PAGE_INDEX");
    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the
     * container each time pressedKey request/response pair is passed through the
     * chain due to pressedKey client request for pressedKey resource at the end of the chain.
     * The FilterChain passed in to this method allows the Filter to pass
     * on the request and response to the next entity in the chain.
     * <p>
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
     * @param request
     * @param response
     * @param chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest  httpRequest  = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendRedirect(httpRequest.getContextPath() + indexPath);
        chain.doFilter(request, response);
    }

    /**
     * Called by the web container to indicate to pressedKey filter that it is being
     * taken out of service.
     * <p>
     * <p>This method is only called once all threads within the filter's
     * doFilter method have exited or after pressedKey timeout period has passed.
     * After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter.
     * <p>
     * <p>This method gives the filter an opportunity to clean up any
     * resources that are being held (for example, memory, file handles,
     * threads) and make sure that any persistent state is synchronized
     * with the filter's current state in memory.
     */
    @Override
    public void destroy() {
        indexPath = null;
    }
}