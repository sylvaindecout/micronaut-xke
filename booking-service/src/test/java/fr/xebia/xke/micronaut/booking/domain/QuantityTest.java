package fr.xebia.xke.micronaut.booking.domain;

import org.junit.jupiter.api.Test;

import static fr.xebia.xke.micronaut.booking.domain.Quantity.ERROR;
import static org.assertj.core.api.Assertions.*;

class QuantityTest {

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
    void should_initialize_from_zero() {
        assertThat(new Quantity(0L).getValue()).isEqualTo(0L);
    }

    @Test
    void should_initialize_from_positive_value() {
        assertThat(new Quantity(1L).getValue()).isEqualTo(1L);
    }

    @Test
    void should_fail_to_subtract_null_quantity() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Quantity(12L).subtract(null));
    }

    @Test
    void should_fail_to_subtract_from_ERROR_quantity() {
        assertThat(ERROR.subtract(new Quantity(0L)))
                .isEqualTo(ERROR);
    }

    @Test
    void should_fail_to_subtract_ERROR_quantity() {
        assertThat(new Quantity(10L).subtract(ERROR))
                .isEqualTo(ERROR);
    }

    @Test
    void should_fail_to_subtract_greater_quantity() {
        assertThat(new Quantity(12L).subtract(new Quantity(13L)))
                .isEqualTo(ERROR);
    }

    @Test
    void should_subtract_lesser_quantity() {
        assertThat(new Quantity(12L).subtract(new Quantity(1L)))
                .isEqualTo(new Quantity(11L));
    }

    @Test
    void should_subtract_equal_quantity() {
        assertThat(new Quantity(1L).subtract(new Quantity(1L)))
                .isEqualTo(new Quantity(0L));
    }

    @Test
    void should_consider_as_greater_than_a_lesser_quantity() {
        assertThat(new Quantity(12L).isGreaterThan(new Quantity(11L)))
                .isTrue();
    }

    @Test
    void should_not_consider_as_greater_than_a_greater_quantity() {
        assertThat(new Quantity(12L).isGreaterThan(new Quantity(13L)))
                .isFalse();
    }

    @Test
    void should_not_consider_as_greater_than_an_equal_quantity() {
        assertThat(new Quantity(12L).isGreaterThan(new Quantity(12L)))
                .isFalse();
    }

    @Test
    void should_not_consider_as_greater_than_ERROR_quantity() {
        assertThat(new Quantity(12L).isGreaterThan(ERROR))
                .isFalse();
    }

    @Test
    void should_not_consider_ERROR_quantity_as_greater_a_valid_quantity() {
        assertThat(ERROR.isGreaterThan(new Quantity(0L)))
                .isFalse();
    }

    @Test
    void should_fail_to_check_if_quantity_is_greater_than_null() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Quantity(12L).isGreaterThan(null));
    }

}
