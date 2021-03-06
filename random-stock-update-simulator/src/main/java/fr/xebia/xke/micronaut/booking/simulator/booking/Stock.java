package fr.xebia.xke.micronaut.booking.simulator.booking;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Value
@Builder
@AllArgsConstructor
@JsonDeserialize(builder = Stock.StockBuilder.class)
public class Stock {

    @NotBlank String article;
    @NotNull @PositiveOrZero Long quantity;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class StockBuilder {}

}
