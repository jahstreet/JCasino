package by.sasnouskikh.jcasino.entity;

import java.io.Serializable;

/**
 * The class provides root abstraction for bean classes.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Serializable
 * @see Cloneable
 */
public abstract class Entity implements Serializable, Cloneable {

    /**
     * Builds {@link String} representation of this instance.
     *
     * @return built {@link String} representation of this instance
     */
    @Override
    public String toString() {
        return "Entity{" + '}';
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
    protected Entity clone() throws CloneNotSupportedException {
        return (Entity) super.clone();
    }
}