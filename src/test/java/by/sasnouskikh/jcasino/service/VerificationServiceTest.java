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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.SQLException;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class VerificationServiceTest {

    private static final Admin              ADMIN                 = new Admin();
    private static final PlayerVerification VERIFICATION          = new PlayerVerification();
    private static final PlayerVerification VERIFICATION_VERIFIED = new PlayerVerification();
    private static final int                PLAYER_ID             = 18;
    private static final int                ADMIN_ID              = 8;
    private static final byte               VER_MASK              = 0b011;
    private static final String             COMMENTARY            = "anyCommentary";

    static {
        VERIFICATION.setPlayerId(PLAYER_ID);
        VERIFICATION.setStatus(PlayerVerification.VerificationStatus.NOT_VERIFIED);

        VERIFICATION_VERIFIED.setPlayerId(PLAYER_ID);
        VERIFICATION_VERIFIED.setEmailVerified(true);
        VERIFICATION_VERIFIED.setProfileVerified(true);
        VERIFICATION_VERIFIED.setScanVerified(true);
        VERIFICATION_VERIFIED.setStatus(PlayerVerification.VerificationStatus.VERIFIED);

        ADMIN.setId(ADMIN_ID);
    }

    @Mock
    private PlayerDAO           playerDAO;
    @Mock
    private DAOHelper           daoHelper;
    @InjectMocks
    private VerificationService verificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(daoHelper.getPlayerDAO()).thenReturn(playerDAO);
    }

    @Test
    public void changePlayerVerStatusPlayerDAOTakeCallCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        verificationService.changePlayerVerStatus(PLAYER_ID, VER_MASK,
                                                  VerificationService.MaskOperation.AND, ADMIN, COMMENTARY);

        verify(playerDAO).takeVerification(PLAYER_ID);
    }

    @Test
    public void changePlayerVerStatusOrPlayerDAOChangeCallCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        verificationService.changePlayerVerStatus(PLAYER_ID, VER_MASK,
                                                  VerificationService.MaskOperation.OR, ADMIN, COMMENTARY);

        verify(playerDAO).changeVerificationStatus(PLAYER_ID, VER_MASK, ADMIN_ID, COMMENTARY);
    }

    @Test
    public void changePlayerVerStatusAndPlayerDAOChangeCallCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        verificationService.changePlayerVerStatus(PLAYER_ID, VER_MASK,
                                                  VerificationService.MaskOperation.AND, ADMIN, COMMENTARY);

        verify(playerDAO).changeVerificationStatus(PLAYER_ID, VER_MASK, ADMIN_ID, COMMENTARY);
    }

    @Test
    public void changePlayerVerStatusReturnPlayerDAOChangeResultTrueCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);

        Assert.assertTrue(verificationService.changePlayerVerStatus(PLAYER_ID, VER_MASK,
                                                                    VerificationService.MaskOperation.AND,
                                                                    ADMIN, COMMENTARY));
    }

    @Test
    public void changePlayerVerStatusReturnPlayerDAOChangeResultFalseCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(false);

        Assert.assertFalse(verificationService.changePlayerVerStatus(PLAYER_ID, VER_MASK,
                                                                     VerificationService.MaskOperation.AND,
                                                                     ADMIN, COMMENTARY));
    }

    @Test
    public void changePlayerVerStatusDAOExceptionThrownOnTakeReturnFalseCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);

        Assert.assertFalse(verificationService.changePlayerVerStatus(PLAYER_ID, VER_MASK,
                                                                     VerificationService.MaskOperation.AND,
                                                                     ADMIN, COMMENTARY));
    }

    @Test
    public void changePlayerVerStatusDAOExceptionThrownOnChangeReturnFalseCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(verificationService.changePlayerVerStatus(PLAYER_ID, VER_MASK,
                                                                     VerificationService.MaskOperation.AND,
                                                                     ADMIN, COMMENTARY));
    }

    @Test
    public void cancelScanVerificationPlayerDAOTakeCallCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(true);
        verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY);

        verify(playerDAO).takeVerification(PLAYER_ID);
    }

    @Test
    public void cancelScanVerificationPlayerDAOChangeCallCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(true);
        verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY);

        verify(playerDAO).changeVerificationStatus(PLAYER_ID, VER_MASK, ADMIN_ID, COMMENTARY);
    }

    @Test
    public void cancelScanVerificationPlayerDAOChangeScanPathCallCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(true);
        verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY);

        verify(playerDAO).changeScanPath(eq(PLAYER_ID), isNull(String.class));
    }

    @Test
    public void cancelScanVerificationReturnPlayerDAOResultTrueCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(true);

        Assert.assertTrue(verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY));
    }

    @Test
    public void cancelScanVerificationReturnPlayerDAOChangeResultFalseCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(false);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(true);

        Assert.assertFalse(verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY));
    }

    @Test
    public void cancelScanVerificationReturnPlayerDAOChangeScanPathResultFalseCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(false);

        Assert.assertFalse(verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY));
    }

    @Test
    public void cancelScanVerificationDAOExceptionThrownOnTakeReturnFalseCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(true);

        Assert.assertFalse(verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY));
    }

    @Test
    public void cancelScanVerificationDAOExceptionThrownOnChangeReturnFalseCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString()))
        .thenThrow(new DAOException("Database connection error."));
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(true);

        Assert.assertFalse(verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY));
    }

    @Test
    public void cancelScanVerificationDAOExceptionThrownOnChangeScanPathReturnFalseCheck() throws DAOException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class)))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY));
    }

    @Test
    public void cancelScanVerificationSQLExceptionThrownOnDAOHelperCommitReturnFalseCheck() throws DAOException, SQLException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(true);
        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();

        Assert.assertFalse(verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY));
    }

    @Test
    public void cancelScanVerificationSQLExceptionThrownOnDAOHelperBeginTransactionReturnFalseCheck() throws DAOException, SQLException {
        when(playerDAO.takeVerification(anyInt())).thenReturn(VERIFICATION_VERIFIED);
        when(playerDAO.changeVerificationStatus(anyInt(), anyByte(), anyInt(), anyString())).thenReturn(true);
        when(playerDAO.changeScanPath(anyInt(), isNull(String.class))).thenReturn(true);
        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();

        Assert.assertFalse(verificationService.cancelScanVerification(PLAYER_ID, ADMIN, COMMENTARY));
    }


}