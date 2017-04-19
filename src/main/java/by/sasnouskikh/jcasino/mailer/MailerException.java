package by.sasnouskikh.jcasino.mailer;

import java.security.PrivilegedActionException;

/**
 * The class provides custom Exception to throw at 'mailer' package classes.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Exception
 */
public class MailerException extends Exception {

    /**
     * Constructs pressedKey new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by pressedKey
     * call to {@link #initCause}.
     */
    public MailerException() {
    }

    /**
     * Constructs pressedKey new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * pressedKey call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                method.
     */
    public MailerException(String message) {
        super(message);
    }

    /**
     * Constructs pressedKey new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).  (A
     *                <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public MailerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs pressedKey new exception with the specified cause and pressedKey detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).  (A <tt>null</tt>
     *              value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public MailerException(Throwable cause) {
        super(cause);
    }
}