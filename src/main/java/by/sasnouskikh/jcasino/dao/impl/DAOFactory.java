package by.sasnouskikh.jcasino.dao.impl;

import by.sasnouskikh.jcasino.db.WrappedConnection;

public class DAOFactory {

    private DAOFactory() {
    }

    public static PlayerDAOImpl getPlayerDAO() {
        return new PlayerDAOImpl();
    }

    public static PlayerDAOImpl getPlayerDAO(WrappedConnection connection) {
        return new PlayerDAOImpl(connection);
    }

    public static UserDAOImpl getUserDAO() {
        return new UserDAOImpl();
    }

    public static UserDAOImpl getUserDAO(WrappedConnection connection) {
        return new UserDAOImpl(connection);
    }

    public static AdminDAOImpl getAdminDAO() {
        return new AdminDAOImpl();
    }

    public static AdminDAOImpl getAdminDAO(WrappedConnection connection) {
        return new AdminDAOImpl(connection);
    }

    public static NewsDAOImpl getNewsDAO() {
        return new NewsDAOImpl();
    }

    public static NewsDAOImpl getNewsDAO(WrappedConnection connection) {
        return new NewsDAOImpl(connection);
    }

    public static TransactionDAOImpl getTransactionDAO() {
        return new TransactionDAOImpl();
    }

    public static TransactionDAOImpl getTransactionDAO(WrappedConnection connection) {
        return new TransactionDAOImpl(connection);
    }

    public static StreakDAOImpl getStreakDAO() {
        return new StreakDAOImpl();
    }

    public static StreakDAOImpl getStreakDAO(WrappedConnection connection) {
        return new StreakDAOImpl(connection);
    }

    public static LoanDAOImpl getLoanDAO() {
        return new LoanDAOImpl();
    }

    public static LoanDAOImpl getLoanDAO(WrappedConnection connection) {
        return new LoanDAOImpl(connection);
    }

    public static QuestionDAOImpl getQuestionDAO() {
        return new QuestionDAOImpl();
    }

    public static QuestionDAOImpl getQuestionDAO(WrappedConnection connection) {
        return new QuestionDAOImpl(connection);
    }
}
