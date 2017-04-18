package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.db.ConnectionPool;
import by.sasnouskikh.jcasino.db.WrappedConnection;
import by.sasnouskikh.jcasino.entity.bean.Loan;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * The class provides DAO abstraction for {@link Loan} objects.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AbstractDAO
 */
public abstract class LoanDAO extends AbstractDAO {

    /**
     * Column names of database table 'loan'.
     */
    protected static final String ID          = "id";
    protected static final String PLAYER_ID   = "player_id";
    protected static final String AMOUNTD     = "amount";
    protected static final String ACQUIRE     = "acquire";
    protected static final String EXPIRE      = "expire";
    protected static final String PERCENT     = "percent";
    protected static final String AMOUNT_PAID = "amount_paid";

    /**
     * Constructs DAO object by taking {@link WrappedConnection} object from {@link ConnectionPool} collection.
     *
     * @see AbstractDAO#AbstractDAO()
     */
    protected LoanDAO() {
    }

    /**
     * Constructs DAO object by assigning {@link AbstractDAO#connection} field definite
     * {@link WrappedConnection} object.
     *
     * @param connection {@link WrappedConnection} to assign to {@link AbstractDAO#connection} field
     * @see AbstractDAO#AbstractDAO(WrappedConnection)
     */
    protected LoanDAO(WrappedConnection connection) {
        super(connection);
    }

    /**
     * Takes {@link List} filled by definite player {@link Loan} objects.
     *
     * @param playerId id of player whose loans to take
     * @return {@link List} filled by definite player {@link Loan} objects
     * @throws DAOException if {@link Exception} occurred while working with database
     */
    public abstract List<Loan> takePlayerLoans(int playerId) throws DAOException;

    /**
     * Takes {@link List} filled by definite player {@link Loan} objects due to definite loan acquire date
     * pattern.
     *
     * @param playerId       id of player whose loans to take
     * @param acquirePattern pattern of loan acquire date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by definite player {@link Loan} objects
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Loan> takePlayerLoans(int playerId, String acquirePattern) throws DAOException;

    /**
     * Takes current unpaid {@link Loan} of definite player.
     *
     * @param playerId id of player whose loan to take
     * @return current unpaid {@link Loan} of definite player
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract Loan takeCurrentLoan(int playerId) throws DAOException;

    /**
     * Takes {@link List} filled by definite {@link Loan} objects due to definite loan acquire and expire
     * date patterns.
     *
     * @param acquirePattern pattern of loan acquire date conforming to <code>SQL LIKE</code> operator
     * @param expirePattern  pattern of loan expire date conforming to <code>SQL LIKE</code> operator
     * @return {@link List} filled by {@link Loan} objects with definite acquire and expire dates
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract List<Loan> takeLoanList(String acquirePattern, String expirePattern) throws DAOException;

    /**
     * Inserts loan to database.
     *
     * @param playerId id of player whose loan to insert
     * @param amount   amount of money player should return to pay loan
     * @param percent  percent multiplier used to count 'amount' value
     * @return int value of inserted loan id
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract int insertLoan(int playerId, BigDecimal amount, BigDecimal percent) throws DAOException;

    /**
     * Updates loan 'amount_paid' value by adding definite value to it.
     *
     * @param loanId id of loan to update
     * @param amount amount of money to add to 'amount_paid' value
     * @return true if operation processed successfully
     * @throws DAOException if {@link SQLException} occurred while working with database
     */
    public abstract boolean payLoan(int loanId, BigDecimal amount) throws DAOException;
}