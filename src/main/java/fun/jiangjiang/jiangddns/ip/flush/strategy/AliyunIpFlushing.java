package fun.jiangjiang.jiangddns.ip.flush.strategy;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

/**
 * @author Lilx
 * @since 2019
 */
@Slf4j
@Service
public class AliyunIpFlushing implements IpFlushing {

    private static final String CONFIG_NAME = "aliyun";

    private AliyunIpFlushingProperties properties;
    private IAcsClient acsClient;

    private AliyunIpFlushing(AliyunIpFlushingProperties properties) {
        this.properties = properties;
        IClientProfile acsClientProfile = DefaultProfile.getProfile(
                Optional.ofNullable(properties.getRegionId()).orElse("cn-hangzhou"),
                Objects.requireNonNull(properties.getAccessKeyId()),
                Objects.requireNonNull(properties.getAccessKeySecret())
        );
        acsClient = new DefaultAcsClient(acsClientProfile);
    }

    @Override
    public void flushIp(String ip) {
        ip = ip.replaceAll("[^0-9.]+", "");
        AliyunIpFlushingProperties.DomainRecordsCondition condition = properties.getCondition();
        DescribeDomainRecordsRequest describeRequest = new DescribeDomainRecordsRequest();
        describeRequest.setDomainName(condition.getDomainName());
        describeRequest.setPageSize(condition.getPageSize());
        describeRequest.setRRKeyWord(condition.getRrKeyWord());
        describeRequest.setTypeKeyWord(condition.getTypeKeyWord());
        describeRequest.setValueKeyWord(condition.getValueKeyWord());
        try {
            DescribeDomainRecordsResponse describeResponse;
            List<DescribeDomainRecordsResponse.Record> domainRecordList = new ArrayList<>(condition.getPageSize().intValue());
            LongAdder pageNumber = new LongAdder();
            do {
                pageNumber.increment();
                describeRequest.setPageNumber(pageNumber.longValue());
                describeResponse = acsClient.getAcsResponse(describeRequest);
                if (CollectionUtils.isNotEmpty(describeResponse.getDomainRecords())) {
                    domainRecordList.addAll(describeResponse.getDomainRecords());
                }
            } while (describeResponse.getTotalCount() > domainRecordList.size()
                    && CollectionUtils.isNotEmpty(describeResponse.getDomainRecords())
                    && describeResponse.getDomainRecords().size() == describeResponse.getPageSize());
            flushIp(domainRecordList, condition, ip);
        } catch (ClientException e) {
            log.error("flush DNS failed.", e);
            throw new RuntimeException(e);
        }
    }

    private void flushIp(List<DescribeDomainRecordsResponse.Record> recordList,
                         AliyunIpFlushingProperties.DomainRecordsCondition condition,
                         String ip) {
        Stream<DescribeDomainRecordsResponse.Record> recordStream = recordList.stream();
        if (StringUtils.isNotBlank(condition.getRemarkRegex())) {
            recordStream = recordStream.filter(record -> Optional.ofNullable(record)
                    .map(DescribeDomainRecordsResponse.Record::getRemark)
                    .map(remark -> remark.matches(condition.getRemarkRegex()))
                    .orElse(false));
        }
        recordStream.map(record -> {
            UpdateDomainRecordRequest updateRequest = new UpdateDomainRecordRequest();
            updateRequest.setRecordId(record.getRecordId());
            updateRequest.setRR(record.getRR());
            updateRequest.setType(record.getType());
            updateRequest.setValue(ip);
            updateRequest.setTTL(record.getTTL());
            updateRequest.setPriority(record.getPriority());
            updateRequest.setLine(record.getLine());
            return updateRequest;
        }).forEach(updateRequest -> {
            try {
                acsClient.getAcsResponse(updateRequest);
                log.info("flush ip to [".concat(updateRequest.getValue()).concat("] succeed"));
            } catch (ClientException e) {
                log.error("flush ip to [".concat(updateRequest.getValue()).concat("] failed"));
                log.error(updateRequest.toString(), e);
            }
        });
    }

    @Configuration
    @EnableConfigurationProperties(AliyunIpFlushingProperties.class)
    @ConditionalOnProperty(prefix = IpFlushing.CONFIG_PREFIX,
            name = IpFlushing.STRATEGY_CONFIG_NAME,
            havingValue = CONFIG_NAME)
    public static class AliyunIpFlushingConfiguration {

        private AliyunIpFlushingProperties properties;

        public AliyunIpFlushingConfiguration(AliyunIpFlushingProperties properties) {
            this.properties = properties;
        }

        @Bean
        public IpFlushing ipFlushing() {
            return new AliyunIpFlushing(properties);
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties(IpFlushing.CONFIG_PREFIX + "." + CONFIG_NAME)
    public static class AliyunIpFlushingProperties {

        private String regionId;
        private String accessKeyId;
        private String accessKeySecret;
        @NestedConfigurationProperty
        private DomainRecordsCondition condition = new DomainRecordsCondition();

        @Getter
        @Setter
        private static class DomainRecordsCondition {

            private String domainName;
            private Long pageSize = 50L;
            private String rrKeyWord;
            private String typeKeyWord;
            private String valueKeyWord;
            private String remarkRegex;
        }
    }
}
