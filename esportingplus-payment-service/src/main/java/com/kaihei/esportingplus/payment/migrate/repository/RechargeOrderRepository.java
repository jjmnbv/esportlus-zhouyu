package com.kaihei.esportingplus.payment.migrate.repository;

import com.kaihei.esportingplus.payment.migrate.entity.RechargeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RechargeOrderRepository extends JpaRepository<RechargeOrder, Integer>, JpaSpecificationExecutor<RechargeOrder> {

//    Stream<RechargeOrder> findByOidIn(Collection<String> oids);
}
