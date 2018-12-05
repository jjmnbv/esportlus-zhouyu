package com.kaihei.esportingplus.resource.data.repository;

import com.kaihei.esportingplus.resource.domain.entity.Resource;

public interface ResourceRepository {
    int deleteByPrimaryKey(Long id);

    int insert(Resource record);

    int insertSelective(Resource record);

    Resource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);
}