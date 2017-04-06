package by.sasnouskikh.jcasino.logic;

import by.sasnouskikh.jcasino.entity.bean.PlayerStats;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;

import java.util.List;

class StatsHelper {

    private StatsHelper() {
    }

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