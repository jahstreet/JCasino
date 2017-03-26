package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;

public class PlayerStats extends Entity {
    private BigDecimal maxBet;
    private BigDecimal totalBet;
    private BigDecimal maxWinRoll;
    private BigDecimal maxWinStreak;
    private BigDecimal totalWin;
    private BigDecimal maxPayment;
    private BigDecimal totalPayment;
    private BigDecimal maxWithdrawal;
    private BigDecimal totalWithdrawal;

    public BigDecimal getMaxBet() {
        return maxBet;
    }

    public void setMaxBet(BigDecimal maxBet) {
        this.maxBet = maxBet;
    }

    public BigDecimal getTotalBet() {
        return totalBet;
    }

    public void setTotalBet(BigDecimal totalBet) {
        this.totalBet = totalBet;
    }

    public BigDecimal getMaxWinRoll() {
        return maxWinRoll;
    }

    public void setMaxWinRoll(BigDecimal maxWinRoll) {
        this.maxWinRoll = maxWinRoll;
    }

    public BigDecimal getMaxWinStreak() {
        return maxWinStreak;
    }

    public void setMaxWinStreak(BigDecimal maxWinStreak) {
        this.maxWinStreak = maxWinStreak;
    }

    public BigDecimal getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(BigDecimal totalWin) {
        this.totalWin = totalWin;
    }

    public BigDecimal getMaxPayment() {
        return maxPayment;
    }

    public void setMaxPayment(BigDecimal maxPayment) {
        this.maxPayment = maxPayment;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getMaxWithdrawal() {
        return maxWithdrawal;
    }

    public void setMaxWithdrawal(BigDecimal maxWithdrawal) {
        this.maxWithdrawal = maxWithdrawal;
    }

    public BigDecimal getTotalWithdrawal() {
        return totalWithdrawal;
    }

    public void setTotalWithdrawal(BigDecimal totalWithdrawal) {
        this.totalWithdrawal = totalWithdrawal;
    }
}