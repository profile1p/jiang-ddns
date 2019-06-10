package fun.jiangjiang.jiangddns.ip.obtaining.strategy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Lilx
 * @since 2019
 */
@SuppressWarnings("ConfigurationProperties")
@Getter
@Setter
@ConfigurationProperties(prefix = IpObtaining.CONFIG_PREFIX)
public class IpObtainingStrategyProperties {

    private String strategy;
}
