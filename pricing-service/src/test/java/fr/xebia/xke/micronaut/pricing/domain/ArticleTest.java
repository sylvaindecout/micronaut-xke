package fr.xebia.xke.micronaut.pricing.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static fr.xebia.xke.micronaut.pricing.domain.Price.euros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@MicronautTest
class ArticleTest {

    @Inject
    private ObjectMapper mapper;

    @Test
    void should_parse_JSON() throws IOException {
        final ObjectReader reader = mapper.reader().forType(Article.class);
        final String json = "{\"reference\":\"BOOK30004\",\"referencePrice\":1299}";

        assertThat(reader.<Article>readValue(json)).isEqualTo(Article.builder()
                .reference(new ArticleReference("BOOK30004"))
                .referencePrice(euros(12.99))
                .build());
    }

    @Test
    void should_fail_to_parse_JSON_if_reference_is_missing() {
        final ObjectReader reader = mapper.reader().forType(Article.class);
        final String json = "{\"price\":1299}";

        assertThatExceptionOfType(JsonProcessingException.class)
                .isThrownBy(() -> reader.readValue(json))
                .withMessageContaining("reference");
    }

    @Test
    void should_fail_to_parse_JSON_if_reference_price_is_missing() {
        final ObjectReader reader = mapper.reader().forType(Article.class);
        final String json = "{\"reference\":\"BOOK30004\"}";

        assertThatExceptionOfType(JsonProcessingException.class)
                .isThrownBy(() -> reader.readValue(json))
                .withMessageContaining("referencePrice");
    }

}
