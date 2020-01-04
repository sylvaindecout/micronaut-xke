package fr.xebia.xke.micronaut.catalogue.domain;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class CatalogueService {

    private final CatalogueStorage catalogueStorage;

    CatalogueService(final CatalogueStorage catalogueStorage) {
        this.catalogueStorage = catalogueStorage;
    }

    public List<Article> getCatalogue() {
        return catalogueStorage.findAll();
    }

    public Optional<Article> getArticle(final ArticleReference articleReference) {
        return catalogueStorage.find(articleReference);
    }

    public void addOrUpdate(final ArticleReference articleReference, final Price price) {
        catalogueStorage.save(Article.builder()
                .reference(articleReference)
                .referencePrice(price)
                .build());
    }

}
