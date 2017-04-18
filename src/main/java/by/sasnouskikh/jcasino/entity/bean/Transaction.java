package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The class represents info about application transactions.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class Transaction extends Entity {

    /**
     * Transaction unique id.
     */
    private int             id;
    /**
     * Player id who made this transaction.
     */
    private int             playerId;
    /**
     * Transaction date.
     */
    private LocalDateTime   date;
    /**
     * Transaction amount.
     */
    private BigDecimal      amount;
    /**
     * Transaction type enumeration instance.
     *
     * @see TransactionType
     */
    private TransactionType type;

    /**
     * Enumeration of available {@link #type} value instances.
     */
    public enum TransactionType {
        REPLENISH, WITHDRAW
    }

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
     * @param id transaction unique id
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
     * @param playerId player id who made this transaction
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * {@link #date} getter.
     *
     * @return {@link #date}
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * {@link #date} setter.
     *
     * @param date transaction date
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
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
     * @param amount transaction amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * {@link #type} getter.
     *
     * @return {@link #type}
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * {@link #type} setter.
     *
     * @param type transaction type enumeration instance
     * @see TransactionType
     */
    public void setType(TransactionType type) {
        this.type = type;
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
        if (!(o instanceof Transaction)) {
            return false;
        }
        Transaction that = (Transaction) o;
        return id == that.id &&
               playerId == that.playerId &&
               Objects.equals(date, that.date) &&
               Objects.equals(amount, that.amount) &&
               type == that.type;
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, playerId, date, amount, type);
    }

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "Transaction{" + "id=" + id +
               ", playerId=" + playerId +
               ", date=" + date +
               ", amount=" + amount +
               ", type=" + type +
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
    protected Transaction clone() throws CloneNotSupportedException {
        return (Transaction) super.clone();
    }
}