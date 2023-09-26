package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import model.constants.Constants;
import model.resources.Ingredients;
import model.resources.LoginUser;
import org.junit.Test;

import java.util.ArrayList;

@Tag("Получение заказов конкретного пользователя")
public class GetOrderTest extends BaseTest {
    private LoginUser loginUser;

    @Test
    @DisplayName("Получение заказов авторизированого пользователя")
    public void getOrderFromAuthUser() {
        userPage.createUser(user);
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(Constants.FIRST_BUN);
        ingredients.add(Constants.SAUCE);
        ingredients.add(Constants.FIRST_MAIN);
        ingredients.add(Constants.SECOND_MAIN);
        ingredients.add(Constants.SECOND_BUN);
        Ingredients newIngredients = new Ingredients(ingredients);
        ValidatableResponse validatableResponse = userPage.createOrderOne(token, newIngredients)
                .assertThat().statusCode(HttpStatus.SC_OK);
        userPage.getOrderWithLogIn(token)
                .assertThat().statusCode(HttpStatus.SC_OK);
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, true, null);
    }

    @Test
    @DisplayName("Получение заказов неавторизированого пользователя")
    public void getOrderFromUnauthUser() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(Constants.FIRST_BUN);
        ingredients.add(Constants.SAUCE);
        ingredients.add(Constants.FIRST_MAIN);
        ingredients.add(Constants.SECOND_MAIN);
        ingredients.add(Constants.SECOND_BUN);
        Ingredients newIngredients = new Ingredients(ingredients);
        userPage.createOrderWithoutLogIn(newIngredients)
                .assertThat().statusCode(HttpStatus.SC_OK);
        ValidatableResponse validatableResponse = userPage.getOrderWithoutLogIn()
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, false, "You should be authorised");
    }
}
