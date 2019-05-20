package fun.jiangjiang.jiangddns.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author LLX
 */
@Component
@Slf4j
public class DynamicFlushDnsJob {

    @Scheduled(cron = "0 */5 * * * *")
    public void dynamicFlushDns() {

//        log.info();
    }
}
