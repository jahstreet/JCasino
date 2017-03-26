package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;

public class PlayerAccount extends Entity {

    private PlayerStatus status;
    private BigDecimal   balance;
    private BigDecimal   thisMonthWithdrawal;
    private Loan         currentLoan;

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getThisMonthWithdrawal() {
        return thisMonthWithdrawal;
    }

    public void setThisMonthWithdrawal(BigDecimal thisMonthWithdrawal) {
        this.thisMonthWithdrawal = thisMonthWithdrawal;
    }

    public Loan getCurrentLoan() {
        return currentLoan;
    }

    public void setCurrentLoan(Loan currentLoan) {
        this.currentLoan = currentLoan;
    }
}