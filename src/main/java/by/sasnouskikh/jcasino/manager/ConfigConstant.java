package by.sasnouskikh.jcasino.manager;

public class ConfigConstant {

    public static final String DEFAULT_LOCALE = "default";
    public static final String LOCALE_RU      = "ru_RU";
    public static final String LOCALE_EN      = "en_US";

    public static final String FORWARD  = "forward";
    public static final String REDIRECT = "redirect";

    public static final String CONTEXT_NEWSLIST = "newsList";

    public static final byte PROFILE_VER_MASK  = 0b001;
    public static final byte EMAIL_VER_MASK    = 0b010;
    public static final byte PASSPORT_VER_MASK = 0b100;
    public static final byte FULL_VER_MASK     = 0b111;

    public static final int LOAN_TERM_MONTH = 1;

    public static final String ATTR_ROLE          = "role";
    public static final String ATTR_USER          = "user";
    public static final String ATTR_PLAYER        = "player";
    public static final String ATTR_ADMIN         = "admin";
    public static final String ATTR_LOCALE        = "locale";
    public static final String ATTR_PREV_QUERY    = "prevQuery";
    public static final String ATTR_ERROR_MESSAGE = "errorMessage";

    public static final String ATTR_EMAIL_INPUT     = "email_input";
    public static final String ATTR_BIRTHDATE_INPUT = "birthdate_input";
    public static final String ATTR_FNAME_INPUT     = "fname_input";
    public static final String ATTR_MNAME_INPUT     = "mname_input";
    public static final String ATTR_LNAME_INPUT     = "lname_input";
    public static final String ATTR_PASSPORT_INPUT  = "passport_input";
    public static final String ATTR_SCAN_INPUT      = "scan_input";
    public static final String ATTR_QUESTION_INPUT  = "question_input";
    public static final String ATTR_ANSWER_INPUT    = "answer_input";
    public static final String ATTR_AMOUNT_INPUT    = "amount_input";
    public static final String ATTR_TRANSACTIONS    = "transactions";
    public static final String ATTR_LOANS           = "loans";
    public static final String ATTR_STREAKS         = "streaks";

    public static final String   INIT_PARAM_UPLOADS = "uploads.dir";
    public static final String   SCAN_UPLOAD_DIR    = "scan";
    public static final String   NEWS_UPLOAD_DIR    = "news";
    public static final String[] AVAILABLE_SCAN_EXT = new String[]{"gif", "jpeg", "jpg", "png"};

    public static final String PARAM_COMMAND          = "command";
    public static final String PARAM_EMAIL            = "email";
    public static final String PARAM_EMAIL_CODE       = "email_code";
    public static final String PARAM_PASSWORD         = "password";
    public static final String PARAM_PASSWORD_AGAIN   = "password_again";
    public static final String PARAM_PASSWORD_OLD     = "old_password";
    public static final String PARAM_LOCALE           = "locale";
    public static final String PARAM_BIRTHDATE        = "birthdate";
    public static final String PARAM_FNAME            = "fname";
    public static final String PARAM_MNAME            = "mname";
    public static final String PARAM_LNAME            = "lname";
    public static final String PARAM_PASSPORT         = "passport";
    public static final String PARAM_SCAN             = "scan";
    public static final String PARAM_QUESTION         = "question";
    public static final String PARAM_ANSWER           = "answer";
    public static final String PARAM_AMOUNT           = "amount";
    public static final String PARAM_TOPIC            = "topic";
    public static final String PARAM_TYPE             = "type";
    public static final String PARAM_MONTH            = "month";
    public static final String PARAM_ALL              = "all";
    public static final String PARAM_SATISFACTION     = "satisfaction";
    public static final String PARAM_ID               = "id";
    public static final String PARAM_PAGE             = "page";
    public static final String PARAM_ELEMENTS_ON_PAGE = "on_page";

    public static final char   QUERY_START_SEPARATOR = '?';
    public static final char   VALUE_SEPARATOR       = '=';
    public static final char   PARAMETER_SEPARATOR   = '&';
    public static final char   NEW_LINE_SEPARATOR    = '\n';
    public static final String MONTH_SEPARATOR       = "-";
    public static final String EMPTY_STRING          = "";
    public static final String WHITESPACE            = " ";
    public static final String DOT                   = ".";

    public static final String PAGE_INDEX              = "/index.jsp";
    public static final String PAGE_REGISTER           = "/pages/register.jsp";
    public static final String PAGE_MAIN               = "/pages/main.jsp";
    public static final String PAGE_SUPPORT            = "/pages/support.jsp";
    public static final String PAGE_STATS              = "/pages/stats.jsp";
    public static final String PAGE_PROFILE            = "/pages/profile.jsp";
    public static final String PAGE_VERIFICATION       = "/pages/verification.jsp";
    public static final String PAGE_EMAIL_VERIFICATION = "/pages/email_verification.jsp";
    public static final String PAGE_RECOVER_PASSWORD   = "/pages/recover_password.jsp";
    public static final String PAGE_ACCOUNT            = "/pages/account.jsp";
    public static final String PAGE_REPLENISH_ACCOUNT  = "/pages/replenish_account.jsp";
    public static final String PAGE_PAY_LOAN           = "/pages/pay_loan.jsp";
    public static final String PAGE_TAKE_LOAN          = "/pages/take_loan.jsp";
    public static final String PAGE_WITHDRAW_MONEY     = "/pages/withdraw_money.jsp";
    public static final String PAGE_OPERATION_HISTORY  = "/pages/operation_history.jsp";
    public static final String PAGE_UPLOAD_PASSPORT    = "/pages/upload_passport.jsp";
    public static final String PAGE_ERROR_500          = "/pages/error/error_500.jsp";

