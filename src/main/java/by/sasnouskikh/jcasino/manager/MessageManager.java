package by.sasnouskikh.jcasino.manager;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides access to '/resources/prop/messages' property file and actions with it.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class MessageManager {
    private static final Logger LOGGER = LogManager.getLogger(MessageManager.class);

    /**
     * Path to bundle 'messages'.
     */
    private static final String         PATH                  = "prop/messages";
    /**
     * The class instance with en_US locale.
     */
    private static final MessageManager messageManagerUS      = new MessageManager(new Locale("en", "US"));
    /**
     * The class instance with ru_RU locale.
     */
    private static final MessageManager messageManagerRU      = new MessageManager(new Locale("ru", "RU"));
    /**
     * The class instance with default locale.
     */
    private static final MessageManager messageManagerDefault = messageManagerRU;
    /**
     * {@link ResourceBundle} instance.
     */
    private ResourceBundle bundle;

    /**
     * Constructs this class instance with given locale.
     *
     * @param locale string representation of locale.
     * @see ResourceBundle
     */
    private MessageManager(Locale locale) {
        bundle = ResourceBundle.getBundle(PATH, locale);
    }

    /**
     * Takes this class instance with given locale.
     *
     * @param locale string representation of locale.
     * @return this class instance
     */
    public static MessageManager getMessageManager(String locale) {
        switch (locale) {
            case LOCALE_EN:
                return messageManagerUS;
            case LOCALE_RU:
                return messageManagerRU;
            default:
                return messageManagerDefault;
        }
    }

    public Locale getLocale() {
        return bundle.getLocale();
    }

    /**
     * Returns value of property by key.
     *
     * @param key of parameters located in properties file
     * @return value of property or empty string if property with given key doesn't exist
     */
    public String getMessage(String key) {
        String property;
        try {
            property = bundle.getString(key);
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ERROR, e);
            property = EMPTY_STRING;
        }
        return property;
    }


}
