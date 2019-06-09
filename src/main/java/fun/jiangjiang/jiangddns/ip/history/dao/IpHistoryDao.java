package fun.jiangjiang.jiangddns.ip.history.dao;

import fun.jiangjiang.jiangddns.ip.history.domain.IpHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author LLX
 */
public interface IpHistoryDao extends JpaRepository<IpHistory, Long> {

    Optional<IpHistory> findFirstByOrderByUpdateTimeDesc();
}
