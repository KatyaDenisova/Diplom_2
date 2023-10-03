package model.steps;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseApi {
    public RequestSpecification RequestSpecification() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        return RestAssured.given().log().all()
                .header("Content-type", "application/json");
    }
}
