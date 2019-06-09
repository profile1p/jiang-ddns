package fun.jiangjiang.jiangddns.ip.obtaining.strategy;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

/**
 * @author Lilx
 * @since 2019
 */
@Getter
@Setter
public abstract class HttpIpObtainingProperties {

    private Duration connectTimeout;
}
