package com.kaihei.esportingplus.resource.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.ConfigBaojiLevel;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;

public interface ConfigBaojiLevelRepository extends CommonRepository<ConfigBaojiLevel> {
    public ConfigBaojiLevel selectByBaojiLevel(@Param("baojiLevel") Integer baojiLevel);
}