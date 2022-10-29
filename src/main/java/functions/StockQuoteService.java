package functions;

import java.util.List;
import java.util.Map;

public interface StockQuoteService {

    Map<String, List<Object>> getQuote(String symbol);

}
