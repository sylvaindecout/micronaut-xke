package fr.xebia.xke.micronaut.pricing.controller;

import fr.xebia.xke.micronaut.pricing.domain.ArticleReference;
import fr.xebia.xke.micronaut.pricing.domain.Price;
import fr.xebia.xke.micronaut.pricing.domain.PricingService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Slf4j
@Validated
@Controller("pricing/articles/{articleReference}")
public class PricingController {

    private final PricingService service;

    PricingController(final PricingService service) {
        this.service = service;
    }

    @Get
    @Produces(APPLICATION_JSON)
    public Flowable<Long> computePrice(@NotBlank final String articleReference) {
        log.info("Computing price for {}", articleReference);
        return service.computePrice(new ArticleReference(articleReference))
                .map(Price::getValueAsCents);
    }

}
