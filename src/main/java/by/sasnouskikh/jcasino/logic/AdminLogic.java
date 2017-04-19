package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * The class provides Logic layer actions available for admin.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class AdminLogic {
    private static final Logger LOGGER = LogManager.getLogger(AdminLogic.class);

    /**
     * Outer forbidding to create this class instances.
     */
    private AdminLogic() {
    }

    /**
     * Calls DAO layer to take {@link Player} object due to its id.
     *
     * @param id player id to take
     * @return taken {@link Player} object
     * @see DAOHelper
     * @see UserDAO#takeUser(int)
     * @see UserLogic#initPlayer(JCasinoUser)
     */
    public static Player takePlayer(int id) {
        JCasinoUser user = null;
        try (DAOHelper daoHelper = new DAOHelper()) {
            UserDAO userDAO = daoHelper.getUserDAO();
            user = userDAO.takeUser(id);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return user != null ? UserLogic.initPlayer(user) : null;
    }

    /**
     * Calls DAO layer to take {@link List} of {@link PlayerVerification} objects of players who are ready to be
     * verified by admin.
     *
     * @return taken {@link List} collection
     * @see DAOHelper
     * @see PlayerDAO#takeReadyForVerification()
     */
    public static List<PlayerVerification> takeReadyForVerification() {
        List<PlayerVerification> verificationList = null;
        try (DAOHelper daoHelper = new DAOHelper()) {
            PlayerDAO playerDAO = daoHelper.getPlayerDAO();
            verificationList = playerDAO.takeReadyForVerification();
        } catch (ConnectionPoolException | DAOException e) {
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
    public static boolean changeAccountStatus(int playerId, Admin admin, String status, String commentary) {
        int adminId = admin.getId();
        status = status.toLowerCase();
        try (DAOHelper daoHelper = new DAOHelper()) {
            PlayerDAO playerDAO = daoHelper.getPlayerDAO();
            return playerDAO.changeAccountStatus(playerId, adminId, status, commentary);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }
}