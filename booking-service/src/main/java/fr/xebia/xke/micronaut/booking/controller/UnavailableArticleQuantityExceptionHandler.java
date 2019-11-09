package fr.xebia.xke.micronaut.booking.controller;

import fr.xebia.xke.micronaut.booking.domain.UnavailableArticleQuantityException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

import static io.micronaut.http.HttpResponse.status;
import static io.micronaut.http.HttpStatus.CONFLICT;

@Produces
@Singleton
@Requires(classes = {UnavailableArticleQuantityException.class, ExceptionHandler.class})
public final class UnavailableArticleQuantityExceptionHandler implements ExceptionHandler<UnavailableArticleQuantityException, HttpResponse> {

    @Override
    public HttpResponse handle(final HttpRequest request, final UnavailableArticleQuantityException exception) {
        return status(CONFLICT);
    }

}
