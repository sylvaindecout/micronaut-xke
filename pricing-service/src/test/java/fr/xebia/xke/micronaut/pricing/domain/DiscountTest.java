package fr.xebia.xke.micronaut.pricing.domain;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Negative;

import static fr.xebia.xke.micronaut.pricing.domain.Discount.noDiscount;
import static fr.xebia.xke.micronaut.pricing.domain.Discount.percentage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DiscountTest {

    @Property
    void should_initialize_from_percentage(@ForAll @IntRange(max = 100) int percentage) {
        assertThat(percentage(percentage).getPercentage()).isEqualTo(percentage);
    }

    @Property
    void should_fail_to_initialize_from_negative_percentage(@ForAll @Negative int percentage) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> percentage(percentage))
                .withMessage("Invalid value: %s (expected: value in [0..100])", percentage);
    }

    @Property
    void should_fail_to_initialize_from_percentage_greater_than_100(@ForAll @IntRange(min = 101) int percentage) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> percentage(percentage))
                .withMessage("Invalid value: %s (expected: value in [0..100])", percentage);
    }

    @Property
    void should_initialize_from_zero() {
        assertThat(noDiscount().getPercentage()).isZero();
    }

}
