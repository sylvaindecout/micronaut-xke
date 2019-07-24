package fr.xebia.xke.micronaut.booking.simulator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class TargetStockTest {

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
                .isIn("BookNumber1", "BookNumber2", "BookNumber3", "MagicianHat");
    }

    @Test
    void should_fail_to_tell_if_it_contains_article_with_null_reference() {
        assertThatNullPointerException()
                .isThrownBy(() -> TargetStock.containsArticle(null));
    }

    @Test
    void should_know_that_it_does_not_contain_articles_that_it_does_not() {
        assertThat(TargetStock.containsArticle("MadHat")).isFalse();
    }

    @Test
    void should_know_that_it_contains_articles_that_it_does() {
        assertThat(TargetStock.containsArticle("MagicianHat")).isTrue();
    }

}
