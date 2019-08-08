package fr.xebia.xke.micronaut.pricing.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class QuantityTest {

    @Test
    void should_initialize() {
        assertThat(new Quantity(1L).getValue()).isEqualTo(1L);
    }

    @Test
    void should_fail_to_initialize_from_null_value() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Quantity(null));
    }

    @Test
    void should_fail_to_initialize_from_negative_value() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Quantity(-1L))
                .withMessage("Quantity cannot be negative (input value: %s)", -1L);
    }

    @Test
    void should_be_greater_than_quantity_with_lesser_value() {
        assertThat(new Quantity(5L).isGreaterThan(new Quantity(4L)))
                .isTrue();
    }

    @Test
    void should_not_be_greater_than_quantity_with_equal_value() {
        assertThat(new Quantity(5L).isGreaterThan(new Quantity(5L)))
                .isFalse();
    }

    @Test
    void should_not_be_greater_than_quantity_with_greater_value() {
        assertThat(new Quantity(5L).isGreaterThan(new Quantity(6L)))
                .isFalse();
    }

    @Test
    void should_fail_to_check_if_it_is_greater_than_null_quantity() {
        assertThatNullPointerException()
                .isThrownBy(() -> assertThat(new Quantity(5L).isGreaterThan(null)));
    }

    @Test
    void should_be_greater_than_zero_if_value_is_positive() {
        assertThat(new Quantity(1L).isGreaterThanZero())
                .isTrue();
    }

    @Test
    void should_not_be_greater_than_zero_if_value_is_zero() {
        assertThat(new Quantity(0L).isGreaterThanZero())
                .isFalse();
    }

}
