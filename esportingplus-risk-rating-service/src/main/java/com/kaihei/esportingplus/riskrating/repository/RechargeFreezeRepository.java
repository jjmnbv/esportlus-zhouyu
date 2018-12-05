package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.RechargeFreeze;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RechargeFreezeRepository extends JpaRepository<RechargeFreeze, Long> {

    List<RechargeFreeze> findByUidIn(List<String> uids);

    Integer deleteByUid(String uid);

    Page<RechargeFreeze> findByUidContaining(String uid, Pageable pageable);
}
