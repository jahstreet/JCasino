package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;

/**
 * The class represents info about slot-machine streak.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class Streak extends Entity {

    /**
     * Streak unique id.
     */
    private int id;
    /**
     * Player id who played this streak.
     */
    private int playerId;

    /**
     * Streak generation date.
     */
    private LocalDateTime    date;
    /**
     * Collection of streak {@link Roll} objects.
     */
    private ArrayDeque<Roll> rolls;

    /**
     * String in special format which contain info about reel position values of this streak rolls.
     */
    private String     roll;
    /**
     * {@link #roll} string encrypted by MD5 encryptor.
     */
    private String     rollMD5;
    /**
     * String in special format which contain info about reel offsets of this streak rolls.
     */
    private String     offset;
    /**
     * String in special format which contain info about played lines of this streak rolls.
     */
    private String     lines;
    /**
     * String in special format which contain info about 1 line bets of this streak rolls.
     */
    private String     bet;
    /**
     * String in special format which contain info about results of this streak rolls.
     */
    private String     result;
    /**
     * Streak total result.
     */
    private BigDecimal total;

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
     * @param id streak unique id
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
     * @param playerId player id who played this streak
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
     * @param date streak generation date
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * {@link #roll} getter.
     *
     * @return {@link #roll}
     */
    public String getRoll() {
        return roll;
    }

    /**
     * {@link #roll} setter.
     *
     * @param roll string in special format which contain info about reel position values of this streak rolls
     */
    public void setRoll(String roll) {
        this.roll = roll;
    }

    /**
     * {@link #rollMD5} getter.
     *
     * @return {@link #rollMD5}
     */
    public String getRollMD5() {
        return rollMD5;
    }

    /**
     * {@link #rollMD5} setter.
     *
     * @param rollMD5 {@link #roll} string encrypted by MD5 encryptor
     */
    public void setRollMD5(String rollMD5) {
        this.rollMD5 = rollMD5;
    }

    /**
     * {@link #offset} getter.
     *
     * @return {@link #offset}
     */
    public String getOffset() {
        return offset;
    }

    /**
     * {@link #offset} setter.
     *
     * @param offset string in special format which contain info about reel offsets of this streak rolls
     */
    public void setOffset(String offset) {
        this.offset = offset;
    }

    /**
     * {@link #lines} getter.
     *
     * @return {@link #lines}
     */
    public String getLines() {
        return lines;
    }

    /**
     * {@link #lines} setter.
     *
     * @param lines string in special format which contain info about played lines of this streak rolls
     */
    public void setLines(String lines) {
        this.lines = lines;
    }

    /**
     * {@link #bet} getter.
     *
     * @return {@link #bet}
     */
    public String getBet() {
        return bet;
    }

    /**
     * {@link #bet} setter.
     *
     * @param bet string in special format which contain info about 1 line bets of this streak rolls
     */
    public void setBet(String bet) {
        this.bet = bet;
    }

    /**
     * {@link #result} getter.
     *
     * @return {@link #result}
     */
    public String getResult() {
        return result;
    }

    /**
     * {@link #result} setter.
     *
     * @param result string in special format which contain info about results of this streak rolls
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * {@link #rolls} getter.
     *
     * @return {@link #rolls}
     */
    public ArrayDeque<Roll> getRolls() {
        return rolls;
    }

    /**
     * {@link #rolls} setter.
     *
     * @param rolls {@link List} of streak {@link Roll} objects
     */
    public void setRolls(ArrayDeque<Roll> rolls) {
        this.rolls = rolls;
    }

    /**
     * {@link #total} getter.
     *
     * @return {@link #total}
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * {@link #total} setter.
     *
     * @param total streak total result
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
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
        if (!(o instanceof Streak)) {
            return false;
        }
        Streak streak = (Streak) o;
        return id == streak.id &&
               playerId == streak.playerId &&
               Objects.equals(date, streak.date) &&
               Objects.equals(rolls, streak.rolls) &&
               Objects.equals(roll, streak.roll) &&
               Objects.equals(rollMD5, streak.rollMD5) &&
               Objects.equals(offset, streak.offset) &&
               Objects.equals(lines, streak.lines) &&
               Objects.equals(bet, streak.bet) &&
               Objects.equals(result, streak.result) &&
               Objects.equals(total, streak.total);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, playerId, date, rolls, roll, rollMD5, offset, lines, bet, result, total);
    }

    @Override
    public String toString() {
        return "Streak{" + "id=" + id +
               ", playerId=" + playerId +
               ", date=" + date +
               ", rolls=" + rolls +
               ", roll='" + roll + '\'' +
               ", rollMD5='" + rollMD5 + '\'' +
               ", offset='" + offset + '\'' +
               ", lines='" + lines + '\'' +
               ", bet='" + bet + '\'' +
               ", result='" + result + '\'' +
               ", total=" + total +
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
    protected Streak clone() throws CloneNotSupportedException {
        Streak           clone       = (Streak) super.clone();
        ArrayDeque<Roll> clonedRolls = new ArrayDeque<>();
        for (Roll roll : rolls) {
            clonedRolls.add(roll.clone());
        }
        clone.setRolls(clonedRolls);
        return clone;
    }
}