package fr.xebia.xke.micronaut.booking.domain;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.configurators.ArbitraryConfiguratorBase;

public final class QuantityArbitraryConfigurator extends ArbitraryConfiguratorBase {

    public Arbitrary<Quantity> configure(final Arbitrary<Quantity> arbitrary, final AtMost below) {
        return arbitrary
                .filter(quantity -> quantity.getValue() <= below.value());
    }

    public Arbitrary<Quantity> configure(final Arbitrary<Quantity> arbitrary, final AtLeast above) {
        return arbitrary
                .filter(quantity -> quantity.getValue() >= above.value());
    }

}
