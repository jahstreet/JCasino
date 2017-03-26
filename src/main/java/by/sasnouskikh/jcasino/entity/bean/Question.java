package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.time.LocalDateTime;

public class Question extends Entity {
    private int           id;
    private int           playerId;
    private QuestionTopic topic;
    private String        question;
    private LocalDateTime questionDate;
    private int           adminid;
    private String        answer;
    private LocalDateTime answerDate;
    private Satisfaction  satisfaction;

    public enum QuestionTopic {
        TRANSACTION, LOAN, RULES, VERIFICATION, BAN, OTHER
    }

    public enum Satisfaction {
        BEST(), GOOD, NORM, BAD, WORST

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public QuestionTopic getTopic() {
        return topic;
    }

    public void setTopic(QuestionTopic topic) {
        this.topic = topic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDateTime getQuestionDate() {
        return questionDate;
    }

    public void setQuestionDate(LocalDateTime questionDate) {
        this.questionDate = questionDate;
    }

    public int getAdminid() {
        return adminid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(LocalDateTime answerDate) {
        this.answerDate = answerDate;
    }

    public Satisfaction getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Satisfaction satisfaction) {
        this.satisfaction = satisfaction;
    }
}