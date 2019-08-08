package fr.xebia.xke.micronaut.pricing.domain;

import fr.xebia.xke.micronaut.pricing.domain.Stock;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@CircuitBreaker(reset = "30s")
@Client("${booking.base-url}/booking")
public interface BookingClient {

    @Get("articles/{articleReference}")
    @Produces(APPLICATION_JSON)
    Stock getStock(String articleReference);

}
