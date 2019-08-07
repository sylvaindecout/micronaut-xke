package fr.xebia.xke.micronaut.booking.simulator;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.google.common.collect.ImmutableMap.copyOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

@Getter(PRIVATE)
@AllArgsConstructor
enum TargetStock {

    BOOK_1("BookNumber1", 100L),
    BOOK_2("BookNumber2", 10L),
    BOOK_3("BookNumber3", 10L),
    MAGICIAN_HAT("MagicianHat", 1L);

    private final String articleReference;
    private final Long targetQuantity;

    static ImmutableMap<String, Long> asMap() {
        return copyOf(stream(values()).collect(toMap(
                TargetStock::getArticleReference,
                TargetStock::getTargetQuantity
        )));
    }

    static String getRandomArticle() {
        final int randomIndex = (int) (Math.random() * TargetStock.values().length);
        return TargetStock.values()[randomIndex].getArticleReference();
    }

    static boolean containsArticle(final String reference) {
        return stream(TargetStock.values())
                .map(TargetStock::getArticleReference)
                .anyMatch(reference::equals);
    }

}
