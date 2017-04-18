package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPoolException;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

public class VerificationLogic {
    private static final Logger LOGGER = LogManager.getLogger(VerificationLogic.class);

    public enum MaskOperation {
        AND, OR
    }

    private VerificationLogic() {
    }

    public static boolean changePlayerVerStatus(int playerId, byte verMask, VerificationLogic.MaskOperation operation, Admin admin, String commentary) {
        int adminId = admin.getId();
        try (DAOHelper daoHelper = new DAOHelper()) {
            PlayerDAO          playerDAO    = daoHelper.getPlayerDAO();
            PlayerVerification verification = playerDAO.takeVerification(playerId);
            byte               newStatus    = VerificationLogic.buildNewStatus(verification, verMask, operation);
            return playerDAO.changeVerificationStatus(playerId, newStatus, adminId, commentary);
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    public static boolean cancelScanVerification(int playerId, Admin admin, String commentary) {
        int adminId = admin.getId();
        try (DAOHelper daoHelper = new DAOHelper()) {
            PlayerDAO          playerDAO    = daoHelper.getPlayerDAO();
            PlayerVerification verification = playerDAO.takeVerification(playerId);
            byte               newStatus    = VerificationLogic.buildNewStatus(verification, (byte) ~PASSPORT_VER_MASK, VerificationLogic.MaskOperation.AND);
            daoHelper.beginTransaction();
            if (playerDAO.changeVerificationStatus(playerId, newStatus, adminId, commentary)
                && playerDAO.changeScanPath(playerId, null)) {
                daoHelper.commit();
                return true;
            }
        } catch (ConnectionPoolException | DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    static byte buildNewStatus(PlayerVerification verification, byte changeMask, MaskOperation operation) {
        byte status = ejectBinStatus(verification);
        return buildNewStatus(status, changeMask, operation);
    }

    private static byte buildNewStatus(byte status, byte changeMask, MaskOperation operation) {
        byte newStatus;
        if (operation == MaskOperation.AND) {
            newStatus = (byte) ((status & changeMask) & FULL_VER_MASK);
        } else {
            newStatus = (byte) ((status | changeMask) & FULL_VER_MASK);
        }
        return newStatus;
    }

    private static byte ejectBinStatus(PlayerVerification verification) {
        byte status = 0;
        if (verification.getStatus() == PlayerVerification.VerificationStatus.VERIFIED) {
            status = FULL_VER_MASK;
        } else {
            if (verification.isProfileVerified()) {
                status |= PROFILE_VER_MASK;
            }
            if (verification.isEmailVerified()) {
                status |= EMAIL_VER_MASK;
            }
            if (verification.isScanVerified()) {
                status |= PASSPORT_VER_MASK;
            }
        }
        return status;
    }
}