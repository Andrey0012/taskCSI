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

        //старые цены
        List<Price> Av = mapper.readValue(new File("src/main/resources/Available_Prices.json"), new TypeReference<>() {
        });

        //новые цены
        List<Price> New = mapper.readValue(new File("src/main/resources/New_Prices.json"), new TypeReference<>() {
        });
        //итоговый список и вывод в консоль
        List<Price> result = ServicePrice.resultMethod(Av, New);
        for (int i = 0; i < result.size(); i++) {
            System.out.println(mapper.writeValueAsString(result.get(i)));
        }
    }
}
