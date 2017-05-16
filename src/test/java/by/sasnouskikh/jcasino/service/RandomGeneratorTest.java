package by.sasnouskikh.jcasino.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class RandomGeneratorTest {

    private static final String EMAIL_CODE_PATTERN = "\\d{8}";
    private static final String PASSWORD_PATTERN   = "\\d[A-Z][a-z]{6}";

    @Test
    public void generateEmailCodeCheck() throws Exception {
        String emailCode = Whitebox.invokeMethod(RandomGenerator.class, "generateEmailCode");

        Assert.assertTrue("Generated string should match pattern: " + EMAIL_CODE_PATTERN,
                          matchPattern(emailCode, EMAIL_CODE_PATTERN));
    }

    @Test
    public void generatePasswordCheck() throws Exception {
        String password = Whitebox.invokeMethod(RandomGenerator.class, "generatePassword");

        Assert.assertTrue("Generated string should match pattern: " + PASSWORD_PATTERN,
                          matchPattern(password, PASSWORD_PATTERN));
    }

    @Test
    public void generateNumberCheck() throws Exception {
        int    from    = 1;
        int    to      = 15;
        String pattern = "[" + from + "-" + to + "]";

        int number = Whitebox.invokeMethod(RandomGenerator.class, "generateNumber", from, to);

        Assert.assertTrue("Generated number should match pattern: " + pattern + ". Generated number: " + number,
                          number >= from && number <= to);
    }

    @Test
    public void generateNumberToLtFromCheck() throws Exception {
        int from     = 2;
        int to       = 1;
        int expected = 2;

        int actual = Whitebox.invokeMethod(RandomGenerator.class, "generateNumber", from, to);

        Assert.assertEquals("Generated number should be equals: " + expected + ". Generated number: " + actual,
                            expected, actual);
    }

    private static boolean matchPattern(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}