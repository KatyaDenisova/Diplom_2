package user;

import io.qameta.allure.junit4.Tag;
import order.BaseTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import model.resources.User;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

@Tag("Изменение данных пользователя")
public class ChangeUserDataTest extends BaseTest {
    private User userUpdate = new User();

    @Test
    @DisplayName("Проверка изменения электронной почты с авторизацией")
    public void checkChangeEmailWithAuth() {
        User userUpdate = user.clone();
        userUpdate.setEmail(RandomStringUtils.randomAlphabetic(8) + "@newpraktikum.ru");
        token = userPage.createUser(user).extract().header("Authorization");
        userPage.updateUser(token, userUpdate);
        ValidatableResponse response = userPage.getUserInfo(token)
                .assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("user.name", equalTo(user.getName()))
                .body("user.email", equalTo(userUpdate.getEmail().toLowerCase()));
        userPage.checkCorrectStatusCodeAndBody(response, true, null);
    }

    @Test
    @DisplayName("Проверка изменения электронной почты без авторизации")
    public void checkChangeEmailWithoutAuth() {
        userUpdate.setEmail(RandomStringUtils.randomAlphabetic(8) + "@newpraktikum.ru");
        ValidatableResponse response = userPage.updateUser("", userUpdate)
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
        userPage.checkCorrectStatusCodeAndBody(response, false, "You should be authorised");
    }
}
