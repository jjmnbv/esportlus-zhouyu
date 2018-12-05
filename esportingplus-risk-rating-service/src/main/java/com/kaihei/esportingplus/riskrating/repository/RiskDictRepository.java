package com.kaihei.esportingplus.riskrating.repository;

import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskDictRepository extends JpaRepository<RiskDict, Long> {
    List<RiskDict> findByGroupCode(String groupCode);
    RiskDict findByGroupCodeAndItemCode(String groupCode,String itemCode);

    List<RiskDict> findByIdIn(List<Long> ids);
}
