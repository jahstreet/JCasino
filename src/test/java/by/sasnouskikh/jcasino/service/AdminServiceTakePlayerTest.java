package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.UserDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import by.sasnouskikh.jcasino.entity.bean.Player;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({AdminService.class})
public class AdminServiceTakePlayerTest {

    private static final JCasinoUser ANY_USER   = new JCasinoUser();
    private static final Player      ANY_PLAYER = new Player();

    @Mock
    private DAOHelper   daoHelper;
    @Mock
    private UserDAO     userDAO;
    @Mock
    private UserService userService;

    private AdminService adminService;

    @Before
    public void setUp() throws Exception {
        adminService = new AdminService(daoHelper);
        when(daoHelper.getUserDAO()).thenReturn(userDAO);
        when(userDAO.takeUser(anyInt())).thenReturn(ANY_USER);
        whenNew(UserService.class).withNoArguments().thenReturn(userService);
        when(userService.initPlayer(any(JCasinoUser.class))).thenReturn(ANY_PLAYER);
    }

    @After
    public void tearDown() {
        adminService = null;
    }

    @Test
    public void takePlayerReturnCheck() throws Exception {
        int playerId = 1;

        Assert.assertEquals(ANY_PLAYER, adminService.takePlayer(playerId));
    }

    @Test
    public void takePlayerUserDAOCallCheck() throws Exception {
        int playerId = 1;

        adminService.takePlayer(playerId);

        verify(userDAO).takeUser(anyInt());
    }

    @Test
    public void takePlayerUserServiceCreateCheck() throws Exception {
        int playerId = 1;

        adminService.takePlayer(playerId);

        verifyNew(UserService.class);
    }

    @Test
    public void takePlayerUserServiceCallCheck() throws Exception {
        int playerId = 1;

        adminService.takePlayer(playerId);

        verify(userService).initPlayer(any(JCasinoUser.class));
    }

    @Test
    public void takePlayerDAOExceptionThrownReturnCheck() throws Exception {
        int playerId = 1;

        reset(userService);
        when(userDAO.takeUser(anyInt())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(adminService.takePlayer(playerId));
    }
}