package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.LoanDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Loan;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(LoanService.class)
public class LoanServicePrivateStaticCallTest {

    private static final String     ANY_PATTERN = "ANY_PATTERN";
    private static final List<Loan> LOAN_LIST   = new ArrayList<>();

    @Mock
    private LoanDAO     loanDAO;
    @Mock
    private DAOHelper   daoHelper;
    @InjectMocks
    private LoanService loanService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(daoHelper.getLoanDAO()).thenReturn(loanDAO);
        PowerMockito.spy(LoanService.class);
        when(loanDAO.takeLoanList(anyString(), anyString())).thenReturn(LOAN_LIST);
    }

    @Test
    public void takeLoanListFilterNotPaidCallCheck() throws Exception {
        loanService.takeLoanList(ANY_PATTERN, ANY_PATTERN, false, true, false);
        PowerMockito.doNothing().when(LoanService.class, "filterNotPaid", any(List.class));
        PowerMockito.verifyPrivate(LoanService.class).invoke("filterNotPaid", any(List.class));
    }

    @Test
    public void takeLoanListSortByRestCallCheck() throws Exception {
        loanService.takeLoanList(ANY_PATTERN, ANY_PATTERN, true, false, false);
        PowerMockito.doNothing().when(LoanService.class, "sortByRest", any(List.class), anyBoolean());
        PowerMockito.verifyPrivate(LoanService.class).invoke("sortByRest", any(List.class), anyBoolean());
    }

    @Test
    public void takeLoanListFilterOverduedCallCheck() throws Exception {
        loanService.takeLoanList(ANY_PATTERN, ANY_PATTERN, false, false, true);
        PowerMockito.doNothing().when(LoanService.class, "filterOverdued", any(List.class));
        PowerMockito.verifyPrivate(LoanService.class).invoke("filterOverdued", any(List.class));
    }
}