package by.sasnouskikh.jcasino.command;

import by.sasnouskikh.jcasino.command.impl.*;
import by.sasnouskikh.jcasino.command.impl.admin.*;
import by.sasnouskikh.jcasino.command.impl.navigation.*;
import by.sasnouskikh.jcasino.command.impl.navigation.admin.*;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_ROLE;
import static by.sasnouskikh.jcasino.manager.ConfigConstant.PARAM_COMMAND;

public class CommandFactory {

    private static final String                        MULTIPART_COMMAND_NAME = "multipart";
    private static       HashMap<CommandType, Command> guestCommands          = new HashMap<>();
    private static       HashMap<CommandType, Command> playerCommands         = new HashMap<>();
    private static       HashMap<CommandType, Command> adminCommands          = new HashMap<>();

    static {
        guestCommands.put(CommandType.LOGIN, new LoginCommand());
        guestCommands.put(CommandType.REGISTER, new RegisterCommand());
        guestCommands.put(CommandType.CHANGE_LOCALE, new ChangeLocaleCommand());
        guestCommands.put(CommandType.RECOVER_PASSWORD, new RecoverPasswordCommand());
        guestCommands.put(CommandType.SEND_SUPPORT, new SendSupportCommand());
        guestCommands.put(CommandType.GOTO_INDEX, new GotoIndexCommand());
        guestCommands.put(CommandType.GOTO_SUPPORT, new GotoSupportCommand());
        guestCommands.put(CommandType.GOTO_REGISTER, new GotoRegisterCommand());
        guestCommands.put(CommandType.GOTO_RECOVER_PASSWORD, new GotoRecoverPasswordCommand());
        guestCommands.put(CommandType.GOTO_ERROR_500, new GotoError500Command());
        guestCommands.put(CommandType.BACK_FROM_ERROR, new BackFromErrorCommand());
        guestCommands.put(CommandType.GOTO_GAME_FRUITS, new GotoGameFruitsCommand());

        playerCommands.putAll(guestCommands);
        playerCommands.put(CommandType.LOGOUT, new LogoutCommand());
        playerCommands.put(CommandType.GOTO_MAIN, new GotoMainCommand());
        playerCommands.put(CommandType.GOTO_STATS, new GotoStatsCommand());
        playerCommands.put(CommandType.MULTIPART, new MultipartFormCommand());
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

        adminCommands.putAll(guestCommands);
        adminCommands.put(CommandType.MULTIPART, new MultipartFormCommand());
        adminCommands.put(CommandType.LOGOUT, new LogoutCommand());
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

    private enum CommandType {
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
        GOTO_GAME_FRUITS
    }

    private CommandFactory() {
    }

    public static Command defineCommand(HttpServletRequest request) {
        Command              command;
        String               commandName;
        JCasinoUser.UserRole role = (JCasinoUser.UserRole) request.getSession().getAttribute(ATTR_ROLE);
        if (!ServletFileUpload.isMultipartContent(request)) {
            commandName = request.getParameter(PARAM_COMMAND);
            if (commandName == null || commandName.trim().isEmpty()) {
                //TODO return exception log or/and another command
                // commandName = ... ;
            }
        } else {
            commandName = MULTIPART_COMMAND_NAME;
        }
        command = defineCommand(commandName, role);
        return command;
    }

    private static Command defineCommand(String commandName, JCasinoUser.UserRole role) {
        commandName = commandName.replaceAll("-", "_").toUpperCase();
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
}