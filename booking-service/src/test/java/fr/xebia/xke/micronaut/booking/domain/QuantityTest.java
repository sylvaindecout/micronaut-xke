package fr.xebia.xke.micronaut.booking.domain;

import net.jqwik.api.Assume;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.Negative;
import net.jqwik.api.constraints.Positive;

import static fr.xebia.xke.micronaut.booking.domain.Quantity.ERROR;
import static fr.xebia.xke.micronaut.booking.domain.Quantity.ZERO;
import static org.assertj.core.api.Assertions.*;

class QuantityTest {

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
    void should_initialize_from_zero() {
        assertThat(new Quantity(0L).getValue()).isEqualTo(0L);
    }

    @Property
    void should_initialize_from_positive_value(@ForAll @Positive long value) {
        assertThat(new Quantity(value).getValue()).isEqualTo(value);
    }

    @Property
    void should_fail_to_subtract_null_quantity(@ForAll Quantity initialQuantity) {
        assertThatNullPointerException()
                .isThrownBy(() -> initialQuantity.subtract(null));
    }

    @Property
    void should_fail_to_subtract_from_ERROR_quantity(@ForAll Quantity subtractedQuantity) {
        assertThat(ERROR.subtract(subtractedQuantity))
                .isEqualTo(ERROR);
    }

    @Property
    void should_fail_to_subtract_ERROR_quantity(@ForAll Quantity initialQuantity) {
        assertThat(initialQuantity.subtract(ERROR))
                .isEqualTo(ERROR);
    }

    @Property
    void should_fail_to_subtract_greater_quantity(@ForAll Quantity initialQuantity, @ForAll Quantity subtractedQuantity) {
        Assume.that(subtractedQuantity.getValue() > initialQuantity.getValue());
        assertThat(initialQuantity.subtract(subtractedQuantity))
                .isEqualTo(ERROR);
    }

    @Example
    void should_subtract_lesser_quantity() {
        assertThat(new Quantity(12L).subtract(new Quantity(1L)))
                .isEqualTo(new Quantity(11L));
    }

    @Property
    void should_subtract_lesser_quantity(@ForAll Quantity initialQuantity, @ForAll Quantity subtractedQuantity) {
        Assume.that(subtractedQuantity.getValue() < initialQuantity.getValue());
        assertThat(initialQuantity.subtract(subtractedQuantity))
                .isNotEqualTo(ERROR);
    }

    @Property
    void should_subtract_equal_quantity(@ForAll Quantity initialQuantity) {
        final Quantity equalQuantity = new Quantity(initialQuantity.getValue());
        assertThat(initialQuantity.subtract(equalQuantity))
                .isEqualTo(ZERO);
    }

    @Property
    void should_consider_as_greater_than_a_lesser_quantity(@ForAll Quantity quantity, @ForAll Quantity lesserQuantity) {
        Assume.that(lesserQuantity.getValue() < quantity.getValue());
        assertThat(quantity.isGreaterThan(lesserQuantity))
                .isTrue();
    }

    @Property
    void should_not_consider_as_greater_than_a_greater_quantity(@ForAll Quantity quantity, @ForAll Quantity greaterQuantity) {
        Assume.that(greaterQuantity.getValue() > quantity.getValue());
        assertThat(quantity.isGreaterThan(greaterQuantity))
                .isFalse();
    }

    @Property
    void should_not_consider_as_greater_than_an_equal_quantity(@ForAll Quantity quantity) {
        final Quantity equalQuantity = new Quantity(quantity.getValue());
        assertThat(quantity.isGreaterThan(equalQuantity))
                .isFalse();
    }

    @Property
    void should_not_consider_as_greater_than_ERROR_quantity(@ForAll Quantity initialQuantity) {
        assertThat(initialQuantity.isGreaterThan(ERROR))
                .isFalse();
    }

    @Property
    void should_not_consider_ERROR_quantity_as_greater_a_valid_quantity(@ForAll Quantity subtractedQuantity) {
        assertThat(ERROR.isGreaterThan(subtractedQuantity))
                .isFalse();
    }

    @Property
    void should_fail_to_check_if_quantity_is_greater_than_null(@ForAll Quantity initialQuantity) {
        assertThatNullPointerException()
                .isThrownBy(() -> initialQuantity.isGreaterThan(null));
    }

}
