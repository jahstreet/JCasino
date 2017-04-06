package by.sasnouskikh.jcasino.manager;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.LOCALE_EN;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.LOCALE_RU;

public class MessageManager {
    private static final Logger         LOGGER                = LogManager.getLogger(MessageManager.class);
    private static final String         PATH                  = "prop/messages";
    private static final MessageManager messageManagerUS      = new MessageManager(new Locale("en", "US"));
    private static final MessageManager messageManagerRU      = new MessageManager(new Locale("ru", "RU"));
    private static final MessageManager messageManagerDefault = new MessageManager(Locale.getDefault());
    private ResourceBundle bundle;

    private MessageManager(Locale locale) {
        bundle = ResourceBundle.getBundle(PATH, locale);
    }

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
     * method that returns value of property by key,
     * may throw RuntimeException if property not found with FATAL log
     *
     * @param key of parameters located in properties file
     * @return value of configuration property
     */
    public String getMessage(String key) {
        String property;
        try {
            property = bundle.getString(key);
        } catch (MissingResourceException e) {
            LOGGER.log(Level.FATAL, e);
            throw new RuntimeException(e);
        }
        return property;
    }


}
