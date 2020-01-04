package fr.xebia.xke.micronaut.catalogue.domain;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

import javax.inject.Singleton;

@Singleton
public class CatalogueService {

    private final CatalogueStorage catalogueStorage;

    CatalogueService(final CatalogueStorage catalogueStorage) {
        this.catalogueStorage = catalogueStorage;
    }

    public Flowable<Article> getCatalogue() {
        return catalogueStorage.findAll();
    }

    public Maybe<Article> getArticle(final ArticleReference articleReference) {
        return catalogueStorage.find(articleReference);
    }

    public void addOrUpdate(final ArticleReference articleReference, final Price price) {
        catalogueStorage.save(Article.builder()
                .reference(articleReference)
                .referencePrice(price)
                .build());
    }

}
