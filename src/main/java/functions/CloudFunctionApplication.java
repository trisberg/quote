package functions;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  StockQuoteService stockQuoteService;
  @Bean
  public Function<Message<Map<String, List<Object>>>, Map<String, List<Object>>> quote() {
    return (inputMessage) -> {
      Map<String, List<Object>> result = new HashMap<>();
      if (!inputMessage.getPayload().isEmpty()) {
        String symbol = inputMessage.getPayload().keySet().iterator().next();
        result = stockQuoteService.getQuote(symbol);
      }
      return result;
    };
  }
}
