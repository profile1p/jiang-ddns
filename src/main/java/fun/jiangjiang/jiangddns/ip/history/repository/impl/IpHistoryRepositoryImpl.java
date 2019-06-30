package fun.jiangjiang.jiangddns.ip.history.repository.impl;

import fun.jiangjiang.jiangddns.ip.history.dao.IpHistoryDao;
import fun.jiangjiang.jiangddns.ip.history.domain.IpHistory;
import fun.jiangjiang.jiangddns.ip.history.repository.IpHistoryRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author LLX
 */
@Repository
public class IpHistoryRepositoryImpl implements IpHistoryRepository {

    @Resource
    private IpHistoryDao ipHistoryDao;

    @Cacheable("lastIp")
    @Override
    public Optional<IpHistory> lastIpHistory() {
        return ipHistoryDao.findFirstByOrderByCreationTimeDesc();
    }

    @CachePut("lastIp")
    @Override
    public Optional<IpHistory> updateIpHistory(IpHistory ipHistory) {
        return Optional.of(ipHistoryDao.save(ipHistory));
    }
}
