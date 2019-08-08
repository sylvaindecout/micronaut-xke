package fr.xebia.xke.micronaut.pricing.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public final class ArticleReference {

    private static final int MIN_LENGTH = 5;

    @JsonProperty("reference")
    String value;

    public ArticleReference(@NonNull final String value) {
        checkArgument(value.length() >= MIN_LENGTH,
                "Article reference must contain at least %s characters (input value: %s)", MIN_LENGTH, value);
        this.value = value;
    }

}
