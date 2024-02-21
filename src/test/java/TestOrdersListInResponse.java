import Config.*;
import io.qameta.allure.Description;
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

public class TestOrdersListInResponse {


    Courier courier;
    int courierId;
    int orderTrack;

    @Before  // Задаем базовый URI
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;

    }
    @Test
    public void TestOrdersListInResponse(){
        Response response = given()
                .header(Data.requestHeader)
                .and()
                .get("/api/v1/orders");
        response.then().assertThat().body("orders",notNullValue());
    }

//    @Test
//    public void TestOrdersListInResponse(){
//    // Создаем курьера
//                given()
//                .header(Data.requestHeader)
//                .and()
//                .body(courier.getNewCourierRequestBody())
//                .when()
//                .post(courier.getNewCourierAPIPath());
//    // Залогиниваем курьера
//        Response responseLogin =
//                given()
//                        .header(Data.requestHeader)
//                        .and()
//                        .body(courier.getLoginCourierRequestBody())
//                        .when()
//                        .post(courier.getLoginCourierAPIPath());
//        // Узнаем id курьера
//        courierId = responseLogin.jsonPath().getInt("id");
//        // Создаем заказ
//        Order order = new Order("Saske", "Uchiha","Kanoha 34", "Kanoha Station", "+1234567890",
//                4, "12.03.2022", "Don't give my scooter to Naruto!", List.of("Black"));
//        Response responseOrder = given()
//                .header(Data.requestHeader)
//                .and()
//                .body(order)
//                .when()
//                .post(order.getNewOrderPath());
//        // узнаем трек заказа
//        orderTrack = responseOrder.jsonPath().getInt("track");
//        // созданный курьер принимает заказ
//        String orderAcceptBody = "{\"track\":"+orderTrack+"}";
//        Response responseOrderAccept = given().header(Data.requestHeader)
//                .and()
//                .body(orderAcceptBody)
//                .put(("api/v1/orders/accept/1"+"?courierId="+courierId));
//        System.out.println(responseOrderAccept.getBody().asString());
//
//        Response responseOrderList = given()
//                .header(Data.requestHeader)
//                .and()
//                .get("api/v1/orders?courierId="+courierId);
//        System.out.println(responseOrderList.getBody().asString());
//        responseOrderList.then()
//                .statusCode(200)
//                .and()
//                .body("order.firstName", equalTo("Saske"))
//                .and()
//                .body("order.lastName", equalTo("Uchiha"));
//
//    }
//    @After
//    @Description("Deletion of a courier if exists")
//    public void deleteTestCourierIfExist() {
//        try {
//            given()
//                    .header(Data.requestHeader)
//                    .when()
//                    .delete(courier.getDeleteCourierAPIPath(courierId));
//
//        } catch (NullPointerException e) {
//            System.out.println("Курьер не был создан, его невозможно удалить!");
//        }
//    }
}
