package socialnetwork.repository.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.models.User;
import socialnetwork.repository.RepositoryInterface;
import socialnetwork.repository.UserRepositoryTestSetter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDataBaseRepositoryTest extends UserRepositoryTestSetter {
    String url = ApplicationContext.getProperty("network.database.url");
    String user = ApplicationContext.getProperty("network.database.user");
    String password = ApplicationContext.getProperty("network.database.password");
    UserDatabaseRepository testRepository;
    @Override
    public RepositoryInterface<Long, User> getRepository() {
        if(testRepository == null)
            testRepository = new UserDatabaseRepository(url, user, password);
        return testRepository;
    }

    @BeforeAll
    static void setUpDatabase(){
        DatabaseCleaner.clearDatabase();
    }

    @BeforeEach
    public void setUp(){
        UserDatabaseTableSetter.setUp(getTestData());
    }
}