package fr.xebia.xke.micronaut.catalogue.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static fr.xebia.xke.micronaut.catalogue.domain.Price.euros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@MicronautTest
class CatalogueTest {

    @Inject
    private ObjectMapper mapper;

    @Test
    void should_generate_JSON() throws IOException {
        final ObjectWriter writer = mapper.writer().forType(Catalogue.class);
        final Catalogue object = Catalogue.of(
                Article.builder()
                        .reference(new ArticleReference("BOOK30004"))
                        .referencePrice(euros(12.99))
                        .build(),
                Article.builder()
                        .reference(new ArticleReference("BOOK10006"))
                        .referencePrice(euros(99.99))
                        .build()
        );
        final String expectedJson = "[{\"reference\":\"BOOK30004\",\"referencePrice\":1299},{\"reference\":\"BOOK10006\",\"referencePrice\":9999}]";

        assertThat(writer.writeValueAsString(object)).isEqualTo(expectedJson);
    }

    @Test
    void should_parse_JSON() throws IOException {
        final ObjectReader reader = mapper.reader().forType(Catalogue.class);
        final String json = "[{\"reference\":\"BOOK30004\",\"referencePrice\":1299},{\"reference\":\"BOOK10006\",\"referencePrice\":9999}]";

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

}
