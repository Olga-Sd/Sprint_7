import Config.*;
import io.restassured.RestAssured;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import Config.Configuration;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.List;

@RunWith(Parameterized.class)
public class TestScooterColorsInOrderParametrized {
    // Данный класс содержит параметризованные проверки задания цвета самоката на эндпойнте "/api/v1/orders" (при создании заказа)
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
        order = new Order("Saske", "Uchiha","Kanoha 34", "Kanoha Station", "+1234567890",
                4, "12.03.2022", "Don't give my scooter to Naruto!",this.color);
        Response response = given()
                .header(Data.requestHeader)
                .and()
                .body(order)
                .when()
                .post(order.getNewOrderPath());
        orderId = response.jsonPath().getInt("track");
        response.then().assertThat().statusCode(201)
                .and().body("track",notNullValue());
        response =  given()
                .header(Data.requestHeader)
                .when()
                .get(order.getOrderByIdPath(orderId));
        response.then()
                .statusCode(200)
                .and()
                .body("order.color", equalTo(color));

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
