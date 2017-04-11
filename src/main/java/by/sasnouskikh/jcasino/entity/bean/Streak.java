package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayDeque;

public class Streak extends Entity {

    private int id;
    private int playerId;

    private LocalDateTime    date;
    private ArrayDeque<Roll> rolls;

    private String     roll;
    private String     rollMD5;
    private String     offset;
    private String     lines;
    private String     bet;
    private String     result;
    private BigDecimal total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getRollMD5() {
        return rollMD5;
    }

    public void setRollMD5(String rollMD5) {
        this.rollMD5 = rollMD5;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayDeque<Roll> getRolls() {
        return rolls;
    }

    public void setRolls(ArrayDeque<Roll> rolls) {
        this.rolls = rolls;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}