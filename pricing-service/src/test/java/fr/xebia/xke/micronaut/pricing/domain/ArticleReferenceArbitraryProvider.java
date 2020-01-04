package fr.xebia.xke.micronaut.pricing.domain;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;

import java.util.Set;

import static java.util.Collections.singleton;
import static net.jqwik.api.Arbitraries.strings;

public final class ArticleReferenceArbitraryProvider implements ArbitraryProvider {

    @Override
    public boolean canProvideFor(final TypeUsage targetType) {
        return targetType.isOfType(ArticleReference.class);
    }

    @Override
    public Set<Arbitrary<?>> provideFor(final TypeUsage targetType, final SubtypeProvider subtypeProvider) {
        return singleton(strings().ofMinLength(5).map(ArticleReference::new));
    }

}
