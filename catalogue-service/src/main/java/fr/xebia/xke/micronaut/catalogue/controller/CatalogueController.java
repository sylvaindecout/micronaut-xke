package fr.xebia.xke.micronaut.catalogue.controller;

import fr.xebia.xke.micronaut.catalogue.domain.Article;
import fr.xebia.xke.micronaut.catalogue.domain.ArticleReference;
import fr.xebia.xke.micronaut.catalogue.domain.CatalogueService;
import fr.xebia.xke.micronaut.catalogue.domain.Price;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Optional;

import static io.micronaut.http.HttpResponse.accepted;
import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Slf4j
@Validated
@Controller("catalogue/articles")
public class CatalogueController {

    private final CatalogueService service;

    CatalogueController(final CatalogueService service) {
        this.service = service;
    }

    @Get
    @Produces(APPLICATION_JSON)
    public Flowable<Article> getCatalogue() {
        log.info("Accessing catalogue");
        return service.getCatalogue();
    }

    @Get("{articleReference}")
    @Produces(APPLICATION_JSON)
    public Maybe<Article> getArticle(@NotBlank final String articleReference) {
        log.info("Accessing catalogue for article '{}'", articleReference);
        return service.getArticle(new ArticleReference(articleReference));
    }

    @Put("{articleReference}{?priceAsCents}")
    public HttpResponse<Void> addOrUpdate(@NotBlank final String articleReference, @Nullable @Positive @QueryValue("priceAsCents") final Integer priceAsCents) {
        return Optional.ofNullable(priceAsCents)
                .map(Integer::longValue)
                .map(Price::cents)
                .map(price -> addOrUpdate(articleReference, price))
                .orElseGet(HttpResponse::badRequest);
    }

    private HttpResponse<Void> addOrUpdate(final String articleReference, final Price price) {
        log.info("Add or update article '{}' (reference price: {})", articleReference, price);
        service.addOrUpdate(new ArticleReference(articleReference), price);
        return accepted();
    }

}
