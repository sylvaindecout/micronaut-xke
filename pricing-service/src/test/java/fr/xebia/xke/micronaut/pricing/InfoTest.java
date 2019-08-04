package fr.xebia.xke.micronaut.pricing;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Map;

import static io.micronaut.http.HttpRequest.GET;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class InfoTest {

    @Inject
    @Client("/")
    private HttpClient client;

    @Test
    void should_expose_health_endpoint() {
        final HttpResponse<Map> response = client.toBlocking()
                .exchange(GET("/health"), Map.class);

        assertThat(response.status().getCode()).isEqualTo(200);
        assertThat(response.body().get("status")).isEqualTo("UP");
    }

    @Test
    void should_expose_GIT_commit_details_in_info_endpoint() {
        final HttpResponse<Map> response = client.toBlocking()
                .exchange(GET("/info"), Map.class);

        assertThat(response.status().getCode()).isEqualTo(200);
        assertThatResponseBodyContainsCommitInfo(response.body());
    }

    private static void assertThatResponseBodyContainsCommitInfo(final Map<Object, Object> body) {
        assertThat(body).isNotNull();
        assertThat(body).containsKey("git");
        final Map<Object, Object> gitInfo = getChild(body, "git");
        assertThat(gitInfo).containsKeys("commit", "branch");
        final Map<Object, Object> commitInfo = getChild(gitInfo, "commit");
        assertThat(commitInfo).containsKeys("message", "time", "id", "user");
    }

    private static Map<Object, Object> getChild(final Map<Object, Object> parent, final String key) {
        return (Map<Object, Object>) parent.get(key);
    }

}
