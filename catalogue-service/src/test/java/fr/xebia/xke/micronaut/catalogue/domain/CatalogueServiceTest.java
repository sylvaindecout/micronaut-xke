package fr.xebia.xke.micronaut.catalogue.domain;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class CatalogueServiceTest {

    private final CatalogueStorage catalogueStorage = mock(CatalogueStorage.class);
    private final CatalogueService service = new CatalogueService(catalogueStorage);

    @Property
    void should_get_catalogue_from_storage(@ForAll List<Article> articles) {
        given(catalogueStorage.findAll()).willReturn(articles);

        assertThat(service.getCatalogue()).isEqualTo(articles);
    }

    @Property
    void should_get_existing_article_from_storage(@ForAll Article article) {
        given(catalogueStorage.find(article.getReference())).willReturn(Optional.of(article));

        assertThat(service.getArticle(article.getReference())).contains(article);
    }

    @Property
    void should_get_unknown_article_from_storage(@ForAll ArticleReference reference) {
        given(catalogueStorage.find(reference)).willReturn(Optional.empty());

        assertThat(service.getArticle(reference)).isEmpty();
    }

    @Property
    void should_add_or_update_article_to_storage(@ForAll ArticleReference reference, @ForAll Price price) {
        service.addOrUpdate(reference, price);

        then(catalogueStorage).should().save(Article.builder()
                .reference(reference)
                .referencePrice(price)
                .build());
    }

}
