package by.sasnouskikh.jcasino.tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The class provides custom function to use on JSP pages which provides easier date and time formatting.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class JCasinoDateFormatter {

    /**
     * Prints if given to method date or time parameter is null.
     */
    private static final String NOT_AVAILABLE = "-";

    /**
     * Outer forbidding to create this class instances.
     */
    private JCasinoDateFormatter() {
    }

    /**
     * Formats given {@link LocalDateTime} object due to given pattern.
     *
     * @param localDateTime {@link LocalDateTime} object to format
     * @param pattern       format pattern
     * @return formatted string or {@link #NOT_AVAILABLE}
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ofPattern(pattern)) : NOT_AVAILABLE;
    }

    /**
     * Formats given {@link LocalDate} object due to given pattern.
     *
     * @param localDate {@link LocalDate} object to format
     * @param pattern   format pattern
     * @return formatted string or {@link #NOT_AVAILABLE}
     */
    public static String formatLocalDate(LocalDate localDate, String pattern) {
        return localDate != null ? localDate.format(DateTimeFormatter.ofPattern(pattern)) : NOT_AVAILABLE;
    }
}