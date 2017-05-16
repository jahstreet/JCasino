package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.LoanDAO;
import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Loan;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.LOAN_TERM_MONTH;

/**
 * The class provides {@link LoanDAO} implementation for MySQL database.
 *
 * @author Sasnouskikh Aliaksandr
 */
class LoanDAOImpl extends LoanDAO {

    /**
     * Column name used in SQL queries. Equals 'amount-amount_paid' value.
     */
    private static final String AMOUNT_REST = "rest";

    /**
     * Selects loans of definite player and orders them by expire date in descending order.
     */
    private static final String SQL_SELECT_BY_PLAYER_ID = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                          "FROM loan " +
                                                          "WHERE player_id=? " +
                                                          "ORDER BY expire DESC";
    /**
     * Selects loan by its id.
     */
    private static final String SQL_SELECT_BY_ID        = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                          "FROM loan " +
                                                          "WHERE id=?";

    /**
     * Selects loans of definite player where acquire date is like definite pattern and orders them by expire date in
     * descending order.
     */
    private static final String SQL_SELECT_PLAYER_LIKE_ACQUIRE = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                                 "FROM loan " +
                                                                 "WHERE player_id=? AND acquire LIKE ? " +
                                                                 "ORDER BY expire DESC";
    /**
     * Selects current unpaid loans of definite player. Application service implies that result should be 1 loan.
     */
    private static final String SQL_SELECT_PLAYER_CURRENT      = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                                 "FROM loan " +
                                                                 "WHERE player_id=? AND amount>amount_paid";

    /**
     * Selects loans of definite player where acquire date and expire date are like definite patterns and orders them
     * by expire date value in descending order.
     */
    private static final String SQL_SELECT_LIKE_ACQUIRE_EXPIRE = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                                 "FROM loan " +
                                                                 "WHERE acquire LIKE ? AND expire LIKE ? " +
                                                                 "ORDER BY expire DESC";
    /**
     * Inserts loan to database.
     */
    private static final String SQL_INSERT                     = "INSERT INTO loan (player_id, amount, acquire, expire, percent) " +
                                                                 "VALUES (?, ?, NOW(), ?, ?)";
    /**
     * Updates loan 'amount_paid' value by adding definite value to it.
     */
    private static final String SQL_UPDATE_AMOUNT_PAID_ADD     = "UPDATE loan " +
                                                                 "SET amount_paid=amount_paid+? " +
                                                                 "WHERE id=?";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    LoanDAOImpl() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    LoanDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes {@link Loan} by its id.
     *
     * @param id loan id
     * @return taken {@link Loan} object or null
     * @throws DAOException if {@link Exception} occurred while working with database
     */
    @Override
    public Loan takeLoan(int id) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return buildLoan(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking loan. " + e);
        }
    }

    /**
     * Takes {@link List} filled by definite player {@link Loan} objects.
     *
     * @param playerId id of player whose loans to take
     * @return {@link List} filled by definite player {@link Loan} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildLoanList(ResultSet)
     */
    @Override
    public List<Loan> takePlayerLoans(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PLAYER_ID)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildLoanList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking player loans. " + e);
        }
    }

    /**
     * Takes {@link List} filled by definite player {@link Loan} objects due to definite loan acquire date
     * pattern.
     *
     * @param playerId       id of player whose loans to take
     * @param acquirePattern pattern of loan acquire date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by definite player {@link Loan} objects or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildLoanList(ResultSet)
     */
    @Override
    public List<Loan> takePlayerLoans(int playerId, String acquirePattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PLAYER_LIKE_ACQUIRE)) {
            statement.setInt(1, playerId);
            statement.setString(2, acquirePattern);
            ResultSet resultSet = statement.executeQuery();
            return buildLoanList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking player loans. " + e);
        }
    }

    /**
     * Takes current unpaid {@link Loan} of definite player.
     *
     * @param playerId id of player whose loan to take
     * @return current unpaid {@link Loan} of definite player or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildLoan(ResultSet)
     */
    @Override
    public Loan takeCurrentLoan(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PLAYER_CURRENT)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildLoan(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking current loan. " + e);
        }
    }

    /**
     * Takes {@link List} filled by definite {@link Loan} objects due to definite loan acquire and expire
     * date patterns.
     *
     * @param acquirePattern pattern of loan acquire date conforming to <code>SQL LIKE</code> operator
     * @param expirePattern  pattern of loan expire date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by {@link Loan} objects with definite acquire and expire dates or null
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     * @see #buildLoanList(ResultSet)
     */
    @Override
    public List<Loan> takeLoanList(String acquirePattern, String expirePattern) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_LIKE_ACQUIRE_EXPIRE)) {
            statement.setString(1, acquirePattern);
            statement.setString(2, expirePattern);
            ResultSet resultSet = statement.executeQuery();
            return buildLoanList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking loans list. " + e);
        }
    }

    /**
     * Inserts loan to database.
     *
     * @param playerId id of player whose loan to insert
     * @param amount   amount of money player should return to pay loan
     * @param percent  percent multiplier used to count 'amount' value
     * @return int value of inserted loan generated id or 0
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String, int)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public int insertLoan(int playerId, BigDecimal amount, BigDecimal percent) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, playerId);
            statement.setBigDecimal(2, amount);
            statement.setDate(3, Date.valueOf(LocalDate.now().plusMonths(LOAN_TERM_MONTH)));
            statement.setBigDecimal(4, percent);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DAOException("Database connection error while inserting loan. " + e);
        }
    }

    /**
     * Updates loan 'amount_paid' value by adding definite value to it.
     *
     * @param loanId id of loan to update
     * @param amount amount of money to add to 'amount_paid' value
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     * @see WrappedConnection#prepareStatement(String)
     * @see PreparedStatement
     * @see ResultSet
     */
    @Override
    public boolean payLoan(int loanId, BigDecimal amount) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_AMOUNT_PAID_ADD)) {
            statement.setBigDecimal(1, amount);
            statement.setInt(2, loanId);
            if (statement.executeUpdate() == 1) {
                return true;
            } else {
                throw new DAOException("No or more than 1 loan associated with given id: '" + loanId + "'. Check database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Database connection error while paying loan. " + e);
        }
    }

    /**
     * Builds {@link Loan} object by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link Loan} object or null
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called
     *                      on a closed result set
     */
    private Loan buildLoan(ResultSet resultSet) throws SQLException {
        Loan loan = null;
        if (resultSet.next()) {
            loan = new Loan();
            loan.setId(resultSet.getInt(ID));
            loan.setPlayerId(resultSet.getInt(PLAYER_ID));
            loan.setAmount(resultSet.getBigDecimal(AMOUNTD));
            loan.setAcquire(resultSet.getDate(ACQUIRE).toLocalDate());
            loan.setExpire(resultSet.getDate(EXPIRE).toLocalDate());
            loan.setPercent(resultSet.getBigDecimal(PERCENT));
            loan.setRest(resultSet.getBigDecimal(AMOUNT_REST));
        }
        return loan;
    }

    /**
     * Builds {@link List} object filled by {@link Loan} objects by parsing {@link ResultSet} object.
     *
     * @param resultSet {@link ResultSet} object to parse
     * @return parsed {@link List} object or null
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called
     *                      on a closed result set
     * @see #buildLoan(ResultSet)
     */
    private List<Loan> buildLoanList(ResultSet resultSet) throws SQLException {
        List<Loan> loanList = new ArrayList<>();
        Loan       loan;
        do {
            loan = buildLoan(resultSet);
            if (loan != null) {
                loanList.add(loan);
            }
        } while (loan != null);
        return !loanList.isEmpty() ? loanList : null;
    }
}