package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class UserServiceTest {

    private static final String      EMAIL           = "anY@email.by ";
    private static final String      PASSWORD        = "anyPa55w0rd";
    private static final String      PASSWORD_MD5    = "1d79d14d00506d66f0ec1ea9847776a0";
    private static final JCasinoUser ANY_ADMIN       = new JCasinoUser();
    private static final Admin       DEFINITE_ADMIN  = new Admin();
    private static final JCasinoUser ANY_PLAYER      = new JCasinoUser();
    private static final Player      DEFINITE_PLAYER = new Player();
    private static final JCasinoUser ANY_USER        = new JCasinoUser();
    private static final int         USER_ID         = 99;

    static {
        ANY_PLAYER.setRole(JCasinoUser.UserRole.PLAYER);
        ANY_ADMIN.setRole(JCasinoUser.UserRole.ADMIN);

        DEFINITE_ADMIN.setId(2);
        DEFINITE_PLAYER.setId(4);

        ANY_USER.setId(USER_ID);
    }

    @Mock
    private UserDAO     userDAO;
    @Mock
    private DAOHelper   daoHelper;
    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(daoHelper.getUserDAO()).thenReturn(userDAO);
    }

    @Test
    public void authorizeUserUserDAOTakeCallCheck() throws DAOException {
        when(userDAO.authorizeUser(anyString(), anyString())).thenReturn(null);
        userService.authorizeUser(EMAIL, PASSWORD);

        verify(userDAO).authorizeUser(EMAIL.toLowerCase().trim(), PASSWORD_MD5);
    }

    @Test
    public void authorizeUserReturnUserDAOTakeResultNullCheck() throws DAOException {
        when(userDAO.authorizeUser(anyString(), anyString())).thenReturn(null);

        Assert.assertNull(userService.authorizeUser(EMAIL, PASSWORD));
    }

    @Test
    public void authorizeUserDAOExceptionThrownReturnNullCheck() throws DAOException {
        when(userDAO.authorizeUser(anyString(), anyString())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(userService.authorizeUser(EMAIL, PASSWORD));
    }

    @Test
    @PrepareForTest(UserService.class)
    public void authorizeUserReturnUserDAOTakeResultPlayerCheck() throws Exception {
        when(userDAO.authorizeUser(anyString(), anyString())).thenReturn(ANY_PLAYER);
        UserService spy = PowerMockito.spy(new UserService(daoHelper));
        PowerMockito.when(spy, MemberMatcher.method(UserService.class, "initPlayer", JCasinoUser.class))
                    .withArguments(any(JCasinoUser.class)).thenReturn(DEFINITE_PLAYER);

        Assert.assertEquals(DEFINITE_PLAYER, spy.authorizeUser(EMAIL, PASSWORD));
    }

    @Test
    @PrepareForTest(UserService.class)
    public void authorizeUserReturnUserDAOTakeResultAdminCheck() throws Exception {
        when(userDAO.authorizeUser(anyString(), anyString())).thenReturn(ANY_ADMIN);
        UserService spy = PowerMockito.spy(new UserService(daoHelper));
        PowerMockito.when(spy, MemberMatcher.method(UserService.class, "initAdmin", JCasinoUser.class))
                    .withArguments(any(JCasinoUser.class)).thenReturn(DEFINITE_ADMIN);

        Assert.assertEquals(DEFINITE_ADMIN, spy.authorizeUser(EMAIL, PASSWORD));
    }

    @Test
    public void checkPasswordUserDAOCheckCallCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenReturn(true);
        userService.checkPassword(ANY_USER, PASSWORD);

        verify(userDAO).checkPassword(USER_ID, PASSWORD_MD5);
    }

    @Test
    public void checkPasswordReturnUserDAOCheckResultTrueCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenReturn(true);

        Assert.assertTrue(userService.checkPassword(ANY_USER, PASSWORD));
    }

    @Test
    public void checkPasswordReturnUserDAOCheckResultFalseCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenReturn(false);

        Assert.assertFalse(userService.checkPassword(ANY_USER, PASSWORD));
    }

    @Test
    public void checkPasswordDAOExceptionThrownReturnFalseCheck() throws DAOException {
        when(userDAO.checkPassword(anyInt(), anyString())).thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(userService.checkPassword(ANY_USER, PASSWORD));
    }

}