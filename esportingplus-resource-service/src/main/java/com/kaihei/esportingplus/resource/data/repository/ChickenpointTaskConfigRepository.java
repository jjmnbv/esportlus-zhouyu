package com.kaihei.esportingplus.resource.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.ChickenpointTaskConfig;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ChickenpointTaskConfigRepository extends CommonRepository<ChickenpointTaskConfig> {

    public List<ChickenpointTaskConfig> selectEfficientChickpointTaskConfig(@Param("currentDate") Date currentDate);
}