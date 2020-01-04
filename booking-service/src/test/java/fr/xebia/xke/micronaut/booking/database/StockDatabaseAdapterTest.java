package fr.xebia.xke.micronaut.booking.database;

import fr.xebia.xke.micronaut.booking.domain.ArticleReference;
import fr.xebia.xke.micronaut.booking.domain.Stock;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class StockDatabaseAdapterTest {

    private static final ArticleReference ARTICLE = new ArticleReference("BOOK00001");

    @Inject
    private StockDatabaseAdapter adapter;

    @Test
    void should_update_database_with_new_article() {
        final Stock stock = Stock.of(ARTICLE, 159);

        adapter.save(stock);

        assertThat(adapter.findByArticleReference(ARTICLE).blockingSingle()).isEqualTo(stock);
    }

    @Test
    void should_update_database_with_existing_article() {
        adapter.save(Stock.of(ARTICLE, 159));

        adapter.save(Stock.of(ARTICLE, 3_000));

        assertThat(adapter.findByArticleReference(ARTICLE).blockingSingle()).isEqualTo(Stock.of(ARTICLE, 3_000));
    }

    @Test
    void should_not_find_entity_for_unknown_article() {
        assertThat(adapter.findByArticleReference(ARTICLE).blockingSingle()).isNull();
    }

}
