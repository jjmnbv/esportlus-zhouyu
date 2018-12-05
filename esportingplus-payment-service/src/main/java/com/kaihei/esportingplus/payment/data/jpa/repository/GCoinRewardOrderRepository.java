package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.GCoinRewardOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GCoinRewardOrderRepository extends JpaRepository<GCoinRewardOrder, Long>, JpaSpecificationExecutor<GCoinRewardOrder> {

}
