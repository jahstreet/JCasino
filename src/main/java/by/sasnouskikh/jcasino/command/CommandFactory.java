package by.sasnouskikh.jcasino.command;

import by.sasnouskikh.jcasino.command.impl.*;
import by.sasnouskikh.jcasino.command.impl.admin.*;
import by.sasnouskikh.jcasino.command.impl.navigation.*;
import by.sasnouskikh.jcasino.command.impl.navigation.admin.*;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_ROLE;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PARAM_COMMAND;

/**
 * The factory of Commands suitable to use with {@link by.sasnouskikh.jcasino.controller.MainController}.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Command
 */
public class CommandFactory {
    private static final Logger LOGGER = LogManager.getLogger(CommandFactory.class);

    /**
     * Name of command to work with multipart/form-data enctype request
     */
    private static final String MULTIPART_COMMAND_NAME = "multipart";

    /**
     * {@link HashMap} collection of commands available to
     * {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole#GUEST}.
     */
    private static HashMap<CommandType, Command> guestCommands  = new HashMap<>();

    /**
     * {@link HashMap} collection of commands available to
     * {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole#PLAYER}.
     */
    private static HashMap<CommandType, Command> playerCommands = new HashMap<>();

    /**
     * {@link HashMap} collection of commands available to
     * {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole#ADMIN}.
     */
    private static HashMap<CommandType, Command> adminCommands  = new HashMap<>();

