package fr.xebia.xke.micronaut.catalogue.domain;

import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.LongRange;
import net.jqwik.api.constraints.Negative;

import java.util.regex.Pattern;

import static fr.xebia.xke.micronaut.catalogue.domain.Price.cents;
import static fr.xebia.xke.micronaut.catalogue.domain.Price.euros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PriceTest {

    private static final Pattern EUROS_WITH_2_DECIMALS = Pattern.compile("[0-9]*\\.[0-9][0-9]€");

    @Property
    void should_initialize_from_value_as_cents(@ForAll @LongRange long valueAsCents) {
        assertThat(cents(valueAsCents).getValueAsCents()).isEqualTo(valueAsCents);
    }

    @Property
    void should_fail_to_initialize_from_negative_value_in_cents(@ForAll @Negative long valueAsCents) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> cents(valueAsCents))
                .withMessage("Invalid value: %s (expected: positive value)", valueAsCents);
    }

    @Example
    void should_initialize_from_value_as_euros() {
        assertThat(euros(12.99).getValueAsCents()).isEqualTo(1299);
    }

    @Example
    void should_initialize_from_value_in_euros_by_truncating_digits_over_2() {
        assertThat(euros(12.999).getValueAsCents()).isEqualTo(1299);
    }

    @Property
    void should_fail_to_initialize_from_negative_value_in_euros(@ForAll @Negative double valueAsEuros) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> euros(valueAsEuros))
                .withMessage("Invalid value: %s (expected: positive value)", valueAsEuros);
    }

    @Example
    void should_print_value_as_euros() {
        assertThat(euros(12.99).toString()).isEqualTo("12.99€");
    }

    @Property
    void should_print_value_with_2_decimals(@ForAll Price price) {
        assertThat(price.toString()).matches(EUROS_WITH_2_DECIMALS);
    }

}
