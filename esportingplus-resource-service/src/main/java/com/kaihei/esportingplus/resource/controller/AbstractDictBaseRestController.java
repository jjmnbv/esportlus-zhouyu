package com.kaihei.esportingplus.resource.controller;


import com.kaihei.esportingplus.api.params.DictionaryUpdateParam;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.resource.data.manager.DictManager;
import com.kaihei.esportingplus.resource.domain.entity.DictEntity;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class AbstractDictBaseRestController<T extends DictEntity> {

    @Autowired
    private List<DictManager<T>> dictManagers;

    private Type suportType;

    private Map<Type, DictManager<T>> cacheManager = new HashMap<>();

    public AbstractDictBaseRestController() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        this.suportType = genericSuperclass.getActualTypeArguments()[0];
    }

    @GetMapping
    public ResponsePacket<List<T>> getAll() {
        return ResponsePacket.onSuccess(this.getSuportManager().getAll());
    }

    protected DictManager<T> getSuportManager() {
        DictManager<T> dictManager = cacheManager.get(suportType);
        if (dictManager != null) {
            return dictManager;
        }

        dictManager = dictManagers.stream().filter(l -> l.suport(suportType)).findFirst()
                .orElse(null);
        cacheManager.put(suportType, dictManager);

        return dictManager;
    }

    /**
     * 通过Id查找字典元素
     */
    @GetMapping("{id}")
    public ResponsePacket<T> findById(@PathVariable Integer id) {
        return ResponsePacket.onSuccess(getSuportManager().findById(id));
    }

    /**
     * 接口：添加字典元素
     */
    @PostMapping
    public ResponsePacket<Integer> addDictElement(@RequestBody T t) {
        return ResponsePacket.onSuccess(getSuportManager().insert(t));
    }

    /**
     * 接口：根据Id删除字典元素
     */
    @DeleteMapping("{id}")
    public ResponsePacket<Integer> delDictElementById(@PathVariable Integer id) {
        return ResponsePacket.onSuccess(getSuportManager().deleteById(id));
    }

    /**
     * 接口：根据Id更新元素
     */
    @PutMapping("{id}")
    public ResponsePacket<Integer> updateElementById(@PathVariable Integer id, @RequestBody T t) {
        t.setId(id);
        return ResponsePacket.onSuccess(getSuportManager().updateById(t));
    }

}
