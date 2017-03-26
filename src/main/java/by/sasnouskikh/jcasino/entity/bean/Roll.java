package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.math.BigDecimal;
import java.util.Arrays;

public class Roll extends Entity {

    private int[]      roll;
    private int[]      offset;
    private boolean[]  lines;
    private BigDecimal bet;
    private BigDecimal result;

    public boolean[] getLines() {
        return lines;
    }

    public void setLines(boolean[] lines) {
        this.lines = lines;
    }

    public int[] getRoll() {
        return roll;
    }

    public void setRoll(int[] roll) {
        this.roll = roll;
    }

    public int[] getOffset() {
        return offset;
    }

    public void setOffset(int[] offset) {
        this.offset = offset;
    }

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Roll{");
        sb.append("lines=").append(Arrays.toString(lines));
        sb.append(", roll=").append(Arrays.toString(roll));
        sb.append(", offset=").append(Arrays.toString(offset));
        sb.append(", bet=").append(bet);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }
}