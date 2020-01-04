package fr.xebia.xke.micronaut.catalogue.database;

import fr.xebia.xke.micronaut.catalogue.domain.Article;
import fr.xebia.xke.micronaut.catalogue.domain.ArticleReference;
import fr.xebia.xke.micronaut.catalogue.domain.CatalogueStorage;
import io.micronaut.spring.tx.annotation.Transactional;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

import static io.reactivex.BackpressureStrategy.BUFFER;
import static java.util.stream.Collectors.toList;

@Singleton
public class CatalogueDatabaseAdapter implements CatalogueStorage {

    private final CatalogueRepository catalogueRepository;

    CatalogueDatabaseAdapter(final CatalogueRepository catalogueRepository) {
        this.catalogueRepository = catalogueRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Flowable<Article> findAll() {
        final Iterable<Article> articles = catalogueRepository.findAll().stream()
                .map(ArticleEntity::toDomain)
                .collect(toList());
        return Observable.fromIterable(articles).toFlowable(BUFFER);
    }

    @Override
    @Transactional(readOnly = true)
    public Maybe<Article> find(final ArticleReference reference) {
        return catalogueRepository.find(reference.getValue())
                .map(ArticleEntity::toDomain)
                .map(Maybe::just)
                .orElseGet(Maybe::empty);
    }

    @Override
    @Transactional
    public void save(@NotNull Article article) {
        final ArticleEntity articleEntity = ArticleEntity.builder()
                .articleReference(article.getReference().getValue())
                .price(article.getReferencePrice().getValueAsCents())
                .build();
        catalogueRepository.save(articleEntity);
    }

}
