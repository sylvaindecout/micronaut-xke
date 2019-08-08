package fr.xebia.xke.micronaut.pricing.domain;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static fr.xebia.xke.micronaut.pricing.domain.Price.euros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@MicronautTest
class PricingServiceTest {

    private static final ArticleReference AVAILABLE_ARTICLE = new ArticleReference("BookNumber1");
    private static final ArticleReference UNKNOWN_ARTICLE = new ArticleReference("UNKNOWN");
    private static final ArticleReference HUNDRED_EURO_ARTICLE_REFERENCE = new ArticleReference("100_EUROS");

    @Inject
    PricingService service;

    @Inject
    BookingClient bookingClient;

    @Test
    void should_fail_to_compute_price_for_unknown_article() {
        assertThatExceptionOfType(UnknownArticleException.class)
                .isThrownBy(() -> service.computePrice(UNKNOWN_ARTICLE))
                .withMessage("Article not found: %s", UNKNOWN_ARTICLE);
    }

    @Test
    void should_fail_to_compute_price_for_unavailable_article() {
        given(bookingClient.getStock(AVAILABLE_ARTICLE.getValue()))
                .willReturn(Stock.of(AVAILABLE_ARTICLE, 0L));

        assertThat(service.computePrice(AVAILABLE_ARTICLE))
                .isEmpty();
    }

    @Test
    void should_apply_20_percent_discount_to_article_with_10_available_items() {
        given(bookingClient.getStock(HUNDRED_EURO_ARTICLE_REFERENCE.getValue()))
                .willReturn(Stock.of(HUNDRED_EURO_ARTICLE_REFERENCE, 10L));

        assertThat(service.computePrice(HUNDRED_EURO_ARTICLE_REFERENCE))
                .contains(euros(80.00));
    }

    @Test
    void should_apply_10_percent_discount_to_article_with_9_available_items() {
        given(bookingClient.getStock(HUNDRED_EURO_ARTICLE_REFERENCE.getValue()))
                .willReturn(Stock.of(HUNDRED_EURO_ARTICLE_REFERENCE, 9L));

        assertThat(service.computePrice(HUNDRED_EURO_ARTICLE_REFERENCE))
                .contains(euros(90.00));
    }

    @Test
    void should_apply_10_percent_discount_to_article_with_5_available_items() {
        given(bookingClient.getStock(HUNDRED_EURO_ARTICLE_REFERENCE.getValue()))
                .willReturn(Stock.of(HUNDRED_EURO_ARTICLE_REFERENCE, 5L));

        assertThat(service.computePrice(HUNDRED_EURO_ARTICLE_REFERENCE))
                .contains(euros(90.00));
    }

    @Test
    void should_apply_no_discount_to_article_with_4_available_items() {
        given(bookingClient.getStock(HUNDRED_EURO_ARTICLE_REFERENCE.getValue()))
                .willReturn(Stock.of(HUNDRED_EURO_ARTICLE_REFERENCE, 4L));

        assertThat(service.computePrice(HUNDRED_EURO_ARTICLE_REFERENCE))
                .contains(euros(100.00));
    }

    @MockBean(CatalogueClient.class)
    CatalogueClient mockCatalogue() {
        final CatalogueClient client = mock(CatalogueClient.class);
        given(client.getCatalogue()).willReturn(Catalogue.of(
                Article.builder()
                        .reference(AVAILABLE_ARTICLE)
                        .referencePrice(euros(12.99))
                        .build(),
                Article.builder()
                        .reference(HUNDRED_EURO_ARTICLE_REFERENCE)
                        .referencePrice(euros(100.00))
                        .build()
        ));
        return client;
    }

    @MockBean(BookingClient.class)
    BookingClient mockBooking() {
        return mock(BookingClient.class);
    }

}
