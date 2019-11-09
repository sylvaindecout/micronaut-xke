package fr.xebia.xke.micronaut.booking.domain;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class UnavailableArticleQuantityExceptionTest {

    private static final ArticleReference VALID_ARTICLE = new ArticleReference("BOOK00001");
    private static final Quantity VALID_QUANTITY = new Quantity(13L);

    @Test
    void should_initialize_exception_with_explicit_message() {
        final Exception exception = new UnavailableArticleQuantityException(new ArticleReference("BOOK00001"), new Quantity(12L), new Quantity(2L));
        assertThat(exception.getMessage())
                .isEqualTo("Requested quantity (%s) is unavailable for article '%s' (available: %s)", 12, "BOOK00001", 2);
    }

    @Test
    void should_fail_to_initialize_exception_if_requested_article_is_missing() {
        assertThatNullPointerException().isThrownBy(() ->
                new UnavailableArticleQuantityException(null, VALID_QUANTITY, VALID_QUANTITY));
    }

    @Test
    void should_fail_to_initialize_exception_if_requested_quantity_is_missing() {
        assertThatNullPointerException().isThrownBy(() ->
                new UnavailableArticleQuantityException(VALID_ARTICLE, null, VALID_QUANTITY));
    }

    @Test
    void should_fail_to_initialize_exception_if_available_quantity_is_missing() {
        assertThatNullPointerException().isThrownBy(() ->
                new UnavailableArticleQuantityException(VALID_ARTICLE, VALID_QUANTITY, null));
    }

}
