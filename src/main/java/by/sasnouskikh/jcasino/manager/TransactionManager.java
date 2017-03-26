package by.sasnouskikh.jcasino.manager;

import by.sasnouskikh.jcasino.entity.bean.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class TransactionManager {

    private TransactionManager() {
    }

    public static BigDecimal defineMaxPayment(List<Transaction> transactions) {
        BigDecimal maxPayment = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getAmount();
            if (maxPayment.compareTo(amount) < 0) {
                maxPayment = amount;
            }
        }
        return maxPayment;
    }

    public static BigDecimal countTotalPayment(List<Transaction> transactions) {
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                totalPayment = totalPayment.add(amount);
            }
        }
        return totalPayment;
    }

    public static BigDecimal defineMaxWithdrawal(List<Transaction> transactions) {
        BigDecimal maxWithdrawal = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getAmount();
            if (maxWithdrawal.compareTo(amount) > 0) {
                maxWithdrawal = amount;
            }
        }
        return maxWithdrawal.abs();
    }

    public static BigDecimal countTotalWithdrawal(List<Transaction> transactions) {
        BigDecimal totalWithdrawal = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                totalWithdrawal = totalWithdrawal.add(amount);
            }
        }
        return totalWithdrawal.abs();
    }
}