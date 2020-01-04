package fr.xebia.xke.micronaut.pricing.domain;

import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.LongRange;
import net.jqwik.api.constraints.Negative;

import static org.assertj.core.api.Assertions.*;

class QuantityTest {

    @Property
    void should_initialize(@ForAll @LongRange long value) {
        assertThat(new Quantity(value).getValue()).isEqualTo(value);
    }

    @Property
    void should_fail_to_initialize_from_null_value() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Quantity(null));
    }

    @Property
    void should_fail_to_initialize_from_negative_value(@ForAll @Negative long value) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Quantity(value))
                .withMessage("Quantity cannot be negative (input value: %s)", value);
    }

    @Property
    void should_be_greater_than_quantity_with_lesser_value(@ForAll Quantity greaterQuantity, @ForAll Quantity lesserQuantity) {
        Assume.that(greaterQuantity.getValue() > lesserQuantity.getValue());

        assertThat(greaterQuantity.isGreaterThan(lesserQuantity))
                .isTrue();
    }

    @Property
    void should_not_be_greater_than_quantity_with_equal_value(@ForAll @LongRange long value) {
        assertThat(new Quantity(value).isGreaterThan(new Quantity(value)))
                .isFalse();
    }

    @Property
    void should_not_be_greater_than_quantity_with_greater_value(@ForAll Quantity greaterQuantity, @ForAll Quantity lesserQuantity) {
        Assume.that(greaterQuantity.getValue() > lesserQuantity.getValue());

        assertThat(lesserQuantity.isGreaterThan(greaterQuantity))
                .isFalse();
    }

    @Property
    void should_fail_to_check_if_it_is_greater_than_null_quantity(@ForAll Quantity quantity) {
        assertThatNullPointerException()
                .isThrownBy(() -> assertThat(quantity.isGreaterThan(null)));
    }

    @Property
    void should_be_greater_than_zero_if_value_is_positive(@ForAll Quantity quantity) {
        Assume.that(quantity.getValue() > 0);

        assertThat(quantity.isGreaterThanZero())
                .isTrue();
    }

    @Property
    void should_not_be_greater_than_zero_if_value_is_zero() {
        assertThat(new Quantity(0L).isGreaterThanZero())
                .isFalse();
    }

}
