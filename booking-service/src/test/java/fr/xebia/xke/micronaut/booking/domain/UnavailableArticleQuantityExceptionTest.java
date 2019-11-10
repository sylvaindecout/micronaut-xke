package fr.xebia.xke.micronaut.booking.domain;


import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class UnavailableArticleQuantityExceptionTest {

    @Property
    void should_initialize_exception_with_explicit_message(@ForAll ArticleReference requestedArticle,
                                                           @ForAll Quantity requestedQuantity,
                                                           @ForAll Quantity availableQuantity) {
        final Exception exception = new UnavailableArticleQuantityException(requestedArticle, requestedQuantity, availableQuantity);
        assertThat(exception.getMessage())
                .isEqualTo("Requested quantity (%s) is unavailable for article '%s' (available: %s)",
                        requestedQuantity, requestedArticle, availableQuantity);
    }

    @Property
    void should_fail_to_initialize_exception_if_requested_article_is_missing(@ForAll Quantity requestedQuantity,
                                                                             @ForAll Quantity availableQuantity) {
        assertThatNullPointerException().isThrownBy(() ->
                new UnavailableArticleQuantityException(null, requestedQuantity, availableQuantity));
    }

    @Property
    void should_fail_to_initialize_exception_if_requested_quantity_is_missing(@ForAll ArticleReference requestedArticle,
                                                                              @ForAll Quantity availableQuantity) {
        assertThatNullPointerException().isThrownBy(() ->
                new UnavailableArticleQuantityException(requestedArticle, null, availableQuantity));
    }

    @Property
    void should_fail_to_initialize_exception_if_available_quantity_is_missing(@ForAll ArticleReference requestedArticle,
                                                                              @ForAll Quantity requestedQuantity) {
        assertThatNullPointerException().isThrownBy(() ->
                new UnavailableArticleQuantityException(requestedArticle, requestedQuantity, null));
    }

}
