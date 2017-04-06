package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.dao.AbstractDAO;
import by.sasnouskikh.jcasino.dao.DAOException;
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

public class LoanDAOImpl extends AbstractDAO<Integer, Loan> {

    private static final String SQL_SELECT_BY_PLAYER_ID        = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                                 "FROM loan " +
                                                                 "WHERE player_id=? " +
                                                                 "ORDER BY expire DESC";
    private static final String SQL_SELECT_PLAYER_LIKE_ACQUIRE = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                                 "FROM loan " +
                                                                 "WHERE player_id=? AND acquire LIKE ? " +
                                                                 "ORDER BY expire DESC";
    private static final String SQL_SELECT_PLAYER_CURRENT      = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                                 "FROM loan " +
                                                                 "WHERE player_id=? AND amount>amount_paid";
    private static final String SQL_SELECT_LIKE_ACQUIRE_EXPIRE = "SELECT id, player_id, amount, acquire, expire, percent, amount-amount_paid AS rest " +
                                                                 "FROM loan " +
                                                                 "WHERE acquire LIKE ? AND expire LIKE ? " +
                                                                 "ORDER BY expire DESC";
    private static final String SQL_INSERT                     = "INSERT INTO loan (player_id, amount, acquire, expire, percent) " +
                                                                 "VALUES (?, ?, NOW(), ?, ?)";
    private static final String SQL_UPDATE_AMOUNT_PAID_ADD     = "UPDATE loan " +
                                                                 "SET amount_paid=amount_paid+? " +
                                                                 "WHERE id=?";

    private static final int LOAN_TERM_MONTH = 1;

    LoanDAOImpl() {
    }

    LoanDAOImpl(WrappedConnection connection) {
        super(connection);
    }

    public List<Loan> takePlayerLoans(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PLAYER_ID)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildLoanList(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking player loans. " + e);
        }
    }

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

    public Loan takeCurrentLoan(int playerId) throws DAOException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PLAYER_CURRENT)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            return buildLoan(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Database connection error while taking current loan. " + e);
        }
    }

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

    private Loan buildLoan(ResultSet resultSet) throws SQLException {
        Loan loan = null;
        if (resultSet.next()) {
            loan = new Loan();
            loan.setId(resultSet.getInt("id"));
            loan.setPlayerId(resultSet.getInt("player_id"));
            loan.setAmount(resultSet.getBigDecimal("amount"));
            loan.setAcquire(resultSet.getDate("acquire").toLocalDate());
            loan.setExpire(resultSet.getDate("expire").toLocalDate());
            loan.setPercent(resultSet.getBigDecimal("percent"));
            loan.setRest(resultSet.getBigDecimal("rest"));
        }
        return loan;
    }

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