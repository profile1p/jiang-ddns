package fun.jiangjiang.jiangddns.ip.flush.strategy;

/**
 * @author Lilx
 * @since 2019
 */
public interface IpFlushing {

    String CONFIG_PREFIX = "jiang.ip-flushing";
    String STRATEGY_CONFIG_NAME = "strategy";

    void flushIp(String ip);
}
