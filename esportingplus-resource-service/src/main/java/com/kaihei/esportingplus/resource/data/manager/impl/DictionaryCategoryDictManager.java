package com.kaihei.esportingplus.resource.data.manager.impl;

import com.kaihei.esportingplus.resource.data.manager.AbstractDictManager;
import com.kaihei.esportingplus.resource.data.manager.dao.DictionaryCategoryDAO;
import com.kaihei.esportingplus.resource.data.repository.DictionaryCategoryMapper;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DependsOn("redisConfig")
public class DictionaryCategoryDictManager extends AbstractDictManager<DictionaryCategory> {


    @Autowired
    private DictionaryCategoryMapper dictionaryCategoryMapper;

    @Delegate
    @Autowired
    private DictionaryCategoryDAO dictionaryCategoryDAO;
    /**
     * Load数据
     */
    @Override
    protected List<DictionaryCategory> loadData() {
        return dictionaryCategoryMapper.selectAll();
    }

    /**
     * 获取Redis缓存的Key
     *
     * 由子类实现
     */
    @Override
    protected String getRedisKey() {
        return "resource:cache:DictionaryCategory";
    }

    @Autowired
    private DictionaryDictManager dictionaryDictManager;

    @Override
    protected void afterLoadedData(List<DictionaryCategory> data) {
        //字典分类：根据Pid组成ListMap
        Map<Integer, List<DictionaryCategory>> childrenListMap = data.parallelStream()
                .collect(Collectors.groupingBy(DictionaryCategory::getParentId));

        //字典分类：根据Id组成Map
        Map<Integer, DictionaryCategory> categoryMap = data.parallelStream()
                .collect(Collectors.toMap(DictionaryCategory::getId, e -> e));

        data.parallelStream().forEach(dc -> {
            //配置上级分类
            dc.setParentDictionaryCategory(categoryMap.get(dc.getParentId()));

            //配置下级分类
            dc.setChildrenDictionaryCategorys(childrenListMap.get(dc.getId()));
        });

        //重配置 字典的分类信息
        List<Dictionary> dictionarys = dictionaryDictManager.getAll();
        dictionarys.parallelStream().forEach(dictionary -> {
            //重配置字典的字典分类信息
            DictionaryCategory dictionaryCategory = categoryMap.get(dictionary.getCategoryId());
            dictionary.setDictionaryCategory(dictionaryCategory);
        });
    }
}
