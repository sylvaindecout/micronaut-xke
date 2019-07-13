package fr.xebia.xke.micronaut.booking;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Map;

import static io.micronaut.http.HttpRequest.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class InfoTest {

    @Inject
    @Client("/")
    private HttpClient client;

    @Test
    void should_expose_GIT_commit_details_in_info_endpoint() {
        final HttpResponse<Map> response = client.toBlocking()
                .exchange(GET("/info"), Map.class);

        assertEquals(response.status().getCode(), 200);

        final Map<?, ?> json = response.body();

        assertNotNull(json.get("git"));
        assertNotNull(((Map) json.get("git")).get("commit"));
        assertNotNull(((Map) ((Map) json.get("git")).get("commit")).get("message"));
        assertNotNull(((Map) ((Map) json.get("git")).get("commit")).get("time"));
        assertNotNull(((Map) ((Map) json.get("git")).get("commit")).get("id"));
        assertNotNull(((Map) ((Map) json.get("git")).get("commit")).get("user"));
        assertNotNull(((Map) json.get("git")).get("branch"));
    }

}
