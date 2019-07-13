package fr.xebia.xke.micronaut.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public final class Quantity {

    @JsonProperty("quantity")
    Long value;

    public Quantity(@NonNull final Long value) {
        checkArgument(value >= 0,
                "Quantity cannot be negative (input value: %s)", value);
        this.value = value;
    }

}
