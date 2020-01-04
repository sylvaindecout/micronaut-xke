package fr.xebia.xke.micronaut.booking.domain;

import io.micronaut.spring.tx.annotation.Transactional;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class BookingService {

    private final StockStorage stockStorage;

    BookingService(final StockStorage stockStorage) {
        this.stockStorage = stockStorage;
    }

    public Maybe<Stock> getStock(final ArticleReference article) {
        return stockStorage.findByArticleReference(article);
    }

    @Transactional
    public void order(final ArticleReference article, final Quantity quantity) {
        final Stock updatedStock = Optional.of(stockStorage.findByArticleReference(article))
                .map(Maybe::blockingGet)
                .map(initialStock -> initialStock.subtract(quantity))
                .orElseThrow(() -> new UnknownArticleException(article));
        stockStorage.save(updatedStock);
    }

    public void updateStock(final Stock stock) {
        stockStorage.save(stock);
    }

}
