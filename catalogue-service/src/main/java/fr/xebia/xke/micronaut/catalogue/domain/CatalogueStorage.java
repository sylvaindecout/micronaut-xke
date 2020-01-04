package fr.xebia.xke.micronaut.catalogue.domain;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface CatalogueStorage {

    Flowable<Article> findAll();

    Maybe<Article> find(final ArticleReference reference);

    void save(Article article);

}
