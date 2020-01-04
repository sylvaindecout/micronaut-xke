package fr.xebia.xke.micronaut.booking.domain;

import io.reactivex.Maybe;

import javax.validation.constraints.NotNull;

public interface StockStorage {

    Maybe<Stock> findByArticleReference(@NotNull ArticleReference article);

    void save(@NotNull Stock stock);

}
