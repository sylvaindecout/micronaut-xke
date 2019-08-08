package fr.xebia.xke.micronaut.pricing.domain;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
@JsonDeserialize(using = Stock.StockDeserializer.class)
public class Stock {

    @NonNull ArticleReference article;
    @NonNull Quantity quantity;

    public static Stock of(final ArticleReference article, final long quantity) {
        return new Stock(article, new Quantity(quantity));
    }

    static final class StockDeserializer extends StdDeserializer<Stock> {

        protected StockDeserializer() {
            super(Stock.class);
        }

        @Override
        public Stock deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            final JsonNode root = parser.getCodec().readTree(parser);
            if (!root.has("article")) {
                throw new JsonParseException(parser, "Mandatory field is missing: article");
            }
            if (!root.has("quantity")) {
                throw new JsonParseException(parser, "Mandatory field is missing: quantity");
            }
            return Stock.of(
                    new ArticleReference(root.get("article").textValue()),
                    root.get("quantity").asLong()
            );
        }
    }

}
