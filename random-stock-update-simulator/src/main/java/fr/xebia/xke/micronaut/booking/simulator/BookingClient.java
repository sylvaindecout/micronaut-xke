package fr.xebia.xke.micronaut.booking.simulator;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;

@Client("/booking")
public interface BookingClient {

    @Put("articles/{articleReference}")
    HttpResponse setQuantity(String articleReference, @Body Stock stock);

    @Post("articles/{articleReference}/order{?quantity}")
    HttpResponse order(String articleReference, @QueryValue final Integer quantity);

}
