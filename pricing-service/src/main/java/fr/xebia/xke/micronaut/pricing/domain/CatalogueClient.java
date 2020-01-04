package fr.xebia.xke.micronaut.pricing.domain;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;

import java.util.Optional;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@CircuitBreaker(reset = "30s")
@Client("${catalogue.base-url}/catalogue")
public interface CatalogueClient {

    @Get("articles/{reference}")
    @Produces(APPLICATION_JSON)
    Optional<Article> getArticle(String reference);

}
