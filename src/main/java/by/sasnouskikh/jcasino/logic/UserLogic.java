package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.manager.JCasinoEncryptor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class provides Logic layer actions available for user.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class UserLogic {
    private static final Logger LOGGER = LogManager.getLogger(UserLogic.class);

    /**
     * Outer forbidding to create this class instances.
     */
    private UserLogic() {
    }

    /**
     * Provides authorisation operation logic for user. Calls DAO layer to init {@link JCasinoUser} object due to given
     * parameters.
     *
     * @param email    user e-mail
     * @param password user password
     * @return initialized {@link JCasinoUser} object
     * @see DAOHelper
     * @see JCasinoEncryptor
     * @see UserDAO#authorizeUser(String, String)
     * @see #initPlayer(JCasinoUser)
     * @see #initAdmin(JCasinoUser)
     */
    public static JCasinoUser authorizeUser(String email, String password) {
        JCasinoUser user = null;
        email = email.toLowerCase().trim();
        password = JCasinoEncryptor.encryptMD5(password);
        try (DAOHelper daoHelper = new DAOHelper()) {
            UserDAO userDAO = daoHelper.getUserDAO();
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

    /**
     * Checks if definite user entered his right password.
     *
     * @param user     user who enters password
     * @param password entered password
     * @return true if user entered right password
     * @see DAOHelper
     * @see JCasinoEncryptor
     * @see UserDAO#checkPassword(int, String)
     */
    public static boolean checkPassword(JCasinoUser user, String password) {
        password = JCasinoEncryptor.encryptMD5(password);
        int id = user.getId();
        try (DAOHelper daoHelper = new DAOHelper()) {
            UserDAO userDAO = daoHelper.getUserDAO();
            return userDAO.checkPassword(id, password);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Inits {@link Player} object corresponding to given {@link JCasinoUser} object.
     *
     * @param user {@link JCasinoUser} object
     * @return initialized {@link Player} object
     * @see PlayerLogic#initPlayerInfo(Player)
     */
    static Player initPlayer(JCasinoUser user) {
        Player player = new Player();
        player.setId(user.getId());
        player.setEmail(user.getEmail());
        player.setRole(JCasinoUser.UserRole.PLAYER);
        player.setRegistrationDate(user.getRegistrationDate());
        PlayerLogic.initPlayerInfo(player);
        return player;
    }

    /**
     * Inits {@link Admin} object corresponding to given {@link JCasinoUser} object.
     *
     * @param user {@link JCasinoUser} object
     * @return initialized {@link Admin} object
     */
    private static Admin initAdmin(JCasinoUser user) {
        Admin admin = new Admin();
        admin.setId(user.getId());
        admin.setEmail(user.getEmail());
        admin.setRole(JCasinoUser.UserRole.ADMIN);
        admin.setRegistrationDate(user.getRegistrationDate());
        return admin;
    }
}