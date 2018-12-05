//package com.kaihei.esportingplus.resource.data.manager.impl;
//
//import com.kaihei.esportingplus.resource.data.manager.AbstractDictManager;
//import com.kaihei.esportingplus.resource.data.repository.DictionaryExpandPropertyMapper;
//import com.kaihei.esportingplus.resource.data.repository.DictionaryExpandPropertyValueMapper;
//import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
//import com.kaihei.esportingplus.resource.domain.entity.DictionaryExpand;
//import com.kaihei.esportingplus.resource.domain.entity.DictionaryExpandProperty;
//import com.kaihei.esportingplus.resource.domain.entity.DictionaryExpandPropertyValue;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import org.springframework.beans.factory.annotation.Autowired;
//
////@Component
//public class DictionaryExpandDictManager extends AbstractDictManager<DictionaryExpand> {
//
//    @Autowired
//    private DictionaryExpandPropertyMapper dictionaryExpandPropertyMapper;
//
//    @Autowired
//    private DictionaryExpandPropertyValueMapper dictionaryExpandPropertyValueMapper;
//    /**
//     * Load数据
//     */
//    @Override
//    protected List<DictionaryExpand> loadData() {
//        return dictionaryExpandPropertyMapper.selectAllDictionaryExpands();
//    }
//
//    /**
//     * 获取Redis缓存的Key
//     *
//     * 由子类实现
//     */
//    @Override
//    protected String getRedisKey() {
//        return "resource:cache:DictionaryExpand";
//    }
//
//    @Autowired
//    private DictionaryDictManager dictionaryDictManager;
//
//    /**
//     * 操作数据库
//     *
//     * 根据Id删除元素、由子类实现
//     */
//    @Override
//    protected Integer doDeleteById(Integer id) {
//        if (id == null) {
//            return 0;
//        }
//        return dictionaryExpandPropertyMapper.deleteByPrimaryKey(id);
//    }
//
//    /**
//     * 删除扩展属性值
//     */
//    public Integer deleteValueByValueId(Integer id) {
//        int delete = dictionaryExpandPropertyValueMapper.deleteByPrimaryKey(id);
//        //触发重载
//        if (delete != 0) {
//            deleteById(null);
//        }
//        return delete;
//    }
//
//    /**
//     * 数据库更新操作
//     *
//     * 由子类实现
//     */
//    @Override
//    protected Integer doUpdateById(DictionaryExpand dictionaryExpand) {
//        int update = 0;
//        if (dictionaryExpand.getId() != null) {
//            DictionaryExpandProperty dictionaryExpandProperty = DictionaryExpandProperty.builder()
//                    .id(dictionaryExpand.getId())
//                    .name(dictionaryExpand.getName())
//                    .remark(dictionaryExpand.getRemark())
//                    .status(dictionaryExpand.getStatus())
//                    .orderIndex(dictionaryExpand.getOrderIndex())
//                    .gmtModified(new Date())
//                    .build();
//            update = dictionaryExpandPropertyMapper
//                    .updateByPrimaryKeySelective(dictionaryExpandProperty);
//        }
//
//        if (dictionaryExpand.getPropertyValueId() != null && dictionaryExpand.getValue() != null) {
//            DictionaryExpandPropertyValue dictionaryExpandPropertyValue = DictionaryExpandPropertyValue
//                    .builder()
//                    .id(dictionaryExpand.getPropertyValueId())
//                    .propertyValue(dictionaryExpand.getValue())
//                    .gmtModified(new Date())
//                    .build();
//            update = dictionaryExpandPropertyValueMapper
//                    .updateByPrimaryKeySelective(dictionaryExpandPropertyValue);
//        }
//
//        return update;
//    }
//
//    /**
//     * 操作数据库
//     *
//     * 插入一个元素、由子类实现
//     */
//    @Override
//    protected Integer doInsert(DictionaryExpand dictionaryExpand) {
//        int insert = 0;
//        Integer dictionaryExpandId = dictionaryExpand.getId();
//
//        if (dictionaryExpandId == null) {
//            DictionaryExpandProperty dictionaryExpandProperty = DictionaryExpandProperty.builder()
//                    .status(1)
//                    .gmtCreate(new Date())
//                    .orderIndex(dictionaryExpand.getOrderIndex())
//                    .dictionaryId(dictionaryExpand.getDictionaryId())
//                    .remark(dictionaryExpand.getRemark())
//                    .name(dictionaryExpand.getName())
//                    .build();
//            insert = dictionaryExpandPropertyMapper.insertSelective(dictionaryExpandProperty);
//            dictionaryExpandId = dictionaryExpandProperty.getId();
//        }
//
//        if (dictionaryExpand.getValue() == null) {
//            return insert;
//        }
//
//        DictionaryExpandPropertyValue dictionaryExpandPropertyValue = DictionaryExpandPropertyValue
//                .builder()
//                .propertyId(dictionaryExpandId)
//                .gmtCreate(new Date())
//                .propertyValue(dictionaryExpand.getValue())
//                .build();
//        insert = dictionaryExpandPropertyValueMapper
//                .insertSelective(dictionaryExpandPropertyValue);
//
//        return insert;
//    }
//
//    @Override
//    protected void afterLoadedData(List<DictionaryExpand> data) {
//        //排序
//        data.sort(Comparator.comparingInt(DictionaryExpand::getOrderIndex)
//                .thenComparingInt(DictionaryExpand::getPropertyValueId));
//        //根据字典Id分类
//        Map<Integer, List<DictionaryExpand>> dictionaryExpandListMap = data.parallelStream()
//                .collect(Collectors.groupingBy(DictionaryExpand::getDictionaryId));
//
//        List<Dictionary> dictionaries = dictionaryDictManager.getAll();
//        dictionaries.parallelStream().forEach(dictionary -> dictionary
//                .setExpandProperties(dictionaryExpandListMap.get(dictionary.getId())));
//    }
//}
