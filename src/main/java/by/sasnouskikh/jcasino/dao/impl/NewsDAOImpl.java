package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.NewsDAO;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.News;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The class provides {@link NewsDAO} implementation for MySQL database.
 *
 * @author Sasnouskikh Aliaksandr
 */
class NewsDAOImpl extends NewsDAO {

    /**
     * Selects all news.
     */
    private static final String SQL_SELECT_ALL    = "SELECT id, date, header, text, admin_id " +
                                                    "FROM news";
    /**
     * Selects definite news by its id.
     */
    private static final String SQL_SELECT_BY_ID  = "SELECT id, date, header, text, admin_id " +
                                                    "FROM news " +
                                                    "WHERE id=?";
    /**
     * Inserts news to database.
     */
    private static final String SQL_INSERT        = "INSERT INTO news(date, header, text, admin_id) " +
                                                    "VALUES(NOW(), ?, ?, ?)";
    /**
     * Updates definite news header and fixes admin who proceeded it.
     */
    private static final String SQL_UPDATE_HEADER = "UPDATE news " +
                                                    "SET header=?, admin_id=? " +
                                                    "WHERE id=?";
    /**
     * Updates definite news text and fixes admin who proceeded it.
     */
    private static final String SQL_UPDATE_TEXT   = "UPDATE news " +
                                                    "SET text=?, admin_id=? " +
                                                    "WHERE id=?";
    /**
     * Deletes definite news by its id.
     */
    private static final String SQL_DELETE_BY_ID  = "DELETE FROM news " +
                                                    "WHERE id=?";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    NewsDAOImpl() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    NewsDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes {@link List} filled by all {@link News} objects.
     *
     * @return {@link List} filled by all {@link News} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#createStatement()
     * @see Statement
     * @see ResultSet
     * @see #buildNewsList(ResultSet)
     */
    @Override
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

    /**
     * Takes {@link News} by its id.
     *
     * @return {@link News} with definite id or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildNews(ResultSet)
     */
    @Override
    public News takeNews(int newsId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, newsId);
            ResultSet resultSet = statement.executeQuery();
            return buildNews(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking news. " + e);
        }
    }

    /**
     * Inserts {@link News} to database.
     *
     * @return int value of inserted news generated id or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String, int)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
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

    /**
     * Updates definite {@link News} 'header' and fixes admin who proceeded it.
     *
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
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

    /**
     * Updates definite {@link News} 'text' and fixes admin who proceeded it.
     *
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
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

    /**
     * Deletes definite {@link News}.
     *
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     */
    @Override
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

    /**
     * Builds {@link News} object by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link News} object or null
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    private News buildNews(ResultSet resultSet) throws SQLException {
        News news = null;
        if (resultSet.next()) {
            news = new News();
            news.setId(resultSet.getInt(ID));
            news.setDate(resultSet.getDate(DATE).toLocalDate());
            news.setHeader(resultSet.getString(HEADER));
            news.setText(resultSet.getString(TEXT));
            news.setAdminId(resultSet.getInt(ADMIN_ID));
        }
        return news;
    }

    /**
     * Builds {@link List} object filled by {@link News} objects by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link List} object or null
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     * @see #buildNews(ResultSet)
     */
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