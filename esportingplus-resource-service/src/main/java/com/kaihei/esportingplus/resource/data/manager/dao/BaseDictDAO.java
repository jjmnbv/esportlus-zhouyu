package com.kaihei.esportingplus.resource.data.manager.dao;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.resource.data.manager.DictManager;
import com.kaihei.esportingplus.resource.data.manager.anno.DictCacheRemoveAll;
import com.kaihei.esportingplus.resource.data.manager.anno.DictCacheable;
import com.kaihei.esportingplus.resource.domain.entity.DictEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public abstract class BaseDictDAO<T extends DictEntity> {

    @DictCacheRemoveAll
    public Integer insert(T t) {
        return getRepository().insertSelective(t);
    }

    @DictCacheRemoveAll

    public Integer insert(List<T> ts) {
        return ts.parallelStream().mapToInt(getRepository()::insertSelective).sum();
    }

    @DictCacheRemoveAll

    public Integer updateById(T t) {
        return getRepository().updateByPrimaryKeySelective(t);
    }

    @DictCacheRemoveAll

    public Integer updateById(List<T> ts) {
        return ts.parallelStream().mapToInt(getRepository()::updateByPrimaryKeySelective).sum();
    }

    @DictCacheRemoveAll

    public Integer deleteById(Integer id) {
        return getRepository().deleteByPrimaryKey(id);
    }

    @DictCacheable("${id}")
    public T findById(Integer id) {
        List<T> all = getDictManager().getAll();
        return CollectionUtils.find(all, it -> it.getId().equals(id)).orElse(null);
    }

    protected abstract CommonRepository<T> getRepository();

    protected abstract DictManager<T> getDictManager();
}
