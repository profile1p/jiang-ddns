package fun.jiangjiang.jiangddns.ip.obtaining.strategy;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

/**
 * @author Lilx
 * @since 2019
 */
@Slf4j
public class HttpStringIpObtaining implements IpObtaining {

    private static final String CONFIG_NAME = "http-string";
    private URI obtainingUri;
    private HttpClient httpClient;
    private HttpRequest.Builder httpRequestBuilder;

    private HttpStringIpObtaining(HttpStringIpObtainingProperties properties) throws URISyntaxException {
        this.obtainingUri = properties.getUrl().toURI();
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Optional.ofNullable(properties.getConnectTimeout()).orElseGet(() -> Duration.ofSeconds(10)))
                .build();
        httpRequestBuilder = HttpRequest.newBuilder().GET();
    }

    @Override
    public String obtainIp() {
        try {
            return httpClient.send(httpRequestBuilder.uri(obtainingUri).build(),
                    HttpResponse.BodyHandlers.ofString()
            ).body();
        } catch (IOException | InterruptedException e) {
            log.error("obtain IP address failed.", e);
        }
        return null;
    }

    @Configuration
    @EnableConfigurationProperties(HttpStringIpObtainingProperties.class)
    @ConditionalOnProperty(prefix = IpObtaining.CONFIG_PREFIX,
            name = IpObtaining.STRATEGY_CONFIG_NAME,
            havingValue = CONFIG_NAME)
    public static class HttpStringIpObtainingConfiguration {

        private final HttpStringIpObtainingProperties properties;

        public HttpStringIpObtainingConfiguration(HttpStringIpObtainingProperties properties) {
            this.properties = properties;
        }

        @Bean
        public IpObtaining ipObtaining() throws URISyntaxException {
            return new HttpStringIpObtaining(properties);
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties(IpObtaining.CONFIG_PREFIX + "." + CONFIG_NAME)
    public static class HttpStringIpObtainingProperties extends HttpIpObtainingProperties {

    }
}
