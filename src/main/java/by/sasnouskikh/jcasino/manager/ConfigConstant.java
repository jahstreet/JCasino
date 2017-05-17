package by.sasnouskikh.jcasino.manager;

import java.math.BigDecimal;

public class ConfigConstant {

    /**
     * Locale strings
     */
    public static final String DEFAULT_LOCALE = "default";
    public static final String LOCALE_RU      = "ru_RU";
    public static final String LOCALE_EN      = "en_US";

    /**
     * NewsList {@link javax.servlet.ServletContext} attribute
     */
    public static final String CONTEXT_NEWSLIST = "newsList";

    /**
     * Upload images config constants
     *
     * @see javax.servlet.ServletContext#getInitParameter(String)
     */
    public static final String   INIT_PARAM_UPLOADS       = "uploads.dir";
    public static final String   SCAN_UPLOAD_DIR          = "scan";
    public static final String   NEWS_UPLOAD_DIR          = "news";
    public static final String   NEWS_IMAGE_NAME_PATTERN  = "news-image";
    public static final String[] AVAILABLE_SCAN_EXT       = new String[]{"gif", "jpeg", "jpg", "png"};
    public static final String[] AVAILABLE_NEWS_IMAGE_EXT = new String[]{"jpg"};

    /**
     * Additional string constants
     */
    public static final char   QUERY_START_SEPARATOR = '?';
    public static final char   VALUE_SEPARATOR       = '=';
    public static final char   PARAMETER_SEPARATOR   = '&';
    public static final char   MESSAGE_SEPARATOR     = ' ';
    public static final String PERCENT               = "%";
    public static final String EMPTY_STRING          = "";
    public static final String WHITESPACE            = " ";
    public static final String DOT                   = ".";
    public static final String ALL                   = "all";

    /**
     * Profile verification byte masks
     */
    public static final byte PROFILE_VER_MASK  = 0b001;
    public static final byte EMAIL_VER_MASK    = 0b010;
    public static final byte PASSPORT_VER_MASK = 0b100;
    public static final byte FULL_VER_MASK     = 0b111;

    /**
     * Other configuration constants.
     */
    public static final int LOAN_TERM_MONTH = 1;

    /**
     * Slot-machine constants
     */
    public static final int        REEL_LENGTH      = 60;
    public static final int        BETS_IN_STREAK   = 10;
    public static final int        REEL_NUMBER      = 3;
    public static final int        LINE_NUMBER      = 5;
    public static final BigDecimal DEMO_START_MONEY = BigDecimal.valueOf(200);

    /**
     * AJAX response parameter names
     */
    public static final String AJAX_STREAK_INFO = "streakInfo";
    public static final String AJAX_ROLL_NUMBER = "rollNumber";
    public static final String AJAX_WIN_RESULT  = "winResult";
    public static final String AJAX_OFFSETS     = "offsets";
    public static final String AJAX_LINES       = "lines";
    public static final String AJAX_MONEY       = "money";

    /**
     * {@link javax.servlet.http.HttpServletRequest} and {@link javax.servlet.http.HttpSession} attribute common names
     */
    public static final String ATTR_ROLE          = "role";
    public static final String ATTR_USER          = "user";
    public static final String ATTR_PLAYER        = "player";
    public static final String ATTR_ADMIN         = "admin";
    public static final String ATTR_LOCALE        = "locale";
    public static final String ATTR_PREV_QUERY    = "prevQuery";
    public static final String ATTR_ERROR_MESSAGE = "errorMessage";

    /**
     * {@link javax.servlet.http.HttpServletRequest} and {@link javax.servlet.http.HttpSession} attribute local names
     */
    public static final String ATTR_AMOUNT_INPUT        = "amount_input";
    public static final String ATTR_ANSWER_INPUT        = "answer_input";
    public static final String ATTR_BIRTHDATE_INPUT     = "birthdate_input";
    public static final String ATTR_COMMENTARY_INPUT    = "commentary_input";
    public static final String ATTR_CURRENT_STREAK      = "current_streak";
    public static final String ATTR_DEMO_PLAY           = "demo_play";
    public static final String ATTR_EMAIL_INPUT         = "email_input";
    public static final String ATTR_FNAME_INPUT         = "fname_input";
    public static final String ATTR_LNAME_INPUT         = "lname_input";
    public static final String ATTR_LOANS               = "loans";
    public static final String ATTR_MNAME_INPUT         = "mname_input";
    public static final String ATTR_MONEY_INPUT         = "money_input";
    public static final String ATTR_MONTH_INPUT         = "month_input";
    public static final String ATTR_MONTH_ACQUIRE_INPUT = "month_acquire_input";
    public static final String ATTR_MONTH_EXPIRE_INPUT  = "month_expire_input";
    public static final String ATTR_PASSPORT_INPUT      = "passport_input";
    public static final String ATTR_QUESTION            = "question";
    public static final String ATTR_QUESTION_INPUT      = "question_input";
    public static final String ATTR_QUESTION_LIST       = "questionList";
    public static final String ATTR_STREAK_INFO         = "streak_info";
    public static final String ATTR_STREAKS             = "streaks";
    public static final String ATTR_TRANSACTIONS        = "transactions";
    public static final String ATTR_VERIFICATION_LIST   = "verification_list";

