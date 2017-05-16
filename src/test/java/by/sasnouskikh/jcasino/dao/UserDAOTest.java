package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class UserDAOTest extends AbstractDAOTest {

    private static final String TABLE_USER    = "user";
    private static final String XML_USER_DATA = "by/sasnouskikh/jcasino/dao/transaction_data.xml";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        beforeData = buildDataSet(XML_USER_DATA);
        DatabaseOperation.CLEAN_INSERT.execute(connection, beforeData);
    }

    @Test
    public void checkEmailExistTrueCheck() throws DAOException {
        String email = "bover@json.com";

        boolean actual = daoHelper.getUserDAO().checkEmailExist(email);

        Assert.assertTrue(String.format("Method should return `true`, if given e-mail exists in database, " +
                                        "but it returns: %s", actual), actual);
    }

    @Test
    public void checkEmailExistFalseCheck() throws DAOException {
        String email = "no@such.email";

        boolean actual = daoHelper.getUserDAO().checkEmailExist(email);

        Assert.assertFalse(String.format("Method should return `false`, if given e-mail doesn't exist in database, " +
                                         "but it returns: %s", actual), actual);
    }

    @Test
    public void authorizeUserCheck() throws DAOException {
        int                  id               = 100;
        String               email            = "anhelina@gmail.com";
        String               password         = "c070202aef2d05077de4a4d16bf26875";
        JCasinoUser.UserRole role             = JCasinoUser.UserRole.PLAYER;
        LocalDate            registrationDate = LocalDate.now();

        JCasinoUser expected = new JCasinoUser();
        expected.setId(id);
        expected.setEmail(email);
        expected.setRegistrationDate(registrationDate);
        expected.setRole(role);

        JCasinoUser actual = daoHelper.getUserDAO().authorizeUser(email, password);

        Assert.assertEquals(String.format("\nExpected:\t%s\nActual:\t%s", expected, actual),
                            expected, actual);
    }

    @Test
    public void authorizeUserNoMatchCheck() throws DAOException {
        String email    = "no@such.email";
        String password = "anypassword";

        JCasinoUser actual = daoHelper.getUserDAO().authorizeUser(email, password);

        Assert.assertNull("Taken user object value expected to be null.", actual);
    }

    @Test
    public void takeUserByIdCheck() throws DAOException {
        int                  id               = 100;
        String               email            = "anhelina@gmail.com";
        JCasinoUser.UserRole role             = JCasinoUser.UserRole.PLAYER;
        LocalDate            registrationDate = LocalDate.now();

        JCasinoUser expected = new JCasinoUser();
        expected.setId(id);
        expected.setEmail(email);
        expected.setRegistrationDate(registrationDate);
        expected.setRole(role);

        JCasinoUser actual = daoHelper.getUserDAO().takeUser(id);

        Assert.assertEquals(String.format("\nExpected:\t%s\nActual:\t%s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeUserByIdNoSuchCheck() throws DAOException {
        int id = 103;

        JCasinoUser actual = daoHelper.getUserDAO().takeUser(id);

        Assert.assertNull("Taken user object value expected to be null.", actual);
    }

    @Test
    public void takeUserByEmailCheck() throws DAOException {
        int                  id               = 101;
        String               email            = "anhel@gmail.com";
        JCasinoUser.UserRole role             = JCasinoUser.UserRole.PLAYER;
        LocalDate            registrationDate = LocalDate.now();

        JCasinoUser expected = new JCasinoUser();
        expected.setId(id);
        expected.setEmail(email);
        expected.setRegistrationDate(registrationDate);
        expected.setRole(role);

        JCasinoUser actual = daoHelper.getUserDAO().takeUser(email);

        Assert.assertEquals(String.format("\nExpected:\t%s\nActual:\t%s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeUserByEmailNoSuchCheck() throws DAOException {
        String email = "no@such.email";

        JCasinoUser actual = daoHelper.getUserDAO().takeUser(email);

        Assert.assertNull("Taken user object value expected to be null.", actual);
    }

    @Test
    public void checkPasswordTrueCheck() throws DAOException {
        int    id       = 100;
        String password = "c070202aef2d05077de4a4d16bf26875";

        boolean actual = daoHelper.getUserDAO().checkPassword(id, password);

        Assert.assertTrue(String.format("Method should return `true`, if given password matches database value due to " +
                                        "given user id, but it returns: %s", actual), actual);
    }

    @Test
    public void checkPasswordFalseCheck() throws DAOException {
        int    id       = 100;
        String password = "invalidPassword123";

        boolean actual = daoHelper.getUserDAO().checkPassword(id, password);

        Assert.assertFalse(String.format("Method should return `false`, if given password doesn't match database value " +
                                         "due to given user id, but it returns: %s", actual), actual);
    }

    @Test
    public void checkPasswordNoSuchIdCheck() throws DAOException {
        int    id       = 103;
        String password = "anyPasswordValue00019";

        boolean actual = daoHelper.getUserDAO().checkPassword(id, password);

        Assert.assertFalse(String.format("Method should return `false`, if given there is no given user id in database, " +
                                         "but it returns: %s", actual), actual);
    }
}