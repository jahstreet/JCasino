package by.sasnouskikh.jcasino.mailer;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * The class provides actions of e-mail sending.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class MailerSSL {

    /**
     * Access info of e-mail box and other settings.
     */
    private static final String USERNAME = "slots.jcasino@gmail.com";
    private static final String PASSWORD = "s-a100500";
    private static final Properties PROPS;

    static {
        PROPS = new Properties();
        PROPS.put("mail.smtp.host", "smtp.gmail.com");
        PROPS.put("mail.smtp.socketFactory.port", "465");
        PROPS.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        PROPS.put("mail.smtp.auth", "true");
        PROPS.put("mail.smtp.port", "465");
    }

    /**
     * Outer forbidding to create this class instances.
     */
    private MailerSSL() {
    }

    /**
     * Sends e-mail to definite e-mail address.
     *
     * @param subject name of subject
     * @param text    text of e-mail message
     * @param toEmail e-mail address
     * @return true if operation proceeded successfully
     * @throws MailerException if {@link MessagingException} occurred during e-mail sending
     */
    public static boolean sendEmail(String subject, String text, String toEmail) throws MailerException {
        Session session = Session.getDefaultInstance(PROPS, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            throw new MailerException("E-mail sending was failed. " + e);
        }
    }
}