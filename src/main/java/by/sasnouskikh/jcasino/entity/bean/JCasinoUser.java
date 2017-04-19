package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * The class represents info about application user.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class JCasinoUser extends Entity {

    /**
     * User unique id.
     */
    private int       id;
    /**
     * User password encrypted with MD5 encryptor to login the system.
     */
    private String    password;
    /**
     * User e-mail used to login the system and other features.
     */
    private String    email;
    /**
     * User role.
     */
    private UserRole  role;
    /**
     * User registration date.
     */
    private LocalDate registrationDate;

    /**
     * Enumeration of available user roles.
     */
    public enum UserRole {
        /**
         * Player role instance.
         */
        PLAYER("player"),
        /**
         * Admin role instance.
         */
        ADMIN("admin"),
        /**
         * Guest role instance.
         */
        GUEST("guest");

        /**
         * String representation of user role instance.
         */
        private String role;

        /**
         * User role instance constructor.
         *
         * @param role string representation of user role instance
         */
        UserRole(String role) {
            this.role = role;
        }

        /**
         * Getter of string representation of user role instance.
         *
         * @return string representation of user role instance
         */
        public String getRole() {
            return role;
        }
    }

    /**
     * Basic instance constructor.
     */
    public JCasinoUser() {
    }

    /**
     * Instance constructor.
     *
     * @param password user password encrypted by MD5 encryptor
     * @param email    user e-mail
     */
    public JCasinoUser(String password, String email) {
        this.password = password;
        this.email = email;
    }

    /**
     * {@link #id} getter.
     *
     * @return {@link #id} value
     */
    public int getId() {
        return id;
    }

    /**
     * {@link #id} setter.
     *
     * @param id user id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * {@link #password} getter.
     *
     * @return {@link #password} value
     */
    public String getPassword() {
        return password;
    }

    /**
     * {@link #password} setter.
     *
     * @param password user password encrypted by MD5 encryptor
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@link #email} getter.
     *
     * @return {@link #email} value
     */
    public String getEmail() {
        return email;
    }

    /**
     * {@link #email} setter.
     *
     * @param email user e-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * {@link #role} getter.
     *
     * @return {@link #role}
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * {@link #role} setter.
     *
     * @param role user role
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * {@link #registrationDate} getter.
     *
     * @return {@link #registrationDate}
     */
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    /**
     * {@link #registrationDate} setter.
     *
     * @param registrationDate registration date
     */
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Checks if this instance equals given object.
     *
     * @param o object to compare with
     * @return true if this instance equals given object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JCasinoUser)) {
            return false;
        }
        JCasinoUser user = (JCasinoUser) o;
        return id == user.id &&
               Objects.equals(password, user.password) &&
               Objects.equals(email, user.email) &&
               role == user.role &&
               Objects.equals(registrationDate, user.registrationDate);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, password, email, role, registrationDate);
    }

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "JCasinoUser{" + "id=" + id +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", role=" + role +
               ", registrationDate=" + registrationDate +
               '}';
    }

    /**
     * Clones instance of this object.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not support the {@code Cloneable} interface.
     *                                    Subclasses that override the {@code clone} method can also throw this
     *                                    exception to indicate that an instance cannot be cloned.
     * @see Cloneable
     */
    @Override
    protected JCasinoUser clone() throws CloneNotSupportedException {
        return (JCasinoUser) super.clone();
    }
}
