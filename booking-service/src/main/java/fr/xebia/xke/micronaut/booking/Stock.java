package fr.xebia.xke.micronaut.booking;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Stock.StockBuilder.class)
public final class Stock {

    @JsonUnwrapped @NonNull ArticleReference article;
    @JsonUnwrapped @NonNull Quantity quantity;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class StockBuilder {}

}
