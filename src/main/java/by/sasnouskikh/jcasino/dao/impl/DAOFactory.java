package by.sasnouskikh.jcasino.dao.impl;

public class DAOFactory {

    private DAOFactory() {
    }

    public static PlayerDAOImpl getPlayerDAO() {
        return new PlayerDAOImpl();
    }

    public static UserDAOImpl getUserDAO() {
        return new UserDAOImpl();
    }

    public static AdminDAOImpl getAdminDAO() {
        return new AdminDAOImpl();
    }

    public static NewsDAOImpl getNewsDAO() {
        return new NewsDAOImpl();
    }
}
