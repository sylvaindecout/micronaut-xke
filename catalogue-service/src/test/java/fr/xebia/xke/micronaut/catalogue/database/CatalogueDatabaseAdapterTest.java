package fr.xebia.xke.micronaut.catalogue.database;

import fr.xebia.xke.micronaut.catalogue.domain.Article;
import fr.xebia.xke.micronaut.catalogue.domain.ArticleReference;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static fr.xebia.xke.micronaut.catalogue.domain.Price.euros;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class CatalogueDatabaseAdapterTest {

    private static final ArticleReference REFERENCE = new ArticleReference("BOOK00001");
    private static final Article ARTICLE_1 = Article.builder()
            .reference(new ArticleReference("BOOK00001"))
            .referencePrice(euros(12.99))
            .build();
    private static final Article ARTICLE_2 = Article.builder()
            .reference(new ArticleReference("BOOK00002"))
            .referencePrice(euros(19.22))
            .build();
    private static final Article ARTICLE_3 = Article.builder()
            .reference(new ArticleReference("BOOK00003"))
            .referencePrice(euros(19.22))
            .build();

    @Inject
    private CatalogueDatabaseAdapter adapter;

    @Test
    void should_update_database_with_new_article() {
        assertThat(adapter.findAll()).isEmpty();
        final Article newArticle = Article.builder()
                .reference(REFERENCE)
                .referencePrice(euros(12.99))
                .build();

        adapter.save(newArticle);

        assertThat(adapter.findAll()).containsExactly(newArticle);
    }

    @Test
    void should_update_database_with_existing_article() {
        final Article formerArticle = Article.builder()
                .reference(REFERENCE)
                .referencePrice(euros(12.99))
                .build();
        adapter.save(formerArticle);
        assertThat(adapter.findAll()).containsExactly(formerArticle);
        final Article updatedArticle = Article.builder()
                .reference(REFERENCE)
                .referencePrice(euros(19.22))
                .build();

        adapter.save(updatedArticle);

        assertThat(adapter.findAll()).containsExactly(updatedArticle);
    }

    @Test
    void should_expose_all_articles_in_database() {
        adapter.save(ARTICLE_1);
        adapter.save(ARTICLE_2);
        adapter.save(ARTICLE_3);

        assertThat(adapter.findAll()).containsExactly(ARTICLE_1, ARTICLE_2, ARTICLE_3);
    }

    @Test
    void should_expose_article_in_database() {
        adapter.save(ARTICLE_1);

        assertThat(adapter.find(ARTICLE_1.getReference())).contains(ARTICLE_1);
    }

    @Test
    void should_fail_to_expose_article_not_in_database() {
        assertThat(adapter.find(REFERENCE)).isEmpty();
    }

}
