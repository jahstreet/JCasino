package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.News;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.PlayerStatus;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import by.sasnouskikh.jcasino.validator.FormValidator;
import org.apache.commons.fileupload.FileItem;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * The class provides Service layer actions available for admin.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class AdminService extends AbstractService {
    private static final Logger LOGGER = LogManager.getLogger(AdminService.class);

    private static final String UTF_8_ENCODING = "UTF-8";

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
     * Adds news to database.
     *
     * @param formItems collection parsed from multipart-form request
     * @param admin     admin who adds news
     * @param uploadDir upload directory
     * @return added {@link News} object or null
     * @see DAOHelper
     * @see NewsService#addNews(String, String, FileItem, String, Admin, String)
     * @see FileItem
     */
    public static News addNews(List<FileItem> formItems, Admin admin, String uploadDir) throws ServiceException, UnsupportedEncodingException {
        String   header    = null;
        String   text      = null;
        String   locale    = null;
        FileItem newsImage = null;
        boolean  valid     = true;
        for (FileItem item : formItems) {
            String fieldName = item.getFieldName();
            if (item.isFormField()) {
                if (PARAM_HEADER.equals(fieldName)) {
                    header = item.getString(UTF_8_ENCODING);
                    if (!FormValidator.validateNewsHeader(header)) {
                        valid = false;
                    }
                }
                if (PARAM_TEXT.equals(fieldName)) {
                    text = item.getString(UTF_8_ENCODING);
                    if (!FormValidator.validateNewsText(text)) {
                        valid = false;
                    }
                }
                if (PARAM_LOCALE.equals(fieldName)) {
                    locale = item.getString(UTF_8_ENCODING);
                    if (!FormValidator.validateNewsLocale(locale)) {
                        valid = false;
                    }
                }
            } else {
                if (PARAM_NEWS_IMAGE.equals(fieldName)) {
                    newsImage = item;
                }
            }
        }
        try (NewsService newsService = new NewsService()) {
            return valid ? newsService.addNews(header, text, newsImage, locale, admin, uploadDir) : null;
        }
    }

    /**
     * Edit news data in database.
     *
     * @param formItems collection parsed from multipart-form request
     * @param admin     admin who adds news
     * @param uploadDir upload directory
     * @return edited {@link News} object or null
     * @see DAOHelper
     * @see FileItem
     */
    public static News editNews(List<FileItem> formItems, Admin admin, String uploadDir) throws UnsupportedEncodingException, ServiceException {
        String   header    = null;
        String   text      = null;
        FileItem newsImage = null;
        int      newsId    = 0;
        boolean  valid     = true;
        for (FileItem item : formItems) {
            String fieldName = item.getFieldName();
            if (item.isFormField()) {
                if (PARAM_HEADER.equals(fieldName)) {
                    header = item.getString(UTF_8_ENCODING);
                    if (!FormValidator.validateNewsHeader(header)) {
                        valid = false;
                    }
                }
                if (PARAM_TEXT.equals(fieldName)) {
                    text = item.getString(UTF_8_ENCODING);
                    if (!FormValidator.validateNewsText(text)) {
                        valid = false;
                    }
                }
                if (PARAM_ID.equals(fieldName)) {
                    String idString = item.getString(UTF_8_ENCODING);
                    if (FormValidator.validateId(idString)) {
                        newsId = Integer.parseInt(idString);
                    } else {
                        valid = false;
                    }
                }
            } else {
                if (PARAM_NEWS_IMAGE.equals(fieldName)) {
                    newsImage = item;
                }
            }
        }
        try (NewsService newsService = new NewsService()) {
            return valid ? newsService.editNews(newsId, header, newsImage, text, admin, uploadDir) : null;
        }
    }

    /**
     * Calls DAO layer to take {@link Player} object due to its id.
     *
     * @param id player id to take
     * @return taken {@link Player} object or null
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
     * @return taken list of {@link Player} objects
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
            if (users == null) {
                return players;
            }
            players.addAll(users.stream()
                                .filter(user -> user.getRole() == JCasinoUser.UserRole.PLAYER)
                                .map(userService::initPlayer).collect(Collectors.toList()));
            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                if (players.isEmpty()) {
                    return players;
                }
                PlayerStatus.StatusEnum status = PlayerStatus.StatusEnum.valueOf(statusFilter.toUpperCase().trim());
                players.removeIf(player -> player.getAccount().getStatus().getStatus() != status);
            }
            if (verificationFilter != null && !verificationFilter.trim().isEmpty()) {
                if (players.isEmpty()) {
                    return players;
                }
                PlayerVerification.VerificationStatus status = PlayerVerification.VerificationStatus.valueOf(verificationFilter.toUpperCase().trim());
                players.removeIf(player -> player.getVerification().getStatus() != status);
            }
            if (!ignoreRanges) {
                if (players.isEmpty()) {
                    return players;
                }
                String[]   withdrawalRange = monthWithdrawalRangeString.split(RANGE_SPLITERATOR);
                BigDecimal botWithdrawal   = BigDecimal.valueOf(Double.parseDouble(withdrawalRange[0]));
                BigDecimal topWithdrawal   = BigDecimal.valueOf(Double.parseDouble(withdrawalRange[1]));
                String[]   balanceRange    = balanceRangeString.split(RANGE_SPLITERATOR);
                BigDecimal botBalance      = BigDecimal.valueOf(Double.parseDouble(balanceRange[0]));
                BigDecimal topBalance      = BigDecimal.valueOf(Double.parseDouble(balanceRange[1]));
                players.removeIf(player -> {
                    if (player == null) {
                        return false;
                    }
                    BigDecimal monthWithdrawal = player.getAccount().getThisMonthWithdrawal();
                    BigDecimal balance         = player.getAccount().getBalance();
                    return !(monthWithdrawal.compareTo(botWithdrawal) >= 0 &&
                             monthWithdrawal.compareTo(topWithdrawal) <= 0) ||
                           !(balance.compareTo(botBalance) >= 0 &&
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