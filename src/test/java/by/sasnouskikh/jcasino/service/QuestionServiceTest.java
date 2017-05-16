package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.DAOException;
import by.sasnouskikh.jcasino.dao.QuestionDAO;
import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.entity.bean.Admin;
import by.sasnouskikh.jcasino.entity.bean.Player;
import by.sasnouskikh.jcasino.entity.bean.Question;
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
import org.powermock.reflect.Whitebox;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class QuestionServiceTest {

    private static final int            questionId   = 8;
    private static final int            playerId     = 12;
    private static final int            adminId      = 7;
    private static final String         email        = "anyEmail ";
    private static final String         satisfaction = "anySatisfaction";

    private final        Question       question     = new Question();
    private final        List<Question> questionList = new ArrayList<>();
    private final        Admin          admin        = new Admin();
    private final        Player         player       = new Player();

    @Mock
    private QuestionDAO     questionDAO;
    @Mock
    private DAOHelper       daoHelper;
    @InjectMocks
    private QuestionService questionService;

    {
        questionList.add(question);
        question.setId(questionId);
        question.setPlayerId(playerId);
        admin.setId(adminId);
        player.setId(playerId);
        player.setEmail(email.trim());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(daoHelper.getQuestionDAO()).thenReturn(questionDAO);
    }

    @Test
    public void takeQuestionQuestionDAOTakeCallCheck() throws DAOException {
        when(questionDAO.takeQuestion(anyInt())).thenReturn(question);
        questionService.takeQuestion(questionId);

        verify(questionDAO).takeQuestion(questionId);
    }

    @Test
    public void takeQuestionReturnQuestionDAOTakeResultNullCheck() throws DAOException {
        when(questionDAO.takeQuestion(anyInt())).thenReturn(null);

        Assert.assertNull(questionService.takeQuestion(questionId));
    }

    @Test
    public void takeQuestionReturnQuestionDAOTakeResultNotNullCheck() throws DAOException {
        when(questionDAO.takeQuestion(anyInt())).thenReturn(question);

        Assert.assertEquals(question, questionService.takeQuestion(questionId));
    }

    @Test
    public void takeQuestionDAOExceptionThrownReturnNullCheck() throws DAOException {
        when(questionDAO.takeQuestion(anyInt())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(questionService.takeQuestion(questionId));
    }

    @Test
    public void takePlayerQuestionsQuestionDAOTakeCallCheck() throws DAOException {
        when(questionDAO.takePlayerQuestions(anyInt())).thenReturn(questionList);
        questionService.takePlayerQuestions(playerId);

        verify(questionDAO).takePlayerQuestions(playerId);
    }

    @Test
    public void takePlayerQuestionsReturnQuestionDAOTakeResultNullCheck() throws DAOException {
        when(questionDAO.takePlayerQuestions(anyInt())).thenReturn(null);

        Assert.assertNull(questionService.takePlayerQuestions(playerId));
    }

    @Test
    public void takePlayerQuestionsReturnQuestionDAOTakeResultNotNullCheck() throws DAOException {
        when(questionDAO.takePlayerQuestions(anyInt())).thenReturn(questionList);

        Assert.assertEquals(questionList, questionService.takePlayerQuestions(playerId));
    }

    @Test
    public void takePlayerQuestionsDAOExceptionThrownReturnNullCheck() throws DAOException {
        when(questionDAO.takePlayerQuestions(anyInt())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(questionService.takePlayerQuestions(playerId));
    }

    @Test
    public void takeUnansweredQuestionDAOTakeCallCheck() throws DAOException {
        String topicPattern = "anyPattern";

        when(questionDAO.takeUnanswered(anyString())).thenReturn(questionList);
        questionService.takeUnanswered(topicPattern);

        verify(questionDAO).takeUnanswered(anyString());
    }

    @Test
    public void takeUnansweredModifyDatePatternNotNullCheck() throws DAOException {
        String topicPattern         = "anyPattern";
        String modifiedTopicPattern = "anypattern%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.takeUnanswered(anyString())).thenReturn(questionList);
        questionService.takeUnanswered(topicPattern);
        verify(questionDAO).takeUnanswered(captor.capture());

        Assert.assertEquals(modifiedTopicPattern, captor.getValue());
    }

    @Test
    public void takeUnansweredModifyDatePatternNullCheck() throws DAOException {
        String modifiedTopicPattern = "%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.takeUnanswered(anyString())).thenReturn(questionList);
        questionService.takeUnanswered(null);
        verify(questionDAO).takeUnanswered(captor.capture());

        Assert.assertEquals(modifiedTopicPattern, captor.getValue());
    }

    @Test
    public void takeUnansweredReturnQuestionDAOTakeResultNullCheck() throws DAOException {
        String topicPattern = "anyPattern";

        when(questionDAO.takeUnanswered(anyString())).thenReturn(null);

        Assert.assertNull(questionService.takeUnanswered(topicPattern));
    }

    @Test
    public void takeUnansweredReturnQuestionDAOTakeResultNotNullCheck() throws DAOException {
        String topicPattern = "anyPattern";

        when(questionDAO.takeUnanswered(anyString())).thenReturn(questionList);

        Assert.assertEquals(questionList, questionService.takeUnanswered(topicPattern));
    }

    @Test
    public void takeUnansweredDAOExceptionThrownReturnNullCheck() throws DAOException {
        String topicPattern = "anyPattern";

        when(questionDAO.takeUnanswered(anyString())).thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(questionService.takeUnanswered(topicPattern));
    }

    @Test
    public void takeAnsweredQuestionDAOTakeCallCheck() throws DAOException {
        String topicPattern = "anyTopicPattern";
        String datePattern  = "anyDatePattern";

        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        questionService.takeAnswered(topicPattern, datePattern, false, admin, false);

        verify(questionDAO).takeAnswered(anyString(), anyString(), anyString());
    }

    @Test
    public void takeAnsweredModifyTopicPatternNotNullCheck() throws DAOException {
        String topicPattern         = "anyTopicPattern";
        String datePattern          = "anyDatePattern";
        String modifiedTopicPattern = "anytopicpattern%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        questionService.takeAnswered(topicPattern, datePattern, false, admin, false);
        verify(questionDAO).takeAnswered(captor.capture(), anyString(), anyString());

        Assert.assertEquals(modifiedTopicPattern, captor.getValue());
    }

    @Test
    public void takeAnsweredModifyTopicPatternNullCheck() throws DAOException {
        String datePattern          = "anyDatePattern";
        String modifiedTopicPattern = "%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        questionService.takeAnswered(null, datePattern, false, admin, false);
        verify(questionDAO).takeAnswered(captor.capture(), anyString(), anyString());

        Assert.assertEquals(modifiedTopicPattern, captor.getValue());
    }

    @Test
    public void takeAnsweredModifyDatePatternNotNullCheck() throws DAOException {
        String topicPattern        = "anyTopicPattern";
        String datePattern         = "anyDatePattern";
        String modifiedDatePattern = "anyDatePattern%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        questionService.takeAnswered(topicPattern, datePattern, false, admin, false);
        verify(questionDAO).takeAnswered(anyString(), captor.capture(), anyString());

        Assert.assertEquals(modifiedDatePattern, captor.getValue());
    }

    @Test
    public void takeAnsweredModifyDatePatternNullCheck() throws DAOException {
        String topicPattern        = "anyTopicPattern";
        String modifiedDatePattern = "%";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        questionService.takeAnswered(topicPattern, null, false, admin, false);
        verify(questionDAO).takeAnswered(anyString(), captor.capture(), anyString());

        Assert.assertEquals(modifiedDatePattern, captor.getValue());
    }

    @Test
    public void takeAnsweredAdminIdPatternNotShowMyCheck() throws DAOException {
        String topicPattern = "anyTopicPattern";
        String datePattern  = "anyDatePattern";
        String adminPattern = "";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        questionService.takeAnswered(topicPattern, datePattern, false, admin, false);
        verify(questionDAO).takeAnswered(anyString(), anyString(), captor.capture());

        Assert.assertEquals(adminPattern, captor.getValue());
    }

    @Test
    public void takeAnsweredAdminIdPatternShowMyAdminNotNullCheck() throws DAOException {
        String topicPattern = "anyTopicPattern";
        String datePattern  = "anyDatePattern";
        String adminPattern = String.valueOf(adminId);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        questionService.takeAnswered(topicPattern, datePattern, true, admin, false);
        verify(questionDAO).takeAnswered(anyString(), anyString(), captor.capture());

        Assert.assertEquals(adminPattern, captor.getValue());
    }

    @Test
    public void takeAnsweredQuestionDAONotCallShowMyAdminNullCheck() throws DAOException {
        String topicPattern = "anyTopicPattern";
        String datePattern  = "anyDatePattern";

        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        questionService.takeAnswered(topicPattern, datePattern, true, null, false);
        verify(questionDAO, times(0)).takeAnswered(anyString(), anyString(), anyString());
    }

    @Test
    public void takeAnsweredReturnNullShowMyAdminNullCheck() throws DAOException {
        String topicPattern = "anyTopicPattern";
        String datePattern  = "anyDatePattern";

        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        Assert.assertNull(questionService.takeAnswered(topicPattern, datePattern, true, null, false));
    }

    @Test
    @PrepareForTest(QuestionService.class)
    public void takeAnsweredSatisfactionSortCallCheck() throws Exception {
        String topicPattern = "anyTopicPattern";
        String datePattern  = "anyDatePattern";

        PowerMockito.spy(QuestionService.class);
        PowerMockito.doNothing().when(QuestionService.class, "sortBySatisfaction", any(List.class));
        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        questionService.takeAnswered(topicPattern, datePattern, false, admin, true);
        PowerMockito.verifyPrivate(QuestionService.class).invoke("sortBySatisfaction", any(List.class));
    }

    @Test
    public void takeAnsweredReturnQuestionDAOTakeNotNullCheck() throws DAOException {
        String topicPattern = "anyTopicPattern";
        String datePattern  = "anyDatePattern";

        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(questionList);
        Assert.assertEquals(questionList, questionService.takeAnswered(topicPattern, datePattern, false, admin, false));
    }

    @Test
    public void takeAnsweredReturnQuestionDAOTakeNullCheck() throws DAOException {
        String topicPattern = "anyTopicPattern";
        String datePattern  = "anyDatePattern";

        when(questionDAO.takeAnswered(anyString(), anyString(), anyString())).thenReturn(null);
        Assert.assertNull(questionService.takeAnswered(topicPattern, datePattern, false, admin, false));
    }

    @Test
    public void takeAnsweredDAOExceptionThrownReturnNullCheck() throws DAOException {
        String topicPattern = "anyTopicPattern";
        String datePattern  = "anyDatePattern";

        when(questionDAO.takeAnswered(anyString(), anyString(), anyString()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertNull(questionService.takeAnswered(topicPattern, datePattern, false, admin, false));
    }

    @Test
    public void takeAnsweredSatisfactionSortResultCheck() throws Exception {
        List<Question> questionList = new ArrayList<>();
        Question       question1    = new Question();
        question1.setId(1);
        question1.setSatisfaction(Question.Satisfaction.NORM);
        Question question2 = new Question();
        question2.setId(2);
        question2.setSatisfaction(Question.Satisfaction.BEST);
        Question question3 = new Question();
        question3.setId(3);
        question3.setSatisfaction(Question.Satisfaction.GOOD);
        Question question4 = new Question();
        question4.setId(4);
        question4.setSatisfaction(Question.Satisfaction.GOOD);
        question4.setQuestionDate(LocalDateTime.now());
        Question question5 = new Question();
        question5.setId(5);
        question5.setSatisfaction(Question.Satisfaction.GOOD);
        question5.setQuestionDate(LocalDateTime.now().plusMonths(1));
        Question question6 = new Question();
        question6.setId(6);
        questionList.addAll(Arrays.asList(question1, question2, question3, question4, question5, question6, null));

        int expectedId = 2;

        Whitebox.invokeMethod(QuestionService.class, "sortBySatisfaction", questionList);
        int actualId = questionList.get(0) != null ? questionList.get(0).getId() : 0;

        Assert.assertEquals(expectedId, actualId);
    }

    @Test
    public void sendSupportQuestionDAOInsertCallUserNotNullCheck() throws DAOException {
        String topic    = "anyTopic ";
        String question = "anyQuestion ";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        questionService.sendSupport(player, email, topic, question);

        verify(questionDAO).insertQuestion(playerId,
                                           email.toLowerCase().trim(),
                                           topic.toLowerCase().trim(),
                                           question.trim());
    }

    @Test
    public void sendSupportQuestionDAOInsertUserNotNullResultZeroReturnFalseCheck() throws DAOException {
        String topic    = "anyTopic";
        String question = "anyQuestion";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(0);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        Assert.assertFalse(questionService.sendSupport(player, email, topic, question));
    }

    @Test
    public void sendSupportQuestionDAOInsertUserNotNullResultNotZeroReturnTrueCheck() throws DAOException {
        String topic    = "anyTopic";
        String question = "anyQuestion";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        Assert.assertTrue(questionService.sendSupport(player, email, topic, question));
    }

    @Test
    public void sendSupportQuestionDAOInsertUserNotNullDAOExceptionThrownReturnFalseCheck() throws DAOException {
        String topic    = "anyTopic";
        String question = "anyQuestion";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString()))
        .thenThrow(new DAOException("Database connection error."));
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        Assert.assertFalse(questionService.sendSupport(player, email, topic, question));
    }

    @Test
    public void sendSupportQuestionDAOInsertCallUserNullCheck() throws DAOException {
        String topic    = "anyTopic ";
        String question = "anyQuestion ";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        questionService.sendSupport(null, email, topic, question);

        verify(questionDAO).insertQuestion(email.toLowerCase().trim(),
                                           topic.toLowerCase().trim(),
                                           question.trim());
    }

    @Test
    public void sendSupportQuestionDAOInsertUserNullResultZeroReturnFalseCheck() throws DAOException {
        String topic    = "anyTopic";
        String question = "anyQuestion";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(0);
        Assert.assertFalse(questionService.sendSupport(null, email, topic, question));
    }

    @Test
    public void sendSupportQuestionDAOInsertUserNullResultNotZeroReturnTrueCheck() throws DAOException {
        String topic    = "anyTopic";
        String question = "anyQuestion";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        Assert.assertTrue(questionService.sendSupport(null, email, topic, question));
    }

    @Test
    public void sendSupportQuestionDAOInsertUserNullDAOExceptionThrownReturnFalseCheck() throws DAOException {
        String topic    = "anyTopic";
        String question = "anyQuestion";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString()))
        .thenThrow(new DAOException("Database connection error."));
        Assert.assertFalse(questionService.sendSupport(null, email, topic, question));
    }

    @Test
    public void sendSupportUserNotNullEmailNullReturnFalseCheck() throws DAOException {
        String topic    = "anyTopic";
        String question = "anyQuestion";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        Assert.assertFalse(questionService.sendSupport(player, null, topic, question));
    }

    @Test
    public void sendSupportUserNullEmailNullReturnFalseCheck() throws DAOException {
        String topic    = "anyTopic";
        String question = "anyQuestion";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        Assert.assertFalse(questionService.sendSupport(null, null, topic, question));
    }

    @Test
    public void sendSupportUserNotNullQuestionNullReturnFalseCheck() throws DAOException {
        String topic = "anyTopic";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        Assert.assertFalse(questionService.sendSupport(player, email, topic, null));
    }

    @Test
    public void sendSupportUserNullQuestionNullReturnFalseCheck() throws DAOException {
        String topic = "anyTopic";

        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        Assert.assertFalse(questionService.sendSupport(null, email, topic, null));
    }

    @Test
    public void sendSupportUserNotNullModifiedTopicNullCheck() throws DAOException {
        String question = "anyQuestion";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        questionService.sendSupport(player, email, null, question);
        verify(questionDAO).insertQuestion(anyInt(), anyString(), captor.capture(), anyString());

        Assert.assertEquals("other", captor.getValue());
    }

    @Test
    public void sendSupportUserNullModifiedTopicNullCheck() throws DAOException {
        String question = "anyQuestion";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(questionDAO.insertQuestion(anyInt(), anyString(), anyString(), anyString())).thenReturn(questionId);
        when(questionDAO.insertQuestion(anyString(), anyString(), anyString())).thenReturn(questionId);
        questionService.sendSupport(null, email, null, question);
        verify(questionDAO).insertQuestion(anyString(), captor.capture(), anyString());

        Assert.assertEquals("other", captor.getValue());
    }

    @Test
    public void rateAnswerQuestionDAOChangeCallCheck() throws DAOException {
        when(questionDAO.changeQuestionSatisfaction(anyInt(), anyString())).thenReturn(true);
        questionService.rateAnswer(questionId, satisfaction);

        verify(questionDAO).changeQuestionSatisfaction(questionId, satisfaction.toLowerCase());
    }

    @Test
    public void rateAnswerReturnQuestionDAOChangeResultTrueCheck() throws DAOException {
        when(questionDAO.changeQuestionSatisfaction(anyInt(), anyString())).thenReturn(true);

        Assert.assertTrue(questionService.rateAnswer(questionId, satisfaction));
    }

    @Test
    public void rateAnswerReturnQuestionDAOChangeResultFalseCheck() throws DAOException {
        when(questionDAO.changeQuestionSatisfaction(anyInt(), anyString())).thenReturn(false);

        Assert.assertFalse(questionService.rateAnswer(questionId, satisfaction));
    }

    @Test
    public void rateAnswerDAOExceptionThrownReturnFalseCheck() throws DAOException {
        when(questionDAO.changeQuestionSatisfaction(anyInt(), anyString()))
        .thenThrow(new DAOException("Database connection error."));

        Assert.assertFalse(questionService.rateAnswer(questionId, satisfaction));
    }

    @Test
    public void resetAnswerRatingRateAnswerCallCheck() throws DAOException {
        QuestionService questionService = spy(new QuestionService(daoHelper));
        when(questionService.rateAnswer(anyInt(), anyString())).thenReturn(true);
        questionService.resetAnswerRating(questionId);

        verify(questionService).rateAnswer(eq(questionId), isNull(String.class));
    }

    @Test
    public void resetAnswerRatingReturnRateAnswerResultTrueCheck() throws DAOException {
        QuestionService questionService = spy(this.questionService);
        doReturn(true).when(questionService).rateAnswer(anyInt(), isNull(String.class));

        Assert.assertTrue(questionService.resetAnswerRating(questionId));
    }

    @Test
    public void resetAnswerRatingReturnRateAnswerResultFalseCheck() throws DAOException {
        QuestionService questionService = spy(this.questionService);
        doReturn(false).when(questionService).rateAnswer(anyInt(), isNull(String.class));

        Assert.assertFalse(questionService.resetAnswerRating(questionId));
    }
}