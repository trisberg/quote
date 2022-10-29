package functions;

import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YahooStockQuoteService implements StockQuoteService {

    @Override
    public Map<String, List<Object>> getQuote(String symbol) {
        Map<String, List<Object>> result = new HashMap<>();
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
        return result;
    }

}
