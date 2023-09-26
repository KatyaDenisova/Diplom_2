package order;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import model.constants.Constants;
import model.resources.Ingredients;
import model.resources.LoginUser;
import org.junit.Test;

import java.util.ArrayList;

@Tag("Создание заказа")
public class OrderTest extends BaseTest {
    private LoginUser loginUser;

    @Test
    @DisplayName("Создание заказа с авторизацией и ингридиентами")
    public void createOrderWithAuth() {
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
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, true, null);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингридиентов")
    public void createOrderWithAuthWithoutIng() {
        userPage.createUser(user);
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        Ingredients newIngredients = new Ingredients(null);
        ValidatableResponse validatableResponse = userPage.createOrderOne(token, newIngredients)
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, false, "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Создание заказа без авторизациии с ингридиентами")
    public void createOrderWithoutAuthWithIng() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(Constants.FIRST_BUN);
        ingredients.add(Constants.SAUCE);
        ingredients.add(Constants.FIRST_MAIN);
        ingredients.add(Constants.SECOND_MAIN);
        ingredients.add(Constants.SECOND_BUN);
        Ingredients newIngredients = new Ingredients(ingredients);
        ValidatableResponse response = userPage.createOrderWithoutLogIn(newIngredients)
                .assertThat().statusCode(HttpStatus.SC_OK);
        userPage.checkCorrectStatusCodeAndBody(response, true, null);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHash() {
        userPage.createUser(user);
        loginUser = new LoginUser(user.getEmail(), user.getPassword());
        ValidatableResponse response = userPage.loginUser(loginUser);
        token = response.extract().path("accessToken").toString();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(Constants.BAD_FIRST_BUN);
        ingredients.add(Constants.BAD_FIRST_MAIN);
        Ingredients newIngredients = new Ingredients(ingredients);
        ValidatableResponse validatableResponse = userPage.createOrderOne(token, newIngredients)
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        userPage.checkCorrectStatusCodeAndBody(validatableResponse, false, "One or more ids provided are incorrect");
    }
}
