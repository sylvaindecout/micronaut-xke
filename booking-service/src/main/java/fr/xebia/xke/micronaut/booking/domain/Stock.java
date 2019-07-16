package fr.xebia.xke.micronaut.booking.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

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

    public Stock add(@NonNull final Quantity quantity) {
        return Stock.builder()
                .article(this.article)
                .quantity(this.quantity.add(quantity))
                .build();
    }

}
