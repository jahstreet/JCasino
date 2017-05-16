package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.PlayerVerification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {

    @Mock
    private PlayerDAO playerDAO;
    @Mock
    private DAOHelper daoHelper;
    @InjectMocks
    private AdminService adminService;

    @Before
    public void setUp() {
        when(daoHelper.getPlayerDAO()).thenReturn(playerDAO);
    }

    @Test
    public void takeReadyForVerificationPlayerDAOCallCheck() throws DAOException {
        when(playerDAO.takeReadyForVerification()).thenReturn(null);
        adminService.takeReadyForVerification();

        verify(playerDAO).takeReadyForVerification();
    }

    @Test
    public void takeReadyForVerificationReturnPlayerDAOResultNullCheck() throws DAOException {
        when(playerDAO.takeReadyForVerification()).thenReturn(null);

        Assert.assertNull(adminService.takeReadyForVerification());
    }

    @Test
    public void takeReadyForVerificationReturnPlayerDAOResultNotNullCheck() throws DAOException {
        List<PlayerVerification> result = new ArrayList<>();
        when(playerDAO.takeReadyForVerification()).thenReturn(result);

        Assert.assertEquals(result, adminService.takeReadyForVerification());
    }

    @Test
    public void takeReadyForVerificationReturnDAOExceptionThrownCheck() throws DAOException {
        when(playerDAO.takeReadyForVerification()).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(adminService.takeReadyForVerification());
    }

    @Test
    public void changeAccountStatusPlayerDAOCallCheck() throws DAOException {
        int   playerId = 12;
        int   adminId  = 8;
        Admin admin    = new Admin();
        admin.setId(adminId);
        String status     = "anyStatus";
        String commentary = "anyCommentary";

        when(playerDAO.changeAccountStatus(anyInt(), anyInt(), anyString(), anyString())).thenReturn(true);
        adminService.changeAccountStatus(playerId, admin, status, commentary);

        verify(playerDAO).changeAccountStatus(anyInt(), anyInt(), anyString(), anyString());
    }

    @Test
    public void changeAccountStatusReturnPlayerDAOResultTrueCheck() throws DAOException {
        int   playerId = 12;
        int   adminId  = 8;
        Admin admin    = new Admin();
        admin.setId(adminId);
        String status     = "anyStatus";
        String commentary = "anyCommentary";

        when(playerDAO.changeAccountStatus(anyInt(), anyInt(), anyString(), anyString())).thenReturn(true);

        Assert.assertTrue(adminService.changeAccountStatus(playerId, admin, status, commentary));
    }

    @Test
    public void changeAccountStatusReturnPlayerDAOResultFalseCheck() throws DAOException {
        int   playerId = 12;
        int   adminId  = 8;
        Admin admin    = new Admin();
        admin.setId(adminId);
        String status     = "anyStatus";
        String commentary = "anyCommentary";

        when(playerDAO.changeAccountStatus(anyInt(), anyInt(), anyString(), anyString())).thenReturn(false);

        Assert.assertFalse(adminService.changeAccountStatus(playerId, admin, status, commentary));
    }

    @Test
    public void changeAccountStatusReturnDAOExceptionThrownCheck() throws DAOException {
        int   playerId = 12;
        int   adminId  = 8;
        Admin admin    = new Admin();
        admin.setId(adminId);
        String status     = "anyStatus";
        String commentary = "anyCommentary";

        when(playerDAO.changeAccountStatus(anyInt(), anyInt(), anyString(), anyString()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(adminService.changeAccountStatus(playerId, admin, status, commentary));
    }

    @Test
    public void changeAccountStatusModifyStatusCheck() throws DAOException {
        int   playerId = 12;
        int   adminId  = 8;
        Admin admin    = new Admin();
        admin.setId(adminId);
        String status     = "StAsUs";
        String commentary = "anyCommentary";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        adminService.changeAccountStatus(playerId, admin, status, commentary);
        verify(playerDAO).changeAccountStatus(anyInt(), anyInt(), captor.capture(), anyString());

        Assert.assertEquals(status.toLowerCase(), captor.getValue());
    }

    @Test
    public void changeAccountStatusTakeAdminIdCheck() throws DAOException {
        int   playerId = 12;
        int   adminId  = 8;
        Admin admin    = new Admin();
        admin.setId(adminId);
        String status     = "anyStatus";
        String commentary = "anyCommentary";

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        adminService.changeAccountStatus(playerId, admin, status, commentary);
        verify(playerDAO).changeAccountStatus(anyInt(), captor.capture(), anyString(), anyString());

        Assert.assertEquals(adminId, captor.getValue().intValue());
    }

    @Test
    public void changeAccountStatusNotModifyCommentaryCheck() throws DAOException {
        int   playerId = 12;
        int   adminId  = 8;
        Admin admin    = new Admin();
        admin.setId(adminId);
        String status     = "StAsUs";
        String commentary = "anyCommentary";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        adminService.changeAccountStatus(playerId, admin, status, commentary);
        verify(playerDAO).changeAccountStatus(anyInt(), anyInt(), anyString(), captor.capture());

        Assert.assertEquals(commentary, captor.getValue());
    }
}