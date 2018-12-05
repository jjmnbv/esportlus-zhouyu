package com.kaihei.esportingplus.resource.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryExpand;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryExpandProperty;
import java.util.List;

public interface DictionaryExpandPropertyMapper extends CommonRepository<DictionaryExpandProperty> {

    List<DictionaryExpand> selectAllDictionaryExpands();
}