package by.sasnouskikh.jcasino.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.INIT_PARAM_UPLOADS;

/**
 * <p>The class provides easier showing images on JSP pages.
 * <p>Example: {@code <img src="${pageContext.request.contextPath}/image/relative_image_path.jpg"} where
 * 'relative_image_path' is relative to
 * '{@code request.getServletContext().getInitParameter(INIT_PARAM_UPLOADS) + "/"}'. In this app it is:
 * <pre>{@code
 *   <context-param>
 *     <param-name>uploads.dir</param-name>
 *     <u><param-value>/Library/apache-tomcat-8.5.9/uploads</param-value></u>
 *   </context-param>}</pre>
 * <p>See at Web.xml.
 *
 * @author Sasnouskikh Aliaksandr
 * @see HttpServlet
 * @see WebServlet
 */
@WebServlet(name = "ShowImageServlet", urlPatterns = {"/image/*"})
public class ShowImageServlet extends HttpServlet {

    /**
     * Processes request sent by POST method. Is unsupported in this {@link HttpServlet}.
     *
     * @param request  request from client to get parameters to work with
     * @param response response to client with parameters to work with on client side
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * Processes request sent by GET method. Writes to {@link HttpServletResponse} image found by taken path.
     *
     * @param request  request from client to get parameters to work with
     * @param response response to client with parameters to work with on client side
     * @see HttpServletRequest
     * @see javax.servlet.ServletContext
     * @see Files#copy(Path, OutputStream)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getPathInfo();
        File   file     = new File(request.getServletContext().getInitParameter(INIT_PARAM_UPLOADS) + "/", filename);
        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }
}
