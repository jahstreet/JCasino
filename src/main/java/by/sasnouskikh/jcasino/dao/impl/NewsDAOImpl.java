package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.News;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NewsDAOImpl extends AbstractDAO<Integer, News> {

    private static final String SQL_SELECT_ALL    = "SELECT id, date, header, text, admin_id " +
                                                    "FROM news";
    private static final String SQL_SELECT_BY_ID  = "SELECT id, date, header, text, admin_id " +
                                                    "FROM news " +
                                                    "WHERE id=?";
    private static final String SQL_INSERT        = "INSERT INTO news(date, header, text, admin_id) " +
                                                    "VALUES(NOW(), ?, ?, ?)";
    private static final String SQL_UPDATE_HEADER = "UPDATE news " +
                                                    "SET header=?, admin_id=? " +
                                                    "WHERE id=?";
    private static final String SQL_UPDATE_TEXT   = "UPDATE news " +
                                                    "SET text=?, admin_id=? " +
                                                    "WHERE id=?";
    private static final String SQL_DELETE_BY_ID  = "DELETE FROM news " +
                                                    "WHERE id=?";

    NewsDAOImpl() {
    }

    NewsDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    public List<News> takeNews() throws DAOException {
        List<News> newsList;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
            newsList = buildNewsList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking news. " + e);
        }
        return newsList;
    }

    public News takeNews(int newsId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, newsId);
            ResultSet resultSet = statement.executeQuery();
            return buildNews(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking news. " + e);
        }
    }

    public int insertNews(int adminId, String header, String text) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, header);
            statement.setString(2, text);
            statement.setInt(3, adminId);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting news. " + e);
        }
    }

    public boolean changeNewsHeader(int newsId, String header, int adminId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_HEADER)) {
            statement.setString(1, header);
            statement.setInt(2, adminId);
            statement.setInt(3, newsId);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 news associated with given id: '" + newsId + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing news header. " + e);
        }
    }

    public boolean changeNewsText(int newsId, String text, int adminId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_TEXT)) {
            statement.setString(1, text);
            statement.setInt(2, adminId);
            statement.setInt(3, newsId);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 news associated with given id: '" + newsId + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while changing news text. " + e);
        }
    }

    public boolean deleteNews(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 news associated with given id: '" + id + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while deleting news. " + e);
        }
    }

    private News buildNews(ResultSet resultSet) throws SQLException {
        News news = null;
        if (resultSet.next()) {
            news = new News();
            news.setId(resultSet.getInt("id"));
            news.setDate(resultSet.getDate("date").toLocalDate());
            news.setHeader(resultSet.getString("header"));
            news.setText(resultSet.getString("text"));
            news.setAdminId(resultSet.getInt("admin_id"));
        }
        return news;
    }

    private List<News> buildNewsList(ResultSet resultSet) throws SQLException {
        List<News> newsList = new ArrayList<>();
        News       news;
        do {
            news = buildNews(resultSet);
            if (news != null) {
                newsList.add(news);
            }
        } while (news != null);
        return newsList;
    }
}