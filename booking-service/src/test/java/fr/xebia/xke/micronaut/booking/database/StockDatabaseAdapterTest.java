package fr.xebia.xke.micronaut.booking.database;

import fr.xebia.xke.micronaut.booking.domain.ArticleReference;
import fr.xebia.xke.micronaut.booking.domain.Stock;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

//FIXME: disabling transactions is only a workaround to get it working until 1.2.0 is released (cf. https://github.com/micronaut-projects/micronaut-core/issues/1871)
@MicronautTest(transactional = false)
class StockDatabaseAdapterTest {

    private static final ArticleReference ARTICLE = new ArticleReference("BOOK00001");

    @Inject
    private StockDatabaseAdapter adapter;

    @Test
    void should_update_database(){
        final Stock stock = Stock.of(ARTICLE, 159);
        adapter.save(stock);

        assertThat(adapter.findByArticleReference(ARTICLE)).contains(stock);
    }

    @Test
    void should_not_find_entity_for_unknown_article(){
        assertThat(adapter.findByArticleReference(ARTICLE)).isEmpty();
    }

}
