package fun.jiangjiang.jiangddns.ip.history.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LLX
 */
@Getter
@Setter
@Entity
@Table(name = "ip_history", indexes = @Index(columnList = "updateTime"))
public class IpHistory {

    @Id
    @TableGenerator(name = "table_generator",
            table = "t_id_generator",
            pkColumnName = "id_name",
            pkColumnValue = "ip_history_id",
            valueColumnName = "id_value",
            initialValue = 1000000,
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_generator")
    private Long ipHistoryId;

    @Column(length = 16)
    private String currentIpAddress;

    @Column
    private LocalDateTime updateTime;
}
