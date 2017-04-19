package by.sasnouskikh.jcasino.game;

import static by.sasnouskikh.jcasino.game.ReelValue.*;

/**
 * The class provides container for the array of available reel value of slot-machine enumeration instances in order
 * they placed on reel.
 *
 * @author Sasnouskikh Aliaksandr
 * @see ReelValue
 */
public class ReelValues {

    /**
     * Container for the array of available reel value of slot-machine enumeration instances in order they placed on
     * reel.
     */
    private ReelValue[] reelValues;

    /**
     * Constructs the array of available reel value of slot-machine enumeration instances in order they placed on
     * reel. Public - to have an access from JSP (jsp:useBean).
     */
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

    /**
     * Constructs the array of available reel value of slot-machine enumeration instances based on definite array.
     *
     * @param reelValues array to init {@link #reelValues}
     */
    public ReelValues(ReelValue[] reelValues) {
        this.reelValues = reelValues;
    }

    /**
     * {@link #reelValues} getter.
     *
     * @return {@link #reelValues}
     */
    public ReelValue[] getReelValues() {
        return reelValues;
    }

    /**
     * {@link #reelValues} setter.
     *
     * @param reelValues array to init {@link #reelValues}
     */
    public void setReelValues(ReelValue[] reelValues) {
        this.reelValues = reelValues;
    }
}