package fr.xebia.xke.micronaut.pricing.domain;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.LongRange;

import static fr.xebia.xke.micronaut.pricing.domain.Discount.noDiscount;
import static fr.xebia.xke.micronaut.pricing.domain.Discount.percentage;
import static fr.xebia.xke.micronaut.pricing.domain.DiscountResolver.resolveDiscountForQuantity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DiscountResolverTest {

    @Property
    void should_fail_to_resolve_discount_for_null_quantity() {
        assertThatNullPointerException()
                .isThrownBy(() -> resolveDiscountForQuantity(null));
    }

    @Property
    void should_not_offer_discount_for_articles_that_are_almost_out_of_stock(@ForAll @LongRange(max = 4) Quantity availableQuantity) {
        assertThat(resolveDiscountForQuantity(availableQuantity))
                .isEqualTo(noDiscount());
    }

    @Property
    void should_not_offer_a_little_discount_for_articles_that_are_in_little_quantity(@ForAll @LongRange(min = 5, max = 9) Quantity availableQuantity) {
        assertThat(resolveDiscountForQuantity(availableQuantity))
                .isEqualTo(percentage(10));
    }

    @Property
    void should_not_offer_a_big_discount_for_articles_that_are_in_large_quantity(@ForAll @LongRange(min = 10) Quantity availableQuantity) {
        assertThat(resolveDiscountForQuantity(availableQuantity))
                .isEqualTo(percentage(20));
    }

}
