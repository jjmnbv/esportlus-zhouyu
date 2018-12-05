package com.kaihei.esportingplus.customer.center.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.customer.center.domain.entity.CompaintItemPicture;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CompaintItemPictureRepository extends CommonRepository<CompaintItemPicture> {
    void insertCompaintItemPicture(List<CompaintItemPicture> list);

    List<CompaintItemPicture> selectCompaintItemPicturesByCompaintId(
        @Param("compaintId") Integer compaintId);
}