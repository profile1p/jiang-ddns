package fun.jiangjiang.jiangddns.ip.obtaining.strategy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Optional;

/**
 * @author Lilx
 * @since 2019
 */
public class HttpStringIpObtaining implements IpObtaining {

    private static final String CONFIG_NAME = "http-string";
    private URL obtainingUrl;
    private HttpClient httpClient;

    public HttpStringIpObtaining(URL obtainingUrl, Duration connectTimeout) {
        this.obtainingUrl = obtainingUrl;
        httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();
    }

    @Override
    public String obtainIp() {
        // TODO:llx
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
        public IpObtaining ipObtaining() throws MalformedURLException {
            return new HttpStringIpObtaining(new URL(properties.getUrl()),
                    Optional.ofNullable(properties.getConnectTimeout()).orElseGet(() -> Duration.ofSeconds(10)));
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = IpObtaining.CONFIG_PREFIX + CONFIG_NAME)
    public static class HttpStringIpObtainingProperties extends HttpIpObtainingProperties {

        private String url;
    }
}
