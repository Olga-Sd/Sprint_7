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

public class TestLoginCourier {
    // Данный класс содержит позитивные и негативные проверки эндпойнта POST /api/v1/courier/login (логин курьера в системе)
    Courier courier;
    int courierId;

    @Before  // Задаем базовый URI и создаем экземпляр класса Courier
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;
        courier = new Courier();
    }

    @Test
    @DisplayName("Courier can log in")
    @Description("Courier can log in with all required data. We get courier id in response body")
    public void testCanLogInWithProperRequiredFields() {
        createCourier();
        Response responseLogin = loginCourier();
        responseLogin.then().assertThat()
                .statusCode(200)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Courier cannot get in app without Login")
    @Description("Courier cannot get in app without Login. He gets 400 Bad request")
    public void testCannotGetInAppWithoutLogin() {
        createCourier();
        String loginKeeper = courier.getLogin();
        courier.setLogin("");
        Response responseLogin = loginCourier();
        responseLogin.then().assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
        courier.setLogin(loginKeeper);
    }

    @Test
    @DisplayName("Courier cannot get in app without a Password")
    @Description("Courier cannot get in app without a Password. He gets 400 Bad request")
    public void testCannotGetInAppWithoutPassword() {
        createCourier();
        String pwdKeeper = courier.getPassword();
        courier.setPassword("");
        Response responseLogin = loginCourier();
        responseLogin.then().assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
        courier.setPassword(pwdKeeper);
    }

    @Test
    @DisplayName("Courier cannot get in app with wrong Login")
    @Description("Courier cannot get in app with wrong Login. He gets 404 Not found")
    public void testCannotGetInAppWithWrongLogin() {
        createCourier();
        String loginKeeper = courier.getLogin();
        courier.setLogin("WrongLogin");
        Response responseLogin = loginCourier();
        responseLogin.then().assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courier.setLogin(loginKeeper);
    }

    @Test
    @DisplayName("Courier cannot get in app with a wrong Password")
    @Description("Courier cannot get in app with a wrong Password. He gets 404 Not found")
    public void testCannotGetInAppWithWrongPassword() {
        createCourier();
        String pwdKeeper = courier.getPassword();
        courier.setPassword("WrongPwd");
        Response responseLogin = loginCourier();
        responseLogin.then().assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courier.setPassword(pwdKeeper);
    }

    @Test
    @DisplayName("Non-existent courier cannot get in app")
    @Description("Courier cannot get in app with a wrong Password and Login. He gets 404 Not found")
    public void testNonExistentCourierCannotLogIn() {
        createCourier();
        String pwdKeeper = courier.getPassword();
        String loginKeeper = courier.getLogin();
        courier.setPassword("WrongPwd");
        courier.setLogin("WrongLogin");
        Response responseLogin = loginCourier();
        responseLogin.then().assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courier.setPassword(pwdKeeper);
        courier.setLogin(loginKeeper);
    }

    @After
    @Description("Deletion of a courier if exists")
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
        try {
            courierId = responseLogin.jsonPath().getInt("id");
        } catch (NullPointerException e) {
        }
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