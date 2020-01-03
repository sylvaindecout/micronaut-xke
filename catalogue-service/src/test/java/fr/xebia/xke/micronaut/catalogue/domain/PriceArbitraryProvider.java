package fr.xebia.xke.micronaut.catalogue.domain;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;

import java.util.Set;

import static java.util.Collections.singleton;
import static net.jqwik.api.Arbitraries.longs;

public final class PriceArbitraryProvider implements ArbitraryProvider {

    public boolean canProvideFor(final TypeUsage targetType) {
        return targetType.isOfType(Price.class);
    }

    @Override
    public Set<Arbitrary<?>> provideFor(final TypeUsage targetType, final SubtypeProvider subtypeProvider) {
        return singleton(
                longs().greaterOrEqual(0).map(Price::cents)
        );
    }

}
