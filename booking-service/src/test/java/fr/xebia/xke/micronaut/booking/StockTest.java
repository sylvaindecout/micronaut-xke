package fr.xebia.xke.micronaut.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.xebia.xke.micronaut.booking.domain.ArticleReference;
import fr.xebia.xke.micronaut.booking.domain.Quantity;
import fr.xebia.xke.micronaut.booking.domain.Stock;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

//FIXME: disabling transactions is only a workaround to get it working until 1.2.0 is released (cf. https://github.com/micronaut-projects/micronaut-core/issues/1871)
@MicronautTest(transactional = false)
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
    void should_fail_to_add_null_quantity() {
        final Stock initialStock = Stock.of(ARTICLE, 12);

        assertThatNullPointerException()
                .isThrownBy(() -> initialStock.add(null));
    }

    @Test
    void should_add_quantity() {
        final Stock initialStock = Stock.of(ARTICLE, 12);
        final Quantity increment = new Quantity(1L);
        final Stock incrementedStock = Stock.of(ARTICLE, 13);

        assertThat(initialStock.add(increment))
                .isEqualTo(incrementedStock);
    }

}
