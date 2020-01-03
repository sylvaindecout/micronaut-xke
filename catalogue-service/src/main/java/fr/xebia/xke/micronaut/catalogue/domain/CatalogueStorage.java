package fr.xebia.xke.micronaut.catalogue.domain;

import java.util.List;

public interface CatalogueStorage {

    List<Article> findAll();

    void save(Article article);

}
