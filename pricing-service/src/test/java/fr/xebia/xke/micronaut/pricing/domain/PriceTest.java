package fr.xebia.xke.micronaut.pricing.domain;

import org.junit.jupiter.api.Test;

import static fr.xebia.xke.micronaut.pricing.domain.Discount.percentage;
import static fr.xebia.xke.micronaut.pricing.domain.Price.euros;
import static org.assertj.core.api.Assertions.*;

class PriceTest {

    @Test
    void should_initialize_from_value_as_euros() {
        assertThat(euros(12.99).getValueAsCents()).isEqualTo(1299);
    }

    @Test
    void should_initialize_from_value_in_euros_by_truncating_digits_over_2() {
        assertThat(euros(12.999).getValueAsCents()).isEqualTo(1299);
    }

    @Test
    void should_fail_to_initialize_from_negative_value_in_euros() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> euros(-12.99))
                .withMessage("Invalid value: %s (expected: positive value)", -12.99);
    }

    @Test
    void should_print_value_as_euros() {
        assertThat(euros(12.99).toString()).isEqualTo("12.99€");
    }

    @Test
    void should_apply_discount() {
        assertThat(euros(10.).apply(percentage(10))).isEqualTo(euros(9.));
    }

    @Test
    void should_fail_to_apply_null_discount() {
        assertThatNullPointerException()
                .isThrownBy(() -> assertThat(euros(10.).apply(null)));
    }

}
