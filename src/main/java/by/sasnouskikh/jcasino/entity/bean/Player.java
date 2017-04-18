package by.sasnouskikh.jcasino.entity.bean;

import java.util.Objects;

/**
 * The class represents info about application player.
 *
 * @author Sasnouskikh Aliaksandr
 * @see JCasinoUser
 */
public class Player extends JCasinoUser {

    /**
     * Object which contains player profile data.
     */
    private PlayerProfile      profile;
    /**
     * Object which contains player account data.
     */
    private PlayerAccount      account;
    /**
     * Object which contains player stats data.
     */
    private PlayerStats        stats;
    /**
     * Object which contains player verification data.
     */
    private PlayerVerification verification;

    /**
     * {@link #profile} getter.
     *
     * @return {@link #profile}
     */
    public PlayerProfile getProfile() {
        return profile;
    }

    /**
     * {@link #profile} setter.
     *
     * @param profile {@link PlayerProfile} object
     */
    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
    }

    /**
     * {@link #account} getter.
     *
     * @return {@link #account}
     */
    public PlayerAccount getAccount() {
        return account;
    }

    /**
     * {@link #account} setter.
     *
     * @param account {@link PlayerAccount} object
     */
    public void setAccount(PlayerAccount account) {
        this.account = account;
    }

    /**
     * {@link #stats} getter.
     *
     * @return {@link PlayerStats} object
     */
    public PlayerStats getStats() {
        return stats;
    }

    /**
     * {@link #stats} setter.
     *
     * @param stats {@link PlayerStats} object
     */
    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }

    /**
     * {@link #verification} getter.
     *
     * @return {@link #verification}
     */
    public PlayerVerification getVerification() {
        return verification;
    }

    /**
     * {@link #verification} setter.
     *
     * @param verification {@link PlayerVerification} object
     */
    public void setVerification(PlayerVerification verification) {
        this.verification = verification;
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
        if (!(o instanceof Player)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Player player = (Player) o;
        return Objects.equals(profile, player.profile) &&
               Objects.equals(account, player.account) &&
               Objects.equals(stats, player.stats) &&
               Objects.equals(verification, player.verification);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), profile, account, stats, verification);
    }

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "Player{" + super.toString() +
               "profile=" + profile +
               ", account=" + account +
               ", stats=" + stats +
               ", verification=" + verification +
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
    protected Player clone() throws CloneNotSupportedException {
        Player clone = (Player) super.clone();
        clone.setProfile(profile.clone());
        clone.setAccount(account.clone());
        clone.setStats(stats.clone());
        clone.setVerification(verification.clone());
        return clone;
    }
}