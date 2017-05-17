package by.sasnouskikh.jcasino.validator;

import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormValidatorTest {

    @Before
    public void setUp() {
    }

    @Test
    public void validateEmailValidCheck() {
        String validEmail = "any@valid.by";

        Assert.assertTrue(FormValidator.validateEmail(validEmail));
    }

    @Test
    public void validateEmailInvalidCheck() {
        String inValidEmail = "anyInvalid@va_12lid.bbby";

        Assert.assertFalse(FormValidator.validateEmail(inValidEmail));
    }

    @Test
    public void validatePasswordValidCheck() {
        String validPassword = "S-a100500";

        Assert.assertTrue(FormValidator.validatePassword(validPassword));
    }

    @Test
    public void validatePasswordInvalidCheck() {
        String inValidPassword = "abc8284771";

        Assert.assertFalse(FormValidator.validatePassword(inValidPassword));
    }

    @Test
    public void validatePasswordMatchesCheck() {
        String firstPassword  = "S-a100500";
        String secondPassword = "S-a100500";

        Assert.assertTrue(FormValidator.validatePassword(firstPassword, secondPassword));
    }

    @Test
    public void validatePasswordNotMatchesCheck() {
        String firstPassword  = "S-a100500";
        String secondPassword = "Sa100500";

        Assert.assertFalse(FormValidator.validatePassword(firstPassword, secondPassword));
    }

    @Test
    public void validateNameValidCheck() {
        String validName = "AnyName";

        Assert.assertTrue(FormValidator.validateName(validName));
    }

    @Test
    public void validateNameInvalidCheck() {
        String invalidName = "любоеИмя";

        Assert.assertFalse(FormValidator.validateName(invalidName));
    }

    @Test
    public void validateQuestionValidCheck() {
        String validQuestion = "AnyQuestion";

        Assert.assertTrue(FormValidator.validateQuestion(validQuestion));
    }

    @Test
    public void validateQuestionInvalidCheck() {
        String invalidQuestion = "Any question which length is more than 64 symbols at least1234567890?";

        Assert.assertFalse(FormValidator.validateQuestion(invalidQuestion));
    }

    @Test
    public void validateAnswerValidCheck() {
        String validAnswer = "AnyAnswer";

        Assert.assertTrue(FormValidator.validateAnswer(validAnswer));
    }

    @Test
    public void validateAnswerInvalidCheck() {
        String invalidAnswer = "Any answer which length is more than 32 symbols.";

        Assert.assertFalse(FormValidator.validateAnswer(invalidAnswer));
    }

    @Test
    public void validateBirthdateValidCheck() {
        String validBirthdate = "1991-09-24";

        Assert.assertTrue(FormValidator.validateBirthdate(validBirthdate));
    }

    @Test
    public void validateBirthdateInvalidCheck() {
        String invalidBirthdate = LocalDate.now().minusYears(17)
                                           .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Assert.assertFalse(FormValidator.validateBirthdate(invalidBirthdate));
    }

    @Test
    public void validatePassportValidCheck() {
        String validPassport = "KH1731245";

        Assert.assertTrue(FormValidator.validatePassport(validPassport));
    }

    @Test
    public void validatePassportInvalidCheck() {
        String invalidPassport = "More than 30 symbols in passport number string.";

        Assert.assertFalse(FormValidator.validatePassport(invalidPassport));
    }

    @Test
    public void validateAmountValidCheck() {
        String validAmount = "20.1";

        Assert.assertTrue(FormValidator.validateAmount(validAmount));
    }

    @Test
    public void validateAmountInvalidCheck() {
        String invalidAmount = "8472.001";

        Assert.assertFalse(FormValidator.validateAmount(invalidAmount));
    }

    @Test
    public void validateTopicValidCheck() {
        String validTopic = Question.QuestionTopic.OTHER.toString().toLowerCase();

        Assert.assertTrue(FormValidator.validateTopic(validTopic));
    }

    @Test
    public void validateTopicInvalidCheck() {
        String invalidTopic = "anyInvalidTopic";

        Assert.assertFalse(FormValidator.validateTopic(invalidTopic));
    }

    @Test
    public void validateSupportValidCheck() {
        String validSupport = "anySupport";

        Assert.assertTrue(FormValidator.validateSupport(validSupport));
    }

    @Test
    public void validateSupportInvalidCheck() {
        StringBuilder invalidSupport = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            invalidSupport.append("123456789012345678901234567890123456");
        }

        Assert.assertFalse(FormValidator.validateSupport(invalidSupport.toString()));
    }

    @Test
    public void validateDateMonthValidCheck() {
        String validDateMonth = "2014-02";

        Assert.assertTrue(FormValidator.validateDateMonth(validDateMonth));
    }

    @Test
    public void validateDateMonthInvalidCheck() {
        String invalidDateMonth = "2014-02-11";

        Assert.assertFalse(FormValidator.validateDateMonth(invalidDateMonth));
    }

    @Test
    public void validateSatisfactionValidCheck() {
        String validSatisfaction = Question.Satisfaction.GOOD.toString().toLowerCase();

        Assert.assertTrue(FormValidator.validateSatisfaction(validSatisfaction));
    }

    @Test
    public void validateSatisfactionInvalidCheck() {
        String invalidSatisfaction = "anyInvalidSatisfaction";

        Assert.assertFalse(FormValidator.validateSatisfaction(invalidSatisfaction));
    }

    @Test
    public void validateTransactionTypeValidCheck() {
        String validTransactionType = Transaction.TransactionType.REPLENISH.toString().toLowerCase();

        Assert.assertTrue(FormValidator.validateTransactionType(validTransactionType));
    }

    @Test
    public void validateTransactionTypeInvalidCheck() {
        String invalidTransactionType = "anyInvalidTransactionType";

        Assert.assertFalse(FormValidator.validateTransactionType(invalidTransactionType));
    }

    @Test
    public void validateAccountStatusValidCheck() {
        String validAccountStatus = PlayerStatus.StatusEnum.BASIC.toString().toLowerCase();

        Assert.assertTrue(FormValidator.validateAccountStatus(validAccountStatus));
    }

    @Test
    public void validateAccountStatusInvalidCheck() {
        String invalidAccountStatus = "anyInvalidAccountStatus";

        Assert.assertFalse(FormValidator.validateAccountStatus(invalidAccountStatus));
    }

    @Test
    public void validateIdValidCheck() {
        String validId = "1";

        Assert.assertTrue(FormValidator.validateId(validId));
    }

    @Test
    public void validateIdInvalidCheck() {
        String invalidId = "0";

        Assert.assertFalse(FormValidator.validateId(invalidId));
    }

    @Test
    public void validateNewsHeaderValidCheck() {
        String validNewsHeader = "Header.";

        Assert.assertTrue(FormValidator.validateNewsHeader(validNewsHeader));
    }

    @Test
    public void validateNewsHeaderInvalidCheck() {
        String invalidNewsHeader = "Header which contains more than 45 symbols isn't valid.";

        Assert.assertFalse(FormValidator.validateNewsHeader(invalidNewsHeader));
    }

    @Test
    public void validateNewsTextValidCheck() {
        String validNewsText = "Text.";

        Assert.assertTrue(FormValidator.validateNewsText(validNewsText));
    }

    @Test
    public void validateNewsTextInvalidCheck() {
        StringBuilder invalidNewsText = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            invalidNewsText.append("123456789012345678901234567890123456");
        }

        Assert.assertFalse(FormValidator.validateNewsText(invalidNewsText.toString()));
    }

    @Test
    public void validateFloatAmountValidCheck() {
        String validAmount = "20.1";

        Assert.assertTrue(FormValidator.validateFloatAmount(validAmount));
    }

    @Test
    public void validateFloatAmountInvalidCheck() {
        String invalidAmount = "8472.001";

        Assert.assertFalse(FormValidator.validateFloatAmount(invalidAmount));
    }
    @Test
    public void validateNewsLocaleValidCheck() {
        String validLocale = "rU";

        Assert.assertTrue(FormValidator.validateNewsLocale(validLocale));
    }

    @Test
    public void validateNewsLocaleInvalidCheck() {
        String invalidLocale = "rus";

        Assert.assertFalse(FormValidator.validateNewsLocale(invalidLocale));
    }


}