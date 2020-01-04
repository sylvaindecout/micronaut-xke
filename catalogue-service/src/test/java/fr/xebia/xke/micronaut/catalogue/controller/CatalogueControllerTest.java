package fr.xebia.xke.micronaut.catalogue.controller;

import fr.xebia.xke.micronaut.catalogue.database.CatalogueDatabaseAdapter;
import fr.xebia.xke.micronaut.catalogue.domain.Article;
import fr.xebia.xke.micronaut.catalogue.domain.ArticleReference;
import fr.xebia.xke.micronaut.catalogue.domain.CatalogueStorage;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.reactivex.Flowable;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static fr.xebia.xke.micronaut.HttpClientResponseExceptionConditions.status;
import static fr.xebia.xke.micronaut.catalogue.domain.Price.euros;
import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.PUT;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static io.reactivex.Maybe.empty;
import static io.reactivex.Maybe.just;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@MicronautTest
class CatalogueControllerTest {

    private static final Article ARTICLE_1 = Article.builder()
            .reference(new ArticleReference("BOOK30004"))
            .referencePrice(euros(12.99))
            .build();
    private static final Article ARTICLE_2 = Article.builder()
            .reference(new ArticleReference("BOOK10006"))
            .referencePrice(euros(99.99))
            .build();

    @Inject
    @Client("/catalogue")
    private RxStreamingHttpClient client;

    @Inject
    private CatalogueStorage catalogueRepository;

    @MockBean(CatalogueDatabaseAdapter.class)
    CatalogueStorage mockCatalogueRepository() {
        return mock(CatalogueStorage.class);
    }

    @Test
    void should_expose_all_articles() {
        given(catalogueRepository.findAll()).willReturn(Flowable.fromArray(
                ARTICLE_1,
                ARTICLE_2
        ));

        final Flowable<Article> response = client
                .jsonStream(GET("/articles"), Article.class);

        assertThat(response.blockingIterable()).containsExactly(
                ARTICLE_1,
                ARTICLE_2
        );
    }

    @Test
    void should_expose_existing_article() {
        given(catalogueRepository.find(ARTICLE_1.getReference())).willReturn(just(ARTICLE_1));

        final HttpResponse<Article> response = client.toBlocking()
                .exchange(GET(format("/articles/%s", ARTICLE_1.getReference().getValue())), Article.class);

        assertThat(response.status().getCode()).isEqualTo(200);
        assertThat(response.getBody()).contains(ARTICLE_1);
    }

    @Test
    void should_fail_to_expose_unknown_article() {
        final ArticleReference unknownReference = new ArticleReference("unknown");
        given(catalogueRepository.find(unknownReference)).willReturn(empty());

        final ThrowingCallable call = () -> client.toBlocking()
                .exchange(GET(format("/articles/%s", unknownReference.getValue())), Article.class);

        assertThatExceptionOfType(HttpClientResponseException.class)
                .isThrownBy(call)
                .has(status(NOT_FOUND));
    }

    @Test
    void should_update_catalogue() {
        final HttpResponse<Void> response = client.toBlocking()
                .exchange(PUT(format("/articles/%s?priceAsCents=%s", "BOOK30004", 1299), ""), Void.class);

        assertThat(response.status().getCode()).isEqualTo(202);
        then(catalogueRepository).should().save(Article.builder()
                .reference(new ArticleReference("BOOK30004"))
                .referencePrice(euros(12.99))
                .build());
    }

    @Test
    void should_fail_to_update_catalogue_if_price_is_missing() {
        final ThrowingCallable call = () -> client.toBlocking()
                .exchange(PUT(format("/articles/%s", "BOOK30004"), ""), Void.class);

        assertThatExceptionOfType(HttpClientResponseException.class)
                .isThrownBy(call)
                .has(status(BAD_REQUEST));
        then(catalogueRepository).should(never()).save(any(Article.class));
    }

    @Test
    void should_fail_to_update_catalogue_if_price_is_not_greater_than_zero() {
        final ThrowingCallable call = () -> client.toBlocking()
                .exchange(PUT(format("/articles/%s?priceAsCents=%s", "BOOK30004", 0), ""), Void.class);

        assertThatExceptionOfType(HttpClientResponseException.class)
                .isThrownBy(call)
                .has(status(BAD_REQUEST));
        then(catalogueRepository).should(never()).save(any(Article.class));
    }

}
