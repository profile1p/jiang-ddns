package fun.jiangjiang.jiangddns.ip.obtaining.strategy;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.Duration;

/**
 * @author Lilx
 * @since 2019
 */
@Getter
@Setter
public abstract class HttpIpObtainingProperties {

    /**
     * URL to obtaining IP address, full url includes http/https protocol words
     */
    private URL url;
    /**
     * HTTP connection timeout
     */
    private Duration connectTimeout;
}
