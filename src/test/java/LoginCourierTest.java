import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;


public class LoginCourierTest {
    private CourierSteps courierSteps;
    private Courier courier;
    private int id;
    private final static String ERROR_MESSAGE_404 = "Учетная запись не найдена";
    private final static String ERROR_MESSAGE_400 = "Недостаточно данных для входа";

    @Before
    public void setUp() {
        courierSteps = new CourierSteps();
        courier = CourierDataCreator.getDefault();
    }

    @After
    public void cleanUp() {
        courierSteps.delete(id);
    }

    @Test
    @DisplayName("Выполнение логина курьером")
    public void checkSignInPositive() {
        courierSteps.create(courier);
        ValidatableResponse login = courierSteps.login(Credentials.from(courier));
        int actualStatusCode = login.extract().statusCode();
        id = login.extract().path("id");
        assertNotNull(id);
        assertEquals(SC_OK,actualStatusCode);
    }

    @Test
    @DisplayName("Попытка логина с некорректным значением поля 'Login'")
    public void checkSignInWithWrongLogin() {
        courierSteps.create(courier);
        ValidatableResponse correctLogin = courierSteps.login(Credentials.from(courier));
        id = correctLogin.extract().path("id");
        courier.setLogin("1");
        ValidatableResponse wrongLogin = courierSteps.login(Credentials.from(courier));
        int actualStatusCode = wrongLogin.extract().statusCode();
        assertEquals(HTTP_NOT_FOUND, actualStatusCode);
        String actualMessage = wrongLogin.extract().path("message");
        assertEquals(ERROR_MESSAGE_404, actualMessage);
    }

    @Test
    @DisplayName("Попытка логина с некорректным паролем")
    public void checkSignInWithWrongPassword() {
        courierSteps.create(courier);
        ValidatableResponse correctLogin = courierSteps.login(Credentials.from(courier));
        id = correctLogin.extract().path("id");
        courier.setPassword("1");
        ValidatableResponse wrongLogin = courierSteps.login(Credentials.from(courier));
        int actualStatusCode = wrongLogin.extract().statusCode();
        assertEquals(HTTP_NOT_FOUND, actualStatusCode);
        String actualMessage = wrongLogin.extract().path("message");
        assertEquals(ERROR_MESSAGE_404, actualMessage);
    }

    @Test
    @DisplayName("Попытка логина без обязательного поля 'Login'")
    public void checkSignInWithoutLogin() {
        courierSteps.create(courier);
        ValidatableResponse correctLogin = courierSteps.login(Credentials.from(courier));
        id = correctLogin.extract().path("id");
        courier.setLogin(null);
        ValidatableResponse wrongLogin = courierSteps.login(Credentials.from(courier));
        int actualStatusCode = wrongLogin.extract().statusCode();
        assertEquals(HTTP_BAD_REQUEST, actualStatusCode);
        String actualMessage = wrongLogin.extract().path("message");
        assertEquals(ERROR_MESSAGE_400, actualMessage);
    }
}