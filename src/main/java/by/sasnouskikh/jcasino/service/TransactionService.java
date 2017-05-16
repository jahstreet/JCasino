package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.TransactionDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides Service layer actions with transactions.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class TransactionService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger(TransactionService.class);

    /**
     * Default instance constructor.
     */
    public TransactionService() {
    }

    /**
     * Constructs instance using definite {@link DAOHelper} object.
     */
    public TransactionService(DAOHelper daoHelper) {
        super(daoHelper);
    }

    /**
     * Calls DAO layer to take {@link List} collection of definite player {@link Transaction} objects which were
     * processed on given month.
     *
     * @param id    player id
     * @param month string representation of month value in format 'yyyy-mm'
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see TransactionDAO#takePlayerTransactions(int, String)
     */
    public List<Transaction> takePlayerTransactions(int id, String month) {
        List<Transaction> transactionList = null;
        String            monthPattern    = (month != null ? month.trim() : EMPTY_STRING) + PERCENT;
        TransactionDAO    transactionDAO  = daoHelper.getTransactionDAO();
        try {
            transactionList = transactionDAO.takePlayerTransactions(id, monthPattern);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return transactionList;
    }

    /**
     * Calls DAO layer to take {@link List} collection of {@link Transaction} objects due to given parameters.
     *
     * @param filterByType string representation of {@link by.sasnouskikh.jcasino.entity.bean.Transaction.TransactionType}
     *                     value instance or 'all'
     * @param month        string representation of month value in format 'yyyy-mm'
     * @param sortByAmount is need to sort result collection by {@link Transaction#amount}
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see TransactionDAO#takeTransactionList(String)
     * @see #filterByType(List, Transaction.TransactionType)
     * @see #sortByAmount(List, boolean)
     */
    public List<Transaction> takeTransactionList(String filterByType, String month, boolean sortByAmount) {
        List<Transaction> transactionList = null;
        String            monthPattern    = (month != null ? month.trim() : EMPTY_STRING) + PERCENT;
        TransactionDAO    transactionDAO  = daoHelper.getTransactionDAO();
        try {
            transactionList = transactionDAO.takeTransactionList(monthPattern);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        if (filterByType != null && !ALL.equals(filterByType.trim()) && !filterByType.trim().isEmpty()) {
            filterByType(transactionList, Transaction.TransactionType.valueOf(filterByType.trim().toUpperCase()));
        }
        if (sortByAmount) {
            sortByAmount(transactionList, false);
        }
        return transactionList;
    }

    /**
     * Defines max payment value from {@link List} collection of {@link Transaction} objects.
     *
     * @param transactions {@link List} collection of {@link Transaction} objects to be filtered
     * @return defined {@link BigDecimal} value
     */
    static BigDecimal defineMaxPayment(List<Transaction> transactions) {
        BigDecimal maxPayment = BigDecimal.ZERO;
        if (transactions != null) {
            for (Transaction transaction : transactions) {
                BigDecimal                  amount = transaction.getAmount();
                Transaction.TransactionType type   = transaction.getType();
                if (type == Transaction.TransactionType.REPLENISH
                    && maxPayment.compareTo(amount) < 0) {
                    maxPayment = amount;
                }
            }
        }
        return maxPayment;
    }

    /**
     * Counts total payment value from {@link List} collection of {@link Transaction} objects.
     *
     * @param transactions {@link List} collection of {@link Transaction} objects to be filtered
     * @return counted {@link BigDecimal} value
     */
    static BigDecimal countTotalPayment(List<Transaction> transactions) {
        BigDecimal totalPayment = BigDecimal.ZERO;
        if (transactions != null) {
            for (Transaction transaction : transactions) {
                BigDecimal                  amount = transaction.getAmount();
                Transaction.TransactionType type   = transaction.getType();
                if (type == Transaction.TransactionType.REPLENISH) {
                    totalPayment = totalPayment.add(amount);
                }
            }
        }
        return totalPayment;
    }

    /**
     * Defines max withdrawal value from {@link List} collection of {@link Transaction} objects.
     *
     * @param transactions {@link List} collection of {@link Transaction} objects to be filtered
     * @return defined {@link BigDecimal} value
     */
    static BigDecimal defineMaxWithdrawal(List<Transaction> transactions) {
        BigDecimal maxWithdrawal = BigDecimal.ZERO;
        if (transactions != null) {
            for (Transaction transaction : transactions) {
                BigDecimal                  amount = transaction.getAmount();
                Transaction.TransactionType type   = transaction.getType();
                if (type == Transaction.TransactionType.WITHDRAW
                    && maxWithdrawal.compareTo(amount) < 0) {
                    maxWithdrawal = amount;
                }
            }
        }
        return maxWithdrawal;
    }

    /**
     * Counts total withdrawal value from {@link List} collection of {@link Transaction} objects.
     *
     * @param transactions {@link List} collection of {@link Transaction} objects to be filtered
     * @return counted {@link BigDecimal} value
     */
    static BigDecimal countTotalWithdrawal(List<Transaction> transactions) {
        BigDecimal totalWithdrawal = BigDecimal.ZERO;
        if (transactions != null) {
            for (Transaction transaction : transactions) {
                BigDecimal                  amount = transaction.getAmount();
                Transaction.TransactionType type   = transaction.getType();
                if (type == Transaction.TransactionType.WITHDRAW) {
                    totalWithdrawal = totalWithdrawal.add(amount);
                }
            }
        }
        return totalWithdrawal;
    }

    /**
     * Filters {@link List} collection of {@link Transaction} objects by removing {@link Transaction} objects of {@link
     * by.sasnouskikh.jcasino.entity.bean.Transaction.TransactionType} different from given.
     *
     * @param list {@link List} collection of {@link Transaction} objects to be filtered
     * @param type {@link by.sasnouskikh.jcasino.entity.bean.Transaction.TransactionType} value of {@link
     *             Transaction#type} field with which {@link Transaction} objects to keep
     * @see List#removeIf(Predicate)
     */
    private static void filterByType(List<Transaction> list, Transaction.TransactionType type) {
        if (list == null || list.isEmpty()) {
            return;
        }
        list.removeIf(s -> (s == null || s.getType() != type));
    }

    /**
     * Sorts {@link List} collection of {@link Transaction} objects by {@link Transaction#amount} field values.
     *
     * @param list      {@link List} collection of {@link Transaction} objects to be sorted
     * @param ascending marker of sort order
     * @see Collections#sort(List, Comparator)
     * @see AmountComparator
     */
    private static void sortByAmount(List<Transaction> list, boolean ascending) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Comparator<Transaction> comparator = new AmountComparator();
        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(list, comparator);
    }

    private static class AmountComparator implements Comparator<Transaction> {

        /**
         * Compares its two arguments for order.  Returns pressedKey negative integer,
         * zero, or pressedKey positive integer as the first argument is less than, equal
         * to, or greater than the second.
         * <p>In the foregoing description, the notation
         * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
         * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
         * <tt>0</tt>, or <tt>1</tt> according to whether the value of
         * <i>expression</i> is negative, zero or positive.
         * <p>The implementor must ensure that <tt>sgn(compare(x, y)) ==
         * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
         * implies that <tt>compare(x, y)</tt> must throw an exception if and only
         * if <tt>compare(y, x)</tt> throws an exception.)
         * <p>The implementor must also ensure that the relation is transitive:
         * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
         * <tt>compare(x, z)&gt;0</tt>.
         * <p>Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
         * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
         * <tt>z</tt>.
         * <p>It is generally the case, but <i>not</i> strictly required that
         * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
         * any comparator that violates this condition should clearly indicate
         * this fact.  The recommended language is "Note: this comparator
         * imposes orderings that are inconsistent with equals."
         * <p>Compares {@link Transaction} objects due to their {@link Transaction#amount} field values
         *
         * @param o1 the first object to be compared.
         * @param o2 the second object to be compared.
         * @return pressedKey negative integer, zero, or pressedKey positive integer as the first argument is less than,
         * equal to, or greater than the second.
         * @throws NullPointerException if an argument is null and this comparator does not permit null arguments
         * @throws ClassCastException   if the arguments' types prevent them from being compared by this comparator.
         */
        @Override
        public int compare(Transaction o1, Transaction o2) {
            if (o1 == null || o1.getAmount() == null) {
                return -1;
            }
            if (o2 == null || o2.getAmount() == null) {
                return 1;
            }
            return o1.getAmount().compareTo(o2.getAmount());
        }
    }
}