package fr.xebia.xke.micronaut.booking.domain;

import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import java.util.Optional;

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
    void should_get_stock_from_database(@ForAll ArticleReference article, @ForAll Optional<Stock> stock) {
        given(stockStorage.findByArticleReference(article)).willReturn(stock);

        assertThat(service.getStock(article)).isEqualTo(stock);
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
        given(stockStorage.findByArticleReference(article)).willReturn(Optional.of(initialStock));

        service.order(article, requestedQuantity);

        then(stockStorage).should().save(initialStock.subtract(requestedQuantity));
    }

    @Property
    void should_reject_order_for_unavailable_quantity(@ForAll ArticleReference article, @ForAll Quantity requestedQuantity,
                                                      @ForAll Quantity initialQuantity) {
        Assume.that(requestedQuantity.isGreaterThan(initialQuantity));

        reset(stockStorage);
        given(stockStorage.findByArticleReference(article)).willReturn(Optional.of(Stock.of(article, initialQuantity.getValue())));

        assertThatExceptionOfType(UnavailableArticleQuantityException.class)
                .isThrownBy(() -> service.order(article, requestedQuantity))
                .withMessage("Requested quantity (%s) is unavailable for article '%s' (available: %s)",
                        requestedQuantity, article, initialQuantity);
        then(stockStorage).should(never()).save(any());
    }

    @Property
    void should_reject_order_for_unknown_article(@ForAll ArticleReference article, @ForAll Quantity requestedQuantity) {
        reset(stockStorage);
        given(stockStorage.findByArticleReference(article)).willReturn(Optional.empty());

        assertThatExceptionOfType(UnknownArticleException.class)
                .isThrownBy(() -> service.order(article, requestedQuantity))
                .withMessage("Article not found: %s", article);
        then(stockStorage).should(never()).save(any());
    }

}
