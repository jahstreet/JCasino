package by.sasnouskikh.jcasino.manager;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageManager {
    private static final Logger LOGGER = LogManager.getLogger(MessageManager.class);
    private static final String PATH   = "prop/messages";
    private ResourceBundle bundle;

    public MessageManager(String locale) {
        bundle = ResourceBundle.getBundle(PATH, Locale.forLanguageTag(locale));
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
