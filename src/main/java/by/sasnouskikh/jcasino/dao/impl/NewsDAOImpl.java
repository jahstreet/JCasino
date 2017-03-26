package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.News;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NewsDAOImpl extends AbstractDAO<Integer, News> {

    private static final String SQL_NEWS_SELECT = "SELECT * " +
                                                  "FROM news";

    public ArrayList<News> takeNews() throws DAOException {
        ArrayList<News> newsList;
        try (WrappedConnection connection = ConnectionPool.getInstance().takeConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_NEWS_SELECT);
            newsList = buildNewsList(resultSet);
        } catch (ConnectionPoolException | SQLException e) {
            throw new DAOException("Database connection error while taking news. " + e);
        }
        return newsList;
    }

    private ArrayList<News> buildNewsList(ResultSet resultSet) throws SQLException {
        ArrayList<News> newsList = new ArrayList<>();
        while (resultSet.next()) {
            News news = new News();
            news.setId(resultSet.getInt("id"));
            news.setDate(resultSet.getDate("date").toLocalDate());
            news.setHeader(resultSet.getString("header"));
            news.setText(resultSet.getString("text"));
            news.setAdminId(resultSet.getInt("admin_id"));
            newsList.add(news);
        }
        return newsList;
    }
}