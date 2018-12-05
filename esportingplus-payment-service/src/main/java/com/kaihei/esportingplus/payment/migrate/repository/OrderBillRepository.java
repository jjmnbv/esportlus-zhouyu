package com.kaihei.esportingplus.payment.migrate.repository;

import com.kaihei.esportingplus.payment.migrate.entity.OrderBill;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: tangtao
 **/
public interface OrderBillRepository extends JpaRepository<OrderBill, Integer>, JpaSpecificationExecutor<OrderBill> {

    List<OrderBill> findByOrderIdInOrderByCreateTimeDesc(Collection<String> orderIds);

    List<OrderBill> findByOrderIdInAndOrderTypeOrderByCreateTimeDesc(Collection<String> orderIds, String orderType);

    List<OrderBill> findByOrderIdInAndBillTypeInAndOrderTypeOrderByCreateTimeDesc(Collection<String> orderIds, Collection<Integer> billTypes,
            String orderType);
}
