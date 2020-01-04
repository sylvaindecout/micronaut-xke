package fr.xebia.xke.micronaut.pricing.domain;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.LongRange;

import static fr.xebia.xke.micronaut.pricing.domain.Discount.noDiscount;
import static fr.xebia.xke.micronaut.pricing.domain.Discount.percentage;
import static io.reactivex.Maybe.empty;
import static io.reactivex.Maybe.just;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PricingServiceTest {

    private final BookingClient bookingClient = mock(BookingClient.class);
    private final CatalogueClient catalogueClient = mock(CatalogueClient.class);
    private final PricingService service = new PricingService(catalogueClient, bookingClient);

    @Property
    void should_fail_to_compute_price_for_unknown_article(@ForAll ArticleReference articleReference) {
        given(catalogueClient.getArticle(articleReference.getValue())).willReturn(empty());

        assertThatExceptionOfType(UnknownArticleException.class)
                .isThrownBy(() -> service.computePrice(articleReference))
                .withMessage("Article not found: %s", articleReference);
    }

    @Property
    void should_fail_to_compute_price_for_unavailable_article(@ForAll Article article) {
        final ArticleReference articleReference = article.getReference();

        given(catalogueClient.getArticle(articleReference.getValue())).willReturn(just(article));
        given(bookingClient.getStock(articleReference.getValue()))
                .willReturn(just(Stock.of(articleReference, 0L)));

        assertThat(service.computePrice(articleReference).blockingGet())
                .isNull();
    }

    @Property
    void should_apply_20_percent_discount_to_article_with_10_available_items(@ForAll Article article,
                                                                             @ForAll @LongRange(min = 10) long availableQuantity) {
        final ArticleReference articleReference = article.getReference();

        given(catalogueClient.getArticle(articleReference.getValue())).willReturn(just(article));
        given(bookingClient.getStock(articleReference.getValue()))
                .willReturn(just(Stock.of(articleReference, availableQuantity)));

        assertThat(service.computePrice(articleReference).blockingGet())
                .isEqualTo(article.getReferencePrice().apply(percentage(20)));
    }

    @Property
    void should_apply_10_percent_discount_to_article_with_5_available_items(@ForAll Article article,
                                                                            @ForAll @LongRange(min = 5, max = 9) long availableQuantity) {
        final ArticleReference articleReference = article.getReference();

        given(catalogueClient.getArticle(articleReference.getValue())).willReturn(just(article));
        given(bookingClient.getStock(articleReference.getValue()))
                .willReturn(just(Stock.of(articleReference, availableQuantity)));

        assertThat(service.computePrice(articleReference).blockingGet())
                .isEqualTo(article.getReferencePrice().apply(percentage(10)));
    }

    @Property
    void should_apply_no_discount_to_article_with_4_available_items(@ForAll Article article,
                                                                    @ForAll @LongRange(min = 1, max = 4) long availableQuantity) {
        final ArticleReference articleReference = article.getReference();

        given(catalogueClient.getArticle(articleReference.getValue())).willReturn(just(article));
        given(bookingClient.getStock(articleReference.getValue()))
                .willReturn(just(Stock.of(articleReference, availableQuantity)));

        assertThat(service.computePrice(articleReference).blockingGet())
                .isEqualTo(article.getReferencePrice().apply(noDiscount()));
    }

}
