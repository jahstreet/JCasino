package by.sasnouskikh.jcasino.command.ajax;

import by.sasnouskikh.jcasino.command.ajax.command.SpinCommand;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_ROLE;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PARAM_COMMAND;

public class AjaxCommandFactory {
    private static final Logger LOGGER = LogManager.getLogger(AjaxCommandFactory.class);

    private static HashMap<AjaxCommandFactory.CommandType, AjaxCommand> guestCommands  = new HashMap<>();
    private static HashMap<AjaxCommandFactory.CommandType, AjaxCommand> playerCommands = new HashMap<>();

    static {
        guestCommands.put(CommandType.SPIN, new SpinCommand());

        playerCommands.putAll(guestCommands);

    }

    private enum CommandType {
        SPIN
    }

    public static AjaxCommand defineCommand(HttpServletRequest request) {
        String               commandName = request.getParameter(PARAM_COMMAND);
        JCasinoUser.UserRole role        = (JCasinoUser.UserRole) request.getSession().getAttribute(ATTR_ROLE);
        if (!validateCommandName(commandName)) {
            LOGGER.log(Level.ERROR, "Request doesn't have command parameter. Check JSP.");
            return null;
        }
        return defineCommand(commandName, role);
    }

    private static AjaxCommand defineCommand(String commandName, JCasinoUser.UserRole role) {
        CommandType commandType = CommandType.valueOf(commandName.replaceAll("-", "_").toUpperCase());
        AjaxCommand command;
        if (role == JCasinoUser.UserRole.PLAYER) {
            command = playerCommands.get(commandType);
        } else {
            command = guestCommands.get(commandType);
        }
        return command;
    }

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