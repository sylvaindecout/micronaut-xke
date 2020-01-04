package fr.xebia.xke.micronaut.pricing.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@JsonDeserialize(builder = Article.ArticleBuilder.class)
public class Article {

    @JsonUnwrapped @NonNull ArticleReference reference;
    @JsonUnwrapped @NonNull Price referencePrice;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class ArticleBuilder {}

}
