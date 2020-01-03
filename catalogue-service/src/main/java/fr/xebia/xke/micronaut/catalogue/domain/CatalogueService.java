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

}
