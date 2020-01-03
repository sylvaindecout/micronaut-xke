package fr.xebia.xke.micronaut.booking.simulator;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.domains.AbstractDomainContextBase;
import net.jqwik.api.domains.Domain;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static net.jqwik.api.Arbitraries.of;
import static net.jqwik.api.Arbitraries.strings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class TargetStockTest {

    private static final List<String> REGISTERED_ARTICLE_REFERENCES = asList("BookNumber1", "BookNumber2", "BookNumber3", "MagicianHat");

    @Test
    void should_expose_target_quantities_per_article_as_a_map() {
        assertThat(TargetStock.asMap())
                .containsEntry("BookNumber1", 100L)
                .containsEntry("BookNumber2", 10L)
                .containsEntry("BookNumber3", 10L)
                .containsEntry("MagicianHat", 1L);
    }

    @Test
    void should_get_random_article_from_target_stock() {
        assertThat(TargetStock.getRandomArticle())
                .isIn(REGISTERED_ARTICLE_REFERENCES);
    }

    @Property
    void should_fail_to_tell_if_it_contains_article_with_null_reference() {
        assertThatNullPointerException()
                .isThrownBy(() -> TargetStock.containsArticle(null));
    }

    @Property
    @Domain(UnregisteredArticles.class)
    void should_know_that_it_does_not_contain_articles_that_it_does_not(@ForAll String reference) {
        assertThat(TargetStock.containsArticle(reference)).isFalse();
    }

    @Property
    @Domain(RegisteredArticles.class)
    void should_know_that_it_contains_articles_that_it_does(@ForAll String reference) {
        assertThat(TargetStock.containsArticle(reference)).isTrue();
    }

    private static final class UnregisteredArticles extends AbstractDomainContextBase {
        UnregisteredArticles() {
            registerArbitrary(String.class, strings().all()
                    .filter(reference -> !REGISTERED_ARTICLE_REFERENCES.contains(reference)));
        }
    }

    private static final class RegisteredArticles extends AbstractDomainContextBase {
        RegisteredArticles() {
            registerArbitrary(String.class, of(REGISTERED_ARTICLE_REFERENCES));
        }
    }

}
