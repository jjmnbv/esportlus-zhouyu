package com.kaihei.esportingplus.payment.migrate.repository;

import com.kaihei.esportingplus.payment.migrate.entity.WechatPayOrder;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author: tangtao
 **/
public interface WechatPayOrderRepository extends JpaRepository<WechatPayOrder, String>, JpaSpecificationExecutor<WechatPayOrder> {

    Integer countByAttachInAndStatusGreaterThan(Collection<String> attach, Integer status);

    Integer countByAttachAndStatusGreaterThan(String attach, Integer status);

    @Query(value = "select * from payment_wechatpayorder where out_trade_no >?3 and attach=?1 and status>?2 order by out_trade_no limit ?4", nativeQuery = true)
    List<WechatPayOrder> findByAttachAndStatusGreaterThan(String attach, Integer status, String startIndex, Integer limit);

    @Query(value = "select * from payment_wechatpayorder where out_trade_no >?3 and attach in (?1) and status>?2 order by out_trade_no limit ?4", nativeQuery = true)
    List<WechatPayOrder> findByAttachInAndStatusGreaterThan(List<String> attaches, Integer status, String startIndex, Integer limit);

}
