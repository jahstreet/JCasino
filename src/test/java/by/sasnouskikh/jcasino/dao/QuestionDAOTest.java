package by.sasnouskikh.jcasino.dao;

import by.sasnouskikh.jcasino.entity.bean.Question;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class QuestionDAOTest extends AbstractDAOTest {

    private static final String TABLE_QUESTION                  = "question";
    private static final String XML_QUESTION_DATA               = "by/sasnouskikh/jcasino/dao/question_data.xml";
    private static final String XML_INSERTED_QUESTION           = "by/sasnouskikh/jcasino/dao/question_data_inserted_question.xml";
    private static final String XML_INSERTED_ANONYMOUS_QUESTION = "by/sasnouskikh/jcasino/dao/question_data_inserted_anonymous_question.xml";
    private static final String XML_CHANGED_SATISFACTION        = "by/sasnouskikh/jcasino/dao/question_data_changed_satisfaction.xml";
    private static final String XML_ANSWERED                    = "by/sasnouskikh/jcasino/dao/question_data_answered.xml";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        beforeData = buildDataSet(XML_QUESTION_DATA);
        DatabaseOperation.CLEAN_INSERT.execute(connection, beforeData);
    }

    @Test
    public void takeQuestionCheck() throws DAOException {
        int                    questionId = 1;
        int                    playerId   = 100;
        String                 email      = "tramp@tramp.tr";
        Question.QuestionTopic topic      = Question.QuestionTopic.OTHER;
        String                 question   = "Why are you so bad people?";
        LocalDateTime          date       = Timestamp.valueOf("2017-02-23 00:00:00.0").toLocalDateTime();

        Question expected = new Question();
        expected.setId(questionId);
        expected.setPlayerId(playerId);
        expected.setEmail(email);
        expected.setTopic(topic);
        expected.setQuestion(question);
        expected.setQuestionDate(date);

        Question actual = daoHelper.getQuestionDAO().takeQuestion(questionId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", expected, actual),
                            expected, actual);
    }

    @Test
    public void takeQuestionNoSuchCheck() throws DAOException {
        int questionId = 25;

        Question actual = daoHelper.getQuestionDAO().takeQuestion(questionId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takePlayerQuestionsCheck() throws DAOException {
        int playerId          = 100;
        int expectedRowNumber = 2;

        List<Question> questionList    = daoHelper.getQuestionDAO().takePlayerQuestions(playerId);
        int            actualRowNumber = questionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takePlayerQuestionsNoQuestionsCheck() throws DAOException {
        int playerId = 102;

        List<Question> actual = daoHelper.getQuestionDAO().takePlayerQuestions(playerId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takePlayerQuestionsNoSuchCheck() throws DAOException {
        int playerId = 103;

        List<Question> actual = daoHelper.getQuestionDAO().takePlayerQuestions(playerId);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takeUnansweredCheck() throws DAOException {
        String topicPattern      = "other%";
        int    expectedRowNumber = 2;

        List<Question> questionList    = daoHelper.getQuestionDAO().takeUnanswered(topicPattern);
        int            actualRowNumber = questionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takeUnansweredAllTopicsCheck() throws DAOException {
        String topicPattern      = "%";
        int    expectedRowNumber = 2;

        List<Question> questionList    = daoHelper.getQuestionDAO().takeUnanswered(topicPattern);
        int            actualRowNumber = questionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takeUnansweredNoQuestionsCheck() throws DAOException {
        String topicPattern = "unknownTopic%";

        List<Question> actual = daoHelper.getQuestionDAO().takeUnanswered(topicPattern);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void takeAnsweredWithPatternsCheck() throws DAOException {
        String topicPattern      = "%";
        String monthPattern      = "2017-03%";
        String adminPattern      = "1%";
        int    expectedRowNumber = 2;

        List<Question> questionList    = daoHelper.getQuestionDAO().takeAnswered(topicPattern, monthPattern, adminPattern);
        int            actualRowNumber = questionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takeAnsweredAllCheck() throws DAOException {
        String topicPattern      = "%";
        String monthPattern      = "%";
        String adminPattern      = "%";
        int    expectedRowNumber = 6;

        List<Question> questionList    = daoHelper.getQuestionDAO().takeAnswered(topicPattern, monthPattern, adminPattern);
        int            actualRowNumber = questionList.size();

        Assert.assertEquals(String.format("Rows number expected: %d, actual: %d", expectedRowNumber, actualRowNumber),
                            expectedRowNumber, actualRowNumber);
    }

    @Test
    public void takeAnsweredNoQuestionsCheck() throws DAOException {
        String topicPattern = "unknownTopic%";
        String monthPattern = "%";
        String adminPattern = "%";

        List<Question> actual = daoHelper.getQuestionDAO().takeAnswered(topicPattern, monthPattern, adminPattern);

        Assert.assertEquals(String.format("Expected: %s\nActual: %s", null, actual),
                            null, actual);
    }

    @Test
    public void insertQuestionCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      playerId = 100;
        String   email    = "entered@mail.com";
        String   topic    = "other";
        String   question = "Test question text.";
        String[] ignore   = {"q_date"};

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED_QUESTION);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_QUESTION);

        daoHelper.getQuestionDAO().insertQuestion(playerId, email, topic, question);
        ITable actualTable = connection.createTable(TABLE_QUESTION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void insertQuestionIdCheck() throws DAOException {
        int    playerId = 100;
        String email    = "entered@mail.com";
        String topic    = "other";
        String question = "Test question text.";

        int expected = 9;

        int actual = daoHelper.getQuestionDAO().insertQuestion(playerId, email, topic, question);

        Assert.assertEquals(String.format("Inserted question id expected: %d, actual: %d", expected, actual),
                            expected, actual);
    }

    @Test(expected = DAOException.class)
    public void questionPlayerFkConstraintCheck() throws DAOException {
        int    playerId = 103;
        String email    = "entered@mail.com";
        String topic    = "other";
        String question = "Test question text.";

        daoHelper.getQuestionDAO().insertQuestion(playerId, email, topic, question);

        Assert.fail("A FK constraint `jcasino`.`question`, CONSTRAINT `fk_question_player` FOREIGN KEY (`player_id`) " +
                    "REFERENCES `player` (`id`) should exist.");
    }

    @Test(expected = DAOException.class)
    public void questionTopicEnumCheck() throws DAOException {
        int    playerId = 100;
        String email    = "entered@mail.com";
        String topic    = "invalidTopic";
        String question = "Test question text.";

        daoHelper.getQuestionDAO().insertQuestion(playerId, email, topic, question);

        Assert.fail("Value of `topic` should match definite topic-enum values.");
    }

    @Test
    public void insertAnonymousQuestionCheck() throws DAOException, DatabaseUnitException, SQLException {
        String   email    = "entered@mail.com";
        String   topic    = "other";
        String   question = "Test question text.";
        String[] ignore   = {"q_date"};

        IDataSet expectedDataSet = buildDataSet(XML_INSERTED_ANONYMOUS_QUESTION);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_QUESTION);

        daoHelper.getQuestionDAO().insertQuestion(email, topic, question);
        ITable actualTable = connection.createTable(TABLE_QUESTION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void insertAnonymousQuestionIdCheck() throws DAOException {
        String email    = "entered@mail.com";
        String topic    = "other";
        String question = "Test question text.";

        int expected = 9;

        int actual = daoHelper.getQuestionDAO().insertQuestion(email, topic, question);

        Assert.assertEquals(String.format("Inserted question id expected: %d, actual: %d", expected, actual),
                            expected, actual);
    }

    @Test
    public void changeQuestionSatisfactionCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      id           = 4;
        String   satisfaction = "good";
        String[] ignore       = {"q_date"};

        IDataSet expectedDataSet = buildDataSet(XML_CHANGED_SATISFACTION);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_QUESTION);

        daoHelper.getQuestionDAO().changeQuestionSatisfaction(id, satisfaction);
        ITable actualTable = connection.createTable(TABLE_QUESTION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void changeQuestionSatisfactionTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    id           = 4;
        String satisfaction = "good";

        boolean actual = daoHelper.getQuestionDAO().changeQuestionSatisfaction(id, satisfaction);

        Assert.assertTrue(String.format("Method should return `true`, if it proceeded successfully, but it returns: %s", actual),
                          actual);
    }

    @Test(expected = DAOException.class)
    public void changeQuestionSatisfactionNoIdCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    id           = 10;
        String satisfaction = "good";

        daoHelper.getQuestionDAO().changeQuestionSatisfaction(id, satisfaction);

        Assert.fail("DAOException should be thrown if no player with given ID in database.");
    }

    @Test(expected = DAOException.class)
    public void questionSatisfactionEnumCheck() throws DAOException {
        int    id           = 4;
        String satisfaction = "invalidSatisfaction";

        daoHelper.getQuestionDAO().changeQuestionSatisfaction(id, satisfaction);

        Assert.fail("Value of `satisfaction` should match definite topic-enum values.");
    }

    @Test
    public void answerQuestionCheck() throws DAOException, DatabaseUnitException, SQLException {
        int      id      = 1;
        int      adminId = 2;
        String   answer  = "Test answer.";
        String[] ignore  = {"q_date", "a_date"};

        IDataSet expectedDataSet = buildDataSet(XML_ANSWERED);
        ITable   expectedTable   = expectedDataSet.getTable(TABLE_QUESTION);

        daoHelper.getQuestionDAO().answerQuestion(id, answer, adminId);
        ITable actualTable = connection.createTable(TABLE_QUESTION);

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignore);
    }

    @Test
    public void answerQuestionTrueCheck() throws DAOException, DatabaseUnitException, SQLException {
        int    id      = 1;
        int    adminId = 2;
        String answer  = "Test answer.";

        boolean actual = daoHelper.getQuestionDAO().answerQuestion(id, answer, adminId);

        Assert.assertTrue(String.format("Method should return `true`, if it proceeded successfully, but it returns: %s", actual),
                          actual);
    }

    @Test(expected = DAOException.class)
    public void answerQuestionNoSuchCheck() throws DAOException {
        int    id      = 10;
        int    adminId = 2;
        String answer  = "Test answer.";

        daoHelper.getQuestionDAO().answerQuestion(id, answer, adminId);

        Assert.fail("DAOException should be thrown if no question with given ID in database.");
    }

    @Test(expected = DAOException.class)
    public void questionAdminFkConstraintCheck() throws DAOException {
        int    id      = 1;
        int    adminId = 3;
        String answer  = "Test answer.";

        daoHelper.getQuestionDAO().answerQuestion(id, answer, adminId);

        Assert.fail("A FK constraint `jcasino`.`question`, CONSTRAINT `fk_question_user` FOREIGN KEY (`admin_id`) " +
                    "REFERENCES `user` (`id`) should exist.");
    }


}