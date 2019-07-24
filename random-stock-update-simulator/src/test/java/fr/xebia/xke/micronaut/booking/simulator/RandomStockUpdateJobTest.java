package fr.xebia.xke.micronaut.booking.simulator;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.micronaut.http.HttpResponse.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@MicronautTest
class RandomStockUpdateJobTest {

    @Inject
    private RandomStockUpdateJob job;

    @Inject
    private BookingClient bookingClient;

    @Test
    void should_order_one_random_item() {
        job.randomOrders();

        then(bookingClient).should(times(1)).order(oneOfTheArticlesInTargetStock(), eq(1));
    }

    @Test
    void should_order_replenish_stocks() {
        job.replenishStocks();

        then(bookingClient).should(times(1)).setQuantity("BookNumber1", new Stock("BookNumber1", 100L));
        then(bookingClient).should(times(1)).setQuantity("BookNumber2", new Stock("BookNumber2", 10L));
        then(bookingClient).should(times(1)).setQuantity("BookNumber3", new Stock("BookNumber3", 10L));
        then(bookingClient).should(times(1)).setQuantity("MagicianHat", new Stock("MagicianHat", 1L));
    }

    private static String oneOfTheArticlesInTargetStock() {
        return argThat(TargetStock::containsArticle);
    }

    @MockBean(BookingClient.class)
    BookingClient allPassBookingClient() {
        final BookingClient client = mock(BookingClient.class);
        given(client.order(any(), any())).willReturn(accepted());
        given(client.setQuantity(any(), any())).willReturn(noContent());
        return client;
    }

}
