package com.kaihei.esportingplus.resource.data.manager.dao;

import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.common.tools.HanyuPinyinUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.resource.data.manager.DictManager;
import com.kaihei.esportingplus.resource.data.manager.anno.DictCacheRemoveAll;
import com.kaihei.esportingplus.resource.data.manager.anno.DictCacheable;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryCategoryDictManager;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.data.repository.DictionaryMapper;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class DictionaryDAO extends BaseDictDAO<Dictionary> {

    @Autowired
    private DictionaryDictManager dictionaryDictManager;

    @Autowired
    private DictionaryCategoryDAO dictionaryCategoryDAO;

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private DictionaryCategoryDictManager dictionaryCategoryDictManager;

    @Override
    protected CommonRepository<Dictionary> getRepository() {
        return dictionaryMapper;
    }

    @Override
    protected DictManager<Dictionary> getDictManager() {
        return dictionaryDictManager;
    }

    /**
     * 自动创建Code,批量插入
     */
    @DictCacheRemoveAll
    public Integer batchInsertWithAutoCreateCode(List<Dictionary> dictionaries) {
        List<Dictionary> list = dictionaries.parallelStream().peek(d -> {
            Dictionary dictionary = null;
            if (d.getParentId() != null) {
                dictionary = this.findById(d.getParentId());
            }

            String code = Optional.ofNullable(dictionary).map(Dictionary::getCode)
                    .orElseGet(
                            () -> Optional.ofNullable(d.getCategoryId())
                                    .map(dictionaryCategoryDAO::findById)
                                    .map(DictionaryCategory::getCode).orElse(null)
                    );

            d.setCode(code + "_"
                    + HanyuPinyinUtils.getPinyinString(d.getName()));
        }).collect(Collectors.toList());

        return insert(list);
    }

    @Transactional
    @DictCacheRemoveAll
    public Integer updateWithAutoCreateCodeById(List<Dictionary> dictionaries) {
        //把id为空的拿来插入
        List<Dictionary> insertDictionaries = dictionaries.stream()
                .filter(f -> f.getDictId() == null)
                .collect(Collectors.toList());
        batchInsertWithAutoCreateCode(insertDictionaries);

        //移除id为空的列表拿来更新
        dictionaries.removeAll(insertDictionaries);
        dictionaries.parallelStream().forEach(this::generateUpdateCode);
        return updateById(dictionaries);
    }

    @Override
    public Dictionary findById(Integer id) {
        return super.findById(id);
    }

    /**
     * 更新时 Code自动生成
     */
    private void generateUpdateCode(Dictionary dictionary) {
        if (StringUtils.isNotBlank(dictionary.getName())) {
            Dictionary target = this.findById(dictionary.getId());
            Dictionary pd = target.getParentDictionary();
            String code = Optional.ofNullable(pd).map(Dictionary::getCode)
                    .orElseGet(
                            () -> Optional.of(target).map(Dictionary::getDictionaryCategory)
                                    .map(DictionaryCategory::getCode).orElse(null)
                    );
            dictionary.setCode(code + "_"
                    + HanyuPinyinUtils.getPinyinString(dictionary.getName()));
        }
    }

    /**
     * 更新时自动根据name更新Code
     */
    @DictCacheRemoveAll
    public Integer updateWithAutoCreateCodeById(Dictionary dictionary) {
        this.generateUpdateCode(dictionary);
        return updateById(dictionary);
    }


    /**
     * 通过字典Code和分类Code查找字典
     */
    @DictCacheable("code:${code}:categoryCode:${categoryCode}")
    public Dictionary findByCodeAndCategoryCode(String code, String categoryCode) {
        return CollectionUtils.find(dictionaryDictManager.getAll(),
                d -> d.getCode().equals(code) && Objects.nonNull(d.getDictionaryCategory())
                        && d.getDictionaryCategory().getCode().equals(categoryCode)).orElse(null);
    }

    /**
     * 通过ids 查询字典
     *
     * @param ids 字典的Id数组
     * @return 返回字典列表
     */
    @DictCacheable("ids:${ids}")
    public List<Dictionary> findByIds(List<Integer> ids) {
        List<Dictionary> all = dictionaryDictManager.getAll();
        return CollectionUtils.finds(all, it -> ids.contains(it.getId()));
    }

    /**
     * 根据数据字典 id 构造数据字典基础属性
     */
    @DictCacheable("dictId:${dictId}:targetClass:${targetClass}")
    public <T> DictBaseVO<T> buildDictBase(Integer dictId, Class<T> targetClass) {
        Dictionary dictionary = findById(dictId);
        DictBaseVO dictBaseVO = BeanMapper.map(dictionary, DictBaseVO.class);
        String value = dictionary.getValue();
        try {
            T t = JacksonUtils.toBeanWithSnakeThrowEx(value, targetClass);
            dictBaseVO.setValue(t);
        } catch (IOException e) {
            log.debug("Jackson序列化数据字典[{}]的value:[{}]异常", dictId, value);
            dictBaseVO.setValue(value);
        }

        if (ObjectTools.isNull(dictBaseVO)) {
            log.error("未查询到id为:[{}]的数据字典数据", dictId);
            throw new BusinessException(BizExceptionEnum.DICT_NOT_FOUND);
        }
        return dictBaseVO;
    }


    @Transactional
    @DictCacheRemoveAll
    public Integer updateWithOutAutoCreateCode(List<Dictionary> dictionaries) {
        //把id为空的拿来插入
        List<Dictionary> insertDictionaries = dictionaries.stream()
                .filter(f -> f.getDictId() == null)
                .collect(Collectors.toList());
        batchInsertWithAutoCreateCode(insertDictionaries);

        //移除id为空的列表拿来更新
        dictionaries.removeAll(insertDictionaries);
        return updateById(dictionaries);
    }

    /**
     * 根据字典分类Code查询字典
     */
    @DictCacheable("categoryCode:${categoryCode}")
    public List<Dictionary> findByCategoryCode(String categoryCode) {
        List<Dictionary> all = dictionaryDictManager.getAll();
        return CollectionUtils
                .finds(all, it -> it.getDictionaryCategory() != null && categoryCode
                        .equals(it.getDictionaryCategory().getCode()));
    }
}
