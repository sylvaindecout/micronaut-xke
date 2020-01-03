package fr.xebia.xke.micronaut.catalogue.controller;

import fr.xebia.xke.micronaut.catalogue.database.CatalogueDatabaseAdapter;
import fr.xebia.xke.micronaut.catalogue.domain.Article;
import fr.xebia.xke.micronaut.catalogue.domain.ArticleReference;
import fr.xebia.xke.micronaut.catalogue.domain.Catalogue;
import fr.xebia.xke.micronaut.catalogue.domain.CatalogueStorage;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static fr.xebia.xke.micronaut.HttpClientResponseExceptionConditions.status;
import static fr.xebia.xke.micronaut.catalogue.domain.Price.euros;
import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.PUT;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static java.lang.String.format;
import static java.util.Arrays.asList;
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
    private HttpClient client;

    @Inject
    private CatalogueStorage catalogueRepository;

    @MockBean(CatalogueDatabaseAdapter.class)
    CatalogueStorage mockCatalogueRepository() {
        return mock(CatalogueStorage.class);
    }

    @Test
    void should_expose_all_articles() {
        given(catalogueRepository.findAll()).willReturn(asList(
                ARTICLE_1,
                ARTICLE_2
        ));

        final HttpResponse<Catalogue> response = client.toBlocking()
                .exchange(GET("/articles"), Catalogue.class);

        assertThat(response.status().getCode()).isEqualTo(200);
        assertThat(response.getBody()).contains(Catalogue.of(
                ARTICLE_1,
                ARTICLE_2
        ));
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
