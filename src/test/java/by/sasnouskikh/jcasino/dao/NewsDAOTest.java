package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.entity.bean.News;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class NewsDAOTest extends AbstractDAOTest {

    private static final String TABLE_NEWS         = "news";
    private static final String XML_NEWS_DATA      = "by/sasnouskikh/jcasino/dao/news_data.xml";
    private static final String XML_DELETED        = "by/sasnouskikh/jcasino/dao/news_data_deleted.xml";
    private static final String XML_CHANGED_TEXT   = "by/sasnouskikh/jcasino/dao/news_data_changed_text.xml";
    private static final String XML_CHANGED_HEADER = "by/sasnouskikh/jcasino/dao/news_data_changed_header.xml";
    private static final String XML_INSERTED       = "by/sasnouskikh/jcasino/dao/news_data_inserted.xml";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        beforeData = buildDataSet(XML_NEWS_DATA);
        DatabaseOperation.CLEAN_INSERT.execute(connection, beforeData);
    }

    @Test
    public void takeNewsCheck() throws DAOException {
        int    newsId     = 1;
        String newsDate   = "2017-04-14";
        String newsHeader = "header1";
        String newsText   = "текст1";
        int    adminId    = 100;

        News expected = new News();
        expected.setId(newsId);
        expected.setDate(LocalDate.parse(newsDate));
        expected.setHeader(newsHeader);
        expected.setText(newsText);
        expected.setAdminId(adminId);

        News actual = daoHelper.getNewsDAO().takeNews(newsId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeAllNewsCheck() throws DAOException {
        int expectedSize = 5;

        List<News> newsList   = daoHelper.getNewsDAO().takeNews();
        int        actualSize = newsList.size();

        Assert.assertEquals(String.format("Number or rows expected: %d, actual: %d", expectedSize, actualSize),
                            expectedSize, actualSize);
    }

    @Test
    public void deleteNewsCheck() throws DAOException, DatabaseUnitException, SQLException {
        int newsId = 1;

        IDataSet expectedDataSet = buildDataSet(XML_DELETED);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_NEWS);

        daoHelper.getNewsDAO().deleteNews(newsId);
        ITable actualTable = connection.createTable(TABLE_NEWS);
        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable,
                                                                              expectedTable.getTableMetaData().getColumns());
        Assertion.assertEquals(expectedTable, filteredActualTable);
    }

    @Test
    public void deleteNewsTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int newsId = 1;

        boolean actual = daoHelper.getNewsDAO().deleteNews(newsId);

        Assert.assertTrue(String.format("Method should return `true`, if it proceeded successfully, but it returns: %s", actual),
                          actual);
    }

    @Test
    public void changeNewsTextTrueCheck() throws DAOException {
        int    newsId  = 1;
        int    adminId = 101;
        String newText = "текст2";

        boolean actual = daoHelper.getNewsDAO().changeNewsText(newsId, newText, adminId);
        Assert.assertTrue(String.format("Method should return `true`, if it proceeded successfully, but it returns: %s", actual),
                          actual);
    }

    @Test
    public void changeNewsTextCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    newsId  = 1;
        int    adminId = 101;
        String newText = "текст2";

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_TEXT);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_NEWS);

        daoHelper.getNewsDAO().changeNewsText(newsId, newText, adminId);
        ITable actualTable = connection.createTable(TABLE_NEWS);
        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable,
                                                                              expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, filteredActualTable);
    }

    @Test
    public void changeNewsHeaderTrueCheck() throws DAOException {
        int    newsId    = 1;
        int    adminId   = 101;
        String newHeader = "header2";

        boolean actual = daoHelper.getNewsDAO().changeNewsText(newsId, newHeader, adminId);
        Assert.assertTrue(String.format("Expected - 'true', but returned - '%b'", actual), actual);
    }

    @Test
    public void changeNewsHeaderCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    newsId    = 1;
        int    adminId   = 101;
        String newHeader = "header2";

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_HEADER);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_NEWS);

        daoHelper.getNewsDAO().changeNewsHeader(newsId, newHeader, adminId);
        ITable actualTable = connection.createTable(TABLE_NEWS);
        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable,
                                                                              expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, filteredActualTable);
    }

    @Test
    public void insertNewsIdCheck() throws DAOException {
        int    adminId    = 100;
        String newsHeader = "header10";
        String newsText   = "текст10";

        int expectedId = 6;

        int actualId = daoHelper.getNewsDAO().insertNews(adminId, newsHeader, newsText);
        Assert.assertEquals(String.format("Inserted news id expected: %d, actual: %d", expectedId, actualId),
                            expectedId, actualId);

    }

    @Test
    public void insertNewsCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      adminId    = 100;
        String   newsHeader = "header10";
        String   newsText   = "текст10";
        String[] ignore     = {"id", "date"};

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_NEWS);

        daoHelper.getNewsDAO().insertNews(adminId, newsHeader, newsText);
        ITable actualTable = connection.createTable(TABLE_NEWS);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test(expected = DAOException.class)
    public void adminIdFkConstraintCheck() throws DAOException {
        int    adminId    = 102;
        String newsHeader = "header10";
        String newsText   = "текст10";

        daoHelper.getNewsDAO().insertNews(adminId, newsHeader, newsText);

        Assert.fail("A FK constraint (`jcasino`.`news`, CONSTRAINT `fk_user_news` FOREIGN KEY (`admin_id`) " +
                    "REFERENCES `user` (`id`) should exist.");
    }
}