package fr.xebia.xke.micronaut.pricing.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static fr.xebia.xke.micronaut.pricing.domain.Price.euros;
import static org.assertj.core.api.Assertions.*;

@MicronautTest
class CatalogueTest {

    private static final Article ARTICLE_1 = Article.builder()
            .reference(new ArticleReference("12345"))
            .referencePrice(euros(19.99))
            .build();

    private final Catalogue catalogue = Catalogue.of(ARTICLE_1);

    @Inject
    private ObjectMapper mapper;

    @Test
    void should_parse_JSON() throws IOException {
        final ObjectReader reader = mapper.reader().forType(Catalogue.class);
        final String json = "[{\"reference\":\"BOOK30004\",\"referencePrice\":1299}, {\"reference\":\"BOOK10006\",\"referencePrice\":9999}]";

        assertThat(reader.<Catalogue>readValue(json)).isEqualTo(Catalogue.of(
                Article.builder()
                        .reference(new ArticleReference("BOOK30004"))
                        .referencePrice(euros(12.99))
                        .build(),
                Article.builder()
                        .reference(new ArticleReference("BOOK10006"))
                        .referencePrice(euros(99.99))
                        .build()
        ));
    }

    @Test
    void should_fail_to_parse_JSON_if_it_is_not_an_array() {
        final ObjectReader reader = mapper.reader().forType(Catalogue.class);
        final String json = "{\"reference\":\"BOOK30004\",\"price\":1299}";

        assertThatExceptionOfType(JsonProcessingException.class)
                .isThrownBy(() -> reader.readValue(json))
                .withMessageStartingWith("Expected: array");
    }

    @Test
    void should_find_article() {
        assertThat(catalogue.find(ARTICLE_1.getReference()))
                .contains(ARTICLE_1);
    }

    @Test
    void should_not_find_unknown_article() {
        assertThat(catalogue.find(new ArticleReference("UNKNOWN")))
                .isEmpty();
    }

    @Test
    void should_fail_to_find_article_for_null_reference() {
        assertThatNullPointerException()
                .isThrownBy(() -> assertThat(catalogue.find(null)));
    }

}
