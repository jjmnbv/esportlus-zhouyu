package com.kaihei.esportingplus.payment.migrate.repository;

import com.kaihei.esportingplus.payment.migrate.entity.QQPayOrder;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface QQPayOrderRepository extends JpaRepository<QQPayOrder, String>, JpaSpecificationExecutor<QQPayOrder> {

    Integer countByAttachInAndStatusGreaterThan(Collection<String> attach, Integer status);

    Integer countByAttachAndStatusGreaterThan(String attach, Integer status);

    @Query(value = "select * from payment_qqorder where out_trade_no >?3 and attach=?1 and status>?2 order by out_trade_no limit ?4", nativeQuery = true)
    List<QQPayOrder> findByAttachAndStatusGreaterThan(String attach, Integer status, String startIndex, Integer limit);

    @Query(value = "select * from payment_qqorder where out_trade_no >?3 and attach in (?1) and status>?2 order by out_trade_no limit ?4", nativeQuery = true)
    List<QQPayOrder> findByAttachInAndStatusGreaterThan(List<String> attaches, Integer status, String startIndex, Integer limit);

}
