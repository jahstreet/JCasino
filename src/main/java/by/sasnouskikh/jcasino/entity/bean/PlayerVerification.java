package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The class represents info about application player verification.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class PlayerVerification extends Entity {

    /**
     * Player id.
     */
    private int                playerId;
    /**
     * E-mail code to verify player e-mail.
     */
    private String             emailCode;
    /**
     * Player profile verification state.
     */
    private boolean            profileVerified;
    /**
     * Player e-mail verification state.
     */
    private boolean            emailVerified;
    /**
     * Player passport scan verification state.
     */
    private boolean            scanVerified;
    /**
     * Player passport scan relative path.
     *
     * @see by.sasnouskikh.jcasino.controller.ShowImageServlet
     */
    private String             passport;
    /**
     * Player verification status enumeration instance.
     */
    private VerificationStatus status;
    /**
     * Admin id who verified player.
     */
    private int                adminId;
    /**
     * Admin commentary to player verifying.
     */
    private String             commentary;
    /**
     * Player verification date.
     */
    private LocalDateTime      verificationDate;

    /**
     * Enumeration of available {@link #status} value instances.
     */
    public enum VerificationStatus {
        VERIFIED, NOT_VERIFIED
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
     * @param playerId player id
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * {@link #emailCode} getter.
     *
     * @return {@link #emailCode}
     */
    public String getEmailCode() {
        return emailCode;
    }

    /**
     * {@link #emailCode} setter.
     *
     * @param emailCode e-mail code to verify player e-mail
     */
    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    /**
     * {@link #profileVerified} getter.
     *
     * @return {@link #profileVerified}
     */
    public boolean isProfileVerified() {
        return profileVerified;
    }

    /**
     * {@link #profileVerified} setter.
     *
     * @param profileVerified player profile verification state
     */
    public void setProfileVerified(boolean profileVerified) {
        this.profileVerified = profileVerified;
    }

    /**
     * {@link #emailVerified} getter.
     *
     * @return {@link #emailVerified}
     */
    public boolean isEmailVerified() {
        return emailVerified;
    }

    /**
     * {@link #emailVerified} setter.
     *
     * @param emailVerified player e-mail verification state
     */
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * {@link #scanVerified} getter.
     *
     * @return {@link #scanVerified}
     */
    public boolean isScanVerified() {
        return scanVerified;
    }

    /**
     * {@link #scanVerified} setter.
     *
     * @param scanVerified player passport scan verification state
     */
    public void setScanVerified(boolean scanVerified) {
        this.scanVerified = scanVerified;
    }

    /**
     * {@link #status} getter.
     *
     * @return {@link #status}
     */
    public VerificationStatus getStatus() {
        return status;
    }

    /**
     * {@link #status} setter.
     *
     * @param status player verification status enumeration instance
     */
    public void setStatus(VerificationStatus status) {
        this.status = status;
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
     * @param adminId admin id who verified player
     */
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    /**
     * {@link #verificationDate} getter.
     *
     * @return {@link #verificationDate}
     */
    public LocalDateTime getVerificationDate() {
        return verificationDate;
    }

    /**
     * {@link #verificationDate} setter.
     *
     * @param verificationDate player verification date
     */
    public void setVerificationDate(LocalDateTime verificationDate) {
        this.verificationDate = verificationDate;
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
     * @param passport player passport scan relative path
     * @see by.sasnouskikh.jcasino.controller.ShowImageServlet
     */
    public void setPassport(String passport) {
        this.passport = passport;
    }

    /**
     * {@link #commentary} getter.
     *
     * @return {@link #commentary}
     */
    public String getCommentary() {
        return commentary;
    }

    /**
     * {@link #commentary} getter.
     *
     * @param commentary admin commentary to player verifying
     */
    public void setCommentary(String commentary) {
        this.commentary = commentary;
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
        if (!(o instanceof PlayerVerification)) {
            return false;
        }
        PlayerVerification that = (PlayerVerification) o;
        return playerId == that.playerId &&
               profileVerified == that.profileVerified &&
               emailVerified == that.emailVerified &&
               scanVerified == that.scanVerified &&
               adminId == that.adminId &&
               Objects.equals(emailCode, that.emailCode) &&
               Objects.equals(passport, that.passport) &&
               status == that.status &&
               Objects.equals(commentary, that.commentary) &&
               Objects.equals(verificationDate, that.verificationDate);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(playerId, emailCode, profileVerified, emailVerified, scanVerified, passport, status, adminId, commentary, verificationDate);
    }

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "PlayerVerification{" + "playerId=" + playerId +
               ", emailCode='" + emailCode + '\'' +
               ", profileVerified=" + profileVerified +
               ", emailVerified=" + emailVerified +
               ", scanVerified=" + scanVerified +
               ", passport='" + passport + '\'' +
               ", status=" + status +
               ", adminId=" + adminId +
               ", commentary='" + commentary + '\'' +
               ", verificationDate=" + verificationDate +
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
    public PlayerVerification clone() throws CloneNotSupportedException {
        return (PlayerVerification) super.clone();
    }
}