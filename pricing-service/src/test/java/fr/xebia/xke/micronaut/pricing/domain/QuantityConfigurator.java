package fr.xebia.xke.micronaut.pricing.domain;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.configurators.ArbitraryConfiguratorBase;
import net.jqwik.api.constraints.LongRange;
import net.jqwik.api.providers.TypeUsage;

public final class QuantityConfigurator extends ArbitraryConfiguratorBase {

    @Override
    protected boolean acceptTargetType(final TypeUsage targetType) {
        return targetType.isAssignableFrom(Quantity.class);
    }

    public Arbitrary<Quantity> configure(final Arbitrary<Quantity> arbitrary, final LongRange annotation) {
        final Range range = new Range(annotation);
        return arbitrary
                .filter(range::contains);
    }

    private static final class Range {
        private final LongRange range;

        Range(final LongRange range) {
            this.range = range;
        }

        boolean contains(final Quantity quantity) {
            return quantity.getValue() >= range.min()
                    && quantity.getValue() <= range.max();
        }
    }
}
