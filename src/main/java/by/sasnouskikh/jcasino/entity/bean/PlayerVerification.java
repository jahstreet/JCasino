package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.time.LocalDateTime;

public class PlayerVerification extends Entity {
    private int                playerId;
    private String             emailCode;
    private boolean            profileVerified;
    private boolean            emailVerified;
    private boolean            scanVerified;
    private String             passport;
    private VerificationStatus status;
    private int                adminId;
    private String             commentary;
    private LocalDateTime      verificationDate;

    public enum VerificationStatus {
        VERIFIED, NOT_VERIFIED
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public boolean isProfileVerified() {
        return profileVerified;
    }

    public void setProfileVerified(boolean profileVerified) {
        this.profileVerified = profileVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isScanVerified() {
        return scanVerified;
    }

    public void setScanVerified(boolean scanVerified) {
        this.scanVerified = scanVerified;
    }

    public VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public LocalDateTime getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(LocalDateTime verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}