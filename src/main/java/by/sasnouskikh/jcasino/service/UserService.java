package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.manager.JCasinoEncryptor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class provides Service layer actions available for user.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class UserService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    /**
     * Default instance constructor.
     */
    public UserService() {
    }

    /**
     * Constructs instance using definite {@link DAOHelper} object.
     */
    public UserService(DAOHelper daoHelper) {
        super(daoHelper);
    }

    /**
     * Provides authorisation operation service for user. Calls DAO layer to init {@link JCasinoUser} object due to
     * given parameters.
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
    public JCasinoUser authorizeUser(String email, String password) {
        JCasinoUser user = null;
        email = email.toLowerCase().trim();
        password = JCasinoEncryptor.encryptMD5(password);
        UserDAO userDAO = daoHelper.getUserDAO();
        try {
            user = userDAO.authorizeUser(email, password);
        } catch (DAOException e) {
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
    public boolean checkPassword(JCasinoUser user, String password) {
        password = JCasinoEncryptor.encryptMD5(password);
        int     id      = user.getId();
        UserDAO userDAO = daoHelper.getUserDAO();
        try {
            return userDAO.checkPassword(id, password);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Inits {@link Player} object corresponding to given {@link JCasinoUser} object.
     *
     * @param user {@link JCasinoUser} object
     * @return initialized {@link Player} object
     * @see PlayerService#initPlayerInfo(Player)
     */
    Player initPlayer(JCasinoUser user) {
        if (user == null) {
            return null;
        }
        Player player = new Player();
        player.setId(user.getId());
        player.setEmail(user.getEmail());
        player.setRole(JCasinoUser.UserRole.PLAYER);
        player.setRegistrationDate(user.getRegistrationDate());
        try (PlayerService playerService = new PlayerService()) {
            playerService.initPlayerInfo(player);
        }
        return player;
    }

    /**
     * Inits {@link Admin} object corresponding to given {@link JCasinoUser} object.
     *
     * @param user {@link JCasinoUser} object
     * @return initialized {@link Admin} object
     */
    private Admin initAdmin(JCasinoUser user) {
        if (user == null) {
            return null;
        }
        Admin admin = new Admin();
        admin.setId(user.getId());
        admin.setEmail(user.getEmail());
        admin.setRole(JCasinoUser.UserRole.ADMIN);
        admin.setRegistrationDate(user.getRegistrationDate());
        return admin;
    }
}