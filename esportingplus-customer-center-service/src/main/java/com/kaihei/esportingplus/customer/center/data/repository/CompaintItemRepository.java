package com.kaihei.esportingplus.customer.center.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.customer.center.domain.entity.CompaintItem;
import org.apache.ibatis.annotations.Param;

public interface CompaintItemRepository extends CommonRepository<CompaintItem> {
    void insertCompaintItem(CompaintItem compaintItem);

    CompaintItem selectByCompaintId(@Param("compaintId") Integer compaintId);
}