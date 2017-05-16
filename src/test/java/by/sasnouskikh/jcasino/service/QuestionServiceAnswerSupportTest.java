package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.PlayerDAO;
import by.sasnouskikh.jcasino.dao.QuestionDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.PlayerProfile;
import by.sasnouskikh.jcasino.entity.bean.Question;
import by.sasnouskikh.jcasino.mailer.MailerException;
import by.sasnouskikh.jcasino.mailer.MailerSSL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.SQLException;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(MailerSSL.class)
public class QuestionServiceAnswerSupportTest {

    private static final Question      QUESTION       = new Question();
    private static final Admin         ADMIN          = new Admin();
    private static final PlayerProfile PLAYER_PROFILE = new PlayerProfile();
    private static final int           QUESTION_ID    = 8;
    private static final int           ADMIN_ID       = 7;
    private static final String        ANY_EMAIL      = "anyEmail";
    private static final String        ANY_LOCALE     = "anyLocale";
    private static final String        ANY_ANSWER     = "anyAnswer";
    private static final String        ANY_NAME       = "anyName";

    static {
        QUESTION.setId(QUESTION_ID);
        QUESTION.setEmail(ANY_EMAIL);
        QUESTION.setPlayerId(12);
        ADMIN.setId(ADMIN_ID);
        PLAYER_PROFILE.setfName(ANY_NAME);
    }

    @Mock
    private QuestionDAO     questionDAO;
    @Mock
    private PlayerDAO       playerDAO;
    @Mock
    private DAOHelper       daoHelper;
    @InjectMocks
    private QuestionService questionService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(daoHelper.getQuestionDAO()).thenReturn(questionDAO);
        when(daoHelper.getPlayerDAO()).thenReturn(playerDAO);
        when(questionDAO.answerQuestion(anyInt(), anyString(), anyInt())).thenReturn(true);
        when(playerDAO.takeProfile(anyInt())).thenReturn(PLAYER_PROFILE);
        PowerMockito.mockStatic(MailerSSL.class);
        when(MailerSSL.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);
    }

    @Test
    public void answerSupportQuestionNullReturnFalseCheck() throws Exception {
        Assert.assertFalse(questionService.answerSupport(null, ANY_ANSWER, ADMIN, ANY_LOCALE));
    }

    @Test
    public void answerSupportAnswerNullReturnFalseCheck() throws Exception {
        Assert.assertFalse(questionService.answerSupport(QUESTION, null, ADMIN, ANY_LOCALE));
    }

    @Test
    public void answerSupportAdminNullReturnFalseCheck() throws Exception {
        Assert.assertFalse(questionService.answerSupport(QUESTION, ANY_ANSWER, null, ANY_LOCALE));
    }

    @Test
    public void answerSupportQuestionDAOAnswerCallCheck() throws DAOException {
        questionService.answerSupport(QUESTION, ANY_ANSWER, ADMIN, ANY_LOCALE);

        verify(questionDAO).answerQuestion(QUESTION_ID, ANY_ANSWER, ADMIN_ID);
    }

    @Test
    public void answerSupportMailerSSLSendEmailCallCheck() throws DAOException, MailerException {
        String subject = "PLAYER";

        PlayerProfile playerProfile = new PlayerProfile();
        when(playerDAO.takeProfile(anyInt())).thenReturn(playerProfile);

        questionService.answerSupport(QUESTION, ANY_ANSWER, ADMIN, ANY_LOCALE);

        PowerMockito.verifyStatic();
        MailerSSL.sendEmail(eq(subject), anyString(), eq(ANY_EMAIL));
    }

    @Test
    public void answerSupportReturnTrueCheck() {
        Assert.assertTrue(questionService.answerSupport(QUESTION, ANY_ANSWER, ADMIN, ANY_LOCALE));
    }

    @Test
    public void answerSupportDAOExceptionThrownOnQuestionDAOAnswerReturnFalseCheck() throws DAOException {
        when(questionDAO.answerQuestion(anyInt(), anyString(), anyInt()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(questionService.answerSupport(QUESTION, ANY_ANSWER, ADMIN, ANY_LOCALE));
    }

    @Test
    public void answerSupportDAOExceptionThrownOnPlayerDAODAOTakeProfileReturnFalseCheck() throws DAOException {
        when(playerDAO.takeProfile(anyInt())).thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(questionService.answerSupport(QUESTION, ANY_ANSWER, ADMIN, ANY_LOCALE));
    }

    @Test
    public void answerSupportMailerExceptionThrownOnMailerSSLSendEmailReturnFalseCheck() throws MailerException {
        when(MailerSSL.sendEmail(anyString(), anyString(), anyString()))
        .thenThrow(new MailerException("E-mail sending error."));

        Assert.assertFalse(questionService.answerSupport(QUESTION, ANY_ANSWER, ADMIN, ANY_LOCALE));
    }

    @Test
    public void answerSupportSQLExceptionThrownOnDAOHelperCommitReturnFalseCheck() throws SQLException {
        doThrow(new SQLException("Database connection error.")).when(daoHelper).commit();

        Assert.assertFalse(questionService.answerSupport(QUESTION, ANY_ANSWER, ADMIN, ANY_LOCALE));
    }

    @Test
    public void answerSupportSQLExceptionThrownOnDAOHelperBeginTransactionReturnFalseCheck() throws SQLException {
        doThrow(new SQLException("Database connection error.")).when(daoHelper).beginTransaction();

        Assert.assertFalse(questionService.answerSupport(QUESTION, ANY_ANSWER, ADMIN, ANY_LOCALE));
    }

    @Test
    public void answerSupportDefineSubjectPlayerNameCheck() throws MailerException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        questionService.answerSupport(QUESTION, ANY_ANSWER, ADMIN, ANY_LOCALE);

        PowerMockito.verifyStatic();
        MailerSSL.sendEmail(captor.capture(), anyString(), anyString());

        Assert.assertEquals(ANY_NAME, captor.getValue());
    }

    @Test
    public void answerSupportDefineSubjectGuestCheck() throws MailerException {
        String   subject  = "GUEST";
        Question question = new Question();
        question.setEmail(ANY_EMAIL);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        questionService.answerSupport(question, ANY_ANSWER, ADMIN, ANY_LOCALE);

        PowerMockito.verifyStatic();
        MailerSSL.sendEmail(captor.capture(), anyString(), anyString());

        Assert.assertEquals(subject, captor.getValue());
    }


}