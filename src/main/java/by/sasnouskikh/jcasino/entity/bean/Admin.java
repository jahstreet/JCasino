package by.sasnouskikh.jcasino.entity.bean;

import java.util.ArrayList;

public class Admin extends JCasinoUser {
    private ArrayList<News>               news;
    private ArrayList<PlayerVerification> verifications;
    private ArrayList<Question>           questions;
    private ArrayList<Player>             statusedPlayers;

    public ArrayList<News> getNews() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    public ArrayList<PlayerVerification> getVerifications() {
        return verifications;
    }

    public void setVerifications(ArrayList<PlayerVerification> verifications) {
        this.verifications = verifications;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Player> getStatusedPlayers() {
        return statusedPlayers;
    }

    public void setStatusedPlayers(ArrayList<Player> statusedPlayers) {
        this.statusedPlayers = statusedPlayers;
    }
}