    static {
        Command logout    = new LogoutCommand();
        Command multipart = new MultipartFormCommand();

        guestCommands.put(CommandType.LOGIN, new LoginCommand());
        guestCommands.put(CommandType.REGISTER, new RegisterCommand());
        guestCommands.put(CommandType.CHANGE_LOCALE, new ChangeLocaleCommand());
        guestCommands.put(CommandType.RECOVER_PASSWORD, new RecoverPasswordCommand());
        guestCommands.put(CommandType.SEND_SUPPORT, new SendSupportCommand());
        guestCommands.put(CommandType.GOTO_INDEX, new GotoIndexCommand());
        guestCommands.put(CommandType.GOTO_SUPPORT, new GotoSupportCommand());
        guestCommands.put(CommandType.GOTO_REGISTER, new GotoRegisterCommand());
        guestCommands.put(CommandType.GOTO_RECOVER_PASSWORD, new GotoRecoverPasswordCommand());
        guestCommands.put(CommandType.GOTO_RULES, new GotoRulesCommand());
        guestCommands.put(CommandType.GOTO_ERROR_500, new GotoError500Command());
        guestCommands.put(CommandType.BACK_FROM_ERROR, new BackFromErrorCommand());
        guestCommands.put(CommandType.GOTO_GAME_FRUITS, new GotoGameFruitsCommand());
        guestCommands.put(CommandType.GOTO_GAME_FRUITS_SETUP, new GotoGameFruitsSetupCommand());
        guestCommands.put(CommandType.BACK_FROM_GAME, new BackFromGameCommand());

        playerCommands.putAll(guestCommands);
        playerCommands.put(CommandType.LOGOUT, logout);
        playerCommands.put(CommandType.GOTO_MAIN, new GotoMainCommand());
        playerCommands.put(CommandType.GOTO_STATS, new GotoStatsCommand());
        playerCommands.put(CommandType.MULTIPART, multipart);
        playerCommands.put(CommandType.EDIT_PROFILE, new EditProfileCommand());
        playerCommands.put(CommandType.VERIFY_PROFILE, new VerifyProfileCommand());
        playerCommands.put(CommandType.VERIFY_EMAIL, new VerifyEmailCommand());
        playerCommands.put(CommandType.SEND_EMAIL_CODE, new SendEmailCodeCommand());
        playerCommands.put(CommandType.REPLENISH_ACCOUNT, new ReplenishAccountCommand());
        playerCommands.put(CommandType.PAY_LOAN, new PayLoanCommand());
        playerCommands.put(CommandType.TAKE_LOAN, new TakeLoanCommand());
        playerCommands.put(CommandType.WITHDRAW_MONEY, new WithdrawMoneyCommand());
        playerCommands.put(CommandType.SHOW_HISTORY, new ShowHistoryCommand());
        playerCommands.put(CommandType.SET_SATISFACTION, new RateSupportAnswerCommand());
        playerCommands.put(CommandType.RESET_SATISFACTION, new ResetSupportAnswerRatingCommand());
        playerCommands.put(CommandType.GOTO_PLAYER_PROFILE, new GotoPlayerProfileCommand());
        playerCommands.put(CommandType.GOTO_ACCOUNT, new GotoAccountCommand());
        playerCommands.put(CommandType.GOTO_REPLENISH_ACCOUNT, new GotoReplenishAccountCommand());
        playerCommands.put(CommandType.GOTO_PAY_LOAN, new GotoPayLoanCommand());
        playerCommands.put(CommandType.GOTO_TAKE_LOAN, new GotoTakeLoanCommand());
        playerCommands.put(CommandType.GOTO_WITHDRAW_MONEY, new GotoWithdrawMoneyCommand());
        playerCommands.put(CommandType.GOTO_VERIFICATION, new GotoVerificationCommand());
        playerCommands.put(CommandType.GOTO_OPERATION_HISTORY, new GotoOperationHistoryCommand());
        playerCommands.put(CommandType.GOTO_EMAIL_VERIFICATION, new GotoEmailVerificationCommand());
        playerCommands.put(CommandType.GOTO_UPLOAD_PASSPORT, new GotoUploadPassportCommand());

        adminCommands.put(CommandType.GOTO_INDEX, new GotoIndexCommand());
        adminCommands.put(CommandType.GOTO_ERROR_500, new GotoError500Command());
        adminCommands.put(CommandType.BACK_FROM_ERROR, new BackFromErrorCommand());
        adminCommands.put(CommandType.CHANGE_LOCALE, new ChangeLocaleCommand());
        adminCommands.put(CommandType.MULTIPART, multipart);
        adminCommands.put(CommandType.LOGOUT, logout);
        adminCommands.put(CommandType.EDIT_NEWS, new EditNewsCommand());
        adminCommands.put(CommandType.DELETE_NEWS, new DeleteNewsCommand());
        adminCommands.put(CommandType.SHOW_QUESTIONS, new ShowQuestionsCommand());
        adminCommands.put(CommandType.SHOW_ANSWERS, new ShowAnswersCommand());
        adminCommands.put(CommandType.SHOW_TRANSACTIONS, new ShowTransactionsCommand());
        adminCommands.put(CommandType.SHOW_STREAKS, new ShowStreaksCommand());
        adminCommands.put(CommandType.SHOW_LOANS, new ShowLoansCommand());
        adminCommands.put(CommandType.ANSWER_SUPPORT, new AnswerSupportCommand());
        adminCommands.put(CommandType.VERIFY_SCAN, new VerifyScanCommand());
        adminCommands.put(CommandType.CANCEL_SCAN_VERIFICATION, new CancelScanVerificationCommand());
        adminCommands.put(CommandType.CHANGE_ACCOUNT_STATUS, new ChangeAccountStatusCommand());
        adminCommands.put(CommandType.SHOW_PLAYER_SUPPORT, new ShowPlayerSupportCommand());
        adminCommands.put(CommandType.SHOW_PLAYER_TRANSACTIONS, new ShowPlayerTransactionsCommand());
        adminCommands.put(CommandType.SHOW_PLAYER_STREAKS, new ShowPlayerStreaksCommand());
        adminCommands.put(CommandType.SHOW_PLAYER_LOANS, new ShowPlayerLoansCommand());
        adminCommands.put(CommandType.GOTO_ADMIN, new GotoAdminCommand());
        adminCommands.put(CommandType.GOTO_MANAGE_NEWS, new GotoManageNewsCommand());
        adminCommands.put(CommandType.GOTO_MANAGE_SUPPORT, new GotoManageSupportCommand());
        adminCommands.put(CommandType.GOTO_ANSWER_SUPPORT, new GotoAnswerSupportCommand());
        adminCommands.put(CommandType.GOTO_MANAGE_VERIFICATION, new GotoManageVerificationCommand());
        adminCommands.put(CommandType.GOTO_MANAGE_LOANS, new GotoManageLoansCommand());
        adminCommands.put(CommandType.GOTO_MANAGE_PLAYER, new GotoManagePlayerCommand());
        adminCommands.put(CommandType.GOTO_MANAGE_TRANSACTIONS, new GotoManageTransactionsCommand());
        adminCommands.put(CommandType.GOTO_MANAGE_STREAKS, new GotoManageStreaksCommand());
        adminCommands.put(CommandType.GOTO_STATS_REPORT, new GotoStatsReportCommand());
    }

