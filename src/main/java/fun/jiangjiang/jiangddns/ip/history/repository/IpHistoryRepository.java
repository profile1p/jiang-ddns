package fun.jiangjiang.jiangddns.ip.history.repository;

import fun.jiangjiang.jiangddns.ip.history.domain.IpHistory;
import java.util.Optional;

/**
 * @author LLX
 */
public interface IpHistoryRepository {

    Optional<IpHistory> lastIpHistory();

    Optional<IpHistory> updateIpHistory(IpHistory ipHistory);
}
