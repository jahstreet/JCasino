package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.PERCENT;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.RANGE_SPLITERATOR;

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
        Player  player  = null;
        UserDAO userDAO = daoHelper.getUserDAO();
        try (UserService userService = new UserService()) {
            JCasinoUser user = userDAO.takeUser(id);
            player = userService.initPlayer(user);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return player;
    }

    /**
     * Calls DAO layer to take list of {@link Player} objects due to given parameters.
     *
     * @param id player id to take
     * @return taken {@link Player} object
     * @see DAOHelper
     * @see UserDAO#takeUserList(String)
     * @see UserService#initPlayer(JCasinoUser)
     */
    public List<Player> takePlayerList(int id, String statusFilter, String verificationFilter,
                                      String monthWithdrawalRangeString, String balanceRangeString, boolean ignoreRanges) {
        List<Player> players   = new ArrayList<>();
        String       idPattern = id != 0 ? String.valueOf(id) : PERCENT;
        UserDAO      userDAO   = daoHelper.getUserDAO();
        try (UserService userService = new UserService()) {
            List<JCasinoUser> users = userDAO.takeUserList(idPattern);
            players.addAll(users.stream()
                                .filter(user -> user.getRole() == JCasinoUser.UserRole.PLAYER)
                                .map(userService::initPlayer).collect(Collectors.toList()));
            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                PlayerStatus.StatusEnum status = PlayerStatus.StatusEnum.valueOf(statusFilter.toUpperCase().trim());
                players.removeIf(player -> player.getAccount().getStatus().getStatus() != status);
            }
            if (verificationFilter != null && !verificationFilter.trim().isEmpty()) {
                PlayerVerification.VerificationStatus status = PlayerVerification.VerificationStatus.valueOf(verificationFilter.toUpperCase().trim());
                players.removeIf(player -> player.getVerification().getStatus() != status);
            }
            if (!ignoreRanges) {
                String[]   withdrawalRange = monthWithdrawalRangeString.split(RANGE_SPLITERATOR);
                BigDecimal botWithdrawal   = BigDecimal.valueOf(Double.parseDouble(withdrawalRange[0]));
                BigDecimal topWithdrawal   = BigDecimal.valueOf(Double.parseDouble(withdrawalRange[1]));
                players.removeIf(player -> {
                    BigDecimal monthWithdrawal = player.getAccount().getThisMonthWithdrawal();
                    return !(monthWithdrawal.compareTo(botWithdrawal) >= 0 &&
                             monthWithdrawal.compareTo(topWithdrawal) <= 0);
                });

                String[]   balanceRange = balanceRangeString.split(RANGE_SPLITERATOR);
                BigDecimal botBalance   = BigDecimal.valueOf(Double.parseDouble(balanceRange[0]));
                BigDecimal topBalance   = BigDecimal.valueOf(Double.parseDouble(balanceRange[1]));
                players.removeIf(player -> {
                    BigDecimal balance = player.getAccount().getBalance();
                    return !(balance.compareTo(botBalance) >= 0 &&
                             balance.compareTo(topBalance) <= 0);
                });
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            players = null;
        }
        return players;
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