package fr.xebia.xke.micronaut.pricing.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.micronaut.test.annotation.MicronautTest;
import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static fr.xebia.xke.micronaut.pricing.domain.Price.euros;
import static org.assertj.core.api.Assertions.*;

@MicronautTest
class CatalogueTest {

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

    @Property
    void should_find_article(@ForAll Catalogue catalogue) {
        final Article article = anArticleFrom(catalogue);

        assertThat(catalogue.find(article.getReference()))
                .contains(article);
    }

    @Property
    void should_not_find_unknown_article(@ForAll Catalogue catalogue, @ForAll ArticleReference reference) {
        Assume.that(!catalogue.getArticles().containsKey(reference));

        assertThat(catalogue.find(reference))
                .isEmpty();
    }

    @Property
    void should_fail_to_find_article_for_null_reference(@ForAll Catalogue catalogue) {
        assertThatNullPointerException()
                .isThrownBy(() -> assertThat(catalogue.find(null)));
    }

    @Property
    void should_expose_price_for_article(@ForAll Catalogue catalogue){
        final Article article = anArticleFrom(catalogue);

        assertThat(catalogue.getReferencePriceFor(article.getReference()))
                .isEqualTo(article.getReferencePrice());
    }

    @Property
    void should_fail_to_expose_price_for_unknown_article(@ForAll Catalogue catalogue, @ForAll ArticleReference reference){
        Assume.that(!catalogue.getArticles().containsKey(reference));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> catalogue.getReferencePriceFor(reference));
    }

    private static Article anArticleFrom(final Catalogue catalogue) {
        Assume.that(!catalogue.getArticles().isEmpty());
        return catalogue.getArticles().values().iterator().next();
    }
}
