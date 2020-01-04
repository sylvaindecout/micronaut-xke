package fr.xebia.xke.micronaut.booking.database;

import fr.xebia.xke.micronaut.booking.domain.ArticleReference;
import fr.xebia.xke.micronaut.booking.domain.Stock;
import fr.xebia.xke.micronaut.booking.domain.StockStorage;
import io.micronaut.spring.tx.annotation.Transactional;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Singleton
public class StockDatabaseAdapter implements StockStorage {

    private final StockRepository stockRepository;

    StockDatabaseAdapter(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Maybe<Stock> findByArticleReference(@NotNull ArticleReference article) {
        return stockRepository.findByArticleReference(article.getReference())
                .map(StockEntity::toDomain)
                .map(Maybe::just)
                .orElseGet(Maybe::empty);
    }

    @Override
    @Transactional
    public void save(@NotNull Stock stock) {
        final Optional<StockEntity> existingStockEntity = stockRepository.findByArticleReference(stock.getArticle().getReference());
        final StockEntity stockEntity = StockEntity.builder()
                .id(existingStockEntity.map(StockEntity::getId).orElse(null))
                .articleReference(stock.getArticle().getReference())
                .quantity(stock.getQuantity().getValue())
                .build();
        stockRepository.save(stockEntity);
    }

}
