package by.sasnouskikh.jcasino.command;

import by.sasnouskikh.jcasino.command.impl.navigation.GotoIndexCommand;
import by.sasnouskikh.jcasino.manager.QueryManager;
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

import javax.servlet.http.HttpServletRequest;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(QueryManager.class)
public class GotoIndexCommandTest {

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(QueryManager.class);
    }

    @Test
    public void executeReturnCheck() {
        GotoIndexCommand command = new GotoIndexCommand();
        Assert.assertEquals(PageNavigator.FORWARD_PAGE_INDEX, command.execute(request));
    }
}