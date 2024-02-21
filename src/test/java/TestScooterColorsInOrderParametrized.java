import Config.*;
import io.restassured.RestAssured;


import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import Config.Configuration;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.*;

import java.util.List;

@RunWith(Parameterized.class)
public class TestScooterColorsInOrderParametrized {
    Order order;
    List<String> color;
    int orderId;

    public TestScooterColorsInOrderParametrized(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] testScooterColorsData() {
        return new Object[][]{
                {List.of("Black")},
                {List.of("Gray")},
                {List.of("Black", "Gray")},
                {List.of("")}
        };
    }

    @Before  // Задаем базовый URI
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;
    }

    @Test
    public void testScooterColorsInOrder() {
        order = new Order(this.color);
        Response response = given()
                .header(Data.requestHeader)
                .and()
                .body(order)
                .when()
                .post(order.getNewOrderPath());
        orderId = response.jsonPath().getInt("track");
        response.then().assertThat().statusCode(201)
                .and().body("track",notNullValue());

    }
    @After
    public void deleteOrder(){
        try{
            given()
                    .header(Data.requestHeader)
                    .and()
                    .when()
                    .post(order.getFinishOrderPath(orderId));

        }catch (NullPointerException e){}

    }

}
