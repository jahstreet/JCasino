package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.LoanDAO;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.EMPTY_STRING;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PERCENT;

/**
 * The class provides Logic layer actions with loans.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class LoanLogic {
    private static final Logger LOGGER = LogManager.getLogger(LoanLogic.class);

    /**
     * 100 {@link BigDecimal} value constant.
     */
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    /**
     * Outer forbidding to create this class instances.
     */
    private LoanLogic() {
    }

    /**
     * Calls DAO layer to take {@link List} collection of definite player {@link Loan} objects which were acquired on
     * given month.
     *
     * @param id    player id
     * @param month string representation of month value in format 'yyyy-mm'
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see LoanDAO#takePlayerLoans(int, String)
     */
    public static List<Loan> takePlayerLoans(int id, String month) {
        String acquirePattern;
        if (month != null) {
            acquirePattern = month;
        } else {
            acquirePattern = EMPTY_STRING;
        }
        acquirePattern = acquirePattern.trim() + PERCENT;
        List<Loan> loanList = null;
        try (DAOHelper daoHelper = new DAOHelper()) {
            LoanDAO loanDAO = daoHelper.getLoanDAO();
            loanList = loanDAO.takePlayerLoans(id, acquirePattern);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return loanList;
    }

    /**
     * Calls DAO layer to take {@link List} collection of {@link Loan} objects which were acquired on
     * given month, which expiration month matches given and sorts and filters the result if is needed.
     *
     * @param acquire        player id
     * @param expire         string representation of month value in format 'yyyy-mm'
     * @param sortByRest     is need to sort result by {@link Loan#rest} values
     * @param filterNotPaid  is need to filter result and delete paid loans
     * @param filterOverdued is need to filter result and keep only overdued loans
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see LoanDAO#takeLoanList(String, String)
     * @see #filterNotPaid(List)
     * @see #filterOverdued(List)
     * @see #sortByRest(List, boolean)
     */
    public static List<Loan> takeLoanList(String acquire, String expire, boolean sortByRest, boolean filterNotPaid, boolean filterOverdued) {
        List<Loan> loanList = null;
        String     acquirePattern;
        String     expirePattern;
        if (acquire != null && !acquire.trim().isEmpty()) {
            acquirePattern = acquire;
        } else {
            acquirePattern = EMPTY_STRING;
        }
        if (expire != null && !expire.trim().isEmpty()) {
            expirePattern = expire;
        } else {
            expirePattern = EMPTY_STRING;
        }
        acquirePattern = acquirePattern + PERCENT;
        expirePattern = expirePattern + PERCENT;
        try (DAOHelper daoHelper = new DAOHelper()) {
            LoanDAO loanDAO = daoHelper.getLoanDAO();
            loanList = loanDAO.takeLoanList(acquirePattern, expirePattern);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        if (filterNotPaid) {
            filterNotPaid(loanList);
        }
        if (filterOverdued) {
            filterOverdued(loanList);
        }
        if (sortByRest) {
            sortByRest(loanList, false);
        }
        return loanList;
    }

    /**
     * Takes new loan for definite player needs calling DAO layer to insert corresponding data to database and to change
     * player balance on the amount of loan.
     *
     * @param player player who takes the loan
     * @param amount amount of money player want to take
     * @return true if transaction proceeded successfully
     * @see DAOHelper
     * @see LoanDAO#insertLoan(int, BigDecimal, BigDecimal)
     * @see PlayerDAO#changeBalance(int, BigDecimal, Transaction.TransactionType)
     * @see #countLoan(BigDecimal, BigDecimal)
     */
    public static boolean takeNewLoan(Player player, BigDecimal amount) {
        int        id      = player.getId();
        BigDecimal percent = player.getAccount().getStatus().getLoanPercent();
        amount = countLoan(amount, percent);
        try (DAOHelper daoHelper = new DAOHelper()) {
            PlayerDAO playerDAO = daoHelper.getPlayerDAO();
            LoanDAO   loanDAO   = daoHelper.getLoanDAO();
            daoHelper.beginTransaction();
            if (loanDAO.insertLoan(id, amount, percent) != 0
                && playerDAO.changeBalance(id, amount, Transaction.TransactionType.REPLENISH)) {
                daoHelper.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    /**
     * Provides operation of paying loan by player. Calls DAO layer to change player balance and to update paying loan
     * data at database.
     *
     * @param player player who pays the loan
     * @param amount amount of money player want to pay
     * @return true if transaction proceeded successfully
     * @see DAOHelper
     * @see LoanDAO#payLoan(int, BigDecimal)
     * @see PlayerDAO#changeBalance(int, BigDecimal, Transaction.TransactionType)
     */
    public static boolean payLoan(Player player, BigDecimal amount) {
        int id     = player.getId();
        int loanId = player.getAccount().getCurrentLoan().getId();
        try (DAOHelper daoHelper = new DAOHelper()) {
            PlayerDAO playerDAO = daoHelper.getPlayerDAO();
            LoanDAO   loanDAO   = daoHelper.getLoanDAO();
            daoHelper.beginTransaction();
            if (playerDAO.changeBalance(id, amount, Transaction.TransactionType.WITHDRAW)
                && loanDAO.payLoan(loanId, amount)) {
                daoHelper.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    /**
     * Counts loan amount - amount of money player should return to pay loan back.
     *
     * @param amount  amount of money player want to take
     * @param percent loan percent (20 is 20% and OK, 0,20 is 0,2% and not 20%)
     * @return result amount
     * @see BigDecimal
     */
    private static BigDecimal countLoan(BigDecimal amount, BigDecimal percent) {
        return amount.multiply(percent).divide(ONE_HUNDRED, BigDecimal.ROUND_HALF_UP).add(amount);
    }

    /**
     * Filters given {@link List} collection of {@link Loan} objects deleting paid loans from it.
     *
     * @param list {@link List} to be filtered
     * @see List#removeIf(Predicate)
     */
    private static void filterNotPaid(List<Loan> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        list.removeIf(l -> l.getRest().compareTo(BigDecimal.ZERO) == 0);
    }

    /**
     * Filters given {@link List} collection of {@link Loan} objects keeping only overdued loans.
     *
     * @param list {@link List} to be filtered
     * @see List#removeIf(Predicate)
     */
    private static void filterOverdued(List<Loan> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        filterNotPaid(list);
        list.removeIf(l -> l.getExpire().isAfter(LocalDate.now()));
    }

    /**
     * Sorts given {@link List} collection of {@link Loan} objects by {@link Loan#rest} values in descending order.
     *
     * @param list      {@link List} to be sorted
     * @param ascending marker of sorting order
     * @see RestComparator
     * @see Collections#sort(List, Comparator)
     */
    private static void sortByRest(List<Loan> list, boolean ascending) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Comparator<Loan> comparator = new RestComparator();
        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(list, comparator);
    }

    /**
     * The class provides {@link Comparator} of {@link Loan} objects by their {@link Loan#rest} values in ascending
     * order for {@link #sortByRest(List, boolean)} method.
     */
    private static class RestComparator implements Comparator<Loan> {
        /**
         * Compares its two arguments for order.  Returns pressedKey negative integer,
         * zero, or pressedKey positive integer as the first argument is less than, equal
         * to, or greater than the second.<p>
         * In the foregoing description, the notation
         * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
         * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
         * <tt>0</tt>, or <tt>1</tt> according to whether the value of
         * <i>expression</i> is negative, zero or positive.<p>
         * The implementor must ensure that <tt>sgn(compare(x, y)) ==
         * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
         * implies that <tt>compare(x, y)</tt> must throw an exception if and only
         * if <tt>compare(y, x)</tt> throws an exception.)<p>
         * The implementor must also ensure that the relation is transitive:
         * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
         * <tt>compare(x, z)&gt;0</tt>.<p>
         * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
         * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
         * <tt>z</tt>.<p>
         * It is generally the case, but <i>not</i> strictly required that
         * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
         * any comparator that violates this condition should clearly indicate
         * this fact.  The recommended language is "Note: this comparator
         * imposes orderings that are inconsistent with equals."
         *
         * @param o1 the first object to be compared.
         * @param o2 the second object to be compared.
         * @return pressedKey negative integer, zero, or pressedKey positive integer as the first argument is less than,
         * equal to, or greater than the second.
         * @throws NullPointerException if an argument is null and this comparator does not permit null arguments
         * @throws ClassCastException   if the arguments' types prevent them from being compared by this comparator.
         */
        @Override
        public int compare(Loan o1, Loan o2) {
            return o1.getRest().compareTo(o2.getRest());
        }
    }
}