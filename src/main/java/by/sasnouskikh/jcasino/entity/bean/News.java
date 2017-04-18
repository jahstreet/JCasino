package by.sasnouskikh.jcasino.entity.bean;

import by.sasnouskikh.jcasino.entity.Entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * The class represents info about application news.
 *
 * @author Sasnouskikh Aliaksandr
 * @see Entity
 */
public class News extends Entity {
    /**
     * News unique id.
     */
    private int       id;
    /**
     * Admin id who made latest change to this news.
     */
    private int       adminId;
    /**
     * Latest news change date.
     */
    private LocalDate date;
    /**
     * News header.
     */
    private String    header;
    /**
     * News text.
     */
    private String    text;

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
     * @param id news unique id
     */
    public void setId(int id) {
        this.id = id;
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
     * @param adminId admin id who made latest change to this news
     */
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    /**
     * {@link #date} getter.
     *
     * @return {@link #date}
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * {@link #date} setter.
     *
     * @param date latest news change date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * {@link #header} getter.
     *
     * @return {@link #header}
     */
    public String getHeader() {
        return header;
    }

    /**
     * {@link #header} setter.
     *
     * @param header news header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * {@link #text} getter.
     *
     * @return {@link #text}
     */
    public String getText() {
        return text;
    }

    /**
     * {@link #text} setter.
     *
     * @param text news text
     */
    public void setText(String text) {
        this.text = text;
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
        if (!(o instanceof News)) {
            return false;
        }
        News news = (News) o;
        return id == news.id &&
               adminId == news.adminId &&
               Objects.equals(date, news.date) &&
               Objects.equals(header, news.header) &&
               Objects.equals(text, news.text);
    }

    /**
     * Counts this instance hash code.
     *
     * @return counted hash code
     * @see Objects#hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, adminId, date, header, text);
    }

    @Override
    public String toString() {
        return "News{" + "id=" + id +
               ", adminId=" + adminId +
               ", date=" + date +
               ", header='" + header + '\'' +
               ", text='" + text + '\'' +
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
    protected News clone() throws CloneNotSupportedException {
        return (News) super.clone();
    }
}