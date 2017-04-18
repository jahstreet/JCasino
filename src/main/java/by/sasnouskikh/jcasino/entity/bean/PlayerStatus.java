package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The class represents info about application player account status.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class PlayerStatus extends Entity {

    /**
     * Player account status enumeration value.
     *
     * @see StatusEnum
     */
    private StatusEnum status;
    /**
     * Player bet limit for 1 line.
     */
    private BigDecimal betLimit;
    /**
     * Player withdrawal limit for 1 month.
     */
    private BigDecimal withdrawalLimit;
    /**
     * Player loan percent for taking loans.
     */
    private BigDecimal loanPercent;
    /**
     * Player max loan amount to take.
     */
    private BigDecimal maxLoan;
    /**
     * Admin id who changed {@link #status} value.
     */
    private int        adminId;
    /**
     * Admin commentary who changed {@link #status} value.
     */
    private String     commentary;

    /**
     * Enumeration of available {@link #status} value instances.
     */
    public enum StatusEnum {
        BASIC, VIP, BAN, UNACTIVE
    }

    /**
     * {@link #status} getter.
     *
     * @return {@link #status}
     * @see StatusEnum
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * {@link #status} setter.
     *
     * @param status player account status enumeration value
     * @see StatusEnum
     */
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
     * {@link #betLimit} getter.
     *
     * @return {@link #betLimit}
     */
    public BigDecimal getBetLimit() {
        return betLimit;
    }

    /**
     * {@link #betLimit} setter.
     *
     * @param betLimit player bet limit for 1 line
     */
    public void setBetLimit(BigDecimal betLimit) {
        this.betLimit = betLimit;
    }

    /**
     * {@link #withdrawalLimit} getter.
     *
     * @return {@link #withdrawalLimit}
     */
    public BigDecimal getWithdrawalLimit() {
        return withdrawalLimit;
    }

    /**
     * {@link #withdrawalLimit} setter.
     *
     * @param withdrawalLimit player withdrawal limit for 1 month
     */
    public void setWithdrawalLimit(BigDecimal withdrawalLimit) {
        this.withdrawalLimit = withdrawalLimit;
    }

    /**
     * {@link #loanPercent} getter.
     *
     * @return {@link #loanPercent}
     */
    public BigDecimal getLoanPercent() {
        return loanPercent;
    }

    /**
     * {@link #loanPercent} setter.
     *
     * @param loanPercent player loan percent for taking loans
     */
    public void setLoanPercent(BigDecimal loanPercent) {
        this.loanPercent = loanPercent;
    }

    /**
     * {@link #maxLoan} getter.
     *
     * @return {@link #maxLoan}
     */
    public BigDecimal getMaxLoan() {
        return maxLoan;
    }

    /**
     * {@link #maxLoan} setter.
     *
     * @param maxLoan player max loan amount to take
     */
    public void setMaxLoan(BigDecimal maxLoan) {
        this.maxLoan = maxLoan;
    }

    /**
     * {@link #adminId} getter.
     *
     * @return {@link #adminId}
     */
    public int getAdminId() {
        return adminId;
    }

    /**
     * {@link #adminId} setter.
     *
     * @param adminId admin id who changed {@link #status} value
     */
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    /**
     * {@link #commentary} getter.
     *
     * @return {@link #commentary}
     */
    public String getCommentary() {
        return commentary;
    }

    /**
     * {@link #commentary} setter.
     *
     * @param commentary admin commentary who changed {@link #status} value
     */
    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    /**
     * Checks if this instance equals given object.
     *
     * @param o object to compare with
     * @return true if this instance equals given object
     * @see Objects#equals(Object, Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerStatus)) {
            return false;
        }
        PlayerStatus status1 = (PlayerStatus) o;
        return adminId == status1.adminId &&
               status == status1.status &&
               Objects.equals(betLimit, status1.betLimit) &&
               Objects.equals(withdrawalLimit, status1.withdrawalLimit) &&
               Objects.equals(loanPercent, status1.loanPercent) &&
               Objects.equals(maxLoan, status1.maxLoan) &&
               Objects.equals(commentary, status1.commentary);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(status, betLimit, withdrawalLimit, loanPercent, maxLoan, adminId, commentary);
    }

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "PlayerStatus{" + "status=" + status +
               ", betLimit=" + betLimit +
               ", withdrawalLimit=" + withdrawalLimit +
               ", loanPercent=" + loanPercent +
               ", maxLoan=" + maxLoan +
               ", adminId=" + adminId +
               ", commentary='" + commentary + '\'' +
               '}';
    }

    /**
     * Clones instance of this object.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @see Cloneable
     */
    @Override
    protected PlayerStatus clone() throws CloneNotSupportedException {
        return (PlayerStatus) super.clone();
    }
}