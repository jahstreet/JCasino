package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The class represents info about application player account.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class PlayerAccount extends Entity {

    /**
     * Object which contains player status data.
     */
    private PlayerStatus status;
    /**
     * Player balance value.
     */
    private BigDecimal   balance;
    /**
     * Player current month withdrawal value.
     */
    private BigDecimal   thisMonthWithdrawal;
    /**
     * Object which contains player current unpaid {@link Loan} data.
     */
    private Loan         currentLoan;

    /**
     * {@link #status} getter.
     *
     * @return {@link #status}
     */
    public PlayerStatus getStatus() {
        return status;
    }

    /**
     * {@link #status} setter.
     *
     * @param status object which contains player status data
     */
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    /**
     * {@link #balance} getter.
     *
     * @return {@link #balance}
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * {@link #balance} setter.
     *
     * @param balance player balance value
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * {@link #thisMonthWithdrawal} getter.
     *
     * @return {@link #thisMonthWithdrawal}
     */
    public BigDecimal getThisMonthWithdrawal() {
        return thisMonthWithdrawal;
    }

    /**
     * {@link #thisMonthWithdrawal} setter.
     *
     * @param thisMonthWithdrawal player current month withdrawal value
     */
    public void setThisMonthWithdrawal(BigDecimal thisMonthWithdrawal) {
        this.thisMonthWithdrawal = thisMonthWithdrawal;
    }

    /**
     * {@link #currentLoan} getter.
     *
     * @return {@link #currentLoan}
     */
    public Loan getCurrentLoan() {
        return currentLoan;
    }

    /**
     * {@link #currentLoan} setter.
     *
     * @param currentLoan object which contains player current unpaid {@link Loan} data
     */
    public void setCurrentLoan(Loan currentLoan) {
        this.currentLoan = currentLoan;
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
        if (!(o instanceof PlayerAccount)) {
            return false;
        }
        PlayerAccount account = (PlayerAccount) o;
        return Objects.equals(status, account.status) &&
               (Objects.equals(balance, account.balance) || balance.compareTo(account.balance) == 0) &&
               (Objects.equals(thisMonthWithdrawal, account.thisMonthWithdrawal)
                || thisMonthWithdrawal.compareTo(account.thisMonthWithdrawal) == 0) &&
               Objects.equals(currentLoan, account.currentLoan);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(status, balance, thisMonthWithdrawal, currentLoan);
    }

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "PlayerAccount{" + "status=" + status +
               ", balance=" + balance +
               ", thisMonthWithdrawal=" + thisMonthWithdrawal +
               ", currentLoan=" + currentLoan +
               '}';
    }

    /**
     * Clones instance of this object.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not support the {@code Cloneable} interface.
     *                                    Subclasses that override the {@code clone} method can also throw this
     *                                    exception to indicate that an instance cannot be cloned.
     * @see Cloneable
     */
    @Override
    public PlayerAccount clone() throws CloneNotSupportedException {
        PlayerAccount clone = (PlayerAccount) super.clone();
        clone.setStatus(status.clone());
        clone.setCurrentLoan(currentLoan.clone());
        return clone;
    }
}