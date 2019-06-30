package fun.jiangjiang.jiangddns.scheduling;

import fun.jiangjiang.jiangddns.ip.flush.strategy.IpFlushing;
import fun.jiangjiang.jiangddns.ip.history.domain.IpHistory;
import fun.jiangjiang.jiangddns.ip.history.repository.IpHistoryRepository;
import fun.jiangjiang.jiangddns.ip.obtaining.strategy.IpObtaining;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author LLX
 */
@Component
@Slf4j
public class DynamicFlushDnsTask {

    @Resource
    private IpHistoryRepository ipHistoryRepository;
    @Resource
    private IpObtaining ipObtaining;
    @Resource
    private IpFlushing ipFlushing;

    @Scheduled(cron = "${jiang.dynamic-flush-dns.cron}")
    public void dynamicFlushDns() {
        final String newIp = ipObtaining.obtainIp();
        ipHistoryRepository.lastIpHistory()
                .map(IpHistory::getIpAddress)
                .filter(newIp::equals)
                .ifPresentOrElse(
                        oldIp -> log.debug("flush dns did not run.caused by ip is not changed"),
                        () -> {
                            ipFlushing.flushIp(newIp);
                            IpHistory ipHistory = IpHistory.builder().ipAddress(newIp).build();
                            ipHistoryRepository.updateIpHistory(ipHistory);
                            log.info("flush dns succeed with ip [".concat(newIp).concat("]"));
                        }
                );
    }
}
