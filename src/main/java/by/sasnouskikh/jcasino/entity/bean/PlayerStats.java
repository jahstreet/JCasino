package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The class represents info about application player statistics.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class PlayerStats extends Entity {

    /**
     * Player max total bet ever made.
     */
    private BigDecimal maxBet;
    /**
     * Sum of player bets ever made.
     */
    private BigDecimal totalBet;
    /**
     * Max win result of results of player rolls ever made.
     */
    private BigDecimal maxWinRoll;
    /**
     * Max win result of results of player streaks ever made.
     */
    private BigDecimal maxWinStreak;
    /**
     * Sum of win results of player rolls ever made.
     */
    private BigDecimal totalWin;
    /**
     * Max replenish transaction amount player ever made.
     */
    private BigDecimal maxPayment;
    /**
     * Total replenish transaction amount player ever made sum.
     */
    private BigDecimal totalPayment;
    /**
     * Max withdraw transaction amount player ever made.
     */
    private BigDecimal maxWithdrawal;
    /**
     * Total withdraw transaction amount player ever made sum.
     */
    private BigDecimal totalWithdrawal;

    /**
     * {@link #maxBet} getter.
     *
     * @return {@link #maxBet}
     */
    public BigDecimal getMaxBet() {
        return maxBet;
    }

    /**
     * {@link #maxBet} setter.
     *
     * @param maxBet player max total bet ever made
     */
    public void setMaxBet(BigDecimal maxBet) {
        this.maxBet = maxBet;
    }

    /**
     * {@link #totalBet} getter.
     *
     * @return {@link #totalBet}
     */
    public BigDecimal getTotalBet() {
        return totalBet;
    }

    /**
     * {@link #totalBet} setter.
     *
     * @param totalBet sum of player bets ever made
     */
    public void setTotalBet(BigDecimal totalBet) {
        this.totalBet = totalBet;
    }

    /**
     * {@link #maxWinRoll} getter.
     *
     * @return {@link #maxWinRoll}
     */
    public BigDecimal getMaxWinRoll() {
        return maxWinRoll;
    }

    /**
     * {@link #maxWinRoll} setter.
     *
     * @param maxWinRoll max win result of results of player rolls ever made
     */
    public void setMaxWinRoll(BigDecimal maxWinRoll) {
        this.maxWinRoll = maxWinRoll;
    }

    /**
     * {@link #maxWinStreak} getter.
     *
     * @return {@link #maxWinStreak}
     */
    public BigDecimal getMaxWinStreak() {
        return maxWinStreak;
    }

    /**
     * {@link #maxWinStreak} setter.
     *
     * @param maxWinStreak max win result of results of player streaks ever made
     */
    public void setMaxWinStreak(BigDecimal maxWinStreak) {
        this.maxWinStreak = maxWinStreak;
    }

    /**
     * {@link #totalWin} getter.
     *
     * @return {@link #totalWin}
     */
    public BigDecimal getTotalWin() {
        return totalWin;
    }

    /**
     * {@link #totalWin} setter.
     *
     * @param totalWin sum of win results of player rolls ever made
     */
    public void setTotalWin(BigDecimal totalWin) {
        this.totalWin = totalWin;
    }

    /**
     * {@link #maxPayment} getter.
     *
     * @return {@link #maxPayment}
     */
    public BigDecimal getMaxPayment() {
        return maxPayment;
    }

    /**
     * {@link #maxPayment} setter.
     *
     * @param maxPayment max replenish transaction amount player ever made
     */
    public void setMaxPayment(BigDecimal maxPayment) {
        this.maxPayment = maxPayment;
    }

    /**
     * {@link #totalPayment} getter.
     *
     * @return {@link #totalPayment}
     */
    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    /**
     * {@link #totalPayment} setter.
     *
     * @param totalPayment total replenish transaction amount player ever made sum
     */
    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    /**
     * {@link #maxWithdrawal} getter.
     *
     * @return {@link #maxWithdrawal}
     */
    public BigDecimal getMaxWithdrawal() {
        return maxWithdrawal;
    }

    /**
     * {@link #maxWithdrawal} setter.
     *
     * @param maxWithdrawal max withdraw transaction amount player ever made
     */
    public void setMaxWithdrawal(BigDecimal maxWithdrawal) {
        this.maxWithdrawal = maxWithdrawal;
    }

    /**
     * {@link #totalWithdrawal} getter.
     *
     * @return {@link #totalWithdrawal}
     */
    public BigDecimal getTotalWithdrawal() {
        return totalWithdrawal;
    }

    /**
     * {@link #totalWithdrawal} setter.
     *
     * @param totalWithdrawal total withdraw transaction amount player ever made sum
     */
    public void setTotalWithdrawal(BigDecimal totalWithdrawal) {
        this.totalWithdrawal = totalWithdrawal;
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
        if (!(o instanceof PlayerStats)) {
            return false;
        }
        PlayerStats that = (PlayerStats) o;
        return Objects.equals(maxBet, that.maxBet) &&
               Objects.equals(totalBet, that.totalBet) &&
               Objects.equals(maxWinRoll, that.maxWinRoll) &&
               Objects.equals(maxWinStreak, that.maxWinStreak) &&
               Objects.equals(totalWin, that.totalWin) &&
               Objects.equals(maxPayment, that.maxPayment) &&
               Objects.equals(totalPayment, that.totalPayment) &&
               Objects.equals(maxWithdrawal, that.maxWithdrawal) &&
               Objects.equals(totalWithdrawal, that.totalWithdrawal);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(maxBet, totalBet, maxWinRoll, maxWinStreak, totalWin, maxPayment, totalPayment, maxWithdrawal, totalWithdrawal);
    }

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "PlayerStats{" + "maxBet=" + maxBet +
               ", totalBet=" + totalBet +
               ", maxWinRoll=" + maxWinRoll +
               ", maxWinStreak=" + maxWinStreak +
               ", totalWin=" + totalWin +
               ", maxPayment=" + maxPayment +
               ", totalPayment=" + totalPayment +
               ", maxWithdrawal=" + maxWithdrawal +
               ", totalWithdrawal=" + totalWithdrawal +
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
    public PlayerStats clone() throws CloneNotSupportedException {
        return (PlayerStats) super.clone();
    }
}