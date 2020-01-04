package fr.xebia.xke.micronaut.pricing.controller;

import fr.xebia.xke.micronaut.pricing.domain.*;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static fr.xebia.xke.micronaut.HttpClientResponseExceptionConditions.status;
import static fr.xebia.xke.micronaut.pricing.domain.Price.euros;
import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@MicronautTest
class PricingControllerTest {

    private static final ArticleReference ARTICLE_1 = new ArticleReference("BOOK000001");
    private static final ArticleReference ARTICLE_2_NOT_IN_CATALOGUE = new ArticleReference("NOT_IN_CATALOGUE");
    private static final ArticleReference ARTICLE_3_UNKNOWN_TO_BOOKING = new ArticleReference("UNKNOWN_TO_BOOKING");
    private static final ArticleReference ARTICLE_4_OUT_OF_STOCK = new ArticleReference("OUT_OF_STOCK");
    private static final Price PRICE_OF_ARTICLE_1 = euros(99.99);

    @Inject
    @Client("/pricing")
    private HttpClient client;

    @Test
    void should_fail_to_expose_price_for_an_article_that_is_not_in_catalogue() {
        final ThrowableAssert.ThrowingCallable call = () -> client.toBlocking()
                .exchange(GET(format("/articles/%s", ARTICLE_2_NOT_IN_CATALOGUE.getValue())), Long.class);

        assertThatExceptionOfType(HttpClientResponseException.class)
                .isThrownBy(call)
                .has(status(NOT_FOUND));
    }

    @Test
    void should_expose_no_price_for_an_article_for_which_stock_is_not_found() {
        final HttpResponse<Long> response = client.toBlocking()
                .exchange(GET(format("/articles/%s", ARTICLE_3_UNKNOWN_TO_BOOKING.getValue())), Long.class);

        assertThat(response.status().getCode()).isEqualTo(204);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void should_expose_no_price_for_article_that_is_out_of_stock() {
        final HttpResponse<Long> response = client.toBlocking()
                .exchange(GET(format("/articles/%s", ARTICLE_4_OUT_OF_STOCK.getValue())), Long.class);

        assertThat(response.status().getCode()).isEqualTo(204);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void should_expose_price_for_article_that_is_in_stock() {
        final HttpResponse<Long> response = client.toBlocking()
                .exchange(GET(format("/articles/%s", ARTICLE_1.getValue())), Long.class);

        assertThat(response.status().getCode()).isEqualTo(200);
        assertThat(response.getBody()).contains(PRICE_OF_ARTICLE_1.getValueAsCents());
    }

    @MockBean(BookingClient.class)
    BookingClient bookingClient() {
        final BookingClient client = mock(BookingClient.class);
        given(client.getStock(ARTICLE_1.getValue()))
                .willReturn(Stock.of(ARTICLE_1, 1L));
        given(client.getStock(ARTICLE_2_NOT_IN_CATALOGUE.getValue()))
                .willReturn(Stock.of(ARTICLE_2_NOT_IN_CATALOGUE, 1L));
        given(client.getStock(ARTICLE_4_OUT_OF_STOCK.getValue()))
                .willReturn(Stock.of(ARTICLE_4_OUT_OF_STOCK, 0L));
        return client;
    }

    @MockBean(CatalogueClient.class)
    CatalogueClient catalogueClient() {
        final CatalogueClient client = mock(CatalogueClient.class);
        given(client.getArticle(any()))
                .willReturn(Optional.empty());
        given(client.getArticle(ARTICLE_1.getValue()))
                .willReturn(Optional.of(Article.builder()
                        .reference(ARTICLE_1)
                        .referencePrice(PRICE_OF_ARTICLE_1)
                        .build()));
        given(client.getArticle(ARTICLE_3_UNKNOWN_TO_BOOKING.getValue()))
                .willReturn(Optional.of(Article.builder()
                        .reference(ARTICLE_3_UNKNOWN_TO_BOOKING)
                        .referencePrice(euros(99.99))
                        .build()));
        given(client.getArticle(ARTICLE_4_OUT_OF_STOCK.getValue()))
                .willReturn(Optional.of(Article.builder()
                        .reference(ARTICLE_4_OUT_OF_STOCK)
                        .referencePrice(euros(99.99))
                        .build()));
        return client;
    }

}
