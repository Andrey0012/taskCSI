import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException, NullPointerException {

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        mapper.registerModule(new JavaTimeModule());

        //старые цены
        List<Price> mainCollectionAv = mapper.readValue(new File("src/main/resources/Available_Prices.json"), new TypeReference<>() {});

        //новые цены
        List<Price> mainCollectionNew = mapper.readValue(new File("src/main/resources/New_Prices.json"), new TypeReference<>() {});

        // итоговые цены
        List<Price> priceList = new ArrayList<>();

        List<Pair> collect2 = mainCollectionNew.stream().map(price -> {
            int i = mainCollectionAv.indexOf(price);
            if (!(i >= 0)) {
                priceList.add(price);
            }
            if (i >= 0) {
                //         return List.of(price, mainCollectionNew.get(i));
                return new Pair(price, mainCollectionAv.get(i));
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

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
//                if (key.getBegin().isAfter(value.getBegin())) {
//                    if (key.getEnd().isAfter(value.getEnd())) {
//                        Price price = new Price(key.getId(), key.getProductCode(), key.getNumber(),
//                                key.getDepart(), value.getBegin(), key.getEnd(), key.getValue());
//                        priceList.add(price);
//                    }
//                } else {
//
//                }
            }
            else {
                priceList.add(key);
                if (key.getBegin().isAfter(value.getBegin())) {
                    Price price = new Price(value.getId(), value.getProductCode(), value.getNumber(),
                            value.getDepart(), value.getBegin(),key.getBegin(), value.getValue());
                    priceList.add(price);
                }
                if (value.getEnd().isAfter(key.getEnd())){
                    Price price = new Price(value.getId(), value.getProductCode(), value.getNumber(),
                            value.getDepart(), key.getEnd(), value.getEnd(), value.getValue());
                    priceList.add(price);
                }
            }
        }
        System.out.println(priceList);
    }
}
