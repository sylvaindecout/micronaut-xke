package fr.xebia.xke.micronaut.booking.domain;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;

import java.util.Set;

import static java.util.Collections.singleton;
import static net.jqwik.api.Arbitraries.forType;
import static net.jqwik.api.Combinators.combine;

public final class StockArbitraryProvider implements ArbitraryProvider {

    @Override
    public boolean canProvideFor(final TypeUsage targetType) {
        return targetType.isOfType(Stock.class);
    }

    @Override
    public Set<Arbitrary<?>> provideFor(final TypeUsage targetType, final SubtypeProvider subtypeProvider) {
        return singleton(
                combine(forType(ArticleReference.class), forType(Quantity.class))
                        .as(((Combinators.F2<ArticleReference, Quantity, Object>) Stock::new))
        );
    }

}
