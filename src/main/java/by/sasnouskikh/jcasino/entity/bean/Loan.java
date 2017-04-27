package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The class represents info about application player loan.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class Loan extends Entity {

    /**
     * Loan unique id.
     */
    private int        id;
    /**
     * Player id who took this loan.
     */
    private int        playerId;
    /**
     * Amount of money player should return to pay loan back.
     */
    private BigDecimal amount;
    /**
     * Loan date of acquiring.
     */
    private LocalDate  acquire;
    /**
     * Loan expiration date.
     */
    private LocalDate  expire;
    /**
     * Loan percent multiplier value while loan was taken.
     */
    private BigDecimal percent;
    /**
     * Amount of money player rest to return to pay loan back.
     */
    private BigDecimal rest;

    /**
     * {@link #id} getter.
     *
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }

    /**
     * {@link #id} setter.
     *
     * @param id loan unique id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * {@link #playerId} getter.
     *
     * @return {@link #playerId}
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * {@link #playerId} setter.
     *
     * @param playerId player id who took this loan
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * {@link #amount} getter.
     *
     * @return {@link #amount}
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * {@link #amount} setter.
     *
     * @param amount amount of money player should return to pay loan back
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * {@link #acquire} getter.
     *
     * @return {@link #acquire}
     */
    public LocalDate getAcquire() {
        return acquire;
    }

    /**
     * {@link #acquire} setter.
     *
     * @param acquire loan date of acquiring
     */
    public void setAcquire(LocalDate acquire) {
        this.acquire = acquire;
    }

    /**
     * {@link #expire} getter.
     *
     * @return {@link #expire}
     */
    public LocalDate getExpire() {
        return expire;
    }

    /**
     * {@link #expire} setter.
     *
     * @param expire loan expiration date
     */
    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }

    /**
     * {@link #percent} getter.
     *
     * @return {@link #percent}
     */
    public BigDecimal getPercent() {
        return percent;
    }

    /**
     * {@link #percent} setter.
     *
     * @param percent loan percent multiplier value while loan was taken
     */
    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    /**
     * {@link #rest} getter.
     *
     * @return {@link #rest}
     */
    public BigDecimal getRest() {
        return rest;
    }

    /**
     * {@link #rest} setter.
     *
     * @param rest amount of money player rest to return to pay loan back
     */
    public void setRest(BigDecimal rest) {
        this.rest = rest;
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
        if (!(o instanceof Loan)) {
            return false;
        }
        Loan loan = (Loan) o;
        return id == loan.id &&
               playerId == loan.playerId &&
               (Objects.equals(amount, loan.amount) || amount.compareTo(amount) == 0) &&
               Objects.equals(acquire, loan.acquire) &&
               Objects.equals(expire, loan.expire) &&
               (Objects.equals(percent, loan.percent) || percent.compareTo(loan.percent) == 0) &&
               (Objects.equals(rest, loan.rest) || rest.compareTo(loan.rest) == 0);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, playerId, amount, acquire, expire, percent, rest);
    }

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "Loan{" + "id=" + id +
               ", playerId=" + playerId +
               ", amount=" + amount +
               ", acquire=" + acquire +
               ", expire=" + expire +
               ", percent=" + percent +
               ", rest=" + rest +
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
    protected Loan clone() throws CloneNotSupportedException {
        return (Loan) super.clone();
    }
}