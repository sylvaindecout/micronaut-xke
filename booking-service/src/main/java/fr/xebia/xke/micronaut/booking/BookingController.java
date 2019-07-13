package fr.xebia.xke.micronaut.booking;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static io.micronaut.http.HttpResponse.noContent;
import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Validated
@Controller("booking/articles/{articleReference}")
public class BookingController {

    @Get
    @Produces(APPLICATION_JSON)
    public Stock getStock(@NotBlank final String articleReference) {
        return new Stock(articleReference, 12); //FIXME: get it from DB!
    }

    @Put
    @Consumes(APPLICATION_JSON)
    public HttpResponse setQuantity(@NotBlank final String articleReference, @NotNull @Body final Stock stock) {
        checkArgument(Objects.equals(stock.getArticleReference(), articleReference),
                "Request body (article: '%s') is not consistent with URL (article: '%s')",
                stock.getArticleReference(), articleReference);
        //TODO: update DB with new quantity
        return noContent();
    }

    @Post("order{?quantity}")
    public Stock order(@NotBlank final String articleReference, @Nullable final Integer quantity) {
        final int orderQuantity = quantity == null ? 1 : quantity;
        //TODO: update DB with orderQuantity
        return new Stock(articleReference, 12); //FIXME: get it from DB!
    }

}
