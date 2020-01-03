package fr.xebia.xke.micronaut.catalogue.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

@Value
@Builder
@JsonSerialize(using = Catalogue.CatalogueSerializer.class)
@JsonDeserialize(using = Catalogue.CatalogueDeserializer.class)
public class Catalogue {

    @Singular
    @NonNull Collection<Article> articles;

    public static Catalogue of(final List<Article> articles) {
        final CatalogueBuilder builder = Catalogue.builder();
        articles.forEach(builder::article);
        return builder.build();
    }

    public static Catalogue of(final Article... articles) {
        return Catalogue.of(asList(articles));
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
                builder.article(codec.readValue(element.traverse(), Article.class));
            }
            return builder.build();
        }
    }

    static final class CatalogueSerializer extends StdSerializer<Catalogue> {

        protected CatalogueSerializer() {
            super(Catalogue.class);
        }

        @Override
        public void serialize(final Catalogue catalogue, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
            generator.writeObject(catalogue.getArticles());
        }
    }

}
