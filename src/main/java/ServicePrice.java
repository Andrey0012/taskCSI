import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServicePrice {

    public static List<Price> resultMethod(List<Price> mainCollectionAv, List<Price> mainCollectionNew) {
        // итоговые цены
        List<Price> priceList = new ArrayList<>();

        List<Pair> collect2 = mainCollectionNew.stream().map(price -> {
            int i = mainCollectionAv.indexOf(price);
            if (!(i >= 0)) {
                //добавляем новую цену без сранения если такой цены не было
                priceList.add(price);
            }
            if (i >= 0) {
                return new Pair(price, mainCollectionAv.get(i));
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        //добавляем все со старой цены если ничего нового по позиции не было
        mainCollectionAv.stream().peek(price -> {
            int i = mainCollectionNew.indexOf(price);
            if (!(i >= 0)) {
                priceList.add(price);
            }
        }).collect(Collectors.toList());

        for (int i = 0; i < collect2.size(); i++) {
            Pair pair = collect2.get(i);
            Price key = (Price) pair.getKey();
            Price value = (Price) pair.getValue();
            if (key.getValue() == value.getValue()) {
                List<LocalDateTime> begin = List.of(key.getBegin(), value.getBegin());
                LocalDateTime localDateTime1Begin = begin.stream().min((x, y) -> x.compareTo(y)).get();
                List<LocalDateTime> end = List.of(key.getEnd(), value.getEnd());
                LocalDateTime localDateTimeEnd = end.stream().max((x, y) -> x.compareTo(y)).get();
                Price price = new Price(key.getId(), key.getProductCode(), key.getNumber(),
                        key.getDepart(), localDateTime1Begin, localDateTimeEnd, key.getValue());
                priceList.add(price);
            } else {
                priceList.add(key);
                if (key.getBegin().isAfter(value.getBegin())) {
                    Price price = new Price(value.getId(), value.getProductCode(), value.getNumber(),
                            value.getDepart(), value.getBegin(), key.getBegin(), value.getValue());
                    priceList.add(price);
                }
                if (value.getEnd().isAfter(key.getEnd())) {
                    Price price = new Price(value.getId(), value.getProductCode(), value.getNumber(),
                            value.getDepart(), key.getEnd(), value.getEnd(), value.getValue());
                    priceList.add(price);
                }
            }
        }
        return priceList;
    }
}
