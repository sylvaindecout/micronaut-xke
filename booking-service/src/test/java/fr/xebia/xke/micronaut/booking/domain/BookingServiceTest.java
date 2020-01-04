package fr.xebia.xke.micronaut.booking.domain;

import io.reactivex.Maybe;
import net.jqwik.api.*;

import static io.reactivex.Maybe.empty;
import static io.reactivex.Maybe.just;
import static net.jqwik.api.Arbitraries.defaultFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private final StockStorage stockStorage = mock(StockStorage.class);
    private final BookingService service = new BookingService(stockStorage);

    @Property
    void should_fail_to_get_stock_from_database_for_unknown_article(@ForAll ArticleReference article) {
        given(stockStorage.findByArticleReference(article)).willReturn(empty());

        assertThat(service.getStock(article).blockingGet()).isNull();
    }

    @Property
    void should_get_stock_from_database(@ForAll ArticleReference article, @ForAll("stock") Maybe<Stock> stock) {
        given(stockStorage.findByArticleReference(article)).willReturn(stock);

        assertThat(service.getStock(article)).isEqualTo(stock);
    }

    @Provide
    private static Arbitrary<Maybe<Stock>> stock() {
        return defaultFor(Stock.class).map(Maybe::just);
    }

    @Property
    void should_update_stock_in_database(@ForAll Stock stock) {
        reset(stockStorage);

        service.updateStock(stock);

        then(stockStorage).should().save(stock);
    }

    @Property
    void should_update_database_on_order(@ForAll ArticleReference article, @ForAll Quantity requestedQuantity,
                                         @ForAll Stock initialStock) {
        Assume.that(!requestedQuantity.isGreaterThan(initialStock.getQuantity()));

        reset(stockStorage);
        given(stockStorage.findByArticleReference(article)).willReturn(just(initialStock));

        service.order(article, requestedQuantity);

        then(stockStorage).should().save(initialStock.subtract(requestedQuantity));
    }

    @Property
    void should_reject_order_for_unavailable_quantity(@ForAll ArticleReference article, @ForAll Quantity requestedQuantity,
                                                      @ForAll Quantity initialQuantity) {
        Assume.that(requestedQuantity.isGreaterThan(initialQuantity));

        reset(stockStorage);
        given(stockStorage.findByArticleReference(article)).willReturn(just(Stock.of(article, initialQuantity.getValue())));

        assertThatExceptionOfType(UnavailableArticleQuantityException.class)
                .isThrownBy(() -> service.order(article, requestedQuantity))
                .withMessage("Requested quantity (%s) is unavailable for article '%s' (available: %s)",
                        requestedQuantity, article, initialQuantity);
        then(stockStorage).should(never()).save(any());
    }

    @Property
    void should_reject_order_for_unknown_article(@ForAll ArticleReference article, @ForAll Quantity requestedQuantity) {
        reset(stockStorage);
        given(stockStorage.findByArticleReference(article)).willReturn(empty());

        assertThatExceptionOfType(UnknownArticleException.class)
                .isThrownBy(() -> service.order(article, requestedQuantity))
                .withMessage("Article not found: %s", article);
        then(stockStorage).should(never()).save(any());
    }

}
