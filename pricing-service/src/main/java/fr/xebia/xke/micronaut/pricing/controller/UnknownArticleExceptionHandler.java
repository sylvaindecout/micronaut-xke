package fr.xebia.xke.micronaut.pricing.controller;

import fr.xebia.xke.micronaut.pricing.domain.UnknownArticleException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

import static io.micronaut.http.HttpResponse.notFound;

@Produces
@Singleton
@Requires(classes = {UnknownArticleException.class, ExceptionHandler.class})
public final class UnknownArticleExceptionHandler implements ExceptionHandler<UnknownArticleException, HttpResponse> {

    @Override
    public HttpResponse handle(final HttpRequest request, final UnknownArticleException exception) {
        return notFound();
    }

}
