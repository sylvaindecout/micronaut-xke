package fr.xebia.xke.micronaut.booking.simulator;

import io.micronaut.http.HttpResponse;
import io.micronaut.scheduling.annotation.Scheduled;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.micronaut.http.HttpStatus.ACCEPTED;
import static io.micronaut.http.HttpStatus.NO_CONTENT;

@Slf4j
@Singleton
public class RandomStockUpdateJob {

    @Inject
    private BookingClient client;

    @Scheduled(fixedDelay = "120s", initialDelay = "5s")
    void replenishStocks() {
        TargetStock.asMap().forEach(this::updateStock);
    }

    @Scheduled(fixedDelay = "10s")
    void randomOrders() {
        order(TargetStock.getRandomArticle(), 1);
    }

    private void updateStock(final String articleReference, final long quantity) {
        final HttpResponse response = client.setQuantity(articleReference, new Stock(articleReference, quantity));
        if (response.getStatus() == NO_CONTENT) {
            log.info("Update current stock of '{}' to {} articles", articleReference, quantity);
        } else {
            log.error("Update current stock of '{}' to {} articles - failed with error '{}'", articleReference, quantity, response.getStatus());
        }
    }

    private void order(final String articleReference, final int quantity) {
        final HttpResponse response = client.order(articleReference, quantity);
        if (response.getStatus() == ACCEPTED) {
            log.info("Order {} '{}' article", quantity, articleReference);
        } else {
            log.error("Order {} '{}' article - failed with error '{}'", quantity, articleReference, response.getStatus());
        }
    }

}
