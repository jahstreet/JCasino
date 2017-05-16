package by.sasnouskikh.jcasino.service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The class provides generation of pseudo-random numbers and values for Service layer need.
 *
 * @author Sasnouskikh Aliaksandr
 */
class RandomGenerator {

    /**
     * Constants for e-mail code generation
     */
    private static final int EMAIL_CODE_LENGTH = 8;
    private static final int RANDOM_INT_BOUND  = 10;

    /**
     * Constants for password generation
     */
    private static final int PASSWORD_MIN_LENGTH   = 8;
    private static final int ASCII_DIGIT_BOUND_BOT = 48;
    private static final int ASCII_DIGIT_BOUND_TOP = 57;
    private static final int ASCII_UPPER_BOUND_BOT = 65;
    private static final int ASCII_UPPER_BOUND_TOP = 90;
    private static final int ASCII_LOWER_BOUND_BOT = 97;
    private static final int ASCII_LOWER_BOUND_TOP = 122;

    /**
     * Outer forbidding to create this class instances.
     */
    private RandomGenerator() {
    }

    /**
     * Generates random e-mail code for player e-mail verification.
     *
     * @return e-mail code
     */
    static String generateEmailCode() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < EMAIL_CODE_LENGTH; i++) {
            builder.append(ThreadLocalRandom.current().nextInt(RANDOM_INT_BOUND));
        }
        return builder.toString();
    }

    /**
     * Generates random password.
     *
     * @return password
     */
    static String generatePassword() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf((char) ThreadLocalRandom.current().nextInt(ASCII_DIGIT_BOUND_BOT, ASCII_DIGIT_BOUND_TOP + 1)));
        builder.append(String.valueOf((char) ThreadLocalRandom.current().nextInt(ASCII_UPPER_BOUND_BOT, ASCII_UPPER_BOUND_TOP + 1)));
        for (int i = 2; i < PASSWORD_MIN_LENGTH; i++) {
            builder.append(String.valueOf((char) ThreadLocalRandom.current().nextInt(ASCII_LOWER_BOUND_BOT, ASCII_LOWER_BOUND_TOP + 1)));
        }
        return builder.toString();
    }

    /**
     * Generates random int number between given values.
     *
     * @param from random number bottom value including
     * @param to   random number top value including
     * @return generated number
     */
    static int generateNumber(int from, int to) {
        if (to <= from) {
            to = from;
        }
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }
}