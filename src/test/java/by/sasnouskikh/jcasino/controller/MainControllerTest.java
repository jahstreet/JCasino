package by.sasnouskikh.jcasino.controller;

import by.sasnouskikh.jcasino.command.Command;
import by.sasnouskikh.jcasino.command.CommandFactory;
import by.sasnouskikh.jcasino.command.PageNavigator;
import by.sasnouskikh.jcasino.entity.bean.JCasinoUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static by.sasnouskikh.jcasino.manager.ConfigConstant.ATTR_ROLE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({MainController.class, CommandFactory.class})
public class MainControllerTest {

    private static final PageNavigator NAVIGATOR = PageNavigator.FORWARD_PAGE_MAIN;

    @Mock
    private HttpServletRequest  request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession         session;
    @Mock
    private Command             command;
    @Mock
    private RequestDispatcher   dispatcher;
    @Mock
    private ServletContext      context;

    private MainController controller = PowerMockito.mock(MainController.class);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(MainController.class);
        PowerMockito.mockStatic(CommandFactory.class);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void processRequestCheck() throws Exception {
        when(session.getAttribute(ATTR_ROLE)).thenReturn(JCasinoUser.UserRole.PLAYER);
        PowerMockito.when(CommandFactory.defineCommand(any(HttpServletRequest.class))).thenReturn(command);
        when(command.execute(any(HttpServletRequest.class))).thenReturn(NAVIGATOR);
        PowerMockito.when(controller.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(any(ServletRequest.class), any(ServletResponse.class));

        PowerMockito.when(controller, "processRequest", request, response).thenCallRealMethod();
        Whitebox.invokeMethod(controller, "processRequest", request, response);

        verify(dispatcher).forward(request, response);
    }
}