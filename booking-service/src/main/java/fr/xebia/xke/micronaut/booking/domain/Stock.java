package fr.xebia.xke.micronaut.booking.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

@Value
@Builder
@JsonDeserialize(builder = Stock.StockBuilder.class)
public final class Stock {

    @JsonUnwrapped @NonNull ArticleReference article;
    @JsonUnwrapped @NonNull Quantity quantity;

    public static Stock of(final ArticleReference article, final long quantity){
        return new Stock(article, new Quantity(quantity));
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class StockBuilder {}

    public Stock subtract(@NonNull final Quantity quantity) {
        return Stock.builder()
                .article(this.article)
                .quantity(this.quantity.subtract(checkAvailability(quantity)))
                .build();
    }

    private Quantity checkAvailability(final Quantity quantity) {
        checkArgument(quantity != Quantity.ERROR, "Invalid input quantity");
        if (quantity.isGreaterThan(this.quantity)) {
            throw new UnavailableArticleQuantityException(this.article, quantity, this.quantity);
        }
        return quantity;
    }

    @Override
    public String toString() {
        return format("%s '%s' items", this.quantity, this.article);
    }
}
