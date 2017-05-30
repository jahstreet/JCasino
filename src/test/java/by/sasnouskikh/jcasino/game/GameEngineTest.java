package by.sasnouskikh.jcasino.game;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Streak;
import by.sasnouskikh.jcasino.entity.bean.Transaction;
import by.sasnouskikh.jcasino.service.ServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.ArrayDeque;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({GameEngine.class, DAOHelper.class})
public class GameEngineTest {

    private static final int        PLAYER_ID    = 18;
    private static final String     ROLL         = "17-29-48_37-7-8_1-55-57_28-55-49_32-32-47_" +
                                                   "58-46-2_4-22-4_39-11-10_43-44-50_43-18-13";
    private static final int[]      OFFSET_ARRAY = {29, 2, 25};
    private static final boolean[]  LINES_ARRAY  = {true, true, false, false, false};
    private static final BigDecimal BET          = BigDecimal.TEN;
    private static final BigDecimal BET_RESULT   = BigDecimal.valueOf(-20);

    @Mock
    private DAOHelper daoHelper;
    @Mock
    private PlayerDAO playerDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mock(DAOHelper.class);
        PowerMockito.whenNew(DAOHelper.class).withNoArguments().thenReturn(daoHelper);
        when(daoHelper.getPlayerDAO()).thenReturn(playerDAO);

        PowerMockito.spy(GameEngine.class);
    }

    @Test
    public void spinPlayerDAOChangeBalanceCallCheck() throws DAOException, ServiceException {
        final Streak streak = new Streak();
        streak.setPlayerId(PLAYER_ID);
        streak.setRoll(ROLL);
        streak.setRolls(new ArrayDeque<>());

        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);
        GameEngine.spin(streak, OFFSET_ARRAY, LINES_ARRAY, BET);

        verify(playerDAO).changeBalance(PLAYER_ID, BET_RESULT, Transaction.TransactionType.REPLENISH);
    }

    @Test(expected = ServiceException.class)
    public void spinServiceExceptionThrownCheck() throws ServiceException, DAOException {
        final Streak streak = new Streak();
        streak.setPlayerId(PLAYER_ID);
        streak.setRoll(ROLL);
        streak.setRolls(new ArrayDeque<>());

        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(false);
        GameEngine.spin(streak, OFFSET_ARRAY, LINES_ARRAY, BET);

        Assert.fail("ServiceException expected to be thrown");
    }

    @Test(expected = ServiceException.class)
    public void spinDAOExceptionThrownOnPlayerDAOChangeBalanceCheck() throws ServiceException, DAOException {
        final Streak streak = new Streak();
        streak.setPlayerId(PLAYER_ID);
        streak.setRoll(ROLL);
        streak.setRolls(new ArrayDeque<>());

        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenThrow(new DAOException("Database connection error."));
        GameEngine.spin(streak, OFFSET_ARRAY, LINES_ARRAY, BET);

        Assert.fail("ServiceException expected to be thrown");
    }

    @Test
    public void spinReturnCheck() throws ServiceException, DAOException {
        final Streak streak = new Streak();
        streak.setPlayerId(PLAYER_ID);
        streak.setRoll(ROLL);
        streak.setRolls(new ArrayDeque<>());

        when(playerDAO.changeBalance(anyInt(), any(BigDecimal.class), any(Transaction.TransactionType.class)))
        .thenReturn(true);

        Assert.assertEquals(BET_RESULT.doubleValue(),
                            GameEngine.spin(streak, OFFSET_ARRAY, LINES_ARRAY, BET).doubleValue(),
                            1e-4);
    }

    @Test
    public void spinDemoReturnCheck() {
        final Streak streak = new Streak();
        streak.setRoll(ROLL);
        streak.setRolls(new ArrayDeque<>());

        Assert.assertEquals(BET_RESULT.doubleValue(),
                            GameEngine.spinDemo(streak, OFFSET_ARRAY, LINES_ARRAY, BET).doubleValue(),
                            1e-4);
    }
}