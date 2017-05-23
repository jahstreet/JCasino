package by.sasnouskikh.jcasino.command;

import javax.servlet.http.HttpServletRequest;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.*;

/**
 * <p>Enumeration of objects used for navigation purposes in {@link by.sasnouskikh.jcasino.controller.MainController}.
 * Each contain 'query' and 'responseType' {@link String} objects.
 * <p>Objects to order {@link by.sasnouskikh.jcasino.controller.MainController} to go to definite page using
 * definite way and response type.
 * <p>There is 3 ways of processing navigation:
 * <ol>
 * <li>Forwarding to jsp-page</li>
 * <li>Forwarding to command-query, which processes navigation to definite page</li>
 * <li>Redirecting to command-query, which processes navigation to definite page</li>
 * </ol>
 *
 * @author Sasnouskikh Aliaksandr
 * @see by.sasnouskikh.jcasino.manager.ConfigConstant
 */
public enum PageNavigator {
    FORWARD_PAGE_INDEX(PAGE_INDEX, FORWARD),
    FORWARD_GOTO_INDEX(GOTO_INDEX, FORWARD),
    REDIRECT_GOTO_INDEX(GOTO_INDEX, REDIRECT),

    FORWARD_PAGE_REGISTER(PAGE_REGISTER, FORWARD),
    FORWARD_GOTO_REGISTER(GOTO_REGISTER, FORWARD),
    REDIRECT_GOTO_REGISTER(GOTO_REGISTER, REDIRECT),

    FORWARD_PAGE_MAIN(PAGE_MAIN, FORWARD),
    FORWARD_GOTO_MAIN(GOTO_MAIN, FORWARD),
    REDIRECT_GOTO_MAIN(GOTO_MAIN, REDIRECT),

    FORWARD_PAGE_SUPPORT(PAGE_SUPPORT, FORWARD),
    FORWARD_GOTO_SUPPORT(GOTO_SUPPORT, FORWARD),
    REDIRECT_GOTO_SUPPORT(GOTO_SUPPORT, REDIRECT),

    FORWARD_PAGE_STATS(PAGE_STATS, FORWARD),
    FORWARD_GOTO_STATS(GOTO_STATS, FORWARD),
    REDIRECT_GOTO_STATS(GOTO_STATS, REDIRECT),

    FORWARD_PAGE_PROFILE(PAGE_PROFILE, FORWARD),
    FORWARD_GOTO_PROFILE(GOTO_PROFILE, FORWARD),
    REDIRECT_GOTO_PROFILE(GOTO_PROFILE, REDIRECT),

    FORWARD_PAGE_VERIFICATION(PAGE_VERIFICATION, FORWARD),
    FORWARD_GOTO_VERIFICATION(GOTO_VERIFICATION, FORWARD),
    REDIRECT_GOTO_VERIFICATION(GOTO_VERIFICATION, REDIRECT),

    FORWARD_PAGE_EMAIL_VERIFICATION(PAGE_EMAIL_VERIFICATION, FORWARD),
    FORWARD_GOTO_EMAIL_VERIFICATION(GOTO_EMAIL_VERIFICATION, FORWARD),
    REDIRECT_GOTO_EMAIL_VERIFICATION(GOTO_EMAIL_VERIFICATION, REDIRECT),

    FORWARD_PAGE_RECOVER_PASSWORD(PAGE_RECOVER_PASSWORD, FORWARD),
    FORWARD_GOTO_RECOVER_PASSWORD(GOTO_RECOVER_PASSWORD, FORWARD),
    REDIRECT_GOTO_RECOVER_PASSWORD(GOTO_RECOVER_PASSWORD, REDIRECT),

    FORWARD_PAGE_ACCOUNT(PAGE_ACCOUNT, FORWARD),
    FORWARD_GOTO_ACCOUNT(GOTO_ACCOUNT, FORWARD),
    REDIRECT_GOTO_ACCOUNT(GOTO_ACCOUNT, REDIRECT),

    FORWARD_PAGE_REPLENISH_ACCOUNT(PAGE_REPLENISH_ACCOUNT, FORWARD),
    FORWARD_GOTO_REPLENISH_ACCOUNT(GOTO_REPLENISH_ACCOUNT, FORWARD),
    REDIRECT_GOTO_REPLENISH_ACCOUNT(GOTO_REPLENISH_ACCOUNT, REDIRECT),

