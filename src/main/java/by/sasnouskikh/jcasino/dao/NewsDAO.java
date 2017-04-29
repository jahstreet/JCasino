package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.News;

import java.sql.SQLException;
import java.util.List;

/**
 * The class provides DAO abstraction for {@link News} objects.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AbstractDAO
 */
public abstract class NewsDAO extends AbstractDAO {

    /**
     * Column names of database table 'news'.
     */
    protected static final String ID       = "id";
    protected static final String DATE     = "date";
    protected static final String HEADER   = "header";
    protected static final String TEXT     = "text";
    protected static final String ADMIN_ID = "admin_id";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    protected NewsDAO() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    protected NewsDAO(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes {@link List} filled by all {@link News} objects.
     *
     * @return {@link List} filled by all {@link News} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<News> takeNews() throws DAOException;

    /**
     * Takes {@link News} by its id.
     *
     * @param newsId id of {@link News} object to take
     * @return {@link News} with definite id or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract News takeNews(int newsId) throws DAOException;

    /**
     * Inserts {@link News} to database.
     *
     * @param adminId id of admin who inserts {@link News} object
     * @param header  news header
     * @param text    news text
     * @return int value of inserted news id or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract int insertNews(int adminId, String header, String text) throws DAOException;

    /**
     * Updates definite {@link News} 'header' and fixes admin who proceeded it.
     *
     * @param newsId  id of news to update
     * @param adminId id of admin who updates {@link News} object
     * @param header  news header
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeNewsHeader(int newsId, String header, int adminId) throws DAOException;

    /**
     * Updates definite {@link News} 'text' and fixes admin who proceeded it.
     *
     * @param newsId  id of news to update
     * @param text    news text
     * @param adminId id of admin who updates {@link News} object
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean changeNewsText(int newsId, String text, int adminId) throws DAOException;

    /**
     * Deletes definite {@link News}=.
     *
     * @param id id of news to delete
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean deleteNews(int id) throws DAOException;
}