package fr.xebia.xke.micronaut.booking.domain;

import lombok.NonNull;

import static java.lang.String.format;

public final class UnavailableArticleQuantityException extends RuntimeException {

    UnavailableArticleQuantityException(@NonNull final ArticleReference requestedArticle, @NonNull final Quantity requestedQuantity, @NonNull final Quantity availableQuantity) {
        super(format("Requested quantity (%s) is unavailable for article '%s' (available: %s)",
                requestedQuantity, requestedArticle, availableQuantity));
    }

}
