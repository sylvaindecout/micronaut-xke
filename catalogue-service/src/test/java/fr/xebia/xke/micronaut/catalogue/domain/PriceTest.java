package fr.xebia.xke.micronaut.catalogue.domain;

import org.junit.jupiter.api.Test;

import static fr.xebia.xke.micronaut.catalogue.domain.Price.euros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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

}
