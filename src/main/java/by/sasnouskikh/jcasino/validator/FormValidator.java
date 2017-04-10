package by.sasnouskikh.jcasino.validator;

import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ALL;

public class FormValidator {

    private static final int    MAX_EMAIL_LENGTH        = 320;
    private static final int    MAX_EMAIL_NAME_LENGTH   = 64;
    private static final int    MAX_EMAIL_DOMAIN_LENGTH = 255;
    private static final String EMAIL_SPLITERATOR       = "@";
    private static final int    EMAIL_PAIR_LENGTH       = 2;
    private static final int    MAX_QUESTION_LENGTH     = 64;
    private static final int    MAX_ANSWER_LENGTH       = 32;
    private static final int    MAX_SUPPORT_LENGTH      = 700;
    private static final int    MAX_NEWS_HEADER_LENGTH  = 45;
    private static final int    MAX_NEWS_TEXT_LENGTH    = 700;

    private static final String EMAIL_REGEX    = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*" +
                                                 "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\\w_-]{8,}$";
    private static final String NAME_REGEX     = "[A-Za-z ]{1,70}";
    private static final String PASSPORT_REGEX = "\\w{1,30}";
    private static final String AMOUNT_REGEX   = "^[0-9]{1,7}\\.?[0-9]{0,2}$";
    private static final String DATE_REGEX     = "^[12][0-9]{3}\\-((0[1-9])|(1[0-2]))$";
    private static final String FLOAT_REGEX     = "^[0-9]+\\.?[0-9]{0,2}$";

    public static boolean validateEmail(String email) {
        if (email == null || email.isEmpty()
            || email.length() > MAX_EMAIL_LENGTH
            || !matchPattern(email, EMAIL_REGEX)) {
            return false;
        }
        String[] emailPair = email.split(EMAIL_SPLITERATOR);
        if (emailPair.length != EMAIL_PAIR_LENGTH) {
            return false;
        }
        String name   = emailPair[0];
        String domain = emailPair[1];
        return name.length() <= MAX_EMAIL_NAME_LENGTH
               && domain.length() <= MAX_EMAIL_DOMAIN_LENGTH;
    }

    public static boolean validatePassword(String password) {
        return !(password == null || password.trim().isEmpty()) && matchPattern(password, PASSWORD_REGEX);
    }

    public static boolean validatePassword(String password, String passwordAgain) {
        return !(password == null || password.trim().isEmpty() || !password.equals(passwordAgain))
               && validatePassword(password);
    }

    public static boolean validateName(String name) {
        return name == null || name.trim().isEmpty() || matchPattern(name, NAME_REGEX);
    }

    public static boolean validateQuestion(String question) {
        return question == null || question.trim().isEmpty() || question.trim().length() <= MAX_QUESTION_LENGTH;
    }

    public static boolean validateAnswer(String answer) {
        return answer == null || answer.trim().isEmpty() || answer.trim().length() <= MAX_ANSWER_LENGTH;
    }

    public static boolean validateBirthdate(String birthdate) {
        if (birthdate == null || birthdate.trim().isEmpty()) {
            return false;
        }
        LocalDate date = LocalDate.parse(birthdate);
        LocalDate now  = LocalDate.now();
        return date.plusYears(18).isBefore(now) || date.plusYears(18).isEqual(now);

    }

    public static boolean validatePassport(String passport) {
        return passport != null && matchPattern(passport, PASSPORT_REGEX);
    }

    public static boolean validateAmount(String amount) {
        return amount != null && matchPattern(amount, AMOUNT_REGEX);
    }

    private static boolean matchPattern(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static boolean validateTopic(String topic) {
        if (topic == null || topic.trim().isEmpty()) {
            return false;
        }
        topic = topic.trim();
        for (Question.QuestionTopic questionTopic : Question.QuestionTopic.values()) {
            if (topic.equalsIgnoreCase(questionTopic.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateSupport(String text) {
        return text != null && !text.trim().isEmpty()
               && text.trim().length() <= MAX_SUPPORT_LENGTH;
    }

    public static boolean validateDateMonth(String date) {
        return !(date == null || date.trim().isEmpty())
               && matchPattern(date, DATE_REGEX);
    }

    public static boolean validateSatisfaction(String satisfaction) {
        if (satisfaction == null || satisfaction.trim().isEmpty()) {
            return false;
        }
        satisfaction = satisfaction.trim();
        for (Question.Satisfaction sat : Question.Satisfaction.values()) {
            if (satisfaction.equalsIgnoreCase(sat.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateTransactionType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return false;
        }
        type = type.trim();
        for (Transaction.TransactionType t : Transaction.TransactionType.values()) {
            if (type.equalsIgnoreCase(t.toString()) || type.equals(ALL)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateId(String id) {
        return !(id == null || id.trim().isEmpty())
               && StringUtils.isNumeric(id)
               && Integer.parseInt(id) > 0;
    }

    public static boolean validateNewsHeader(String header) {
        return header != null && !header.trim().isEmpty() && header.trim().length() <= MAX_NEWS_HEADER_LENGTH;
    }

    public static boolean validateNewsText(String text) {
        return text != null && !text.trim().isEmpty() && text.trim().length() <= MAX_NEWS_TEXT_LENGTH;
    }

    public static boolean isFloat(String source) {
        return source != null && matchPattern(source, AMOUNT_REGEX);
    }
}