    FORWARD_PAGE_TAKE_LOAN(PAGE_TAKE_LOAN, FORWARD),
    FORWARD_GOTO_TAKE_LOAN(GOTO_TAKE_LOAN, FORWARD),
    REDIRECT_GOTO_TAKE_LOAN(GOTO_TAKE_LOAN, REDIRECT),

    FORWARD_PAGE_PAY_LOAN(PAGE_PAY_LOAN, FORWARD),
    FORWARD_GOTO_PAY_LOAN(GOTO_PAY_LOAN, FORWARD),
    REDIRECT_GOTO_PAY_LOAN(GOTO_PAY_LOAN, REDIRECT),

    FORWARD_PAGE_WITHDRAW_MONEY(PAGE_WITHDRAW_MONEY, FORWARD),
    FORWARD_GOTO_WITHDRAW_MONEY(GOTO_WITHDRAW_MONEY, FORWARD),
    REDIRECT_GOTO_WITHDRAW_MONEY(GOTO_WITHDRAW_MONEY, REDIRECT),

    FORWARD_PAGE_OPERATION_HISTORY(PAGE_OPERATION_HISTORY, FORWARD),
    FORWARD_GOTO_OPERATION_HISTORY(GOTO_OPERATION_HISTORY, FORWARD),
    REDIRECT_GOTO_OPERATION_HISTORY(GOTO_OPERATION_HISTORY, REDIRECT),

    FORWARD_PAGE_UPLOAD_PASSPORT(PAGE_UPLOAD_PASSPORT, FORWARD),
    FORWARD_GOTO_UPLOAD_PASSPORT(GOTO_UPLOAD_PASSPORT, FORWARD),
    REDIRECT_GOTO_UPLOAD_PASSPORT(GOTO_UPLOAD_PASSPORT, REDIRECT),

    FORWARD_PAGE_RULES(PAGE_RULES, FORWARD),
    FORWARD_GOTO_RULES(GOTO_RULES, FORWARD),
    REDIRECT_GOTO_RULES(GOTO_RULES, REDIRECT),

    FORWARD_PAGE_ERROR_500(PAGE_ERROR_500, FORWARD),
    FORWARD_GOTO_ERROR_500(GOTO_ERROR_500, FORWARD),
    REDIRECT_GOTO_ERROR_500(GOTO_ERROR_500, REDIRECT),

    FORWARD_BACK_FROM_ERROR(BACK_FROM_ERROR, FORWARD),
    REDIRECT_BACK_FROM_ERROR(BACK_FROM_ERROR, REDIRECT),

    FORWARD_PAGE_ADMIN(PAGE_ADMIN, FORWARD),
    FORWARD_GOTO_ADMIN(GOTO_ADMIN, FORWARD),
    REDIRECT_GOTO_ADMIN(GOTO_ADMIN, REDIRECT),

    FORWARD_PAGE_MANAGE_NEWS(PAGE_MANAGE_NEWS, FORWARD),
    FORWARD_GOTO_MANAGE_NEWS(GOTO_MANAGE_NEWS, FORWARD),
    REDIRECT_GOTO_MANAGE_NEWS(GOTO_MANAGE_NEWS, REDIRECT),

    FORWARD_PAGE_MANAGE_PLAYERS(PAGE_MANAGE_PLAYERS, FORWARD),
    FORWARD_GOTO_MANAGE_PLAYERS(GOTO_MANAGE_PLAYERS, FORWARD),
    REDIRECT_GOTO_MANAGE_PLAYERS(GOTO_MANAGE_PLAYERS, REDIRECT),

    FORWARD_PAGE_MANAGE_SUPPORT(PAGE_MANAGE_SUPPORT, FORWARD),
    FORWARD_GOTO_MANAGE_SUPPORT(GOTO_MANAGE_SUPPORT, FORWARD),
    REDIRECT_GOTO_MANAGE_SUPPORT(GOTO_MANAGE_SUPPORT, REDIRECT),

    FORWARD_PAGE_ANSWER_SUPPORT(PAGE_ANSWER_SUPPORT, FORWARD),
    FORWARD_GOTO_ANSWER_SUPPORT(GOTO_ANSWER_SUPPORT, FORWARD),
    REDIRECT_GOTO_ANSWER_SUPPORT(GOTO_ANSWER_SUPPORT, REDIRECT),

