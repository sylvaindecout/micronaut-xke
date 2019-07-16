package fr.xebia.xke.micronaut.booking.database;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Singleton
class StockRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    StockRepository(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    Optional<StockEntity> findByArticleReference(final String articleReference) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<StockEntity> query = builder.createQuery(StockEntity.class);
        final Root<StockEntity> rootEntry = query.from(StockEntity.class);
        final CriteriaQuery<StockEntity> all = query
                .select(rootEntry)
                .where(builder.equal(rootEntry.get("articleReference"), articleReference));
        final TypedQuery<StockEntity> typedQuery = entityManager.createQuery(all);
        return typedQuery.getResultList().stream()
                .findAny();
    }

    @Transactional
    void save(final StockEntity stock) {
        entityManager.persist(stock);
    }

}
