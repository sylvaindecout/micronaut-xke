package fr.xebia.xke.micronaut.booking.domain;

import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class BookingService {

    private final StockStorage stockStorage;

    BookingService(final StockStorage stockStorage) {
        this.stockStorage = stockStorage;
    }

    public Optional<Stock> getStock(final ArticleReference article){
        return stockStorage.findByArticleReference(article);
    }

    @Transactional
    public void order(final ArticleReference article, final Quantity quantity) {
        final Stock updatedStock = stockStorage.findByArticleReference(article)
                .map(initialStock -> initialStock.add(quantity))
                .orElseThrow(() -> new UnknownArticleException(article));
        stockStorage.save(updatedStock);
    }

    public void updateStock(final Stock stock) {
        stockStorage.save(stock);
    }

}
