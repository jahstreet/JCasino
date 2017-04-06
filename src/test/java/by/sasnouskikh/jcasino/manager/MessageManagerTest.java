package by.sasnouskikh.jcasino.manager;

import java.util.Locale;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class MessageManagerTest {

    public static void main(String[] args) {
        MessageManager manager = MessageManager.getMessageManager("default");
        System.out.println(manager.getLocale());
        System.out.println(manager.getMessage(MESSAGE_INVALID_PASSWORD));
    }
}