package fr.xebia.xke.micronaut.booking;

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

}
