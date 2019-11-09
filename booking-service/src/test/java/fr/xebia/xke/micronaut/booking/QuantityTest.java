package fr.xebia.xke.micronaut.booking;

import fr.xebia.xke.micronaut.booking.domain.Quantity;
import org.junit.jupiter.api.Test;

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
    void should_fail_to_subtract_greater_quantity() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                new Quantity(12L).subtract(new Quantity(13L)));
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
    void should_fail_to_check_if_quantity_is_greater_than_null() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Quantity(12L).isGreaterThan(null));
    }

}
