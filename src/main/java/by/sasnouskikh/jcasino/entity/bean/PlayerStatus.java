package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;

public class PlayerStatus extends Entity {
    private StatusEnum status;
    private BigDecimal betLimit;
    private BigDecimal withdrawalLimit;
    private BigDecimal loanPercent;
    private BigDecimal maxLoan;
    private int adminId;
    private String commentary;

    public enum StatusEnum {
        BASIC, VIP, BAN, UNACTIVE
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public BigDecimal getBetLimit() {
        return betLimit;
    }

    public void setBetLimit(BigDecimal betLimit) {
        this.betLimit = betLimit;
    }

    public BigDecimal getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public void setWithdrawalLimit(BigDecimal withdrawalLimit) {
        this.withdrawalLimit = withdrawalLimit;
    }

    public BigDecimal getLoanPercent() {
        return loanPercent;
    }

    public void setLoanPercent(BigDecimal loanPercent) {
        this.loanPercent = loanPercent;
    }

    public BigDecimal getMaxLoan() {
        return maxLoan;
    }

    public void setMaxLoan(BigDecimal maxLoan) {
        this.maxLoan = maxLoan;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}