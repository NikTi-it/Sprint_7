import com.github.javafaker.Faker;

import java.util.List;

public class OrderDataCreator {

    private static final Faker faker = new Faker();

    public static Order getDefault() {
        List<String> color = List.of("BLACK,GREY");
        return new Order(faker.name().firstName(),faker.name().lastName(),faker.address().streetAddress(),Integer.toString(faker.number().numberBetween(1,5)),"89215434525",faker.number().numberBetween(1,3),faker.backToTheFuture().date(), faker.funnyName().name(),color);
    }

    public static Order getColor(List<String> color) {
        return new Order(faker.name().firstName(),faker.name().lastName(),faker.address().streetAddress(),Integer.toString(faker.number().numberBetween(1,5)),"89215435422",faker.number().numberBetween(1,3),faker.backToTheFuture().date(), faker.funnyName().name(),color);
    }
}