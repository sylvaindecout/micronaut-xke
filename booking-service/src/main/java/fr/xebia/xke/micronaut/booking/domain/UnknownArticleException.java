package fr.xebia.xke.micronaut.booking.domain;

import static java.lang.String.format;

public final class UnknownArticleException extends RuntimeException {

    UnknownArticleException(final ArticleReference article) {
        super(format("Article not found: %s", article));
    }

}
