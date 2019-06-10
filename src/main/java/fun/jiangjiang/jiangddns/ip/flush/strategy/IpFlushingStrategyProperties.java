package fun.jiangjiang.jiangddns.ip.flush.strategy;

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
@ConfigurationProperties(IpFlushing.CONFIG_PREFIX)
public class IpFlushingStrategyProperties {

    private String strategy;
}
