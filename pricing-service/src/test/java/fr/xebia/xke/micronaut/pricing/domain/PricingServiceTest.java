package fr.xebia.xke.micronaut.pricing.domain;

import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.LongRange;

import static fr.xebia.xke.micronaut.pricing.domain.Discount.noDiscount;
import static fr.xebia.xke.micronaut.pricing.domain.Discount.percentage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PricingServiceTest {

    private final BookingClient bookingClient = mock(BookingClient.class);
    private final CatalogueClient catalogueClient = mock(CatalogueClient.class);
    private final PricingService service = new PricingService(catalogueClient, bookingClient);

    @Property
    void should_fail_to_compute_price_for_unknown_article(@ForAll ArticleReference article, @ForAll Catalogue catalogue) {
        Assume.that(!catalogue.getArticles().containsKey(article));

        given(catalogueClient.getCatalogue()).willReturn(catalogue);

        assertThatExceptionOfType(UnknownArticleException.class)
                .isThrownBy(() -> service.computePrice(article))
                .withMessage("Article not found: %s", article);
    }

    @Property
    void should_fail_to_compute_price_for_unavailable_article(@ForAll Catalogue catalogue) {
        final ArticleReference availableArticle = anArticleFrom(catalogue);

        given(catalogueClient.getCatalogue()).willReturn(catalogue);
        given(bookingClient.getStock(availableArticle.getValue()))
                .willReturn(Stock.of(availableArticle, 0L));

        assertThat(service.computePrice(availableArticle))
                .isEmpty();
    }

    @Property
    void should_apply_20_percent_discount_to_article_with_10_available_items(@ForAll Catalogue catalogue,
                                                                             @ForAll @LongRange(min = 10) long availableQuantity) {
        final ArticleReference availableArticle = anArticleFrom(catalogue);

        given(catalogueClient.getCatalogue()).willReturn(catalogue);
        given(bookingClient.getStock(availableArticle.getValue()))
                .willReturn(Stock.of(availableArticle, availableQuantity));

        assertThat(service.computePrice(availableArticle))
                .contains(catalogue.getReferencePriceFor(availableArticle).apply(percentage(20)));
    }

    @Property
    void should_apply_10_percent_discount_to_article_with_5_available_items(@ForAll Catalogue catalogue,
                                                                            @ForAll @LongRange(min = 5, max = 9) long availableQuantity) {
        final ArticleReference availableArticle = anArticleFrom(catalogue);

        given(catalogueClient.getCatalogue()).willReturn(catalogue);
        given(bookingClient.getStock(availableArticle.getValue()))
                .willReturn(Stock.of(availableArticle, availableQuantity));

        assertThat(service.computePrice(availableArticle))
                .contains(catalogue.getReferencePriceFor(availableArticle).apply(percentage(10)));
    }

    @Property
    void should_apply_no_discount_to_article_with_4_available_items(@ForAll Catalogue catalogue,
                                                                    @ForAll @LongRange(min = 1, max = 4) long availableQuantity) {
        final ArticleReference availableArticle = anArticleFrom(catalogue);

        given(catalogueClient.getCatalogue()).willReturn(catalogue);
        given(bookingClient.getStock(availableArticle.getValue()))
                .willReturn(Stock.of(availableArticle, availableQuantity));

        assertThat(service.computePrice(availableArticle))
                .contains(catalogue.getReferencePriceFor(availableArticle).apply(noDiscount()));
    }

    private static ArticleReference anArticleFrom(final Catalogue catalogue) {
        Assume.that(!catalogue.getArticles().isEmpty());
        return catalogue.getArticles().keySet().iterator().next();
    }
}
