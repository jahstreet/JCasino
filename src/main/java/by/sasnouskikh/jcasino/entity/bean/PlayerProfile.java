package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * The class represents info about application player profile.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class PlayerProfile extends Entity {

    /**
     * Player first name.
     */
    private String    fName;
    /**
     * Player middle name.
     */
    private String    mName;
    /**
     * Player last name.
     */
    private String    lName;
    /**
     * Player birthdate name. Player should be at least 18 years old.
     */
    private LocalDate birthDate;
    /**
     * Player passport number name.
     */
    private String    passport;
    /**
     * Player secret question.
     */
    private String    question;

    /**
     * {@link #fName} getter.
     *
     * @return {@link #fName}
     */
    public String getfName() {
        return fName;
    }

    /**
     * {@link #fName} setter.
     *
     * @param fName player first name
     */
    public void setfName(String fName) {
        this.fName = fName;
    }

    /**
     * {@link #mName} getter.
     *
     * @return {@link #mName}
     */
    public String getmName() {
        return mName;
    }

    /**
     * {@link #mName} setter.
     *
     * @param mName player middle name
     */
    public void setmName(String mName) {
        this.mName = mName;
    }

    /**
     * {@link #lName} getter.
     *
     * @return {@link #lName}
     */
    public String getlName() {
        return lName;
    }

    /**
     * {@link #lName} setter.
     *
     * @param lName player last name
     */
    public void setlName(String lName) {
        this.lName = lName;
    }

    /**
     * {@link #birthDate} getter.
     *
     * @return {@link #birthDate}
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * {@link #birthDate} setter.
     *
     * @param birthDate player birthdate
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * {@link #passport} getter.
     *
     * @return {@link #passport}
     */
    public String getPassport() {
        return passport;
    }

    /**
     * {@link #passport} setter.
     *
     * @param passport player passport number
     */
    public void setPassport(String passport) {
        this.passport = passport;
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
     * @param question player secret question
     */
    public void setQuestion(String question) {
        this.question = question;
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
        if (!(o instanceof PlayerProfile)) {
            return false;
        }
        PlayerProfile profile = (PlayerProfile) o;
        return Objects.equals(fName, profile.fName) &&
               Objects.equals(mName, profile.mName) &&
               Objects.equals(lName, profile.lName) &&
               Objects.equals(birthDate, profile.birthDate) &&
               Objects.equals(passport, profile.passport) &&
               Objects.equals(question, profile.question);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(fName, mName, lName, birthDate, passport, question);
    }

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "PlayerProfile{" + "fName='" + fName + '\'' +
               ", mName='" + mName + '\'' +
               ", lName='" + lName + '\'' +
               ", birthDate=" + birthDate +
               ", passport='" + passport + '\'' +
               ", question='" + question + '\'' +
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
    public PlayerProfile clone() throws CloneNotSupportedException {
        return (PlayerProfile) super.clone();
    }
}