package fr.xebia.xke.micronaut.pricing.domain;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class PricingService {

    private final CatalogueClient catalogueClient;
    private final BookingClient bookingClient;

    PricingService(final CatalogueClient catalogueClient, final BookingClient bookingClient) {
        this.catalogueClient = catalogueClient;
        this.bookingClient = bookingClient;
    }

    public Flowable<Price> computePrice(final ArticleReference articleReference) {
        final Price referencePrice = findArticle(articleReference).getReferencePrice();
        return getAvailableQuantity(articleReference)
                .filter(Quantity::isGreaterThanZero)
                .map(DiscountResolver::resolveDiscountForQuantity)
                .map(referencePrice::apply);
    }

    private Article findArticle(final ArticleReference articleReference) {
        return Optional.of(catalogueClient.getArticle(articleReference.getValue()))
                .map(Maybe::blockingGet)
                .orElseThrow(() -> new UnknownArticleException(articleReference));
    }

    private Flowable<Quantity> getAvailableQuantity(final ArticleReference articleReference) {
        return bookingClient.getStock(articleReference.getValue())
                .map(Stock::getQuantity);
    }

}
