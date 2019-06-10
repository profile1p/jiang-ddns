package fun.jiangjiang.jiangddns.ip.history.dao;

import fun.jiangjiang.jiangddns.ip.history.domain.IpHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author LLX
 */
public interface IpHistoryDao extends JpaRepository<IpHistory, Long> {

    Optional<IpHistory> findFirstByOrderByUpdateTimeDesc();
}
