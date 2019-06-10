package fun.jiangjiang.jiangddns.ip.flush.strategy;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Lilx
 * @since 2019
 */
public class AliyunIpFlushing implements IpFlushing {

    private static final String CONFIG_NAME = "aliyun";

    private IAcsClient acsClient;

    private AliyunIpFlushing(AliyunIpFlushingProperties properties) {
        IClientProfile acsClientProfile = DefaultProfile.getProfile(
                Optional.ofNullable(properties.getRegionId()).orElse("cn-hangzhou"),
                Objects.requireNonNull(properties.getAccessKeyId()),
                Objects.requireNonNull(properties.getAccessKeySecret())
        );
        acsClient = new DefaultAcsClient(acsClientProfile);
    }

    @Override
    public void flushIp(String ip) {
        // TODO:llx 先查询后根据条件过滤需要更新的条目的recordId并更新
        DescribeDomainRecordsResponse describeResponse = new DescribeDomainRecordsResponse();
        UpdateDomainRecordRequest updateRequest = new UpdateDomainRecordRequest();

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
        // TODO:llx 增加过滤条件
    }
}
