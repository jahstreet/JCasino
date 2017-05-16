package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides Service layer actions with player verification.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class VerificationService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger(VerificationService.class);

    /**
     * Available verification mask operation enumeration value instances.
     */
    public enum MaskOperation {
        AND, OR
    }

    /**
     * Default instance constructor.
     */
    public VerificationService() {
    }

    /**
     * Constructs instance using definite {@link DAOHelper} object.
     */
    public VerificationService(DAOHelper daoHelper) {
        super(daoHelper);
    }

    /**
     * Calls DAO layer to update player verification status data due given parameters.
     *
     * @param playerId   player id whose data is updating
     * @param verMask    verification binary mask
     * @param operation  type of service operation with current verification binary mask and given verification binary
     *                   mask
     * @param admin      admin id who processes verification status change
     * @param commentary admin commentary
     * @return true if operation proceeded successfully
     * @see DAOHelper
     * @see PlayerDAO#takeVerification(int)
     * @see #buildNewStatus(PlayerVerification, byte, MaskOperation)
     * @see PlayerDAO#changeVerificationStatus(int, byte, int, String)
     */
    public boolean changePlayerVerStatus(int playerId, byte verMask, VerificationService.MaskOperation operation, Admin admin, String commentary) {
        int       adminId   = admin.getId();
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        try {
            PlayerVerification verification = playerDAO.takeVerification(playerId);
            byte               newStatus    = buildNewStatus(verification, verMask, operation);
            return playerDAO.changeVerificationStatus(playerId, newStatus, adminId, commentary);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return false;
    }

    /**
     * Calls DAO layer to cancel player passport scan verification by admin.
     *
     * @param playerId   player id whose data is updating
     * @param admin      admin who cancels passport scan verification
     * @param commentary admin commentary
     * @return true if transaction proceeded successfully
     * @see DAOHelper
     * @see PlayerDAO#takeVerification(int)
     * @see #buildNewStatus(PlayerVerification, byte, MaskOperation)
     * @see PlayerDAO#changeVerificationStatus(int, byte, int, String)
     * @see PlayerDAO#changeScanPath(int, String)
     */
    public boolean cancelScanVerification(int playerId, Admin admin, String commentary) {
        int       adminId   = admin.getId();
        PlayerDAO playerDAO = daoHelper.getPlayerDAO();
        try {
            PlayerVerification verification = playerDAO.takeVerification(playerId);
            byte               newStatus    = buildNewStatus(verification, (byte) ~PASSPORT_VER_MASK, VerificationService.MaskOperation.AND);
            daoHelper.beginTransaction();
            if (playerDAO.changeVerificationStatus(playerId, newStatus, adminId, commentary)
                && playerDAO.changeScanPath(playerId, null)) {
                daoHelper.commit();
                return true;
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Database connection error while doing sql transaction. " + e);
        }
        return false;
    }

    /**
     * Builds new status binary mask from given parameters.
     *
     * @param verification current {@link PlayerVerification} object
     * @param changeMask   verification binary mask
     * @param operation    service operation with current verification binary mask and given verification binary mask
     * @return byte value of built verification binary mask
     * @see #ejectBinStatus(PlayerVerification)
     * @see #buildNewStatus(byte, byte, MaskOperation)
     */
    static byte buildNewStatus(PlayerVerification verification, byte changeMask, MaskOperation operation) {
        byte status = ejectBinStatus(verification);
        return buildNewStatus(status, changeMask, operation);
    }

    /**
     * Builds new status binary mask from given parameters.
     *
     * @param status     current verification binary mask
     * @param changeMask verification binary mask
     * @param operation  service operation with current verification binary mask and given verification binary mask
     * @return byte value of built verification binary mask
     */
    private static byte buildNewStatus(byte status, byte changeMask, MaskOperation operation) {
        byte newStatus;
        if (operation == MaskOperation.AND) {
            newStatus = (byte) ((status & changeMask) & FULL_VER_MASK);
        } else {
            newStatus = (byte) ((status | changeMask) & FULL_VER_MASK);
        }
        return newStatus;
    }

    /**
     * Ejects verification binary mask from given {@link PlayerVerification} object.
     *
     * @param verification {@link PlayerVerification} object to eject binary mask from
     * @return byte value of ejected verification binary mask
     */
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