package fr.xebia.xke.micronaut.catalogue.domain;

import java.util.List;
import java.util.Optional;

public interface CatalogueStorage {

    List<Article> findAll();

    Optional<Article> find(final ArticleReference reference);

    void save(Article article);

}
