package com.kaihei.esportingplus.payment.migrate.repository;

import com.kaihei.esportingplus.payment.migrate.entity.YunOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface YunOrderRepository extends JpaRepository<YunOrder, String>, JpaSpecificationExecutor<YunOrder> {

    @Query(value = "select * from payment_yunorder where out_trade_no >?1 order by out_trade_no limit ?2", nativeQuery = true)
    List<YunOrder> findLimit(String outTradeNo, Integer limit);
}
