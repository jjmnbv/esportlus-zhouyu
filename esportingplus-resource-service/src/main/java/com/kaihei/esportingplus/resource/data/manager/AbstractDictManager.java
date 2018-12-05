package com.kaihei.esportingplus.resource.data.manager;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.resource.domain.entity.DictEntity;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * @author 谢思勇
 * @date 2018年10月2日 17:24:55
 *
 * 抽象类、
 *
 * 实现该类的 {@link #loadData()} 告诉Loader怎么从数据库加载数据
 *
 * 实现该类的{@link #getRedisKey()} 告诉Loader数据Redis的缓存Key是什么
 *
 * 调用{@link #getAll()}方法，以获取需要的数据
 *
 * 子类只需关注实现从Mysql 实现数据的CRUD即可
 */
public abstract class AbstractDictManager<T extends DictEntity> implements DictManager<T> {

    /**
     * 查询缓存器
     */
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected CacheManager cacheManager = CacheManagerFactory.create();
    private AtomicReference<List<T>> cacheHolder = new AtomicReference<>();
    /**
     * 是否需要重新Load数据
     */
    private AtomicBoolean reloadTag = new AtomicBoolean(true);
    /**
     * 支付类型
     */
    private Type suportType;

    public AbstractDictManager() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        this.suportType = parameterizedType.getActualTypeArguments()[0];
        //启动程序一个程序就刷新一次缓存
        cacheManager.del(getRedisKey());
        RedissonClient redissonClient = cacheManager.redissonClient();
        RTopic<String> topic = redissonClient.getTopic(getRedisKey() + ":refresh");
        topic.publish("reload");
    }

    @Override
    public Type getSuportType() {
        return suportType;
    }

    @Override
    public List<T> getAll() {
        if (shouldReload()) {
            reload();
        }
        return cacheHolder.get();
    }

    /**
     * 重新加载
     */
    private void reload() {
        beforeLoadData();
        log.info("开始加载数据");
        List<T> data = this.loadRedisData();
        if (data == null) {
            log.info("开始从数据库Load数据");
            data = this.loadData();
            this.cacheData(data);
            log.info("从数据库Load数据结束");
        }
        this.cacheHolder.set(data);
        log.info("加载数据结束");
        afterLoadedData(cacheHolder.get());
    }

    protected void beforeLoadData() {

    }

    protected void afterLoadedData(List<T> data) {
    }

    /**
     * Load数据
     */
    protected abstract List<T> loadData();

    private List<T> loadRedisData() {
        log.info("开始尝试从Redis加载数据");
        byte[] bytes = cacheManager.get(getRedisKey().getBytes());
        if (bytes == null) {
            log.info("Redis没有数据");
            return null;
        }

        return JSON.parseObject(bytes, ParameterizedTypeImpl
                .make(List.class, new Type[]{getSuportType()},
                        List.class.getDeclaringClass()));
    }


    private void cacheData(List<T> data) {
        log.info("开始缓存到Redis");
        cacheManager.set(getRedisKey(), JSON.toJSONString(data));
        log.info("缓存到Redis结束");
    }

    /**
     * 获取Redis缓存的Key
     *
     * 由子类实现
     */
    protected abstract String getRedisKey();

    /**
     * 是否是支持获取的类型
     */
    @Override
    public boolean suport(Type type) {
        return this.getSuportType().equals(type);
    }

    /**
     * 是否需要重新加载数据
     */
    private boolean shouldReload() {
        return reloadTag.getAndSet(false);
    }

    /**
     * 发送Redis信息
     *
     * 把集群需要刷新缓存标识设为true
     */
    @Override
    public void resetReloadTag() {
        this.reloadTag.set(true);
    }

}
