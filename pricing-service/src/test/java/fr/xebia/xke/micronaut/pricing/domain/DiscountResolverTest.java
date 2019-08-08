package fr.xebia.xke.micronaut.pricing.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.LongStream;

import static fr.xebia.xke.micronaut.pricing.domain.Discount.noDiscount;
import static fr.xebia.xke.micronaut.pricing.domain.Discount.percentage;
import static fr.xebia.xke.micronaut.pricing.domain.DiscountResolver.resolveDiscountForQuantity;
import static java.util.stream.LongStream.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DiscountResolverTest {

    @Test
    void should_fail_to_resolve_discount_for_null_quantity() {
        assertThatNullPointerException()
                .isThrownBy(() -> resolveDiscountForQuantity(null));
    }

    @ParameterizedTest
    @MethodSource("quantitiesWithNoDiscount")
    void should_not_offer_discount_for_articles_that_are_almost_out_of_stock(final long availableQuantity) {
        assertThat(resolveDiscountForQuantity(new Quantity(availableQuantity)))
                .isEqualTo(noDiscount());
    }

    static LongStream quantitiesWithNoDiscount() {
        return rangeClosed(0, 4);
    }

    @ParameterizedTest
    @MethodSource("quantitiesWithLittleDiscount")
    void should_not_offer_a_little_discount_for_articles_that_are_in_little_quantity(final long availableQuantity) {
        assertThat(resolveDiscountForQuantity(new Quantity(availableQuantity)))
                .isEqualTo(percentage(10));
    }

    static LongStream quantitiesWithLittleDiscount() {
        return rangeClosed(5, 9);
    }

    @ParameterizedTest
    @MethodSource("quantitiesWithBigDiscount")
    void should_not_offer_a_big_discount_for_articles_that_are_in_large_quantity(final long availableQuantity) {
        assertThat(resolveDiscountForQuantity(new Quantity(availableQuantity)))
                .isEqualTo(percentage(20));
    }

    static LongStream quantitiesWithBigDiscount() {
        return concat(
                rangeClosed(10, 20),
                of(100, 1000, 10_000, 100_000)
        );
    }

}
