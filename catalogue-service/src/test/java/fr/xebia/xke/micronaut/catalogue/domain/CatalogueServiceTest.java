package fr.xebia.xke.micronaut.catalogue.domain;

import fr.xebia.xke.micronaut.catalogue.database.CatalogueDatabaseAdapter;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static fr.xebia.xke.micronaut.catalogue.domain.Price.euros;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@MicronautTest
class CatalogueServiceTest {

    private static final Article ARTICLE_1 = Article.builder()
            .reference(new ArticleReference("BOOK30004"))
            .referencePrice(euros(12.99))
            .build();
    private static final Article ARTICLE_2 = Article.builder()
            .reference(new ArticleReference("BOOK10006"))
            .referencePrice(euros(99.99))
            .build();
    public static final ArticleReference REFERENCE = new ArticleReference("BOOK0001");

    @Inject
    CatalogueService service;

    @Inject
    CatalogueStorage catalogueStorage;

    @Test
    void should_get_catalogue_from_storage() {
        given(catalogueStorage.findAll()).willReturn(asList(
                ARTICLE_1,
                ARTICLE_2
        ));

        assertThat(service.getCatalogue()).isEqualTo(Catalogue.of(
                ARTICLE_1,
                ARTICLE_2
        ));
    }

    @Test
    void should_add_or_update_article_to_storage() {
        service.addOrUpdate(REFERENCE, euros(12.99));

        then(catalogueStorage).should().save(Article.builder()
                .reference(REFERENCE)
                .referencePrice(euros(12.99))
                .build());
    }

    @MockBean(CatalogueDatabaseAdapter.class)
    CatalogueStorage mockBooking() {
        return mock(CatalogueStorage.class);
    }

}
