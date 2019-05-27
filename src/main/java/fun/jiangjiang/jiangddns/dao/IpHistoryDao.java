package fun.jiangjiang.jiangddns.dao;

import fun.jiangjiang.jiangddns.bean.po.IpHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author LLX
 */
public interface IpHistoryDao extends JpaRepository<IpHistory, Long> {

    Optional<IpHistory> findFirstByOrderByUpdateTimeDesc();
}
