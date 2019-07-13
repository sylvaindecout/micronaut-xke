package fr.xebia.xke.micronaut.booking;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Stock.StockBuilder.class)
public final class Stock {

    @NonNull String articleReference;
    @NonNull Integer quantity; //FIXME: replace with a VO and JsonUnwrapped it!

    @JsonPOJOBuilder(withPrefix = "")
    public static final class StockBuilder {}

}
