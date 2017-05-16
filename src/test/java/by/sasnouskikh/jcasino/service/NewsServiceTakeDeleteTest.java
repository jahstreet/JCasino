package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.NewsDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.News;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceTakeDeleteTest {

    private static final int        NEWS_ID   = 15;
    private static final News       NEWS      = new News();
    private static final List<News> NEWS_LIST = new ArrayList<>();

    static {
        NEWS.setId(NEWS_ID);
        NEWS_LIST.add(NEWS);
    }

    @Mock
    private NewsDAO     newsDAO;
    @Mock
    private DAOHelper   daoHelper;
    @InjectMocks
    private NewsService newsService;

    @Before
    public void setUp() {
        when(daoHelper.getNewsDAO()).thenReturn(newsDAO);
    }

    @Test
    public void takeNewsListNewsDAOCallCheck() throws DAOException {
        when(newsDAO.takeNews()).thenReturn(NEWS_LIST);
        newsService.takeNewsList();

        verify(newsDAO).takeNews();
    }

    @Test
    public void takeNewsListReturnNewsDAOResultNullCheck() throws DAOException {
        when(newsDAO.takeNews()).thenReturn(null);

        Assert.assertNull(newsService.takeNewsList());
    }

    @Test
    public void takeNewsListReturnNewsDAOResultNotNullCheck() throws DAOException {
        when(newsDAO.takeNews()).thenReturn(NEWS_LIST);

        Assert.assertEquals(NEWS_LIST, newsService.takeNewsList());
    }

    @Test
    public void takeNewsListDAOExceptionThrownReturnNullCheck() throws DAOException {
        when(newsDAO.takeNews()).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(newsService.takeNewsList());
    }

    @Test
    public void deleteNewsNewsDAODeleteCallCheck() throws DAOException {
        when(newsDAO.deleteNews(anyInt())).thenReturn(true);
        when(newsDAO.takeNews()).thenReturn(NEWS_LIST);
        newsService.deleteNews(NEWS_ID);

        verify(newsDAO).deleteNews(NEWS_ID);
    }

    @Test
    public void deleteNewsNewsDAOTakeCallCheck() throws DAOException {
        when(newsDAO.deleteNews(anyInt())).thenReturn(true);
        when(newsDAO.takeNews()).thenReturn(NEWS_LIST);
        newsService.deleteNews(NEWS_ID);

        verify(newsDAO).takeNews();
    }

    @Test
    public void deleteNewsNewsDAOTakeNotCallCheck() throws DAOException {
        when(newsDAO.deleteNews(anyInt())).thenReturn(false);
        when(newsDAO.takeNews()).thenReturn(NEWS_LIST);
        newsService.deleteNews(NEWS_ID);

        verify(newsDAO, times(0)).takeNews();
    }

    @Test
    public void deleteNewsDAOExceptionThrownOnDeleteReturnNullCheck() throws DAOException {
        when(newsDAO.deleteNews(anyInt())).thenThrow(new DAOException("Database connection error."));
        when(newsDAO.takeNews()).thenReturn(NEWS_LIST);

        Assert.assertNull(newsService.deleteNews(NEWS_ID));
    }

    @Test
    public void deleteNewsDAOExceptionThrownOnTakeReturnNullCheck() throws DAOException {
        when(newsDAO.deleteNews(anyInt())).thenReturn(true);
        when(newsDAO.takeNews()).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(newsService.deleteNews(NEWS_ID));
    }

    @Test
    public void deleteNewsReturnNotNullCheck() throws DAOException {
        when(newsDAO.deleteNews(anyInt())).thenReturn(true);
        when(newsDAO.takeNews()).thenReturn(NEWS_LIST);

        Assert.assertEquals(NEWS_LIST, newsService.deleteNews(NEWS_ID));
    }
}