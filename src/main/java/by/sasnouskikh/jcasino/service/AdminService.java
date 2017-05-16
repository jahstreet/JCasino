package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * The class provides Service layer actions available for admin.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class AdminService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger(AdminService.class);

    /**
     * Default instance constructor.
     */
    public AdminService() {
    }

    /**
     * Constructs instance using definite {@link DAOHelper} object.
     */
    public AdminService(DAOHelper daoHelper) {
        super(daoHelper);
    }

    /**
     * Calls DAO layer to take {@link Player} object due to its id.
     *
     * @param id player id to take
     * @return taken {@link Player} object
     * @see DAOHelper
     * @see UserDAO#takeUser(int)
     * @see UserService#initPlayer(JCasinoUser)
     */
    public Player takePlayer(int id) {
        JCasinoUser user    = null;
        UserDAO     userDAO = daoHelper.getUserDAO();
        try {
            user = userDAO.takeUser(id);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        try (UserService userService = new UserService()) {
            user = userService.initPlayer(user);
        }
        return (Player) user;
    }

    /**
     * Calls DAO layer to take {@link List} of {@link PlayerVerification} objects of players who are ready to be
     * verified by admin.
     *
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see PlayerDAO#takeReadyForVerification()
     */
    public List<PlayerVerification> takeReadyForVerification() {
        List<PlayerVerification> verificationList = null;
        PlayerDAO                playerDAO        = daoHelper.getPlayerDAO();
        try {
            verificationList = playerDAO.takeReadyForVerification();
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return verificationList;
    }

    /**
     * Calls DAO layer to change player account status and leave admin commentary to this change.
     *
     * @param playerId   player id whose account status to change
     * @param admin      admin who changes player account status
     * @param status     new player account status string value
     * @param commentary admin commentary
     * @return true if changing proceeded successfully
     * @see DAOHelper
     * @see PlayerDAO#changeAccountStatus(int, int, String, String)
     */
    public boolean changeAccountStatus(int playerId, Admin admin, String status, String commentary) {
        int adminId = admin.getId();
        status = status.toLowerCase();
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        try {
            return playerDAO.changeAccountStatus(playerId, adminId, status, commentary);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }
}