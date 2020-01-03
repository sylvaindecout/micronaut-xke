package fr.xebia.xke.micronaut.catalogue.controller;

import fr.xebia.xke.micronaut.catalogue.domain.Catalogue;
import fr.xebia.xke.micronaut.catalogue.domain.CatalogueService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.validation.Validated;
import lombok.extern.slf4j.Slf4j;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Slf4j
@Validated
@Controller("catalogue/articles")
public class CatalogueController {

    private final CatalogueService service;

    CatalogueController(final CatalogueService service) {
        this.service = service;
    }

    @Get
    @Produces(APPLICATION_JSON)
    public Catalogue getCatalogue() {
        log.info("Accessing catalogue");
        return service.getCatalogue();
    }

}
