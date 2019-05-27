package fun.jiangjiang.jiangddns.repository;

import fun.jiangjiang.jiangddns.bean.po.IpHistory;
import java.util.Optional;

/**
 * @author LLX
 */
public interface IpHistoryRepository {

    Optional<IpHistory> lastIpHistory();

    Optional<IpHistory> updateIpHistory(IpHistory ipHistory);
}
