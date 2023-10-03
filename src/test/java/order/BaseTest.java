package order;

import model.resources.User;
import model.resources.UserGenerator;
import model.steps.UserPage;
import org.junit.After;
import org.junit.Before;

public class BaseTest {
    public UserPage userPage = new UserPage();
    public User user = new User();
    public String token;

    @Before
    public void setUp() {
        user = UserGenerator.random();
        userPage = new UserPage();
    }

    @After
    public void deleteUser() {
        if (token != null) {
            userPage.deleteUser(token);
        }
    }
}
