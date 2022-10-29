package functions;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class QuoteServiceTests {

    @MockBean
    StockQuoteService mockQuoteService;

    @Autowired
    StockQuoteService mockQuoteServiceFromContext;

    @Test
    void testGetQuote() {
        Mockito.when(mockQuoteService.getQuote("aapl")).thenReturn(
                Collections.singletonMap("AAPL", Collections.singletonList(BigDecimal.valueOf(155.74))));

        var result = mockQuoteServiceFromContext.getQuote("aapl");
        assertEquals("AAPL", result.keySet().iterator().next());
        assertEquals(BigDecimal.valueOf(155.74), result.get("AAPL").get(0));

        Mockito.verify(mockQuoteService).getQuote("aapl");
    }

}
