package by.sasnouskikh.jcasino.tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JCasinoDateFormatter {
    private static final String NOT_AVAILABLE = "-";

    private JCasinoDateFormatter() {
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ofPattern(pattern)) : NOT_AVAILABLE;
    }

    public static String formatLocalDate(LocalDate localDate, String pattern) {
        return localDate != null ? localDate.format(DateTimeFormatter.ofPattern(pattern)) : NOT_AVAILABLE;
    }
}