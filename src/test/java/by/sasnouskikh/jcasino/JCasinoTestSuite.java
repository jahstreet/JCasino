package by.sasnouskikh.jcasino;

import by.sasnouskikh.jcasino.dao.DAOTestSuite;
import by.sasnouskikh.jcasino.service.ServiceTestSuite;
import by.sasnouskikh.jcasino.validator.FormValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({DAOTestSuite.class, ServiceTestSuite.class, FormValidatorTest.class})
public class JCasinoTestSuite {
}