package fun.jiangjiang.jiangddns.repository.impl;

import fun.jiangjiang.jiangddns.bean.po.IpHistory;
import fun.jiangjiang.jiangddns.dao.IpHistoryDao;
import fun.jiangjiang.jiangddns.repository.IpHistoryRepository;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

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
        return ipHistoryDao.findFirstByOrderByUpdateTimeDesc();
    }

    @CachePut("lastIp")
    @Override
    public Optional<IpHistory> updateIpHistory(IpHistory ipHistory) {
        return Optional.of(ipHistoryDao.save(ipHistory));
    }
}
