package by.sasnouskikh.jcasino.logic;

import java.util.concurrent.ThreadLocalRandom;

public class RandomGenerator {
    private static final int EMAIL_CODE_LENGTH = 8;
    private static final int RANDOM_INT_BOUND  = 10;

    private static final int PASSWORD_MIN_LENGTH   = 8;
    private static final int ASCII_DIGIT_BOUND_BOT = 48;
    private static final int ASCII_DIGIT_BOUND_TOP = 57;
    private static final int ASCII_UPPER_BOUND_BOT = 65;
    private static final int ASCII_UPPER_BOUND_TOP = 90;
    private static final int ASCII_LOWER_BOUND_BOT = 97;
    private static final int ASCII_LOWER_BOUND_TOP = 122;

    private RandomGenerator() {
    }

    public static String generateEmailCode() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < EMAIL_CODE_LENGTH; i++) {
            builder.append(ThreadLocalRandom.current().nextInt(RANDOM_INT_BOUND));
        }
        return builder.toString();
    }

    public static String generatePassword() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf((char) ThreadLocalRandom.current().nextInt(ASCII_DIGIT_BOUND_BOT, ASCII_DIGIT_BOUND_TOP + 1)));
        builder.append(String.valueOf((char) ThreadLocalRandom.current().nextInt(ASCII_UPPER_BOUND_BOT, ASCII_UPPER_BOUND_TOP + 1)));
        for (int i = 2; i < PASSWORD_MIN_LENGTH; i++) {
            builder.append(String.valueOf((char) ThreadLocalRandom.current().nextInt(ASCII_LOWER_BOUND_BOT, ASCII_LOWER_BOUND_TOP + 1)));
        }
        return builder.toString();
    }
}