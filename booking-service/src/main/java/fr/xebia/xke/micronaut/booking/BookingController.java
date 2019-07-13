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
        final ArticleReference article = new ArticleReference(articleReference);
        return new Stock(article, new Quantity(12L)); //FIXME: get it from DB!
    }

    @Put
    @Consumes(APPLICATION_JSON)
    public HttpResponse setQuantity(@NotBlank final String articleReference, @NotNull @Body final Stock stock) {
        final ArticleReference article = new ArticleReference(articleReference);
        checkArgument(Objects.equals(stock.getArticle(), article),
                "Request body (article: %s) is not consistent with URL (article: %s)",
                stock.getArticle(), article);
        //TODO: update DB with new quantity
        return noContent();
    }

    @Post("order{?quantity}")
    public Stock order(@NotBlank final String articleReference, @Nullable final Integer quantity) {
        final ArticleReference article = new ArticleReference(articleReference);
        final int orderQuantity = quantity == null ? 1 : quantity;
        //TODO: update DB with orderQuantity
        return new Stock(article, new Quantity(12L)); //FIXME: get it from DB!
    }

}
