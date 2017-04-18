package by.sasnouskikh.jcasino.game;

import static by.sasnouskikh.jcasino.game.ReelValue.*;

public class ReelValues {

    private ReelValue[] reelValues;

    public ReelValues() {
        reelValues = new ReelValue[]{
        CHERRY, GRAPES, CHERRY, LEMON, CHERRY, GRAPES, CHERRY, APPLE, CHERRY, GRAPES,
        CHERRY, GRAPES, CHERRY, LEMON, CHERRY, ORANGE, CHERRY, GRAPES, CHERRY, GRAPES,
        CHERRY, APPLE, CHERRY, LEMON, CHERRY, GRAPES, CHERRY, BANANA, CHERRY, GRAPES,
        CHERRY, GRAPES, CHERRY, ORANGE, CHERRY, GRAPES, CHERRY, APPLE, CHERRY, GRAPES,
        CHERRY, WATERMELON, CHERRY, LEMON, CHERRY, GRAPES, CHERRY, BANANA, CHERRY, GRAPES,
        CHERRY, APPLE, CHERRY, LEMON, CHERRY, GRAPES, CHERRY, ORANGE, CHERRY, GRAPES
        };
    }

    public ReelValues(ReelValue[] reelValues) {
        this.reelValues = reelValues;
    }

    public ReelValue[] getReelValues() {
        return reelValues;
    }

    public void setReelValues(ReelValue[] reelValues) {
        this.reelValues = reelValues;
    }
}