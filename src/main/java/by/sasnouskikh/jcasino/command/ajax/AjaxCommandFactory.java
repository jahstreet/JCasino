package by.sasnouskikh.jcasino.command.ajax;

import by.sasnouskikh.jcasino.command.ajax.command.FinishStreakCommand;
import by.sasnouskikh.jcasino.command.ajax.command.SpinCommand;
import by.sasnouskikh.jcasino.command.ajax.command.SwitchToDemoCommand;
import by.sasnouskikh.jcasino.command.ajax.command.SwitchToRealCommand;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_ROLE;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PARAM_COMMAND;

/**
 * The factory of Commands suitable to use with {@link by.sasnouskikh.jcasino.controller.AjaxController}.
 *
 * @author Sasnouskikh Aliaksandr
 * @see AjaxCommand
 */
public class AjaxCommandFactory {
    private static final Logger LOGGER = LogManager.getLogger(AjaxCommandFactory.class);

    /**
     * {@link HashMap} collection of commands available to
     * {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole#GUEST}.
     */
    private static HashMap<AjaxCommandFactory.CommandType, AjaxCommand> guestCommands = new HashMap<>();

    /**
     * {@link HashMap} collection of commands available to
     * {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole#PLAYER}.
     */
    private static HashMap<AjaxCommandFactory.CommandType, AjaxCommand> playerCommands = new HashMap<>();

    static {
        guestCommands.put(CommandType.SPIN, new SpinCommand());
        guestCommands.put(CommandType.FINISH_STREAK, new FinishStreakCommand());

        playerCommands.putAll(guestCommands);
        playerCommands.put(CommandType.SWITCH_TO_REAL, new SwitchToRealCommand());
        playerCommands.put(CommandType.SWITCH_TO_DEMO, new SwitchToDemoCommand());
    }

    /**
     * Enumeration of Commands suitable to use with {@link by.sasnouskikh.jcasino.controller.AjaxController}.
     */
    public enum CommandType {
        SPIN,
        SWITCH_TO_REAL,
        SWITCH_TO_DEMO,
        FINISH_STREAK
    }

    /**
     * Private constructor to forbid create {@link AjaxCommandFactory} instances.
     */
    private AjaxCommandFactory() {
    }

    /**
     * Defines {@link AjaxCommand} due to {@link by.sasnouskikh.jcasino.manager.ConfigConstant#PARAM_COMMAND} parameter
     * of {@link HttpServletRequest#getParameter(String)} (should be {@link String#equalsIgnoreCase} to
     * {@link CommandType}) and {@link by.sasnouskikh.jcasino.manager.ConfigConstant#ATTR_ROLE} attribute of
     * {@link javax.servlet.http.HttpSession#getAttribute(String)}
     *
     * @param request request from client to get parameters to work with
     * @return defined {@link AjaxCommand} or null
     * @see by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole
     * @see #validateCommandName(String)
     * @see #defineCommand(String, JCasinoUser.UserRole)
     */
    public static AjaxCommand defineCommand(HttpServletRequest request) {
        String               commandName = request.getParameter(PARAM_COMMAND);
        JCasinoUser.UserRole role        = (JCasinoUser.UserRole) request.getSession().getAttribute(ATTR_ROLE);
        if (!validateCommandName(commandName)) {
            LOGGER.log(Level.ERROR, "Request doesn't have command parameter. Check JSP.");
            return null;
        }
        return defineCommand(commandName, role);
    }

    /**
     * Defines {@link AjaxCommand} due to {@link String} commandName (should be {@link String#equalsIgnoreCase} to
     * {@link CommandType}) and {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole}
     *
     * @param commandName name of the command
     * @param role        user's role
     * @return defined {@link AjaxCommand} or null
     * @see HashMap#get(Object)
     */
    private static AjaxCommand defineCommand(String commandName, JCasinoUser.UserRole role) {
        CommandType commandType = CommandType.valueOf(commandName.replaceAll("-", "_").toUpperCase());
        AjaxCommand command;
        if (role == JCasinoUser.UserRole.PLAYER) {
            command = playerCommands.get(commandType);
        } else if (role == JCasinoUser.UserRole.ADMIN) {
            //stub
            command = null;
        } else {
            command = guestCommands.get(commandType);
        }
        return command;
    }

    /**
     * Checks if {@link String} commandName is valid
     *
     * @param commandName name of the command
     * @return true if commandName is valid
     */
    private static boolean validateCommandName(String commandName) {
        if (commandName == null || commandName.trim().isEmpty()) {
            return false;
        }
        for (AjaxCommandFactory.CommandType type : AjaxCommandFactory.CommandType.values()) {
            if (type.toString().equalsIgnoreCase(commandName)) {
                return true;
            }
        }
        return false;
    }
}