    /**
     * {@link javax.servlet.http.HttpServletRequest} parameter names
     */
    public static final String PARAM_AMOUNT               = "amount";
    public static final String PARAM_ANSWER               = "answer";
    public static final String PARAM_BET                  = "bet";
    public static final String PARAM_BIRTHDATE            = "birthdate";
    public static final String PARAM_COMMAND              = "command";
    public static final String PARAM_COMMENTARY           = "commentary";
    public static final String PARAM_DEMO                 = "demo";
    public static final String PARAM_EMAIL                = "email";
    public static final String PARAM_EMAIL_CODE           = "email_code";
    public static final String PARAM_FILTER_NOT_PAID      = "filter_not_paid";
    public static final String PARAM_FILTER_OVERDUED      = "filter_overdued";
    public static final String PARAM_FNAME                = "fname";
    public static final String PARAM_HEADER               = "header";
    public static final String PARAM_ID                   = "id";
    public static final String PARAM_LINE1                = "line1";
    public static final String PARAM_LINE2                = "line2";
    public static final String PARAM_LINE3                = "line3";
    public static final String PARAM_LINE4                = "line4";
    public static final String PARAM_LINE5                = "line5";
    public static final String PARAM_LNAME                = "lname";
    public static final String PARAM_LOCALE               = "locale";
    public static final String PARAM_MNAME                = "mname";
    public static final String PARAM_MONTH                = "month";
    public static final String PARAM_MONTH_ACQUIRE        = "month_acquire";
    public static final String PARAM_MONTH_EXPIRE         = "month_expire";
    public static final String PARAM_NEWS_IMAGE           = "news_image";
    public static final String PARAM_OFFSET1              = "offset1";
    public static final String PARAM_OFFSET2              = "offset2";
    public static final String PARAM_OFFSET3              = "offset3";
    public static final String PARAM_PASSPORT             = "passport";
    public static final String PARAM_PASSWORD             = "password";
    public static final String PARAM_PASSWORD_AGAIN       = "password_again";
    public static final String PARAM_PASSWORD_OLD         = "old_password";
    public static final String PARAM_QUESTION             = "question";
    public static final String PARAM_SORT_BY_SATISFACTION = "satisfaction";
    public static final String PARAM_SCAN                 = "scan";
    public static final String PARAM_TARGET               = "target";
    public static final String PARAM_FILTER_MY            = "show_my";
    public static final String PARAM_SORT_BY_AMOUNT       = "sort_by_amount";
    public static final String PARAM_SORT_BY_REST         = "sort_by_rest";
    public static final String PARAM_SORT_BY_TOTAL        = "sort_by_total";
    public static final String PARAM_STATUS               = "status";
    public static final String PARAM_TEXT                 = "text";
    public static final String PARAM_TOPIC                = "topic";
    public static final String PARAM_TYPE                 = "type";

    /**
     * Server message property keys
     *
     * @see MessageManager
     */
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
    public static final String MESSAGE_QUESTION_NOT_EXIST        = "question.not.exist";
    public static final String MESSAGE_PLAYER_NOT_EXIST          = "player.not.exist";
    public static final String MESSAGE_PLAYER_EMAIL_EXIST        = "player.email.exist";
    public static final String MESSAGE_PLAYER_NO_LOANS           = "player.no.loans";
    public static final String MESSAGE_PLAYER_NO_STREAKS         = "player.no.streaks";
    public static final String MESSAGE_PLAYER_NO_QUESTIONS       = "player.no.questions";
    public static final String MESSAGE_PLAYER_NO_TRANSACTIONS    = "player.no.transactions";
    public static final String MESSAGE_CODE_NOT_SENT_ERROR       = "code.not.sent.error";
    public static final String MESSAGE_ANSWER_SUPPORT_ERROR      = "answer.support.error";
    public static final String MESSAGE_DUMP_ZIP_ERROR            = "dump.zip.error";
    public static final String MESSAGE_VERIFY_SCAN_ERROR         = "verify.scan.error";
    public static final String MESSAGE_NO_MONEY                  = "not.enough.money";
    public static final String MESSAGE_DATABASE_ACCESS_ERROR     = "database.error";
    public static final String MESSAGE_INVALID_JSP               = "invalid.jspform";
    public static final String MESSAGE_INVALID_SUPPORT           = "invalid.support";
    public static final String MESSAGE_INVALID_NEWS_TEXT         = "invalid.news.text";
    public static final String MESSAGE_INVALID_NEWS_HEADER       = "invalid.news.header";
    public static final String MESSAGE_INVALID_TOPIC             = "invalid.topic";
    public static final String MESSAGE_INVALID_DATE_OR_ALL       = "invalid.date.or.all";
    public static final String MESSAGE_INVALID_HISTORY_TYPE      = "invalid.history.type";
    public static final String MESSAGE_UNKNOWN_COMMAND           = "unknown.command";
    public static final String MESSAGE_INVALID_FILE_EXTENSION    = "invalid.file.ext";
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
    public static final String MESSAGE_STREAK_GENERATION_ERROR   = "streak.generation.error";
    public static final String MESSAGE_SEND_SUPPORT_INTERRUPTED  = "send.support.intrrupted";
    public static final String MESSAGE_MULTIPART_UPLOAD_ERROR    = "multipart.upload.error";
    public static final String EMAIL_PATTERN_NEW_PASSWORD        = "email.pattern.newpassword";
    public static final String EMAIL_PATTERN_VERIFICATION_CODE   = "email.pattern.verificationcode";
    public static final String EMAIL_PATTERN_ANSWER_SUPPORT      = "email.pattern.answersupport";

