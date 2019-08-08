package fr.xebia.xke.micronaut.pricing.domain;

import org.junit.jupiter.api.Test;

import static fr.xebia.xke.micronaut.pricing.domain.Discount.percentage;
import static fr.xebia.xke.micronaut.pricing.domain.Discount.noDiscount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DiscountTest {

    @Test
    void should_initialize_from_percentage(){
        assertThat(percentage(12).getPercentage()).isEqualTo(12);
    }

    @Test
    void should_fail_to_initialize_from_negative_percentage(){
        assertThatIllegalArgumentException()
                .isThrownBy(() -> percentage(-1))
                .withMessage("Invalid value: %s (expected: value in [0..100])", -1);
    }

    @Test
    void should_fail_to_initialize_from_percentage_greater_than_100(){
        assertThatIllegalArgumentException()
                .isThrownBy(() -> percentage(101))
                .withMessage("Invalid value: %s (expected: value in [0..100])", 101);
    }

    @Test
    void should_initialize_from_zero(){
        assertThat(noDiscount().getPercentage()).isZero();
    }

}
