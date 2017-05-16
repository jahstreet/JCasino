package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The class represents info about application support question.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class Question extends Entity {

    /**
     * Question unique id.
     */
    private int           id;
    /**
     * Player id who asked this question.
     */
    private int           playerId;
    /**
     * User e-mail who asked this question.
     */
    private String        email;
    /**
     * Question topic enumeration instance.
     *
     * @see QuestionTopic
     */
    private QuestionTopic topic;
    /**
     * Question text.
     */
    private String        question;
    /**
     * Question date.
     */
    private LocalDateTime questionDate;
    /**
     * Admin id who answered this question.
     */
    private int           adminId;
    /**
     * Admin answer to this question.
     */
    private String        answer;
    /**
     * Answer date.
     */
    private LocalDateTime answerDate;
    /**
     * Player satisfaction with admin answer enumeration instance.
     *
     * @see Satisfaction
     */
    private Satisfaction  satisfaction;

    /**
     * Enumeration of available {@link #topic} value instances.
     */
    public enum QuestionTopic {
        TRANSACTION, LOAN, RULES, VERIFICATION, BAN, OTHER
    }

    /**
     * Enumeration of available {@link #satisfaction} value instances.
     */
    public enum Satisfaction {
        BEST, GOOD, NORM, BAD, WORST

    }

    /**
     * {@link #id} getter.
     *
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }

    /**
     * {@link #id} setter.
     *
     * @param id question unique id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * {@link #playerId} getter.
     *
     * @return {@link #playerId}
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * {@link #playerId} setter.
     *
     * @param playerId player id who asked this question
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * {@link #topic} getter.
     *
     * @return {@link #topic}
     */
    public QuestionTopic getTopic() {
        return topic;
    }

    /**
     * {@link #topic} setter.
     *
     * @param topic question topic enumeration instance
     * @see QuestionTopic
     */
    public void setTopic(QuestionTopic topic) {
        this.topic = topic;
    }

    /**
     * {@link #question} getter.
     *
     * @return {@link #question}
     */
    public String getQuestion() {
        return question;
    }

    /**
     * {@link #question} setter.
     *
     * @param question question text
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * {@link #questionDate} getter.
     *
     * @return {@link #questionDate}
     */
    public LocalDateTime getQuestionDate() {
        return questionDate;
    }

    /**
     * {@link #questionDate} setter.
     *
     * @param questionDate question date
     */
    public void setQuestionDate(LocalDateTime questionDate) {
        this.questionDate = questionDate;
    }

    /**
     * {@link #adminId} getter.
     *
     * @return {@link #adminId}
     */
    public int getAdminId() {
        return adminId;
    }

    /**
     * {@link #adminId} setter.
     *
     * @param adminId admin id who answered this question
     */
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    /**
     * {@link #answer} getter.
     *
     * @return {@link #answer}
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * {@link #answer} setter.
     *
     * @param answer admin answer to this question
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * {@link #answerDate} getter.
     *
     * @return {@link #answerDate}
     */
    public LocalDateTime getAnswerDate() {
        return answerDate;
    }

    /**
     * {@link #answerDate} setter.
     *
     * @param answerDate answer date
     */
    public void setAnswerDate(LocalDateTime answerDate) {
        this.answerDate = answerDate;
    }

    /**
     * {@link #satisfaction} getter.
     *
     * @return {@link #satisfaction}
     */
    public Satisfaction getSatisfaction() {
        return satisfaction;
    }

    /**
     * {@link #satisfaction} setter.
     *
     * @param satisfaction player satisfaction with admin answer enumeration instance
     * @see Satisfaction
     */
    public void setSatisfaction(Satisfaction satisfaction) {
        this.satisfaction = satisfaction;
    }

    /**
     * {@link #email} getter.
     *
     * @return {@link #email}
     */
    public String getEmail() {
        return email;
    }

    /**
     * {@link #email} setter.
     *
     * @param email user e-mail who asked this question
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Checks if this instance equals given object.
     *
     * @param o object to compare with
     * @return true if this instance equals given object
     * @see Objects#equals(Object, Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        Question question1 = (Question) o;
        return id == question1.id &&
               playerId == question1.playerId &&
               adminId == question1.adminId &&
               Objects.equals(email, question1.email) &&
               topic == question1.topic &&
               Objects.equals(question, question1.question) &&
               Objects.equals(questionDate, question1.questionDate) &&
               Objects.equals(answer, question1.answer) &&
               Objects.equals(answerDate, question1.answerDate) &&
               satisfaction == question1.satisfaction;
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, playerId, email, topic, question, questionDate, adminId, answer, answerDate, satisfaction);
    }

    @Override
    public String toString() {
        return "Question{" + "id=" + id +
               ", playerId=" + playerId +
               ", email='" + email + '\'' +
               ", topic=" + topic +
               ", question='" + question + '\'' +
               ", questionDate=" + questionDate +
               ", adminId=" + adminId +
               ", answer='" + answer + '\'' +
               ", answerDate=" + answerDate +
               ", satisfaction=" + satisfaction +
               '}';
    }

    /**
     * Clones instance of this object.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @see Cloneable
     */
    @Override
    public Question clone() throws CloneNotSupportedException {
        return (Question) super.clone();
    }
}