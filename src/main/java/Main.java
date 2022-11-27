import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        mapper.registerModule(new JavaTimeModule());

        List<Price> availablePrice = mapper.readValue(new File("src/main/resources/Available_Prices.json"), new TypeReference<>() {
        });

        List<Price> newPrice = mapper.readValue(new File("src/main/resources/New_Prices.json"), new TypeReference<>() {
        });
        ServicePrice servicePrice= new ServicePrice();
        List<Price> resultPrice = servicePrice.resultMethod(availablePrice, newPrice);
        for (Price price : resultPrice) {
            System.out.println(mapper.writeValueAsString(price));
        }
    }
}
