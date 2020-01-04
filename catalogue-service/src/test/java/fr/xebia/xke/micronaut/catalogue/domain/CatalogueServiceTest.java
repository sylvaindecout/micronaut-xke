package fr.xebia.xke.micronaut.catalogue.domain;

import io.reactivex.Flowable;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static io.reactivex.Maybe.empty;
import static io.reactivex.Maybe.just;
import static net.jqwik.api.Arbitraries.defaultFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class CatalogueServiceTest {

    private final CatalogueStorage catalogueStorage = mock(CatalogueStorage.class);
    private final CatalogueService service = new CatalogueService(catalogueStorage);

    @Property
    void should_get_catalogue_from_storage(@ForAll("catalogue") Flowable<Article> articles) {
        given(catalogueStorage.findAll()).willReturn(articles);

        assertThat(service.getCatalogue()).isEqualTo(articles);
    }

    @Provide
    private static Arbitrary<Flowable<Article>> catalogue() {
        return defaultFor(Article.class).list().map(Flowable::fromIterable);
    }

    @Property
    void should_get_existing_article_from_storage(@ForAll Article article) {
        given(catalogueStorage.find(article.getReference())).willReturn(just(article));

        assertThat(service.getArticle(article.getReference()).blockingGet()).isEqualTo(article);
    }

    @Property
    void should_get_unknown_article_from_storage(@ForAll ArticleReference reference) {
        given(catalogueStorage.find(reference)).willReturn(empty());

        assertThat(service.getArticle(reference).blockingGet()).isNull();
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
