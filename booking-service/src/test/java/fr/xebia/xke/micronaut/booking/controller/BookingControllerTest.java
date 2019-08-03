package fr.xebia.xke.micronaut.booking.controller;

import fr.xebia.xke.micronaut.booking.database.StockDatabaseAdapter;
import fr.xebia.xke.micronaut.booking.domain.ArticleReference;
import fr.xebia.xke.micronaut.booking.domain.Stock;
import fr.xebia.xke.micronaut.booking.domain.StockStorage;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static fr.xebia.xke.micronaut.HttpClientResponseExceptionConditions.status;
import static io.micronaut.http.HttpRequest.*;
import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@MicronautTest
class BookingControllerTest {

    private static final ArticleReference ARTICLE_1 = new ArticleReference("BOOK000001");
    private static final ArticleReference ARTICLE_2 = new ArticleReference("BOOK111112");

    @Inject
    @Client("/booking")
    private HttpClient client;

    @Inject
    private StockStorage stockRepository;

    @MockBean(StockDatabaseAdapter.class)
    StockStorage mockStockRepository() {
        return mock(StockStorage.class);
    }

    @Test
    void should_fail_to_update_stocks_when_an_order_is_made_for_an_unknown_article() {
        given(stockRepository.findByArticleReference(ARTICLE_1)).willReturn(empty());

        final ThrowingCallable call = () -> client.toBlocking()
                .exchange(POST(format("/articles/%s/order", ARTICLE_1), ""), Void.class);

        assertThatExceptionOfType(HttpClientResponseException.class)
                .isThrownBy(call)
                .has(status(NOT_FOUND));
        then(stockRepository).should(never()).save(any(Stock.class));
    }

    @Test
    void should_update_stocks_when_an_order_is_made_with_specified_quantity() {
        given(stockRepository.findByArticleReference(ARTICLE_1))
                .willReturn(Optional.of(Stock.of(ARTICLE_1, 12)));

        final HttpResponse<Void> response = client.toBlocking()
                .exchange(POST(format("/articles/%s/order?quantity=%s", ARTICLE_1.getReference(), 2), ""), Void.class);

        assertThat(response.status().getCode()).isEqualTo(202);
        then(stockRepository).should().save(Stock.of(ARTICLE_1, 14));
    }

    @Test
    void should_update_stocks_by_one_when_an_order_is_made_with_no_specified_quantity() {
        given(stockRepository.findByArticleReference(ARTICLE_1))
                .willReturn(Optional.of(Stock.of(ARTICLE_1, 12)));

        final HttpResponse<Void> response = client.toBlocking()
                .exchange(POST(format("/articles/%s/order", ARTICLE_1.getReference()), ""), Void.class);

        assertThat(response.status().getCode()).isEqualTo(202);
        then(stockRepository).should().save(Stock.of(ARTICLE_1, 13));
    }

    @Test
    void should_fail_to_expose_quantity_in_stock_for_an_unknown_article() {
        given(stockRepository.findByArticleReference(ARTICLE_1)).willReturn(empty());

        final ThrowingCallable call = () -> client.toBlocking()
                .exchange(GET(format("/articles/%s", ARTICLE_1.getReference())), Stock.class);

        assertThatExceptionOfType(HttpClientResponseException.class)
                .isThrownBy(call)
                .has(status(NOT_FOUND));
    }

    @Test
    void should_expose_quantity_in_stock_for_an_article() {
        given(stockRepository.findByArticleReference(ARTICLE_1))
                .willReturn(Optional.of(Stock.of(ARTICLE_1, 12)));

        final HttpResponse<Stock> response = client.toBlocking()
                .exchange(GET(format("/articles/%s", ARTICLE_1.getReference())), Stock.class);

        assertThat(response.status().getCode()).isEqualTo(200);
        assertThat(response.getBody()).contains(Stock.of(ARTICLE_1, 12));
    }

    @Test
    void should_inialize_quantity_in_stock_for_a_new_article() {
        given(stockRepository.findByArticleReference(ARTICLE_1)).willReturn(empty());
        final Stock requestBody = Stock.of(ARTICLE_1, 12);

        final HttpResponse<Void> response = client.toBlocking()
                .exchange(PUT(format("/articles/%s", ARTICLE_1.getReference()), requestBody), Void.class);

        assertThat(response.status().getCode()).isEqualTo(204);
        assertThat(response.getBody()).isEmpty();
        then(stockRepository).should().save(Stock.of(ARTICLE_1, 12));
    }

    @Test
    void should_overwrite_quantity_in_stock_for_an_existing_article() {
        given(stockRepository.findByArticleReference(ARTICLE_1))
                .willReturn(Optional.of(Stock.of(ARTICLE_1, 2000)));
        final Stock requestBody = Stock.of(ARTICLE_1, 12);

        final HttpResponse<Void> response = client.toBlocking()
                .exchange(PUT(format("/articles/%s", ARTICLE_1.getReference()), requestBody), Void.class);

        assertThat(response.status().getCode()).isEqualTo(204);
        assertThat(response.getBody()).isEmpty();
        then(stockRepository).should().save(Stock.of(ARTICLE_1, 12));
    }

    @Test
    void should_fail_to_update_quantity_in_stock_for_an_article_if_reference_is_not_consistent() {
        final Stock requestBody = Stock.of(ARTICLE_2, 12);

        final ThrowingCallable call = () -> client.toBlocking()
                .exchange(PUT(format("/articles/%s", ARTICLE_1.getReference()), requestBody), Void.class);

        assertThatExceptionOfType(HttpClientResponseException.class)
                .isThrownBy(call)
                .withMessageContaining(format("Request body (article: %s) is not consistent with URL (article: %s)",
                        ARTICLE_2, ARTICLE_1));
        then(stockRepository).should(never()).save(any(Stock.class));
    }

}
