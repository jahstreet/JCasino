package by.sasnouskikh.jcasino.manager;

import by.sasnouskikh.jcasino.entity.bean.PlayerStats;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;

import java.util.List;

public class StatsManager {

    private StatsManager() {
    }

    public static PlayerStats buildStats(List<Streak> streaks, List<Transaction> transactions) {
        PlayerStats stats = new PlayerStats();
        stats.setMaxBet(StreakManager.defineMaxBet(streaks));
        stats.setTotalBet(StreakManager.countTotalBet(streaks));
        stats.setMaxWinRoll(StreakManager.defineMaxWinRoll(streaks));
        stats.setMaxWinStreak(StreakManager.defineMaxWinStreak(streaks));
        stats.setTotalWin(StreakManager.countTotalWin(streaks));
        stats.setMaxPayment(TransactionManager.defineMaxPayment(transactions));
        stats.setTotalPayment(TransactionManager.countTotalPayment(transactions));
        stats.setMaxWithdrawal(TransactionManager.defineMaxWithdrawal(transactions));
        stats.setTotalWithdrawal(TransactionManager.countTotalWithdrawal(transactions));
        return stats;
    }
}