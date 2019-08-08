package fr.xebia.xke.micronaut;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.assertj.core.api.Condition;

import java.util.Objects;

public final class HttpClientResponseExceptionConditions {

    private HttpClientResponseExceptionConditions() {}

    public static Condition<HttpClientResponseException> status(final HttpStatus status) {
        return new Condition<>(ex -> Objects.equals(ex.getResponse().status(), status),
                "HTTP status '%s'", status);
    }

}
