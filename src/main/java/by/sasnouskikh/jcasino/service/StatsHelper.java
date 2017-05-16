package by.sasnouskikh.jcasino.service;

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
     * @see StreakService#defineMaxBet(List)
     * @see StreakService#countTotalBet(List)
     * @see StreakService#defineMaxWinRoll(List)
     * @see StreakService#defineMaxWinStreak(List)
     * @see StreakService#countTotalWin(List)
     * @see TransactionService#defineMaxPayment(List)
     * @see TransactionService#countTotalPayment(List)
     * @see TransactionService#defineMaxWithdrawal(List)
     * @see TransactionService#countTotalWithdrawal(List)
     */
    static PlayerStats buildStats(List<Streak> streaks, List<Transaction> transactions) {
        PlayerStats stats = new PlayerStats();
        stats.setMaxBet(StreakService.defineMaxBet(streaks));
        stats.setTotalBet(StreakService.countTotalBet(streaks));
        stats.setMaxWinRoll(StreakService.defineMaxWinRoll(streaks));
        stats.setMaxWinStreak(StreakService.defineMaxWinStreak(streaks));
        stats.setTotalWin(StreakService.countTotalWin(streaks));
        stats.setMaxPayment(TransactionService.defineMaxPayment(transactions));
        stats.setTotalPayment(TransactionService.countTotalPayment(transactions));
        stats.setMaxWithdrawal(TransactionService.defineMaxWithdrawal(transactions));
        stats.setTotalWithdrawal(TransactionService.countTotalWithdrawal(transactions));
        return stats;
    }
}