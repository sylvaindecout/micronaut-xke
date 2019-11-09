package fr.xebia.xke.micronaut.booking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public final class ArticleReference {

    private static final int MIN_LENGTH = 5;

    @JsonProperty("article")
    String reference;

    public ArticleReference(@NonNull final String reference) {
        checkArgument(reference.length() >= MIN_LENGTH,
                "Article reference must contain at least %s characters (input value: %s)", MIN_LENGTH, reference);
        this.reference = reference;
    }

    @Override
    public String toString() {
        return this.reference;
    }

}
