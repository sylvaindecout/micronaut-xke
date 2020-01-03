package fr.xebia.xke.micronaut.booking.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.micronaut.test.annotation.MicronautTest;
import net.jqwik.api.Assume;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

@MicronautTest
class StockTest {

    @Inject
    private ObjectMapper mapper;

    @Test
    void should_map_to_JSON_with_no_information_loss() throws IOException {
        final ObjectWriter writer = mapper.writer().forType(Stock.class);
        final ObjectReader reader = mapper.reader().forType(Stock.class);
        final Stock inputObject = Stock.of(new ArticleReference("BOOK30004"), 12);

        final String json = writer.writeValueAsString(inputObject);
        final Stock outputObject = reader.readValue(json);

        assertThat(outputObject).isEqualTo(inputObject);
    }

    @Example
    void should_subtract_quantity(@ForAll ArticleReference articleReference) {
        final Stock initialStock = Stock.of(articleReference, 12);
        final Quantity decrement = new Quantity(1L);
        final Stock decrementedStock = Stock.of(articleReference, 11);

        assertThat(initialStock.subtract(decrement))
                .isEqualTo(decrementedStock);
    }

    @Property
    void should_subtract_available_quantity(@ForAll Stock initialStock,
                                            @ForAll Quantity decrement) {
        Assume.that(initialStock.getQuantity().getValue() >= decrement.getValue());
        assertThat(initialStock.subtract(decrement)).isNotNull();
    }

    @Property
    void should_fail_to_subtract_unavailable_quantity(@ForAll Stock initialStock,
                                                      @ForAll Quantity decrement) {
        Assume.that(initialStock.getQuantity().getValue() < decrement.getValue());
        assertThatExceptionOfType(UnavailableArticleQuantityException.class)
                .isThrownBy(() -> initialStock.subtract(decrement))
                .withMessage("Requested quantity (%s) is unavailable for article '%s' (available: %s)",
                        decrement, initialStock.getArticle(), initialStock.getQuantity());
    }

    @Property
    void should_fail_to_subtract_null_quantity(@ForAll Stock initialStock) {
        assertThatNullPointerException()
                .isThrownBy(() -> initialStock.subtract(null));
    }

    @Property
    void should_fail_to_subtract_ERROR_quantity(@ForAll Stock initialStock) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> initialStock.subtract(Quantity.ERROR))
                .withMessage("Invalid input quantity");
    }
}
