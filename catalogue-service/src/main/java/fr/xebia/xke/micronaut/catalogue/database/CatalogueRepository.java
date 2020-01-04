package fr.xebia.xke.micronaut.catalogue.database;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Singleton
class CatalogueRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    CatalogueRepository(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    List<ArticleEntity> findAll() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<ArticleEntity> query = builder.createQuery(ArticleEntity.class);
        final Root<ArticleEntity> rootEntry = query.from(ArticleEntity.class);
        final CriteriaQuery<ArticleEntity> all = query
                .select(rootEntry);
        final TypedQuery<ArticleEntity> typedQuery = entityManager.createQuery(all);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true)
    Optional<ArticleEntity> find(final String reference) {
        return Optional.ofNullable(entityManager.find(ArticleEntity.class, reference));
    }

    @Transactional
    void save(final ArticleEntity article) {
        entityManager.merge(article);
    }

}
