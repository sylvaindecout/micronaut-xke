package fr.xebia.xke.micronaut.booking.domain;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.StringLength;

import static org.assertj.core.api.Assertions.*;

class ArticleReferenceTest {

    @Property
    void should_fail_to_initialize_from_null_value() {
        assertThatNullPointerException()
                .isThrownBy(() -> new ArticleReference(null));
    }

    @Property
    void should_fail_to_initialize_from_value_with_not_enough_characters(@ForAll @StringLength(max = 4) String reference) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new ArticleReference(reference))
                .withMessage("Article reference must contain at least 5 characters (input value: %s)", reference);
    }

    @Property
    void should_initialize_from_value_with_enough_characters(@ForAll @StringLength(min = 5) String reference) {
        assertThat(new ArticleReference(reference).getReference()).isEqualTo(reference);
    }

}
