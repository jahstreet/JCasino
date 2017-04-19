package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.entity.bean.PlayerStats;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;

import java.util.List;

/**
 * The class provides helper for work with {@link PlayerStats} objects.
 *
 * @author Sasnouskikh Aliaksandr
 */
class StatsHelper {

    /**
     * Outer forbidding to create this class instances.
     */
    private StatsHelper() {
    }

    /**
     * Builds {@link PlayerStats} object due to given parameters.
     *
     * @param streaks      {@link List} collection of {@link Streak} objects to parse
     * @param transactions {@link List} collection of {@link Transaction} objects to parse
     * @return built {@link PlayerStats} object
     * @see StreakLogic#defineMaxBet(List)
     * @see StreakLogic#countTotalBet(List)
     * @see StreakLogic#defineMaxWinRoll(List)
     * @see StreakLogic#defineMaxWinStreak(List)
     * @see StreakLogic#countTotalWin(List)
     * @see TransactionLogic#defineMaxPayment(List)
     * @see TransactionLogic#countTotalPayment(List)
     * @see TransactionLogic#defineMaxWithdrawal(List)
     * @see TransactionLogic#countTotalWithdrawal(List)
     */
    static PlayerStats buildStats(List<Streak> streaks, List<Transaction> transactions) {
        PlayerStats stats = new PlayerStats();
        stats.setMaxBet(StreakLogic.defineMaxBet(streaks));
        stats.setTotalBet(StreakLogic.countTotalBet(streaks));
        stats.setMaxWinRoll(StreakLogic.defineMaxWinRoll(streaks));
        stats.setMaxWinStreak(StreakLogic.defineMaxWinStreak(streaks));
        stats.setTotalWin(StreakLogic.countTotalWin(streaks));
        stats.setMaxPayment(TransactionLogic.defineMaxPayment(transactions));
        stats.setTotalPayment(TransactionLogic.countTotalPayment(transactions));
        stats.setMaxWithdrawal(TransactionLogic.defineMaxWithdrawal(transactions));
        stats.setTotalWithdrawal(TransactionLogic.countTotalWithdrawal(transactions));
        return stats;
    }
}