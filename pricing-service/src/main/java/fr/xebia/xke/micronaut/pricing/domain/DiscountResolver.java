package fr.xebia.xke.micronaut.pricing.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

import static fr.xebia.xke.micronaut.pricing.domain.Discount.noDiscount;
import static fr.xebia.xke.micronaut.pricing.domain.Discount.percentage;
import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
final class DiscountResolver {

    private static final Discount LITTLE_DISCOUNT = percentage(10);
    private static final Discount BIG_DISCOUNT = percentage(20);
    private static final Quantity THRESHOLD_FOR_BIG_DISCOUNT = new Quantity(9L);
    private static final Quantity THRESHOLD_FOR_LITTLE_DISCOUNT = new Quantity(4L);

    Quantity availableQuantity;

    static Discount resolveDiscountForQuantity(final Quantity availableQuantity) {
        return new DiscountResolver(availableQuantity).resolveDiscount();
    }

    private Discount resolveDiscount() {
        return availableQuantity.isGreaterThan(THRESHOLD_FOR_BIG_DISCOUNT) ? BIG_DISCOUNT
                : availableQuantity.isGreaterThan(THRESHOLD_FOR_LITTLE_DISCOUNT) ? LITTLE_DISCOUNT
                : noDiscount();
    }

}
