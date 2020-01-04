package fr.xebia.xke.micronaut.booking.controller;

import fr.xebia.xke.micronaut.booking.domain.ArticleReference;
import fr.xebia.xke.micronaut.booking.domain.BookingService;
import fr.xebia.xke.micronaut.booking.domain.Quantity;
import fr.xebia.xke.micronaut.booking.domain.Stock;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static io.micronaut.http.HttpResponse.accepted;
import static io.micronaut.http.HttpResponse.noContent;
import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Slf4j
@Validated
@Controller("booking/articles/{articleReference}")
public class BookingController {

    private final BookingService service;

    BookingController(final BookingService service) {
        this.service = service;
    }

    @Get
    @Produces(APPLICATION_JSON)
    public Flowable<Stock> getStock(@NotBlank final String articleReference) {
        log.info("Accessing stock for {}", articleReference);
        return service.getStock(new ArticleReference(articleReference));
    }

    @Put
    @Consumes(APPLICATION_JSON)
    public HttpResponse setQuantity(@NotBlank final String articleReference, @NotNull @Body final Stock stock) {
        log.info("Received stock update for {}: {}", articleReference, stock);
        final ArticleReference article = new ArticleReference(articleReference);
        checkArgument(Objects.equals(stock.getArticle(), article),
                "Request body (article: %s) is not consistent with URL (article: %s)",
                stock.getArticle(), article);
        service.updateStock(stock);
        return noContent();
    }

    @Post("order{?quantity}")
    public HttpResponse order(@NotBlank final String articleReference, @Nullable @QueryValue(defaultValue = "1") final Integer quantity) {
        log.info("Received order for {} {}", quantity, articleReference);
        service.order(
                new ArticleReference(articleReference),
                new Quantity(quantity.longValue())
        );
        return accepted();
    }

}