    /**
     * Enumeration of Commands suitable to use with {@link by.sasnouskikh.jcasino.controller.MainController}.
     */
    public enum CommandType {
        LOGIN,
        LOGOUT,
        REGISTER,
        CHANGE_LOCALE,
        EDIT_PROFILE,
        VERIFY_PROFILE,
        VERIFY_EMAIL,
        SEND_EMAIL_CODE,
        SEND_SUPPORT,
        RECOVER_PASSWORD,
        REPLENISH_ACCOUNT,
        PAY_LOAN,
        TAKE_LOAN,
        WITHDRAW_MONEY,
        SHOW_HISTORY,
        SET_SATISFACTION,
        RESET_SATISFACTION,
        MULTIPART,
        GOTO_INDEX,
        GOTO_REGISTER,
        GOTO_MAIN,
        GOTO_SUPPORT,
        GOTO_STATS,
        GOTO_PLAYER_PROFILE,
        GOTO_VERIFICATION,
        GOTO_EMAIL_VERIFICATION,
        GOTO_UPLOAD_PASSPORT,
        GOTO_RECOVER_PASSWORD,
        GOTO_ACCOUNT,
        GOTO_REPLENISH_ACCOUNT,
        GOTO_PAY_LOAN,
        GOTO_TAKE_LOAN,
        GOTO_WITHDRAW_MONEY,
        GOTO_OPERATION_HISTORY,
        GOTO_RULES,
        GOTO_ERROR_500,
        BACK_FROM_ERROR,
        GOTO_ADMIN,
        EDIT_NEWS,
        DELETE_NEWS,
        SHOW_QUESTIONS,
        SHOW_ANSWERS,
        SHOW_TRANSACTIONS,
        SHOW_STREAKS,
        SHOW_LOANS,
        ANSWER_SUPPORT,
        VERIFY_SCAN,
        CANCEL_SCAN_VERIFICATION,
        CHANGE_ACCOUNT_STATUS,
        SHOW_PLAYER_SUPPORT,
        SHOW_PLAYER_TRANSACTIONS,
        SHOW_PLAYER_STREAKS,
        SHOW_PLAYER_LOANS,
        GOTO_MANAGE_NEWS,
        GOTO_MANAGE_SUPPORT,
        GOTO_ANSWER_SUPPORT,
        GOTO_MANAGE_VERIFICATION,
        GOTO_MANAGE_LOANS,
        GOTO_MANAGE_PLAYER,
        GOTO_MANAGE_TRANSACTIONS,
        GOTO_MANAGE_STREAKS,
        GOTO_STATS_REPORT,
        GOTO_GAME_FRUITS,
        GOTO_GAME_FRUITS_SETUP,
        BACK_FROM_GAME
    }

    /**
     * Private constructor to forbid create {@link CommandFactory} instances.
     */
    private CommandFactory() {
    }

    /**
     * Defines {@link Command} due to {@link by.sasnouskikh.jcasino.manager.ConfigConstant#PARAM_COMMAND} parameter
     * of {@link HttpServletRequest#getParameter(String)} (should be {@link String#equalsIgnoreCase} to
     * {@link CommandType}) and {@link by.sasnouskikh.jcasino.manager.ConfigConstant#ATTR_ROLE} attribute
     * of {@link javax.servlet.http.HttpSession#getAttribute(String)}
     *
     * @param request - request from client to get parameters to work with
     * @return defined {@link Command}
     * @see by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole
     * @see CommandFactory#validateCommandName(String)
     * @see CommandFactory#defineCommand(String, JCasinoUser.UserRole)
     */
    public static Command defineCommand(HttpServletRequest request) {
        String               commandName;
        JCasinoUser.UserRole role = (JCasinoUser.UserRole) request.getSession().getAttribute(ATTR_ROLE);
        if (!ServletFileUpload.isMultipartContent(request)) {
            commandName = request.getParameter(PARAM_COMMAND);
            if (!validateCommandName(commandName)) {
                LOGGER.log(Level.ERROR, "Request doesn't have command parameter or it is invalid: " + commandName +
                                        ". Check JSP.");
                return new GotoIndexCommand();
            }
        } else {
            commandName = MULTIPART_COMMAND_NAME;
        }
        return defineCommand(commandName, role);
    }

    /**
     * Defines {@link Command} due to {@link String} commandName (should be {@link String#equalsIgnoreCase} to
     * {@link CommandType}) and {@link by.sasnouskikh.jcasino.entity.bean.JCasinoUser.UserRole}
     *
     * @param commandName - name of the command
     * @param role        - user's role
     * @return defined {@link Command} or null
     * @see HashMap#get(Object)
     */
    private static Command defineCommand(String commandName, JCasinoUser.UserRole role) {
        commandName = commandName.trim().replaceAll("-", "_").toUpperCase();
        CommandType commandType = CommandType.valueOf(commandName);
        Command     command;
        switch (role) {
            case PLAYER:
                command = playerCommands.get(commandType);
                break;
            case ADMIN:
                command = adminCommands.get(commandType);
                break;
            default:
                command = guestCommands.get(commandType);
        }
        if (command == null) {
            command = new GotoIndexCommand();
        }
        return command;
    }

    /**
     * Checks if {@link String} commandName is valid
     *
     * @param commandName - name of the command
     * @return true if commandName is valid
     */
    private static boolean validateCommandName(String commandName) {
        if (commandName == null || commandName.trim().isEmpty()) {
            return false;
        }
        for (CommandType type : CommandType.values()) {
            if (type.toString().equalsIgnoreCase(commandName)) {
                return true;
            }
        }
        return false;
    }
}