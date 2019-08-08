package fr.xebia.xke.micronaut.pricing.domain;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.stream;

@Value
@Builder
@JsonDeserialize(using = Catalogue.CatalogueDeserializer.class)
public class Catalogue {

    @Singular
    @NonNull Map<ArticleReference, Article> articles;

    public static Catalogue of(final Article... articles) {
        final CatalogueBuilder builder = Catalogue.builder();
        stream(articles).forEach(builder::addArticle);
        return builder.build();
    }

    static final class CatalogueBuilder {
        CatalogueBuilder addArticle(final Article article) {
            return article(article.getReference(), article);
        }
    }

    public Optional<Article> find(@NonNull final ArticleReference articleReference) {
        return Optional.ofNullable(articles.get(articleReference));
    }

    static final class CatalogueDeserializer extends StdDeserializer<Catalogue> {

        protected CatalogueDeserializer() {
            super(Catalogue.class);
        }

        @Override
        public Catalogue deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            final ObjectCodec codec = parser.getCodec();
            final JsonNode root = codec.readTree(parser);
            if (!root.isArray()) {
                throw new JsonParseException(parser, "Expected: array");
            }
            final CatalogueBuilder builder = Catalogue.builder();
            for (final Iterator<JsonNode> it = root.elements(); it.hasNext(); ) {
                final JsonNode element = it.next();
                builder.addArticle(codec.readValue(element.traverse(), Article.class));
            }
            return builder.build();
        }
    }

}
