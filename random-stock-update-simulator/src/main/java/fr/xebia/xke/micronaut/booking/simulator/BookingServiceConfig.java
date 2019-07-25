package fr.xebia.xke.micronaut.booking.simulator;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties("booking")
public class BookingServiceConfig {

    private String baseUrl;

}
