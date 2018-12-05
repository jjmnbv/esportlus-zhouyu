package com.kaihei.esportingplus.resource.data.manager;

import com.kaihei.esportingplus.resource.domain.entity.DictEntity;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 定义数据库操作
 *
 * 定义的操作由抽象类间接完成、实现类真实完成
 */
public interface DictManager<T extends DictEntity> {


    /**
     * 加载数据
     */
    List<T> getAll();

    /**
     * 支持类型
     */
    boolean suport(Type type);

    Type getSuportType();

    void resetReloadTag();

    /**
     * 根据Id更新
     */
    default Integer updateById(T t) {
        return null;
    }

    default Integer updateById(List<T> ts) {
        return null;
    }

    /**
     * 根据Id删除
     */
    default Integer deleteById(Integer id) {
        return null;
    }

    /**
     * 插入一条数据
     */
    default Integer insert(T t) {
        return null;
    }

    default Integer insert(List<T> ts) {
        return null;
    }

    /**
     * 根据Id查找
     */
    default T findById(Integer id) {
        return null;
    }
}