    /**
     * Navigation response types
     */
    public static final String FORWARD  = "forward";
    public static final String REDIRECT = "redirect";

    /**
     * Navigation previous query constant
     */
    public static final String PREV_QUERY = "prevQuery";

    /**
     * Common JSP pages paths
     */
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
    public static final String PAGE_TAKE_LOAN          = "/pages/take_loan.jsp";
    public static final String PAGE_PAY_LOAN           = "/pages/pay_loan.jsp";
    public static final String PAGE_WITHDRAW_MONEY     = "/pages/withdraw_money.jsp";
    public static final String PAGE_OPERATION_HISTORY  = "/pages/operation_history.jsp";
    public static final String PAGE_UPLOAD_PASSPORT    = "/pages/upload_passport.jsp";
    public static final String PAGE_RULES              = "/pages/rules.jsp";
    public static final String PAGE_ERROR_500          = "/pages/error/error_500.jsp";

    /**
     * Common navigation queries
     */
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
    public static final String GOTO_TAKE_LOAN          = "/controller?command=goto_take_loan";
    public static final String GOTO_PAY_LOAN           = "/controller?command=goto_pay_loan";
    public static final String GOTO_WITHDRAW_MONEY     = "/controller?command=goto_withdraw_money";
    public static final String GOTO_OPERATION_HISTORY  = "/controller?command=goto_operation_history";
    public static final String GOTO_UPLOAD_PASSPORT    = "/controller?command=goto_upload_passport";
    public static final String GOTO_RULES              = "/controller?command=goto_rules";
    public static final String GOTO_ERROR_500          = "/controller?command=goto_error_500";
    public static final String BACK_FROM_ERROR         = "/controller?command=back_from_error";

    /**
     * Admin JSP pages paths
     */
    public static final String PAGE_ADMIN               = "/admin/jcasino_admin.jsp";
    public static final String PAGE_MANAGE_NEWS         = "/admin/manage_news.jsp";
    public static final String PAGE_MANAGE_SUPPORT      = "/admin/manage_support.jsp";
    public static final String PAGE_ANSWER_SUPPORT      = "/admin/answer_support.jsp";
    public static final String PAGE_MANAGE_VERIFICATION = "/admin/manage_verification.jsp";
    public static final String PAGE_MANAGE_LOANS        = "/admin/manage_loans.jsp";
    public static final String PAGE_MANAGE_PLAYER       = "/admin/manage_player.jsp";
    public static final String PAGE_MANAGE_TRANSACTIONS = "/admin/manage_transactions.jsp";
    public static final String PAGE_MANAGE_STREAKS      = "/admin/manage_streaks.jsp";
    public static final String PAGE_STATS_REPORT        = "/admin/stats_report.jsp";

    /**
     * Admin navigation queries
     */
    public static final String GOTO_ADMIN               = "/controller?command=goto_admin";
    public static final String GOTO_MANAGE_NEWS         = "/controller?command=goto_manage_news";
    public static final String GOTO_MANAGE_SUPPORT      = "/controller?command=goto_manage_support";
    public static final String GOTO_ANSWER_SUPPORT      = "/controller?command=goto_answer_support";
    public static final String GOTO_MANAGE_VERIFICATION = "/controller?command=goto_manage_verification";
    public static final String GOTO_MANAGE_LOANS        = "/controller?command=goto_manage_loans";
    public static final String GOTO_MANAGE_PLAYER       = "/controller?command=goto_manage_player";
    public static final String GOTO_MANAGE_TRANSACTIONS = "/controller?command=goto_manage_transactions";
    public static final String GOTO_MANAGE_STREAKS      = "/controller?command=goto_manage_streaks";
    public static final String GOTO_STATS_REPORT        = "/controller?command=goto_manage_report";

    /**
     * Game navigation constants
     */
    public static final String GOTO_GAME_FRUITS       = "/controller?command=goto_game_fruits";
    public static final String PAGE_GAME_FRUITS       = "/game/fruits.jsp";
    public static final String GOTO_GAME_FRUITS_SETUP = "/controller?command=goto_game_fruits_setup";
    public static final String PAGE_GAME_FRUITS_SETUP = "/pages/setup_fruits.jsp";

    /**
     * Private constructor to forbid create {@link ConfigConstant} instances.
     */
    private ConfigConstant() {
    }
}