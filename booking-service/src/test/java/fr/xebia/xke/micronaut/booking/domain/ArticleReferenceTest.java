package fr.xebia.xke.micronaut.booking.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArticleReferenceTest {

    @Test
    void should_fail_to_initialize_from_null_value() {
        assertThatNullPointerException()
                .isThrownBy(() -> new ArticleReference(null));
    }

    @Test
    void should_fail_to_initialize_from_value_with_4_characters() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new ArticleReference("1234"))
                .withMessage("Article reference must contain at least 5 characters (input value: %s)", "1234");
    }

    @Test
    void should_initialize_from_value_with_5_characters() {
        assertThat(new ArticleReference("12345").getReference()).isEqualTo("12345");
    }

}
