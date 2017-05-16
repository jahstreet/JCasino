package by.sasnouskikh.jcasino.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({AdminServiceTest.class, AdminServiceTakePlayerTest.class,
                     LoanServiceTest.class, LoanServicePrivateStaticCallTest.class,
                     NewsServiceTakeDeleteTest.class, NewsServiceAddEditNewsTest.class,
                     PlayerServiceTest.class,
                     QuestionServiceTest.class, QuestionServiceAnswerSupportTest.class,
                     RandomGeneratorTest.class,
                     StreakServiceTest.class, StreakServiceStaticTest.class,
                     TransactionServiceTest.class, TransactionServiceStaticTest.class,
                     UserServiceTest.class,
                     VerificationServiceTest.class})
public class ServiceTestSuite {

}