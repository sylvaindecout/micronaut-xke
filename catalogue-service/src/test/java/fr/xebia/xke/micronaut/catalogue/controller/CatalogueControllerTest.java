package fr.xebia.xke.micronaut.catalogue.controller;

import fr.xebia.xke.micronaut.catalogue.database.CatalogueDatabaseAdapter;
import fr.xebia.xke.micronaut.catalogue.domain.Article;
import fr.xebia.xke.micronaut.catalogue.domain.ArticleReference;
import fr.xebia.xke.micronaut.catalogue.domain.Catalogue;
import fr.xebia.xke.micronaut.catalogue.domain.CatalogueStorage;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static fr.xebia.xke.micronaut.catalogue.domain.Price.euros;
import static io.micronaut.http.HttpRequest.GET;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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

}
