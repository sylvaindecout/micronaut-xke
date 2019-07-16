package fr.xebia.xke.micronaut.booking.domain;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface StockStorage {

    Optional<Stock> findByArticleReference(@NotNull ArticleReference article);

    void save(@NotNull Stock stock);

}
