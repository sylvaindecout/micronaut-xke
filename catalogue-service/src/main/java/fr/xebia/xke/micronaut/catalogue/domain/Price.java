package fr.xebia.xke.micronaut.catalogue.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

@Value
public final class Price {

    @JsonProperty("referencePrice")
    Long valueAsCents;

    private Price(@NonNull final Long valueAsCents) {
        checkArgument(valueAsCents >= 0,
                "Invalid value: %s (expected: positive value)", valueAsCents);
        this.valueAsCents = valueAsCents;
    }

    public static Price euros(final double valueAsEuros) {
        checkArgument(valueAsEuros >= 0,
                "Invalid value: %s (expected: positive value)", valueAsEuros);
        return new Price(toCents(valueAsEuros));
    }

    public static Price cents(final long valueAsCents){
        return new Price(valueAsCents);
    }

    private static long toCents(final double valueAsEuros) {
        final double valueAsCents = valueAsEuros * 100;
        return (long) valueAsCents;
    }

    private double asEuros() {
        return valueAsCents / 100.;
    }

    @Override
    public String toString() {
        return format("%.2f€", asEuros());
    }

}
