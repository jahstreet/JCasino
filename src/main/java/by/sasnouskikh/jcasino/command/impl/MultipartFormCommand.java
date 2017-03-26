package by.sasnouskikh.jcasino.command.impl;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.logic.LogicException;
import by.sasnouskikh.jcasino.logic.PlayerLogic;
import by.sasnouskikh.jcasino.manager.MessageManager;
import by.sasnouskikh.jcasino.manager.QueryManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class MultipartFormCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(MultipartFormCommand.class);

    private static final int    MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int    MAX_FILE_SIZE    = 1024 * 1024 * 16; // 16MB
    private static final int    MAX_REQUEST_SIZE = 1024 * 1024 * 20; // 20MB
    private static final String TEMP_REPOSITORY  = "java.io.tmpdir";
    private static final String MULTIPART_QUERY  = "multipart_form_command";
    private static final String UPLOAD_PASSPORT  = "upload_passport";
    private static final String UPLOAD_NEWS      = "upload_news";

    @Override
    public String[] execute(HttpServletRequest request) {
        QueryManager.logMultipartQuery(request, MULTIPART_QUERY);
        HttpSession    session        = request.getSession();
        String         locale         = (String) session.getAttribute(ATTR_LOCALE);
        MessageManager messageManager = new MessageManager(locale);
        StringBuilder  errorMessage   = new StringBuilder();
        String[]       queryParams;
        boolean        uploaded       = false;
        Player         player         = (Player) session.getAttribute(ATTR_PLAYER);
        String         uploadDir      = request.getServletContext().getInitParameter(INIT_PARAM_UPLOADS);

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty(TEMP_REPOSITORY)));
        ServletFileUpload uploader = new ServletFileUpload(factory);
        uploader.setFileSizeMax(MAX_FILE_SIZE);
        uploader.setSizeMax(MAX_REQUEST_SIZE);

        try {
            List<FileItem> formItems = uploader.parseRequest(request);
            if (formItems != null && !formItems.isEmpty()) {
                for (FileItem item : formItems) {
                    String fieldName = item.getFieldName();
                    if (!item.isFormField()) {
                        if (PARAM_SCAN.equals(fieldName)) {
                            QueryManager.logMultipartQuery(request, UPLOAD_PASSPORT);
                            uploaded = PlayerLogic.uploadPassportScan(player, item, uploadDir);
                            break;
                        }
                    }
                }
            } else {
                errorMessage.append(messageManager.getMessage(MESSAGE_INVALID_MULTIPART_FORM)).append(NEW_LINE_SEPARATOR);
            }
        } catch (FileUploadException | LogicException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            errorMessage.append(messageManager.getMessage(MESSAGE_MULTIPART_UPLOAD_ERROR)).append(NEW_LINE_SEPARATOR);
        }
        if (uploaded) {
            queryParams = new String[]{GOTO_VERIFICATION, REDIRECT};
        } else {
            if (errorMessage.toString().isEmpty()) {
                errorMessage.append(messageManager.getMessage(MESSAGE_MULTIPART_UPLOAD_ERROR));
            }
            request.setAttribute(ATTR_ERROR_MESSAGE, errorMessage.toString().trim());
            queryParams = new String[]{PAGE_UPLOAD_PASSPORT, FORWARD};
        }
        return queryParams;
    }
}