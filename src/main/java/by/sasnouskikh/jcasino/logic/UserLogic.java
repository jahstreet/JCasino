package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.impl.DAOFactory;
import by.sasnouskikh.jcasino.dao.impl.UserDAOImpl;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.mailer.MailerException;
import by.sasnouskikh.jcasino.mailer.MailerSSL;
import by.sasnouskikh.jcasino.manager.MessageManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.EMAIL_PATTERN_NEW_PASSWORD;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.WHITESPACE;

public class UserLogic {
    private static final Logger LOGGER = LogManager.getLogger(UserLogic.class);

    private UserLogic() {
    }

    public static JCasinoUser authorizeUser(String email, String password) {
        JCasinoUser user = null;
        email = email.toLowerCase().trim();
        password = JCasinoEncryptor.encryptMD5(password);

        try (UserDAOImpl userDAO = DAOFactory.getUserDAO()) {
            user = userDAO.authorizeUser(email, password);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        if (user != null) {
            if (user.getRole() == JCasinoUser.UserRole.PLAYER) {
                user = initPlayer(user);
            } else {
                user = initAdmin(user);
            }
        }
        return user;
    }

    private static Player initPlayer(JCasinoUser user) {
        Player player = new Player();
        player.setId(user.getId());
        player.setEmail(user.getEmail());
        player.setRole(JCasinoUser.UserRole.PLAYER);
        player.setRegistrationDate(user.getRegistrationDate());
        PlayerLogic.initPlayerInfo(player);
        return player;
    }

    private static Admin initAdmin(JCasinoUser user) {
        Admin admin = new Admin();
        admin.setId(user.getId());
        admin.setEmail(user.getEmail());
        admin.setRole(JCasinoUser.UserRole.PLAYER);
        admin.setRegistrationDate(user.getRegistrationDate());
//        AdminLogic.initAdmin(admin);
        return admin;
    }

    public static boolean changePassword(JCasinoUser user, String oldPassword, String newPassword) {
        int id = user.getId();
        oldPassword = JCasinoEncryptor.encryptMD5(oldPassword);
        newPassword = JCasinoEncryptor.encryptMD5(newPassword);
        try (UserDAOImpl userDAO = DAOFactory.getUserDAO()) {
            return userDAO.checkPassword(id, oldPassword)
                   && userDAO.changePassword(id, newPassword);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean recoverPassword(String email, String locale) {
        email = email.toLowerCase().trim();
        MessageManager messageManager = new MessageManager(locale);
        try (UserDAOImpl userDAO = DAOFactory.getUserDAO()) {
            if (userDAO.checkEmailExist(email)) {
                JCasinoUser user        = userDAO.takeUser(email);
                int         id          = user.getId();
                String      newPassword = RandomGenerator.generatePassword();
                if (!userDAO.changePassword(id, JCasinoEncryptor.encryptMD5(newPassword))) {
                    return false;
                }
                String userName = userDAO.definePlayerNameByEmail(email);
                if (userName == null || userName.trim().isEmpty()) {
                    userName = JCasinoUser.UserRole.PLAYER.toString();
                }
                String message = messageManager.getMessage(EMAIL_PATTERN_NEW_PASSWORD) + WHITESPACE + newPassword;
                return MailerSSL.sendEmail(userName, message, email);
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (MailerException e) {
            LOGGER.log(Level.ERROR, "E-mail wasn't sent while recovering password. " + e);
        }
        return false;
    }

    public static boolean checkPassword(JCasinoUser user, String password) {
        password = JCasinoEncryptor.encryptMD5(password);
        int id = user.getId();
        try (UserDAOImpl userDAO = DAOFactory.getUserDAO()) {
            return userDAO.checkPassword(id, password);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean sendSupport(JCasinoUser user, String email, String topic, String question) {
        email = email.trim().toLowerCase();
        topic = topic.trim().toLowerCase();
        question = question.trim();
        try (UserDAOImpl userDAO = DAOFactory.getUserDAO()) {
            if (user == null) {
                return userDAO.insertQuestion(email, topic, question);
            } else {
                int id = user.getId();
                return userDAO.insertQuestion(id, email, topic, question);
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }
}