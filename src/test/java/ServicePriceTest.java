import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServicePriceTest {

    @Test
    void resultMethodTest() {
        Price price = createPrice("5", 10, 30, 555);
        Price price0 = createPrice("5555", 10, 30, 555);
        Price price1 = createPrice("5", 15, 25, 500);
        List<Price> listNew = List.of(price);
        List<Price> listAvailable = List.of(price0, price1);
        ServicePrice servicePrice = new ServicePrice();
        List<Price> result = servicePrice.resultMethod(listNew, listAvailable);
        assertEquals(4, result.size());
        assertTrue(result.contains(price0));
        assertTrue(result.contains(price1));
        Optional<Price> newPrice = result.stream().filter(s -> (s.getProductCode().equals(price.getProductCode())) && (s.getNumber() == price.getNumber()) && (s.getDepart() == price.getDepart()) &&
                (s.getBegin().equals(price.getBegin())) && (s.getEnd().equals(price1.getBegin())) && (s.getValue() == price.getValue())).findFirst();
        assertTrue(newPrice.isPresent());
        Optional<Price> newPriceTwo = result.stream().filter(s -> (s.getProductCode().equals(price.getProductCode())) && (s.getNumber() == price.getNumber()) && (s.getDepart() == price.getDepart()) &&
                (s.getBegin().equals(price1.getEnd())) && (s.getEnd().equals(price.getEnd())) && (s.getValue() == price.getValue())).findFirst();
        assertTrue(newPriceTwo.isPresent());
    }

    private Price createPrice(String code, int beginDay, int endDay, long value) {
        return new Price(1, code, 1, 1, LocalDateTime.of(2022, Month.JANUARY, beginDay, 0, 0, 0), LocalDateTime.of(2022, Month.JANUARY, endDay, 0, 0, 0), value);
    }
}
