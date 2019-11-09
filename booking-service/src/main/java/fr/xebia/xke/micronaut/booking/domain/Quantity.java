package fr.xebia.xke.micronaut.booking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public final class Quantity {

    public static final Quantity ERROR = new Quantity(0L, true);

    @JsonProperty("quantity")
    Long value;
    @JsonIgnore
    boolean error;

    private Quantity(@NonNull final Long value, final boolean error) {
        checkArgument(value >= 0,
                "Quantity cannot be negative (input value: %s)", value);
        this.value = value;
        this.error = error;
    }

    public Quantity(@NonNull final Long value) {
        this(value, false);
    }

    public Quantity subtract(@NonNull final Quantity quantity) {
        return this.error ? ERROR
                : quantity.error ? ERROR
                : quantity.isGreaterThan(this) ? ERROR
                : new Quantity(this.value - quantity.value);
    }

    public boolean isGreaterThan(final Quantity quantity) {
        return !this.error
                && !quantity.error
                && this.value > quantity.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
