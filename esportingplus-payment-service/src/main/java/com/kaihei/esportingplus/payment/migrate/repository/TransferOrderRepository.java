package com.kaihei.esportingplus.payment.migrate.repository;

import com.kaihei.esportingplus.payment.migrate.entity.TransferOrder;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransferOrderRepository extends JpaRepository<TransferOrder, Integer>, JpaSpecificationExecutor<TransferOrder> {

    List<TransferOrder> findByOutTradeNoIn(Collection<String> outTradeNos);

}
