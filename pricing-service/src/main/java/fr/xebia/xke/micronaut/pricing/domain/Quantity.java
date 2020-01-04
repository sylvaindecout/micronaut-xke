package fr.xebia.xke.micronaut.pricing.domain;

import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public final class Quantity {

    private static final Quantity ZERO = new Quantity(0L);

    Long value;

    public Quantity(@NonNull final Long value) {
        checkArgument(value >= 0,
                "Quantity cannot be negative (input value: %s)", value);
        this.value = value;
    }

    boolean isGreaterThan(@NonNull final Quantity quantity) {
        return this.value > quantity.value;
    }

    boolean isGreaterThanZero() {
        return this.isGreaterThan(ZERO);
    }

}
