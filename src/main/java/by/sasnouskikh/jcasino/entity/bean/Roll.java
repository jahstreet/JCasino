package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * The class represents info about slot-machine roll.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class Roll extends Entity {

    /**
     * Roll reel position values array.
     */
    private int[]      roll;
    /**
     * Roll reel offsets array.
     */
    private int[]      offset;
    /**
     * Roll played lines array.
     */
    private boolean[]  lines;
    /**
     * Roll 1 line bet.
     */
    private BigDecimal bet;
    /**
     * Roll result.
     */
    private BigDecimal result;

    /**
     * {@link #lines} getter.
     *
     * @return {@link #lines}
     */
    public boolean[] getLines() {
        return lines;
    }

    /**
     * {@link #lines} setter.
     *
     * @param lines roll played lines array
     */
    public void setLines(boolean[] lines) {
        this.lines = lines;
    }

    /**
     * {@link #roll} getter.
     *
     * @return {@link #roll}
     */
    public int[] getRoll() {
        return roll;
    }

    /**
     * {@link #roll} setter.
     *
     * @param roll roll reel position values array
     */
    public void setRoll(int[] roll) {
        this.roll = roll;
    }

    /**
     * {@link #offset} getter.
     *
     * @return {@link #offset}
     */
    public int[] getOffset() {
        return offset;
    }

    /**
     * {@link #offset} setter.
     *
     * @param offset roll reel offsets array
     */
    public void setOffset(int[] offset) {
        this.offset = offset;
    }

    /**
     * {@link #bet} getter.
     *
     * @return {@link #bet}
     */
    public BigDecimal getBet() {
        return bet;
    }

    /**
     * {@link #bet} setter.
     *
     * @param bet roll 1 line bet
     */
    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    /**
     * {@link #result} getter.
     *
     * @return {@link #result}
     */
    public BigDecimal getResult() {
        return result;
    }

    /**
     * {@link #result} setter.
     *
     * @param result roll result
     */
    public void setResult(BigDecimal result) {
        this.result = result;
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
        if (!(o instanceof Roll)) {
            return false;
        }
        Roll other = (Roll) o;
        return Arrays.equals(roll, other.roll) &&
               Arrays.equals(offset, other.offset) &&
               Arrays.equals(lines, other.lines) &&
               (Objects.equals(bet, other.bet) || bet.compareTo(other.bet) == 0) &&
               (Objects.equals(result, other.result) || result.compareTo(other.result) == 0);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(roll, offset, lines, bet, result);
    }

    @Override
    public String toString() {
        return "Roll{" + "roll=" + Arrays.toString(roll) +
               ", offset=" + Arrays.toString(offset) +
               ", lines=" + Arrays.toString(lines) +
               ", bet=" + bet +
               ", result=" + result +
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
    public Roll clone() throws CloneNotSupportedException {
        return (Roll) super.clone();
    }
}