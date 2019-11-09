package fr.xebia.xke.micronaut.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.xebia.xke.micronaut.booking.domain.ArticleReference;
import fr.xebia.xke.micronaut.booking.domain.Quantity;
import fr.xebia.xke.micronaut.booking.domain.Stock;
import fr.xebia.xke.micronaut.booking.domain.UnavailableArticleQuantityException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

@MicronautTest
class StockTest {

    private static final ArticleReference ARTICLE = new ArticleReference("BOOK30004");

    @Inject
    private ObjectMapper mapper;

    @Test
    void should_map_to_JSON_with_no_information_loss() throws IOException {
        final ObjectWriter writer = mapper.writer().forType(Stock.class);
        final ObjectReader reader = mapper.reader().forType(Stock.class);
        final Stock inputObject = Stock.of(ARTICLE, 12);

        final String json = writer.writeValueAsString(inputObject);
        final Stock outputObject = reader.readValue(json);

        assertThat(outputObject).isEqualTo(inputObject);
    }

    @Test
    void should_subtract_quantity() {
        final Stock initialStock = Stock.of(ARTICLE, 12);
        final Quantity decrement = new Quantity(1L);
        final Stock decrementedStock = Stock.of(ARTICLE, 11);

        assertThat(initialStock.subtract(decrement))
                .isEqualTo(decrementedStock);
    }

    @Test
    void should_fail_to_subtract_unavailable_quantity() {
        final Stock initialStock = Stock.of(ARTICLE, 12);
        final Quantity decrement = new Quantity(13L);

        assertThatExceptionOfType(UnavailableArticleQuantityException.class)
                .isThrownBy(() -> initialStock.subtract(decrement))
                .withMessage("Requested quantity (13) is unavailable for article 'BOOK30004' (available: 12)");
    }

    @Test
    void should_fail_to_subtract_null_quantity() {
        final Stock initialStock = Stock.of(ARTICLE, 12);

        assertThatNullPointerException()
                .isThrownBy(() -> initialStock.subtract(null));
    }

    @Test
    void should_fail_to_subtract_ERROR_quantity() {
        final Stock initialStock = Stock.of(ARTICLE, 12);
        final Quantity decrement = Quantity.ERROR;

        assertThatIllegalArgumentException()
                .isThrownBy(() -> initialStock.subtract(decrement))
                .withMessage("Invalid input quantity");
    }
}