    FORWARD_PAGE_MANAGE_VERIFICATION(PAGE_MANAGE_VERIFICATION, FORWARD),
    FORWARD_GOTO_MANAGE_VERIFICATION(GOTO_MANAGE_VERIFICATION, FORWARD),
    REDIRECT_GOTO_MANAGE_VERIFICATION(GOTO_MANAGE_VERIFICATION, REDIRECT),

    FORWARD_PAGE_MANAGE_LOANS(PAGE_MANAGE_LOANS, FORWARD),
    FORWARD_GOTO_MANAGE_LOANS(GOTO_MANAGE_LOANS, FORWARD),
    REDIRECT_GOTO_MANAGE_LOANS(GOTO_MANAGE_LOANS, REDIRECT),

    FORWARD_PAGE_MANAGE_PLAYER(PAGE_MANAGE_PLAYER, FORWARD),
    FORWARD_GOTO_MANAGE_PLAYER(GOTO_MANAGE_PLAYER, FORWARD),
    REDIRECT_GOTO_MANAGE_PLAYER(GOTO_MANAGE_PLAYER, REDIRECT),

    FORWARD_PAGE_MANAGE_TRANSACTIONS(PAGE_MANAGE_TRANSACTIONS, FORWARD),
    FORWARD_GOTO_MANAGE_TRANSACTIONS(GOTO_MANAGE_TRANSACTIONS, FORWARD),
    REDIRECT_GOTO_MANAGE_TRANSACTIONS(GOTO_MANAGE_TRANSACTIONS, REDIRECT),

    FORWARD_PAGE_MANAGE_STREAKS(PAGE_MANAGE_STREAKS, FORWARD),
    FORWARD_GOTO_MANAGE_STREAKS(GOTO_MANAGE_STREAKS, FORWARD),
    REDIRECT_GOTO_MANAGE_STREAKS(GOTO_MANAGE_STREAKS, REDIRECT),

    FORWARD_PAGE_STATS_REPORT(PAGE_STATS_REPORT, FORWARD),
    FORWARD_GOTO_STATS_REPORT(GOTO_STATS_REPORT, FORWARD),
    REDIRECT_GOTO_STATS_REPORT(GOTO_STATS_REPORT, REDIRECT),

    FORWARD_PAGE_GAME_FRUITS(PAGE_GAME_FRUITS, FORWARD),
    FORWARD_GOTO_GAME_FRUITS(GOTO_GAME_FRUITS, FORWARD),
    REDIRECT_GOTO_GAME_FRUITS(GOTO_GAME_FRUITS, REDIRECT),

    FORWARD_PAGE_GAME_FRUITS_SETUP(PAGE_GAME_FRUITS_SETUP, FORWARD),
    FORWARD_GOTO_GAME_FRUITS_SETUP(GOTO_GAME_FRUITS_SETUP, FORWARD),
    REDIRECT_GOTO_GAME_FRUITS_SETUP(GOTO_GAME_FRUITS_SETUP, REDIRECT),

    /**
     * Used to order {@link by.sasnouskikh.jcasino.controller.MainController} to take previous query from
     * {@link by.sasnouskikh.jcasino.manager.QueryManager#takePreviousQuery(HttpServletRequest)} and process it using
     * Forward response type
     */
    FORWARD_PREV_QUERY(PREV_QUERY, FORWARD),
    /**
     * Used to order {@link by.sasnouskikh.jcasino.controller.MainController} to take previous query from
     * {@link by.sasnouskikh.jcasino.manager.QueryManager#takePreviousQuery(HttpServletRequest)} and process it using
     * Redirect response type
     */
    REDIRECT_PREV_QUERY(PREV_QUERY, REDIRECT);

    /**
     * Query to process
     */
    private String query;

    /**
     * Response type of query processing
     *
     * @see by.sasnouskikh.jcasino.manager.ConfigConstant#FORWARD
     * @see by.sasnouskikh.jcasino.manager.ConfigConstant#REDIRECT
     */
    private String responseType;

    /**
     * Constructs enumeration objects with definite query and response type of its processing
     *
     * @param query        - query to process
     * @param responseType - response type of query processing
     */
    PageNavigator(String query, String responseType) {
        this.query = query;
        this.responseType = responseType;
    }

    /**
     * Getter of query
     *
     * @return {@link String} query to process
     */
    public String getQuery() {
        return query;
    }

    /**
     * Getter of response type
     *
     * @return {@link String} response type of query processing
     */
    public String getResponseType() {
        return responseType;
    }
}