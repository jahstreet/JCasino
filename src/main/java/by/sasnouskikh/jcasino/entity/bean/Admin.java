package by.sasnouskikh.jcasino.entity.bean;

/**
 * The class represents info about application admin.
 *
 * @author Sasnouskikh Aliaksandr
 * @see JCasinoUser
 */
public class Admin extends JCasinoUser {

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "Admin{" + super.toString() + '}';
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
    protected Admin clone() throws CloneNotSupportedException {
        return (Admin) super.clone();
    }


}