package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.NewsDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.News;
import org.apache.commons.fileupload.FileItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(NewsService.class)
public class NewsServiceAddEditNewsTest {

    private static final News   NEWS       = new News();
    private static final Admin  ADMIN      = new Admin();
    private static final String HEADER     = "News HEADER. Test.";
    private static final String TEXT       = "News TEXT. Test.";
    private static final String UPLOAD_DIR = "UPLOAD_DIR";
    private static final int    ADMIN_ID   = 100;
    private static final int    NEWS_ID    = 15;
    private static final String LOCALE     = "ru";

    static {
        NEWS.setId(NEWS_ID);
        NEWS.setAdminId(ADMIN_ID);
        NEWS.setDate(LocalDate.now());
        NEWS.setText(TEXT);
        NEWS.setHeader(HEADER);

        ADMIN.setId(ADMIN_ID);
    }

    @Mock
    private FileItem    fileItem;
    @Mock
    private NewsDAO     newsDAO;
    @Mock
    private DAOHelper   daoHelper;
    @InjectMocks
    private NewsService newsService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(daoHelper.getNewsDAO()).thenReturn(newsDAO);
        PowerMockito.spy(NewsService.class);
        PowerMockito.doReturn(true)
                    .when(NewsService.class, "updateNewsImage", anyInt(), any(FileItem.class), anyString());
    }

    @Test
    public void addNewsNewsDAOCallInsertCheck() throws Exception {
        when(newsDAO.insertNews(anyInt(), anyString(), anyString(), anyString())).thenReturn(NEWS_ID);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        newsService.addNews(HEADER, TEXT, fileItem, LOCALE, ADMIN, UPLOAD_DIR);

        verify(newsDAO).insertNews(ADMIN_ID, HEADER, TEXT, LOCALE);
    }

    @Test
    public void addNewsNewsDAOCallTakeCheck() throws Exception {
        when(newsDAO.insertNews(anyInt(), anyString(), anyString(), anyString())).thenReturn(NEWS_ID);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        newsService.addNews(HEADER, TEXT, fileItem, LOCALE, ADMIN, UPLOAD_DIR);

        verify(newsDAO).takeNews(NEWS_ID);
    }

    @Test
    public void addNewsNewsCallUpdateImageCheck() throws Exception {
        when(newsDAO.insertNews(anyInt(), anyString(), anyString(), anyString())).thenReturn(NEWS_ID);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        newsService.addNews(HEADER, TEXT, fileItem, LOCALE, ADMIN, UPLOAD_DIR);

        PowerMockito.verifyPrivate(NewsService.class).invoke("updateNewsImage", NEWS_ID, fileItem, UPLOAD_DIR);
    }

    @Test
    public void addNewsDAOExceptionThrownOnInsertReturnNullCheck() throws Exception {
        when(newsDAO.insertNews(anyInt(), anyString(), anyString(), anyString()))
        .thenThrow(new DAOException("Database connection error."));
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);

        Assert.assertNull(newsService.addNews(HEADER, TEXT, fileItem, LOCALE, ADMIN, UPLOAD_DIR));
    }

    @Test
    public void addNewsDAOExceptionThrownOnTakeReturnNullCheck() throws Exception {
        when(newsDAO.insertNews(anyInt(), anyString(), anyString(), anyString())).thenReturn(NEWS_ID);
        when(newsDAO.takeNews(NEWS_ID)).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(newsService.addNews(HEADER, TEXT, fileItem, LOCALE, ADMIN, UPLOAD_DIR));
    }

    @Test(expected = ServiceException.class)
    public void addNewsServiceExceptionThrownOnUpdateImageReturnNullCheck() throws Exception {
        PowerMockito.doThrow(new ServiceException("Update NEWS image error."))
                    .when(NewsService.class, "updateNewsImage", anyInt(), any(FileItem.class), anyString());

        when(newsDAO.insertNews(anyInt(), anyString(), anyString(), anyString())).thenReturn(NEWS_ID);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        newsService.addNews(HEADER, TEXT, fileItem, LOCALE, ADMIN, UPLOAD_DIR);

        Assert.fail("ServiceException expected to be thrown.");
    }

    @Test
    public void addNewsBeginTransactionSQLExceptionThrownReturnNullCheck() throws Exception {
        when(newsDAO.insertNews(anyInt(), anyString(), anyString(), anyString())).thenReturn(NEWS_ID);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();

        Assert.assertNull(newsService.addNews(HEADER, TEXT, fileItem, LOCALE, ADMIN, UPLOAD_DIR));
    }

    @Test
    public void addNewsCommitSQLExceptionThrownReturnNullCheck() throws Exception {
        when(newsDAO.insertNews(anyInt(), anyString(), anyString(), anyString())).thenReturn(NEWS_ID);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();

        Assert.assertNull(newsService.addNews(HEADER, TEXT, fileItem, LOCALE, ADMIN, UPLOAD_DIR));
    }

    @Test
    public void addNewsReturnCheck() throws Exception {
        when(newsDAO.insertNews(anyInt(), anyString(), anyString(), anyString())).thenReturn(NEWS_ID);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);

        Assert.assertEquals(NEWS, newsService.addNews(HEADER, TEXT, fileItem, LOCALE, ADMIN, UPLOAD_DIR));
    }

    @Test
    public void editNewsNewsDAOCallChangeHeaderCheck() throws Exception {
        when(newsDAO.changeNewsHeader(anyInt(), anyString(), anyInt())).thenReturn(true);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        newsService.editNews(NEWS_ID, HEADER, null, null, ADMIN, null);

        verify(newsDAO).changeNewsHeader(NEWS_ID, HEADER, ADMIN_ID);
    }

    @Test
    public void editNewsNewsDAOCallChangeTextCheck() throws Exception {
        when(newsDAO.changeNewsText(anyInt(), anyString(), anyInt())).thenReturn(true);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        newsService.editNews(NEWS_ID, null, null, TEXT, ADMIN, null);

        verify(newsDAO).changeNewsText(NEWS_ID, TEXT, ADMIN_ID);
    }

    @Test
    public void editNewsNewsDAOCallTakeNewsCheck() throws Exception {
        when(newsDAO.changeNewsText(anyInt(), anyString(), anyInt())).thenReturn(true);
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        newsService.editNews(NEWS_ID, null, null, TEXT, ADMIN, null);

        verify(newsDAO).takeNews(NEWS_ID);
    }

    @Test
    public void editNewsCallUpdateImageCheck() throws Exception {
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        newsService.editNews(NEWS_ID, null, fileItem, null, ADMIN, UPLOAD_DIR);

        PowerMockito.verifyPrivate(NewsService.class).invoke("updateNewsImage", NEWS_ID, fileItem, UPLOAD_DIR);
    }

    @Test
    public void editNewsDAOExceptionThrownOnChangeHeaderReturnNullCheck() throws Exception {
        when(newsDAO.changeNewsHeader(anyInt(), anyString(), anyInt()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(newsService.editNews(NEWS_ID, HEADER, null, null, ADMIN, null));
    }

    @Test
    public void editNewsDAOExceptionThrownOnChangeTextReturnNullCheck() throws Exception {
        when(newsDAO.changeNewsText(anyInt(), anyString(), anyInt()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(newsService.editNews(NEWS_ID, null, null, TEXT, ADMIN, null));
    }

    @Test(expected = ServiceException.class)
    public void editNewsServiceExceptionThrownOnUpdateImageCheck() throws Exception {
        PowerMockito.doThrow(new ServiceException("Update NEWS image error."))
                    .when(NewsService.class, "updateNewsImage", anyInt(), any(FileItem.class), anyString());

        newsService.editNews(NEWS_ID, null, fileItem, null, ADMIN, null);

        Assert.fail("ServiceException expected to be thrown.");
    }

    @Test
    public void editNewsReturnNotNullCheck() throws Exception {
        when(newsDAO.takeNews(NEWS_ID)).thenReturn(NEWS);
        newsService.editNews(NEWS_ID, null, null, null, ADMIN, null);

        Assert.assertEquals(NEWS, newsService.editNews(NEWS_ID, null, null, null, ADMIN, null));
    }
}