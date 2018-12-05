package com.kaihei.esportingplus.payment.migrate.repository;

import com.kaihei.esportingplus.payment.migrate.entity.RefundOrder;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RefundOrderRepository extends JpaRepository<RefundOrder, Integer>, JpaSpecificationExecutor<RefundOrder> {

    Integer countByRefundWayInAndVerifyStatus(Collection<Integer> refundWay, Integer verifyStatus);

    @Query(value = "select * from payment_refund where id >?3 and refund_way in (?1) and verify_status=?2 order by id limit ?4", nativeQuery = true)
    List<RefundOrder> findByRefundWayInAndVerifyStatus(List<Integer> refundWay, Integer verifyStatus, Integer startId, Integer limit);
}