    public static final String GOTO_INDEX              = "/controller?command=goto_index";
    public static final String GOTO_REGISTER           = "/controller?command=goto_register";
    public static final String GOTO_MAIN               = "/controller?command=goto_main";
    public static final String GOTO_SUPPORT            = "/controller?command=goto_support";
    public static final String GOTO_STATS              = "/controller?command=goto_stats";
    public static final String GOTO_PROFILE            = "/controller?command=goto_player_profile";
    public static final String GOTO_VERIFICATION       = "/controller?command=goto_verification";
    public static final String GOTO_EMAIL_VERIFICATION = "/controller?command=goto_email_verification";
    public static final String GOTO_RECOVER_PASSWORD   = "/controller?command=goto_recover_password";
    public static final String GOTO_ACCOUNT            = "/controller?command=goto_account";
    public static final String GOTO_REPLENISH_ACCOUNT  = "/controller?command=goto_replenish_account";
    public static final String GOTO_PAY_LOAN           = "/controller?command=goto_pay_loan";
    public static final String GOTO_TAKE_LOAN          = "/controller?command=goto_take_loan";
    public static final String GOTO_WITHDRAW_MONEY     = "/controller?command=goto_withdraw_money";
    public static final String GOTO_OPERATION_HISTORY  = "/controller?command=goto_goto_operation_history";
    public static final String GOTO_ERROR_500          = "/controller?command=goto_error_500";
    public static final String BACK_FROM_ERROR         = "/controller?command=back_from_error";
    public static final String LOGIN_COMMAND_TEMPLATE  = "/controller?command=login&";

    public static final String MESSAGE_INVALID_AMOUNT            = "invalid.amount";
    public static final String MESSAGE_AMOUNT_LIMIT_ERROR        = "amount.limit.error";
    public static final String MESSAGE_INVALID_ANSWER            = "invalid.answer";
    public static final String MESSAGE_INVALID_BIRTHDATE         = "invalid.birthdate";
    public static final String MESSAGE_INVALID_EMAIL             = "invalid.email";
    public static final String MESSAGE_INVALID_EMAIL_EDIT        = "invalid.email.edit";
    public static final String MESSAGE_INVALID_EMAIL_INUSE       = "invalid.email.inuse";
    public static final String MESSAGE_INVALID_EMAIL_MISMATCH    = "invalid.email.mismatch";
    public static final String MESSAGE_INVALID_NAME              = "invalid.name";
    public static final String MESSAGE_INVALID_PASSPORT          = "invalid.passport";
    public static final String MESSAGE_INVALID_PASSWORD          = "invalid.password";
    public static final String MESSAGE_INVALID_QUESTION          = "invalid.question";
    public static final String MESSAGE_INVALID_SUPPORT_QUESTION  = "invalid.support.question";
    public static final String MESSAGE_INVALID_JSP               = "invalid.jspform";
    public static final String MESSAGE_INVALID_TOPIC             = "invalid.topic";
    public static final String MESSAGE_INVALID_DATE              = "invalid.date";
    public static final String MESSAGE_INVALID_DATE_OR_ALL       = "invalid.date.or.all";
    public static final String MESSAGE_INVALID_HISTORY_TYPE      = "invalid.history.type";
    public static final String MESSAGE_INVALID_MULTIPART_FORM    = "invalid.multipart.form";
    public static final String MESSAGE_LOGIN_MISMATCH            = "login.mismatch";
    public static final String MESSAGE_PASSWORD_MISMATCH         = "password.mismatch";
    public static final String MESSAGE_PASSWORD_MISMATCH_CURRENT = "password.mismatch.current";
    public static final String MESSAGE_PAY_LOAN_NOMONEY          = "loan.nomoney";
    public static final String MESSAGE_PAY_LOAN_INTERRUPTED      = "loan.interrupted";
    public static final String MESSAGE_TAKE_LOAN_INTERRUPTED     = "takeloan.interrupted";
    public static final String MESSAGE_RATE_SUPPORT_INTERRUPTED  = "ratesupport.interrupted";
    public static final String MESSAGE_WITHDRAWAL_OVERLIMIT      = "withdrawal.overlimit";
    public static final String MESSAGE_WITHDRAWAL_NOMONEY        = "withdrawal.nomoney";
    public static final String MESSAGE_WITHDRAWAL_INTERRUPTED    = "withdrawal.interrupted";
    public static final String MESSAGE_RECOVER_PASSWORD_ERROR    = "recoverpassword.error";
    public static final String MESSAGE_REPLENISH_INTERRUPTED     = "replenish.interrupted";
    public static final String MESSAGE_EMAILCODE_SEND_ERROR      = "emailcode.send.error";
    public static final String MESSAGE_VERIFY_EMAIL_ERROR        = "verify.email.error";
    public static final String MESSAGE_VERIFY_PROFILE_ERROR      = "verify.profile.error";
    public static final String MESSAGE_DATABASE_ERROR            = "database.error";
    public static final String MESSAGE_SEND_SUPPORT_INTERRUPTED  = "send.support.intrrupted";
    public static final String MESSAGE_MULTIPART_NO_FILE         = "multipart.no.file";
    public static final String MESSAGE_MULTIPART_UPLOAD_ERROR    = "multipart.upload.error";
    public static final String EMAIL_PATTERN_NEW_PASSWORD        = "email.pattern.newpassword";
    public static final String EMAIL_PATTERN_VERIFICATION_CODE   = "email.pattern.verificationcode";

    private ConfigConstant() {
    }
}