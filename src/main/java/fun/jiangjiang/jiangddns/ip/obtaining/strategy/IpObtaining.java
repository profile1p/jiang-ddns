package fun.jiangjiang.jiangddns.ip.obtaining.strategy;

/**
 * @author Lilx
 * @since 2019
 */
public interface IpObtaining {

    String CONFIG_PREFIX = "jiang.ip-obtaining";
    String STRATEGY_CONFIG_NAME = "strategy";

    String obtainIp();
}
