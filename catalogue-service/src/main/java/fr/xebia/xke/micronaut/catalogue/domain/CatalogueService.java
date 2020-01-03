package fr.xebia.xke.micronaut.catalogue.domain;

import javax.inject.Singleton;

@Singleton
public class CatalogueService {

    private final CatalogueStorage catalogueStorage;

    CatalogueService(final CatalogueStorage catalogueStorage) {
        this.catalogueStorage = catalogueStorage;
    }

    public Catalogue getCatalogue() {
        return Catalogue.of(catalogueStorage.findAll());
    }

    public void addOrUpdate(final ArticleReference articleReference, final Price price) {
        catalogueStorage.save(Article.builder()
                .reference(articleReference)
                .referencePrice(price)
                .build());
    }

}
