package com.kaihei.esportingplus.resource.data.repository;

import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;

public interface DictionaryMapper extends CommonRepository<Dictionary> {

    /**
     * 根据 id 查询数据字典基础 vo
     * @param dictId
     * @return
     */
    DictBaseVO selectDictById(Integer dictId);
}