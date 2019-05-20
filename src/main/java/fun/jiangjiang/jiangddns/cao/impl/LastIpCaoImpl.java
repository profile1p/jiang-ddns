package fun.jiangjiang.jiangddns.cao.impl;

import fun.jiangjiang.jiangddns.cao.LastIpCao;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * @author LLX
 */
@Repository
public class LastIpCaoImpl implements LastIpCao {

    @Cacheable("lastIp")
    @Override
    public String getLastIp() {
        return "0.0.0.0";
    }

    @CachePut("lastIp")
    @Override
    public String setLastIp(String lastIp) {
        return lastIp;
    }
}
