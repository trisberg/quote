package functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SpringBootTest
class CloudFunctionApplicationTests {

    @MockBean
    StockQuoteService mockQuoteService;

    @Autowired
    Function<Message<Map<String, List<Object>>>, Map<String, List<Object>>> func;

    @Test
    void testGetQuote() {
        Mockito.when(mockQuoteService.getQuote("aapl")).thenReturn(
                Collections.singletonMap("AAPL", Collections.singletonList(BigDecimal.valueOf(155.74))));

        Message<Map<String, List<Object>>> message =
                MessageBuilder.withPayload(Collections.singletonMap("aapl", Collections.singletonList(null)))
                .build();
        var result = func.apply(message);
        assertEquals("AAPL", result.keySet().iterator().next());
        assertEquals(BigDecimal.valueOf(155.74), result.get("AAPL").get(0));

        Mockito.verify(mockQuoteService).getQuote("aapl");
    }

    @Test
    void testPriceNotFound() {
        Mockito.when(mockQuoteService.getQuote("xyz")).thenReturn(
                Collections.singletonMap("XYZ", Collections.singletonList("No price for symbol found!")));

        Message<Map<String, List<Object>>> message =
                MessageBuilder.withPayload(Collections.singletonMap("xyz", Collections.singletonList(null)))
                        .build();
        var result = func.apply(message);
        assertEquals("XYZ", result.keySet().iterator().next());
        assertEquals("No price for symbol found!", result.get("XYZ").get(0));

        Mockito.verify(mockQuoteService).getQuote("xyz");
    }

    @Test
    void testSymbolNotFound() {
        Mockito.when(mockQuoteService.getQuote("0")).thenReturn(
                Collections.singletonMap("0", Collections.singletonList("Symbol not found!")));

        Message<Map<String, List<Object>>> message =
                MessageBuilder.withPayload(Collections.singletonMap("0", Collections.singletonList(null)))
                        .build();
        var result = func.apply(message);
        assertEquals("0", result.keySet().iterator().next());
        assertEquals("Symbol not found!", result.get("0").get(0));

        Mockito.verify(mockQuoteService).getQuote("0");
    }
}
