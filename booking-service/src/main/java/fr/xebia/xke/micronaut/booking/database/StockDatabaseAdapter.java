package fr.xebia.xke.micronaut.booking.database;

import fr.xebia.xke.micronaut.booking.domain.ArticleReference;
import fr.xebia.xke.micronaut.booking.domain.Stock;
import fr.xebia.xke.micronaut.booking.domain.StockStorage;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Singleton
public final class StockDatabaseAdapter implements StockStorage {

    private final StockRepository stockRepository;

    StockDatabaseAdapter(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Optional<Stock> findByArticleReference(@NotNull ArticleReference article) {
        return stockRepository.findByArticleReference(article.getReference())
                .map(StockEntity::toDomain);
    }

    @Override
    public void save(@NotNull Stock stock) {
        stockRepository.save(StockEntity.fromDomain(stock));
    }

}
