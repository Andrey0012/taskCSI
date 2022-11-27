import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServicePrice {

    public List<Price> resultMethod(List<Price> mainCollectionAv, List<Price> mainCollectionNew) {
        List<Price> priceListResult = new ArrayList<>();

        List<Pair<Price, Price>> collect2 = mainCollectionNew.stream().map(price -> {
            int i = mainCollectionAv.indexOf(price);
            if (i >= 0) {
                return Pair.of(price, mainCollectionAv.get(i));
            }
            priceListResult.add(price);
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        mainCollectionAv.forEach(price -> {
            int i = mainCollectionNew.indexOf(price);
            if (i < 0) {
                priceListResult.add(price);
            }
        });

        for (Pair<Price, Price> pair : collect2) {
            Price newValue = pair.getKey();
            Price availableValue = pair.getValue();
            if (newValue.getValue() == availableValue.getValue()) {
                Price price = valueEqual(newValue, availableValue);
                priceListResult.add(price);
            } else {
                priceListResult.add(newValue);
                if (newValue.getBegin().isAfter(availableValue.getBegin())) {
                    Price price = new Price(availableValue.getId(), availableValue.getProductCode(), availableValue.getNumber(),
                            availableValue.getDepart(), availableValue.getBegin(), newValue.getBegin(), availableValue.getValue());
                    priceListResult.add(price);
                }
                if (availableValue.getEnd().isAfter(newValue.getEnd())) {
                    Price price = new Price(availableValue.getId(), availableValue.getProductCode(), availableValue.getNumber(),
                            availableValue.getDepart(), newValue.getEnd(), availableValue.getEnd(), availableValue.getValue());
                    priceListResult.add(price);
                }
            }
        }
        return priceListResult;
    }

    public static Price valueEqual(Price key, Price value) {
        LocalDateTime begin = Stream.of(key.getBegin(), value.getBegin()).min(LocalDateTime::compareTo).get();
        LocalDateTime end = Stream.of(key.getEnd(), value.getEnd()).max(LocalDateTime::compareTo).get();
        return new Price(key.getId(), key.getProductCode(), key.getNumber(),
                key.getDepart(), begin, end, key.getValue());
    }
}
