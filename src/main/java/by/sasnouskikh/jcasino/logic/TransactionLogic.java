package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.TransactionDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class TransactionLogic {
    private static final Logger LOGGER = LogManager.getLogger(TransactionLogic.class);

    private TransactionLogic() {
    }

    public static List<Transaction> takePlayerTransactions(int id, String month) {
        String monthPattern;
        if (month != null) {
            monthPattern = month;
        } else {
            monthPattern = EMPTY_STRING;
        }
        monthPattern = monthPattern.trim() + PERCENT;
        List<Transaction> transactionList = null;
        try (DAOHelper daoHelper = new DAOHelper()) {
            TransactionDAO transactionDAO = daoHelper.getTransactionDAO();
            transactionList = transactionDAO.takePlayerTransactions(id, monthPattern);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return transactionList;
    }

    public static List<Transaction> takeTransactionList(String type, String month, boolean sortByAmount) {
        List<Transaction> transactionList = null;
        String            monthPattern;
        if (month != null && !month.trim().isEmpty()) {
            monthPattern = month;
        } else {
            monthPattern = EMPTY_STRING;
        }
        monthPattern = monthPattern.trim() + PERCENT;
        try (DAOHelper daoHelper = new DAOHelper()) {
            TransactionDAO transactionDAO = daoHelper.getTransactionDAO();
            transactionList = transactionDAO.takeTransactionList(monthPattern);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        if (!ALL.equals(type)) {
            TransactionLogic.filterByType(transactionList, Transaction.TransactionType.valueOf(type.toUpperCase()));
        }
        if (sortByAmount) {
            TransactionLogic.sortByAmount(transactionList, false);
        }
        return transactionList;
    }

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
        return totalWithdrawal.abs();
    }

    private static void filterByType(List<Transaction> list, Transaction.TransactionType type) {
        if (list == null || list.isEmpty()) {
            return;
        }
        list.removeIf(s -> s.getType() != type);
    }

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
        public int compare(Transaction o1, Transaction o2) {
            return o1.getAmount().compareTo(o2.getAmount());
        }
    }
}