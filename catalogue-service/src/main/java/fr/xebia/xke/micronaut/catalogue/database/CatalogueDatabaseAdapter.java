package fr.xebia.xke.micronaut.catalogue.database;

import fr.xebia.xke.micronaut.catalogue.domain.Article;
import fr.xebia.xke.micronaut.catalogue.domain.CatalogueStorage;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Singleton
public class CatalogueDatabaseAdapter implements CatalogueStorage {

    private final CatalogueRepository catalogueRepository;

    CatalogueDatabaseAdapter(final CatalogueRepository catalogueRepository) {
        this.catalogueRepository = catalogueRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> findAll() {
        return catalogueRepository.findAll().stream()
                .map(ArticleEntity::toDomain)
                .collect(toList());
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
