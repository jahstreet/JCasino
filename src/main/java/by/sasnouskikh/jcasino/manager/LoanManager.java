package by.sasnouskikh.jcasino.manager;

import java.math.BigDecimal;

public class LoanManager {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private LoanManager() {
    }

    public static BigDecimal countLoan(BigDecimal amount, BigDecimal percent) {
        return amount.multiply(percent).divide(ONE_HUNDRED, BigDecimal.ROUND_HALF_UP).add(amount);
    }
}