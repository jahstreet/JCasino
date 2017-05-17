package by.sasnouskikh.jcasino.validator;

import by.sasnouskikh.jcasino.entity.bean.News;
import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ALL;

/**
 * The class provides form input parameters sent with requests and other values validation service.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class FormValidator {

    /**
     * Validation constants.
     */
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
    /**
     * Validation regular expressions.
     */
    private static final String EMAIL_REGEX             = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*" +
                                                          "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String PASSWORD_REGEX          = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\\w_-]{8,}$";
    private static final String NAME_REGEX              = "[A-Za-z ]{1,70}";
    private static final String PASSPORT_REGEX          = "\\w{1,30}";
    private static final String AMOUNT_REGEX            = "^[0-9]{1,7}\\.?[0-9]{0,2}$";
    private static final String DATE_MONTH_REGEX        = "^[12][0-9]{3}\\-((0[1-9])|(1[0-2]))$";

    /**
     * Private constructor to forbid create {@link FormValidator} instances.
     */
    private FormValidator() {
    }

    /**
     * Validates user e-mail.
     *
     * @param email user e-mail
     * @return true if e-mail valid
     */
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

    /**
     * Validates user password.
     *
     * @param password user password
     * @return true if password valid
     */
    public static boolean validatePassword(String password) {
        return !(password == null || password.trim().isEmpty()) && matchPattern(password, PASSWORD_REGEX);
    }

    /**
     * Validates user password and compares it with password entered again.
     *
     * @param password      user password
     * @param passwordAgain user password entered again
     * @return true if password valid and matches to entered again
     */
    public static boolean validatePassword(String password, String passwordAgain) {
        return !(password == null || password.trim().isEmpty() || !password.equals(passwordAgain))
               && validatePassword(password);
    }

    /**
     * Validates user name.
     *
     * @param name user name
     * @return true if name is valid
     */
    public static boolean validateName(String name) {
        return name == null || name.trim().isEmpty() || matchPattern(name, NAME_REGEX);
    }

    /**
     * Validates user secret question.
     *
     * @param question user secret question
     * @return true if question is valid
     */
    public static boolean validateQuestion(String question) {
        return question == null || question.trim().isEmpty() || question.trim().length() <= MAX_QUESTION_LENGTH;
    }

    /**
     * Validates user answer to secret question.
     *
     * @param answer user answer to secret question
     * @return true if answer is valid
     */
    public static boolean validateAnswer(String answer) {
        return answer == null || answer.trim().isEmpty() || answer.trim().length() <= MAX_ANSWER_LENGTH;
    }

    /**
     * Validates user birthdate.
     *
     * @param birthdate user birthdate
     * @return true if birthdate is valid
     */
    public static boolean validateBirthdate(String birthdate) {
        if (birthdate == null || birthdate.trim().isEmpty()) {
            return false;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(birthdate);
        } catch (DateTimeParseException e) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return date.plusYears(18).isBefore(now) || date.plusYears(18).isEqual(now);

    }

    /**
     * Validates user passport number.
     *
     * @param passport user passport number
     * @return true if passport number is valid
     */
    public static boolean validatePassport(String passport) {
        return passport != null && matchPattern(passport, PASSPORT_REGEX);
    }

    /**
     * Validates string representation of decimal amount value.
     *
     * @param amount string representation of decimal amount value
     * @return true if amount is valid
     */
    public static boolean validateAmount(String amount) {
        return amount != null && matchPattern(amount, AMOUNT_REGEX);
    }

    /**
     * Validates string representation of support question topic.
     *
     * @param topic string representation of support question topic
     * @return true if topic is valid
     * @see by.sasnouskikh.jcasino.entity.bean.Question.QuestionTopic
     */
    public static boolean validateTopic(String topic) {
        if (topic == null || topic.trim().isEmpty()) {
            return true;
        }
        topic = topic.trim();
        for (Question.QuestionTopic questionTopic : Question.QuestionTopic.values()) {
            if (topic.equalsIgnoreCase(questionTopic.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates support question or answer text.
     *
     * @param text support question or answer text
     * @return true if text is valid
     */
    public static boolean validateSupport(String text) {
        return text != null && !text.trim().isEmpty()
               && text.trim().length() <= MAX_SUPPORT_LENGTH;
    }

    /**
     * Validates string representation of date month in 'yyyy-mm' format.
     *
     * @param month string representation of date month in 'yyyy-mm' format
     * @return true if month is valid
     */
    public static boolean validateDateMonth(String month) {
        return month == null || month.trim().isEmpty()
               || matchPattern(month, DATE_MONTH_REGEX);
    }

    /**
     * Validates string representation of player satisfaction with admin answer to support question.
     *
     * @param satisfaction string representation of player satisfaction with admin answer to support question
     * @return true if satisfaction is valid
     * @see by.sasnouskikh.jcasino.entity.bean.Question.Satisfaction
     */
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

    /**
     * Validates string representation of transaction type.
     *
     * @param type string representation of transaction type
     * @return true if type is valid
     * @see by.sasnouskikh.jcasino.entity.bean.Transaction.TransactionType
     * @see by.sasnouskikh.jcasino.manager.ConfigConstant#ALL
     */
    public static boolean validateTransactionType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return true;
        }
        type = type.trim();
        for (Transaction.TransactionType t : Transaction.TransactionType.values()) {
            if (type.equalsIgnoreCase(t.toString()) || type.equals(ALL)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates string representation of {@link by.sasnouskikh.jcasino.entity.bean.PlayerStatus.StatusEnum} value.
     *
     * @param status string representation of {@link by.sasnouskikh.jcasino.entity.bean.PlayerStatus.StatusEnum} value
     * @return true if value is valid
     */
    public static boolean validateAccountStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }
        status = status.trim();
        for (PlayerStatus.StatusEnum s : PlayerStatus.StatusEnum.values()) {
            if (status.equalsIgnoreCase(s.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates string representation of id int number.
     *
     * @param id string representation of id int number
     * @return true if id is valid
     * @see StringUtils#isNumeric(String)
     */
    public static boolean validateId(String id) {
        return !(id == null || id.trim().isEmpty())
               && StringUtils.isNumeric(id)
               && Integer.parseInt(id) > 0;
    }

    /**
     * Validates news header.
     *
     * @param header news header
     * @return true if header is valid
     */
    public static boolean validateNewsHeader(String header) {
        return header != null && !header.trim().isEmpty() && header.trim().length() <= MAX_NEWS_HEADER_LENGTH;
    }

    /**
     * Validates news text.
     *
     * @param text news text
     * @return true if text is valid
     */
    public static boolean validateNewsText(String text) {
        return text != null && !text.trim().isEmpty() && text.trim().length() <= MAX_NEWS_TEXT_LENGTH;
    }

    /**
     * Validates string representation of float number.
     *
     * @param source string representation of float number
     * @return true if source is valid
     */
    public static boolean validateFloatAmount(String source) {
        return source != null && matchPattern(source, AMOUNT_REGEX);
    }

    /**
     * Validates string representation of news locale.
     *
     * @param locale string representation of news locale
     * @return true if locale is valid
     */
    public static boolean validateNewsLocale(String locale) {
        if (locale == null || locale.trim().isEmpty()) {
            return true;
        }
        locale = locale.trim().toUpperCase();
        for (News.NewsLocale l : News.NewsLocale.values()) {
            if (locale.equals(l.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if given string matches regular expression pattern.
     *
     * @param string string value
     * @param regex  string regular expression
     * @return true if string matches pattern is valid
     * @see Pattern
     * @see Matcher
     */
    private static boolean matchPattern(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}