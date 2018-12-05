package com.kaihei.esportingplus.user.runner;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.commons.cache.thread.NamedThreadFactory;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.data.pyrepository.MembersBeautifulChickenBrandRepository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersBeautifulChickenBrand;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @Title: InitMembersChickenCacheRunner
 * @Description: 初始化用户鸡牌号缓存
 * @date 2018/9/2215:17
 */
@Component
public class InitMembersChickenCacheRunner implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(InitMembersChickenCacheRunner.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    // 一次批量查询数量
    private static final int BATCH_VERIFY_USER_SIZE = 10000;
    private static int corePoolSize = 10;
    private static int maxPoolSize = 100;
    private static int keepAliveSeconds = 60;
    /**
     * 允许缓冲在队列中的任务数 (0:不缓冲、负数：无限大、正数：缓冲的任务数)
     */
    private static int workQueueSize = 10000;

    ThreadPoolExecutor executor;
    List<Future<Boolean>> futures;

    @Autowired
    private MembersBeautifulChickenBrandRepository membersBeautifulChickenBrandRepository;

    @Autowired
    private MembersUserRepository membersUserRepository;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        // 缓存中无鸡牌id信息，则初始化数据
        if (cacheManager.bitcount(UserRedisKey.USER_CHICKEN_AVAILABLE_KEY) <= 0) {
            logger.info("cmd=run >> begin load chickenId to redis.");

            executor = getThreadPoolExecutor();
            futures = new ArrayList<Future<Boolean>>();
            loadChickenCache();

            logger.info("cmd=run >> end load chickenId to redis.");

        }
    }

    private void loadChickenCache() {
        // 分别提交优质鸡牌和已使用鸡牌号加载任务
        submitCallable(ChickenType.BEAUTIFULCHICKEN);
        submitCallable(ChickenType.HAVINGUSEDCHICKEN);

        // 所有任务提交完后关闭线程
        executor.shutdown();
        // 打印加载进度
        printLoadRate();

    }

    private void submitCallable(ChickenType type) {
        int count = getTotalCount(type);
        // 计算需要加载的次数
        int times = count % BATCH_VERIFY_USER_SIZE == 0 ? count / BATCH_VERIFY_USER_SIZE
                : count / BATCH_VERIFY_USER_SIZE + 1;

        for (int i = 0; i < times; i++) {
            //提交任务，并添加到futures中，后续打印加载进度
            futures.add(executor.submit(
                    new LoadChickenIdsToRedisCallable(i * BATCH_VERIFY_USER_SIZE,
                            BATCH_VERIFY_USER_SIZE, type)));
        }

    }

    private int getTotalCount(ChickenType type) {
        switch (type) {
            case BEAUTIFULCHICKEN:
                return membersBeautifulChickenBrandRepository.selectCount(new
                        MembersBeautifulChickenBrand());
            case HAVINGUSEDCHICKEN:
                return membersUserRepository.selectCount(new MembersUser());
            default:
                return 0;
        }
    }

    private ThreadPoolExecutor getThreadPoolExecutor() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("init-chickenid-%d").build();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(workQueueSize), namedThreadFactory);
    }

    enum ChickenType {
        /**
         * 优质鸡牌
         */
        BEAUTIFULCHICKEN,
        /**
         * 已使用鸡牌
         */
        HAVINGUSEDCHICKEN
    }

    /**
     * 加载鸡牌ID到redis任务，根据输入类型，分别加载优质鸡牌和已使用鸡牌
     */
    class LoadChickenIdsToRedisCallable implements Callable<Boolean> {

        private int offset;
        private int limit;
        private ChickenType type;

        public LoadChickenIdsToRedisCallable(int offset, int limit, ChickenType type) {
            this.offset = offset;
            this.limit = limit;
            this.type = type;
        }

        @Override
        public Boolean call() throws Exception {
            final Set<Long> s = new HashSet<>();

            try {
                List<String> list = selectChickenIdList(offset, limit, type);
                //因查询出的chickenId为字符串，bit参数只能为Long，因此需转换chickenId的类型
                if (CollectionUtils.isNotEmpty(list)) {
                    list.forEach(m -> {
                        if (StringUtils.isNotBlank(m)) {
                            s.add(Long.valueOf(m));
                        }
                    });
                    cacheManager.setbitBatch(UserRedisKey.USER_CHICKEN_AVAILABLE_KEY, s, true);
                }
            } catch (Exception e) {
                logger.error("load chickenId to redis error. pageNum:{},pageSize:{},e", offset,
                        limit, e);
                return false;
            } finally {
                if (s != null) {
                    s.clear();
                }
            }
            return true;
        }

        private List<String> selectChickenIdList(int offset, int limit, ChickenType type) {
            List<String> list = null;

            switch (type) {
                case BEAUTIFULCHICKEN:
                    list = membersBeautifulChickenBrandRepository
                            .selectAllChickenIds(offset, limit);
                    break;
                case HAVINGUSEDCHICKEN:
                    list = membersUserRepository.selectAllHavingUsedChickenIds(offset, limit);
                    break;
                default:
                    break;
            }

            return list;
        }
    }

    /**
     * 打印加载进度，每秒输出一次
     */
    private void printLoadRate() {
        Thread thread = NamedThreadFactory.build()
                .newThread("print-loadchicken-rate", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 每秒钟打印执行进度
                            while (!executor.isTerminated()) {
                                executor.awaitTermination(1, TimeUnit.SECONDS);
                                double progress =
                                        (executor.getCompletedTaskCount() * 100.0) / executor
                                                .getTaskCount();
                                logger.info(
                                        progress + "% done (" + executor.getCompletedTaskCount()
                                                + " init chickenCache.");
                            }

                        } catch (Exception e) {
                            logger.error("init chickenCache error", e);
                        }
                    }
                });
        thread.start();
    }

}
