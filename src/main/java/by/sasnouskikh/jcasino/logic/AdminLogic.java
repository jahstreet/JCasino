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

public class AdminLogic {
    private static final Logger LOGGER = LogManager.getLogger(AdminLogic.class);

    private AdminLogic() {
    }

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