package by.sasnouskikh.jcasino.entity.bean;

import java.util.ArrayList;
import java.util.List;

public class Player extends JCasinoUser {

    private PlayerProfile      profile;
    private PlayerAccount      account;
    private PlayerStats        stats;
    private PlayerVerification verification;
    private List<Transaction>  transactions;
    private List<Streak>       streaks;
    private List<Question>     questions;
    private List<Loan>         loans;

    public PlayerProfile getProfile() {
        return profile;
    }

    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
    }

    public PlayerAccount getAccount() {
        return account;
    }

    public void setAccount(PlayerAccount account) {
        this.account = account;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }

    public PlayerVerification getVerification() {
        return verification;
    }

    public void setVerification(PlayerVerification verification) {
        this.verification = verification;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Streak> getStreaks() {
        return streaks;
    }

    public void setStreaks(List<Streak> streaks) {
        this.streaks = streaks;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}