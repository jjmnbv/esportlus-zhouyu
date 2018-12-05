package com.kaihei.esportingplus.marketing.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.kaihei.esportingplus.marketing.data.repository.MarketUserFreeCouponsRepository;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserFreeCouponsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zl.zhao
 * @description:定时清理用户过期免费券Job
 * @date: 2018/11/21 16:05
 */
@Component
public class CleanUserFreeCouponsJob implements SimpleJob {

    private static Logger logger = LoggerFactory.getLogger(CleanUserFreeCouponsJob.class);

    // 一次批量处理数量
    private static final int BATCH_CLEAN_SIZE = 1000;

    @Autowired
    private MarketUserFreeCouponsService marketUserFreeCouponsService;

    @Autowired
    private MarketUserFreeCouponsRepository marketUserFreeCouponsRepository;

    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("开始每日清理过期免费券任务...");

        submitCallable();

        logger.info("每日清理过期免费券任务完成...");
    }

    private void submitCallable() {
        int count = getTotalCount();
        // 计算需要加载的次数
        int times = count % BATCH_CLEAN_SIZE == 0 ? count / BATCH_CLEAN_SIZE
                : count / BATCH_CLEAN_SIZE + 1;

        for (int i = 0; i < times; i++) {
            marketUserFreeCouponsService.cleanFreeCouponsExpired(0, BATCH_CLEAN_SIZE);
        }

    }

    /**
     * 统计需要处理的数据量
     * @return
     */
    private Integer getTotalCount() {
        return marketUserFreeCouponsRepository.selectListByExpiredCount();
    }

}
