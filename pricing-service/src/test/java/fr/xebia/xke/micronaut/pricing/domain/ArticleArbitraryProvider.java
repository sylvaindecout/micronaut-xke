package fr.xebia.xke.micronaut.pricing.domain;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;

import java.util.Set;

import static java.util.Collections.singleton;
import static net.jqwik.api.Arbitraries.defaultFor;
import static net.jqwik.api.Combinators.withBuilder;

public final class ArticleArbitraryProvider implements ArbitraryProvider {

    @Override
    public boolean canProvideFor(final TypeUsage targetType) {
        return targetType.isOfType(Article.class);
    }

    @Override
    public Set<Arbitrary<?>> provideFor(final TypeUsage targetType, final SubtypeProvider subtypeProvider) {
        return singleton(
                withBuilder(Article::builder)
                        .use(defaultFor(ArticleReference.class)).in(Article.ArticleBuilder::reference)
                        .use(defaultFor(Price.class)).in(Article.ArticleBuilder::referencePrice)
                        .build(Article.ArticleBuilder::build)
        );
    }

}
