package fr.xebia.xke.micronaut.pricing.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.micronaut.test.annotation.MicronautTest;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static fr.xebia.xke.micronaut.pricing.domain.Price.euros;
import static org.assertj.core.api.Assertions.*;

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

    @Property
    void should_initialize_reference(@ForAll ArticleReference reference, @ForAll Price price) {
        final Article article = Article.builder()
                .reference(reference)
                .referencePrice(price)
                .build();
        assertThat(article.getReference()).isEqualTo(reference);
    }

    @Property
    void should_initialize_price(@ForAll ArticleReference reference, @ForAll Price price) {
        final Article article = Article.builder()
                .reference(reference)
                .referencePrice(price)
                .build();
        assertThat(article.getReferencePrice()).isEqualTo(price);
    }

    @Property
    void should_fail_to_initialize_from_null_reference(@ForAll Price price) {
        assertThatNullPointerException()
                .isThrownBy(() -> Article.builder()
                        .reference(null)
                        .referencePrice(price)
                        .build());
    }

    @Property
    void should_fail_to_initialize_from_null_price(@ForAll ArticleReference reference) {
        assertThatNullPointerException()
                .isThrownBy(() -> Article.builder()
                        .reference(reference)
                        .referencePrice(null)
                        .build());
    }

}
