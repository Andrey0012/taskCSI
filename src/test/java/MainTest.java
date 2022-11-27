import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class MainTest {

    @Test
    void test() {
        List<Price> list = new ArrayList<>();//старые цены
        List<Price> list1 = new ArrayList<>();//новые цены
        List<Price> list2 = new ArrayList<>();//итоговые цены

        Price price = new Price((long) 1, "5", 1, 1, LocalDateTime.of(2022, Month.JANUARY, 10, 00, 00, 00), LocalDateTime.of(2022, Month.JANUARY, 30, 00, 00, 00), (long) 555);
        Price price0 = new Price((long) 1, "5555", 1, 1, LocalDateTime.of(2022, Month.JANUARY, 10, 00, 00, 00), LocalDateTime.of(2022, Month.JANUARY, 30, 00, 00, 00), (long) 555);
        Price price1 = new Price((long) 1, "5", 1, 1, LocalDateTime.of(2022, Month.JANUARY, 15, 00, 00, 00), LocalDateTime.of(2022, Month.JANUARY, 25, 00, 00, 00), (long) 500);
        Price price2 = new Price(price.getId(), price.getProductCode(), price.getNumber(), price.getDepart(), price.getBegin(), price1.getBegin(), price.getValue());
        Price price3 = new Price(price.getId(), price.getProductCode(), price.getNumber(), price.getDepart(), price1.getEnd(), price.getEnd(), price.getValue());
        list.add(price);
        list1.add(price0);
        list1.add(price1);
        list2.add(price0);
        list2.add(price1);
        list2.add(price2);
        list2.add(price3);
        Assertions.assertTrue((ServicePrice.resultMethod(list, list1)).equals(list2));
    }
}
