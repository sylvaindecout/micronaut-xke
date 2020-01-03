package fr.xebia.xke.micronaut.pricing.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.micronaut.test.annotation.MicronautTest;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.LongRange;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

@MicronautTest
class StockTest {

    @Inject
    private ObjectMapper mapper;

    @Test
    void should_parse_JSON() throws IOException {
        final ObjectReader reader = mapper.reader().forType(Stock.class);
        final String json = "{\"article\":\"MagicianHat\",\"quantity\":2}";

        assertThat(reader.<Stock>readValue(json))
                .isEqualTo(Stock.of(new ArticleReference("MagicianHat"), 2));
    }

    @Test
    void should_fail_to_parse_JSON_if_article_is_missing() {
        final ObjectReader reader = mapper.reader().forType(Stock.class);
        final String json = "{\"quantity\":2}";

        assertThatExceptionOfType(JsonProcessingException.class)
                .isThrownBy(() -> reader.readValue(json))
                .withMessageStartingWith("Mandatory field is missing: article");
    }

    @Test
    void should_fail_to_parse_JSON_if_quantity_is_missing() {
        final ObjectReader reader = mapper.reader().forType(Stock.class);
        final String json = "{\"article\":\"MagicianHat\"}";

        assertThatExceptionOfType(JsonProcessingException.class)
                .isThrownBy(() -> reader.readValue(json))
                .withMessageStartingWith("Mandatory field is missing: quantity");
    }

    @Property
    void should_initialize_article_reference(@ForAll ArticleReference article, @ForAll Quantity quantity) {
        final Stock stock = Stock.of(article, quantity.getValue());
        assertThat(stock.getArticle()).isEqualTo(article);
    }

    @Property
    void should_initialize_quantity(@ForAll ArticleReference article, @ForAll Quantity quantity) {
        final Stock stock = Stock.of(article, quantity.getValue());
        assertThat(stock.getQuantity()).isEqualTo(quantity);
    }

    @Property
    void should_fail_to_initialize_from_null_value(@ForAll @LongRange long quantity) {
        assertThatNullPointerException()
                .isThrownBy(() -> Stock.of(null, quantity));
    }

}
