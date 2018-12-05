package com.kaihei.esportingplus.resource.data.manager.dao;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.resource.data.manager.DictManager;
import com.kaihei.esportingplus.resource.data.manager.anno.DictCacheable;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryCategoryDictManager;
import com.kaihei.esportingplus.resource.data.repository.DictionaryCategoryMapper;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DictionaryCategoryDAO extends BaseDictDAO<DictionaryCategory> {

    @Autowired
    private DictionaryCategoryDictManager dictionaryCategoryDictManager;

    @Autowired
    private DictionaryCategoryMapper dictionaryCategoryMapper;

    @Override
    protected CommonRepository<DictionaryCategory> getRepository() {
        return dictionaryCategoryMapper;
    }

    @Override
    protected DictManager<DictionaryCategory> getDictManager() {
        return dictionaryCategoryDictManager;
    }

    /**
     * 通过字典分类Code 查找字典分类
     */
    @DictCacheable("categoryCode:${categoryCode}")
    public DictionaryCategory findByCode(final String categoryCode) {
        List<DictionaryCategory> all = dictionaryCategoryDictManager.getAll();

        return CollectionUtils.find(all, it -> it.getCode().equals(categoryCode)).orElse(null);
    }

    @Override
    public DictionaryCategory findById(Integer id) {
        return super.findById(id);
    }
}
