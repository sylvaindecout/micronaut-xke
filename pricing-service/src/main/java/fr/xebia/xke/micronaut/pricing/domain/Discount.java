package fr.xebia.xke.micronaut.pricing.domain;

import com.google.common.collect.Range;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public final class Discount {

    private static final Range<Integer> ACCEPTABLE_RANGE = Range.closed(0, 100);
    private static final Discount NO_DISCOUNT = percentage(0);

    int percentage;

    private Discount(final int percentage){
        checkArgument(ACCEPTABLE_RANGE.contains(percentage),
                "Invalid value: %s (expected: value in %s)", percentage, ACCEPTABLE_RANGE);
        this.percentage = percentage;
    }

    public static Discount percentage(final int percentage) {
        return new Discount(percentage);
    }

    public static Discount noDiscount() {
        return NO_DISCOUNT;
    }

}
