package fr.xebia.xke.micronaut.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class StockTest {

    @Inject
    private ObjectMapper mapper;

    @Test
    void should_map_to_JSON_with_no_information_loss() throws IOException {
        final ObjectWriter writer = mapper.writer().forType(Stock.class);
        final ObjectReader reader = mapper.reader().forType(Stock.class);
        final Stock inputObject = new Stock(new ArticleReference("BOOK30004"), new Quantity(12L));

        final String json = writer.writeValueAsString(inputObject);
        final Stock outputObject = reader.readValue(json);

        assertThat(outputObject).isEqualTo(inputObject);
    }

}
