import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CreateCourierTest {
    private CourierSteps courierSteps;
    private Courier courier;
    private int id;
    private final static String ERROR_MESSAGE_409 = "Этот логин уже используется. Попробуйте другой.";

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
    @DisplayName("Курьера можно создать")
    public void checkCreateCourierPositive() {
        ValidatableResponse create = courierSteps.create(courier);
        ValidatableResponse login = courierSteps.login(Credentials.from(courier));
        int actualStatusCode = create.extract().statusCode();
        id = login.extract().path("id");
        Boolean isCourierCreated = create.extract().path("ok");
        assertEquals(HTTP_CREATED,actualStatusCode);
        assertTrue(isCourierCreated);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void checkCreateExistingCourier() {
        courierSteps.create(courier);
        ValidatableResponse login = courierSteps.login(Credentials.from(courier));
        id = login.extract().path("id");
        ValidatableResponse create = courierSteps.create(courier);
        int statusCode = create.extract().statusCode();
        String message = create.extract().path("message");
        assertEquals(HTTP_CONFLICT,statusCode);
        assertEquals(ERROR_MESSAGE_409,message);
    }
}