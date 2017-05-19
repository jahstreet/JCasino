package by.sasnouskikh.jcasino.controller;

import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * <p>The class provides dump process for images on remote filesystem.
 *
 * @author Sasnouskikh Aliaksandr
 * @see HttpServlet
 * @see WebServlet
 */
@WebServlet(name = "DumpServerDataServlet", urlPatterns = {"/dump"})
public class DumpServerDataServlet extends HttpServlet {

    private static final String TARGET_SCAN   = "scan";
    private static final String TARGET_NEWS   = "news";
    private static final String TARGET_LOGS   = "logs";
    private static final String ZIP_FILE_NAME = "dump.zip";
    private static final String LOGS_DIR      = "/opt/tomcat/temp/logs";

    /**
     * Processes request sent by GET method. Writes to {@link HttpServletResponse} zip-archive found by taken path.
     *
     * @param request  request from client to get parameters to work with
     * @param response response to client with parameters to work with on client side
     * @see HttpServletRequest
     * @see javax.servlet.ServletContext
     * @see Files#copy(Path, OutputStream)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QueryManager.logQuery(request);
        String         locale         = (String) request.getSession().getAttribute(ATTR_LOCALE);
        MessageManager messageManager = MessageManager.getMessageManager(locale);

        String dumpTarget = request.getParameter(PARAM_TARGET);

        if (dumpTarget != null) {
            dumpTarget = dumpTarget.toLowerCase().trim();
            String uploadsDir = getServletContext().getInitParameter(INIT_PARAM_UPLOADS);
            String zipDirPath;
            switch (dumpTarget) {
                case TARGET_SCAN:
                    zipDirPath = uploadsDir + File.separator + SCAN_UPLOAD_DIR;
                    break;
                case TARGET_NEWS:
                    zipDirPath = uploadsDir + File.separator + NEWS_UPLOAD_DIR;
                    break;
                case TARGET_LOGS:
                    zipDirPath = LOGS_DIR;
                    break;
                default:
                    zipDirPath = uploadsDir;
            }
            File            zipDir  = new File(zipDirPath);
            File            zipFile = new File(zipDir.getParent() + File.separator + ZIP_FILE_NAME);
            ZipOutputStream zos     = new ZipOutputStream(new FileOutputStream(zipFile));
            zipDirectory(zipDirPath, zos);
            zos.flush();
            zos.close();
            writeZipFile(zipFile, response);
        } else {
            request.setAttribute(ATTR_ERROR_MESSAGE, messageManager.getMessage(MESSAGE_DUMP_ZIP_ERROR));
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(QueryManager.takePreviousQuery(request));
            dispatcher.forward(request, response);
        }
    }

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

    private void writeZipFile(File file, HttpServletResponse response) throws IOException {
        response.setContentType("application/zip");
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

    private void zipDirectory(String directory, ZipOutputStream zos) throws IOException {
        File     zipDir  = new File(directory);
        String[] dirList = zipDir.list();
        if (dirList == null) {
            return;
        }
        for (String aDirList : dirList) {
            File file = new File(zipDir, aDirList);
            if (file.isDirectory()) {
                String filePath = file.getPath();
                zipDirectory(filePath, zos);
                continue;
            }
            FileInputStream fis      = new FileInputStream(file);
            ZipEntry        zipEntry = new ZipEntry(file.getPath());
            zos.putNextEntry(zipEntry);
            byte[] readBuffer = new byte[1024];
            int    bytesIn;
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }
}
