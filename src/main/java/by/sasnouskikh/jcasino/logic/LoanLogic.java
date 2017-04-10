package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.impl.DAOFactory;
import by.sasnouskikh.jcasino.dao.impl.LoanDAOImpl;
import by.sasnouskikh.jcasino.dao.impl.PlayerDAOImpl;
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

import static by.sasnouskikh.jcasino.manager.ConfigConstant.EMPTY_STRING;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PERCENT;

public class LoanLogic {
    private static final Logger LOGGER = LogManager.getLogger(LoanLogic.class);

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private LoanLogic() {
    }

    public static List<Loan> takePlayerLoans(int id, String month) {
        String monthPattern;
        if (month != null) {
            monthPattern = month;
        } else {
            monthPattern = EMPTY_STRING;
        }
        monthPattern = monthPattern.trim() + PERCENT;
        List<Loan> loanList = null;
        try (LoanDAOImpl loanDAO = DAOFactory.getLoanDAO()) {
            loanList = loanDAO.takePlayerLoans(id, monthPattern);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return loanList;
    }

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
        try (LoanDAOImpl loanDAO = DAOFactory.getLoanDAO()) {
            loanList = loanDAO.takeLoanList(acquirePattern, expirePattern);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        if (filterNotPaid) {
            LoanLogic.filterNotPaid(loanList);
        }
        if (filterOverdued) {
            LoanLogic.filterOverdued(loanList);
        }
        if (sortByRest) {
            LoanLogic.sortByRest(loanList, false);
        }
        return loanList;
    }

    public static boolean takeNewLoan(Player player, BigDecimal amount) {
        int        id      = player.getId();
        BigDecimal percent = player.getAccount().getStatus().getLoanPercent();
        amount = LoanLogic.countLoan(amount, percent);
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            LoanDAOImpl loanDAO = DAOFactory.getLoanDAO(playerDAO.getConnection());
            playerDAO.beginTransaction();
            if (loanDAO.insertLoan(id, amount, percent) != 0
                && playerDAO.changeBalance(id, amount, Transaction.TransactionType.REPLENISH)) {
                playerDAO.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    public static boolean payLoan(Player player, BigDecimal amount) {
        int id     = player.getId();
        int loanId = player.getAccount().getCurrentLoan().getId();
        try (PlayerDAOImpl playerDAO = DAOFactory.getPlayerDAO()) {
            LoanDAOImpl loanDAO = DAOFactory.getLoanDAO(playerDAO.getConnection());
            playerDAO.beginTransaction();
            if (playerDAO.changeBalance(id, amount, Transaction.TransactionType.WITHDRAW)
                && loanDAO.payLoan(loanId, amount)) {
                playerDAO.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    static BigDecimal countLoan(BigDecimal amount, BigDecimal percent) {
        return amount.multiply(percent).divide(ONE_HUNDRED, BigDecimal.ROUND_HALF_UP).add(amount);
    }

    static void filterNotPaid(List<Loan> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        list.removeIf(l -> l.getRest().compareTo(BigDecimal.ZERO) == 0);
    }

    static void filterOverdued(List<Loan> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        filterNotPaid(list);
        list.removeIf(l -> l.getExpire().isAfter(LocalDate.now()));
    }

    static void sortByRest(List<Loan> list, boolean ascending) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Comparator<Loan> comparator = new RestComparator();
        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(list, comparator);
    }

    private static class RestComparator implements Comparator<Loan> {
        /**
         * Compares its two arguments for order.  Returns pressedKey negative integer,
         * zero, or pressedKey positive integer as the first argument is less than, equal
         * to, or greater than the second.<p>
         * <p>
         * In the foregoing description, the notation
         * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
         * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
         * <tt>0</tt>, or <tt>1</tt> according to whether the value of
         * <i>expression</i> is negative, zero or positive.<p>
         * <p>
         * The implementor must ensure that <tt>sgn(compare(x, y)) ==
         * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
         * implies that <tt>compare(x, y)</tt> must throw an exception if and only
         * if <tt>compare(y, x)</tt> throws an exception.)<p>
         * <p>
         * The implementor must also ensure that the relation is transitive:
         * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
         * <tt>compare(x, z)&gt;0</tt>.<p>
         * <p>
         * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
         * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
         * <tt>z</tt>.<p>
         * <p>
         * It is generally the case, but <i>not</i> strictly required that
         * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
         * any comparator that violates this condition should clearly indicate
         * this fact.  The recommended language is "Note: this comparator
         * imposes orderings that are inconsistent with equals."
         *
         * @param o1 the first object to be compared.
         * @param o2 the second object to be compared.
         * @return pressedKey negative integer, zero, or pressedKey positive integer as the
         * first argument is less than, equal to, or greater than the
         * second.
         * @throws NullPointerException if an argument is null and this
         *                              comparator does not permit null arguments
         * @throws ClassCastException   if the arguments' types prevent them from
         *                              being compared by this comparator.
         */
        @Override
        public int compare(Loan o1, Loan o2) {
            return o1.getRest().compareTo(o2.getRest());
        }
    }
}