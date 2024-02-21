import Config.Configuration;
import Config.Data;
import Config.Courier;
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

public class TestCreateCourier {
    // Данный класс содержит позитивные и негативные проверки создания курьера
    Courier courier;
    int courierId;

    @Before  // Задаем базовый URI и создаем экземпляр класса Courier
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;
        courier = new Courier();
    }

    @Test
    @DisplayName("Create new courier")
    @Description("A new courier can be created")
    public void testCanCreateNewCourier() {
        Response responseCreate = createCourier();
        responseCreate.then().assertThat()
                .statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Test 2 identical courier")
    @Description("Test that 2 identical couriers can not be created")
    public void canNotCreateTwoIdenticalCouriers() {
        Response responseCreate = createCourier();
        responseCreate.then().assertThat()
                .statusCode(201)
                .and()
                .body("ok", equalTo(true));
        responseCreate = createCourier();
        responseCreate.then().assertThat()
                .statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @DisplayName("Test 2 identical logins")
    @Description("Test that 2 couriers with identical logins can not be created")
    public void canNotCreateTwoCouriersWithIdenticalLogins() {
        Response responseCreate = createCourier();
        responseCreate.then().assertThat()
                .statusCode(201)
                .and()
                .body("ok", equalTo(true));
        String pwdKeeper = courier.getPassword();
        courier.setPassword("newUnusedPwd");
        String firstNameKeeper = courier.getFirstName();
        courier.setFirstName("newUnusedName");
        responseCreate = createCourier();
        responseCreate.then().assertThat()
                .statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        courier.setPassword(pwdKeeper);
        courier.setFirstName(firstNameKeeper);
    }

    @Test
    @DisplayName("Cannot create courier without login")
    @Description("Courier without login can't be created ")
    public void canNotCreateCourierWithoutLogin() {

        String loginKeeper = courier.getLogin();
        courier.setLogin("");

        Response responseCreate = createCourier();
        responseCreate.then().assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        courier.setLogin(loginKeeper);

    }
    @Test
    @DisplayName("Cannot create courier without password")
    @Description("Courier without password can't be created ")
    public void canNotCreateCourierWithoutPassword() {

        String pwdKeeper = courier.getPassword();
        courier.setPassword("");

        Response responseCreate = createCourier();
        responseCreate.then().assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        courier.setLogin(pwdKeeper);
    }
    @After
    @Description()
    public void deleteTestCourierIfExist() {
        if (courier.isInApp()) {
            loginCourier();
            deleteCourier();
        }
    }

    @Step("Create new courier")
    public Response createCourier() {
        Response responseCreate = given()
                .header(Data.requestHeader)
                .and()
                .body(courier.getNewCourierRequestBody())
                .when()
                .post(courier.getNewCourierAPIPath());
        //System.out.println(responseCreate.body().prettyPrint());
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        return responseCreate;
    }

    @Step("Login courier and get Id")
    @Description("Courier logining and getting courier's id")
    public Response loginCourier() {
        Response responseLogin =
                given()
                        .header(Data.requestHeader)
                        .and()
                        .body(courier.getLoginCourierRequestBody())
                        .when()
                        .post(courier.getLoginCourierAPIPath());
        courierId = responseLogin.jsonPath().getInt("id");
        return responseLogin;
    }

    @Step("Delete courier")
    public void deleteCourier() {
        try {
            given()
                    .header(Data.requestHeader)
                    .when()
                    .delete(courier.getDeleteCourierAPIPath(courierId));

        } catch (NullPointerException e) {
            System.out.println("Курьер не был создан, его невозможно удалить!");
        }
    }


}
