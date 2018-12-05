package com.kaihei.esportingplus.core.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.core.domain.entity.PushMessageRecord;

import java.util.List;

/**
 * @program: esportingplus
 * @description:
 * @author: xusisi
 * @create: 2018-12-01 16:55
 **/
public interface PushMessageRecordRepository extends CommonRepository<PushMessageRecord> {

    public Integer insertRecord(PushMessageRecord record);

    public PushMessageRecord selectById(Integer id);

    public List<PushMessageRecord> selectRecords();

    public Integer getTotalCount();

}
