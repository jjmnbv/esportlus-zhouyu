package com.kaihei.esportingplus.resource.data.manager.impl;

import com.kaihei.esportingplus.resource.data.manager.AbstractDictManager;
import com.kaihei.esportingplus.resource.data.manager.dao.DictionaryDAO;
import com.kaihei.esportingplus.resource.data.repository.DictionaryMapper;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@DependsOn("redisConfig")
@Component
public class DictionaryDictManager extends AbstractDictManager<Dictionary> {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private DictionaryCategoryDictManager dictionaryCategoryDictManager;

    @Delegate
    @Autowired
    private DictionaryDAO dictionaryDAO;

    @Override
    protected List<Dictionary> loadData() {
        return dictionaryMapper.selectAll();
    }



    /**
     * 获取Redis缓存的Key
     */
    @Override
    protected String getRedisKey() {
        return "resource:cache:Dictionary";
    }


    @Override
    public List<Dictionary> getAll() {
        List<Dictionary> dictionaries = super.getAll();
        dictionaryCategoryDictManager.getAll();
        return dictionaries;
    }

    @Override
    protected void afterLoadedData(List<Dictionary> data) {
        log.info("数据加载完、开始进行后处理");
        //排序
        data.sort(Comparator.comparingInt(Dictionary::getOrderIndex));
        //字典根据Id组成Map
        Map<Integer, Dictionary> dictionaryMap = data.parallelStream()
                .collect(Collectors.toMap(Dictionary::getId, e -> e));

        //字典根据ParentId组成Map
        Map<Integer, List<Dictionary>> parentDictsListMap = data.parallelStream()
                .collect(Collectors.groupingBy(Dictionary::getParentId));

        data.parallelStream().forEach(dictionary -> {
            //设置上级字典元素
            Dictionary parentDict = dictionaryMap.get(dictionary.getParentId());
            dictionary.setParentDictionary(parentDict);

            //设置下级字典元素
            List<Dictionary> childrenDictList = parentDictsListMap.get(dictionary.getId());
            dictionary.setChildDictionary(childrenDictList);
        });

        //重配置 字典的分类信息
        List<DictionaryCategory> dictionaryCategories = dictionaryCategoryDictManager.getAll();
        //字典分类：根据Id组成Map
        Map<Integer, DictionaryCategory> categoryMap = dictionaryCategories.parallelStream()
                .collect(Collectors.toMap(DictionaryCategory::getId, e -> e));

        data.parallelStream().forEach(d -> {
            //重配置字典的字典分类信息
            DictionaryCategory dictionaryCategory = categoryMap.get(d.getCategoryId());
            d.setDictionaryCategory(dictionaryCategory);
        });

        log.info("后处理结束");
    }


}
