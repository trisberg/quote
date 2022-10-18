package functions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

@SpringBootApplication
public class CloudFunctionApplication {

  public static void main(String[] args) {
    SpringApplication.run(CloudFunctionApplication.class, args);
  }

  @Bean
  public Function<Message<Map<String, List<Object>>>, Map<String, List<Object>>> quote() {
    return (inputMessage) -> {
      Map<String, List<Object>> result = new HashMap<>();
      if (!inputMessage.getPayload().isEmpty()) {
        String symbol = inputMessage.getPayload().keySet().iterator().next();
        try {
          Stock stock = YahooFinance.get(symbol);
          if (stock != null) {
            var quote = stock.getQuote();
            if (quote == null || quote.getPrice() == null) {
              result.put(symbol.toUpperCase(), Collections.singletonList("No price for symbol found!"));
            } else {
              result.put(symbol.toUpperCase(), Collections.singletonList(quote.getPrice()));
            }
          } else {
            result.put(symbol.toUpperCase(), Collections.singletonList("Symbol not found!"));
          }
        } catch(IOException e) {
          result.put("ERROR", Collections.singletonList(e.toString()));
        }
      }
      return result;
    };
  }
}
