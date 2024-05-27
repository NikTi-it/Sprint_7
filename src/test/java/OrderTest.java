import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class OrderTest {
    private OrderSteps orderSteps;
    private Order order;
    private int track;
    private int statusCode;

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
    }

    @After
    public void cleanUp() {
        ValidatableResponse delete = orderSteps.cancel(track);
        int actualStatusCode = delete.extract().statusCode();
        assertEquals(HTTP_OK,actualStatusCode);
    }

    public OrderTest(Order order, int statusCode) {
        this.order = order;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {OrderDataCreator.getColor(List.of("GREY")) , HTTP_OK},
                {OrderDataCreator.getColor(List.of("BLACK")), HTTP_OK},
                {OrderDataCreator.getColor(List.of("")), HTTP_OK},
                {OrderDataCreator.getDefault(),HTTP_OK}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    public void checkCreateOrderPositive() {
        ValidatableResponse create = orderSteps.create(order);
        int actualStatusCode = create.extract().statusCode();
        assertEquals(HTTP_CREATED,actualStatusCode);
        track = create.extract().path("track");
        assertNotNull(track);
    }
}