package fr.xebia.xke.micronaut.booking;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.micronaut.http.HttpRequest.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@MicronautTest
class BookingControllerTest {

    private static final ArticleReference ARTICLE_1 = new ArticleReference("BOOK000001");
    private static final ArticleReference ARTICLE_2 = new ArticleReference("BOOK111112");

    @Inject
    @Client("/booking")
    private HttpClient client;

    @Test
    void should_update_stocks_when_an_order_is_made() {
        final String uri = format("/articles/%s/order?quantity=%s", ARTICLE_1.getReference(), 2);

        final HttpResponse<Stock> response = client.toBlocking()
                .exchange(POST(uri, ""), Stock.class);

        assertThat(response.status().getCode()).isEqualTo(200);
        assertThat(response.getBody()).contains(new Stock(ARTICLE_1, new Quantity(12L)));
        //TODO: check that DB has been updated
    }

    @Test
    void should_expose_quantity_in_stock_for_an_article() {
        //TODO: set DB so that it has 12 articles
        final String uri = format("/articles/%s", ARTICLE_1.getReference());

        final HttpResponse<Stock> response = client.toBlocking()
                .exchange(GET(uri), Stock.class);

        assertThat(response.status().getCode()).isEqualTo(200);
        assertThat(response.getBody()).contains(new Stock(ARTICLE_1, new Quantity(12L)));
    }

    @Test
    void should_update_quantity_in_stock_for_an_article() {
        final String uri = format("/articles/%s", ARTICLE_1.getReference());
        final Stock requestBody = new Stock(ARTICLE_1, new Quantity(12L));

        final HttpResponse<Void> response = client.toBlocking()
                .exchange(PUT(uri, requestBody), Void.class);

        assertThat(response.status().getCode()).isEqualTo(204);
        assertThat(response.getBody()).isEmpty();
        //TODO: check that DB has been updated
    }

    @Test
    void should_fail_to_update_quantity_in_stock_for_an_article_if_reference_is_not_consistent() {
        final String uri = format("/articles/%s", ARTICLE_1.getReference());
        final Stock requestBody = new Stock(ARTICLE_2, new Quantity(12L));

        final ThrowingCallable call = () -> client.toBlocking()
                .exchange(PUT(uri, requestBody), Void.class);

        assertThatExceptionOfType(HttpClientResponseException.class)
                .isThrownBy(call)
                .withMessageContaining(format("Request body (article: %s) is not consistent with URL (article: %s)",
                        ARTICLE_2, ARTICLE_1));
        //TODO: check that DB has not been updated
    }

}
