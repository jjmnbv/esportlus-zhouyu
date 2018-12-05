package com.kaihei.esportingplus.payment.migrate.service;

import com.google.common.collect.Lists;
import com.kaihei.esportingplus.payment.migrate.constant.ThreadResult;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Semaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: tangtao
 **/
public abstract class AbstractMigrateService {

    Logger logger = LoggerFactory.getLogger(getClass());

    abstract long getTotalCount();

    abstract ThreadResult doMigrate(Semaphore semaphore);

    Integer pageSize;

    String startIndex;

    public List<ThreadResult> migrate(CompletionService<ThreadResult> completionService, Semaphore semaphore, Integer pageSize)
            throws InterruptedException {

        long start = System.currentTimeMillis();
        long totalCount = this.getTotalCount();

        this.pageSize = pageSize;
        this.startIndex = "";

        List<ThreadResult> threadResults = Lists.newLinkedList();

        int totalPage = (int) Math.ceil((double) totalCount / (double) pageSize);

        logger.info(">>> 数据迁移开始，数据量：{}", totalCount);

        for (int i = 1; i <= totalPage; i++) {
            semaphore.acquire();
            completionService.submit(() -> doMigrate(semaphore));
        }
        for (int i = 1; i <= totalPage; i++) {
            try {
                threadResults.add(completionService.take().get());
            } catch (Exception e) {
                threadResults.add(new ThreadResult(e.getMessage()));
                logger.error("迁移线程出错", e);
            }
        }
        printResult(threadResults);

        logger.info(">>> 数据迁移完成 耗时：{}m 处理总数据量：{} 任务数：{}", (System.currentTimeMillis() - start) / 1000f / 60f, totalCount, totalPage);

        return threadResults;
    }

    private void printResult(List<ThreadResult> results) {
        int handleSize = 0;
        int orderSize = 0;
        int billSize = 0;
        int warningSizeSize = 0;
        int errSizeSize = 0;
        List<String> warningData = Lists.newLinkedList();
        List<String> errData = Lists.newLinkedList();
        for (int i = 0; i < results.size(); i++) {
            ThreadResult threadResult = results.get(i);
            handleSize += threadResult.getHandleOrderSize();
            orderSize += threadResult.getOrderSize();
            billSize += threadResult.getBillSize();
            warningSizeSize += threadResult.getWarningSize();
            errSizeSize += threadResult.getErrSize();
            if (threadResult.getWarningData() != null) {
                warningData.addAll(threadResult.getWarningData());
            }
            if (threadResult.getErrData() != null) {
                errData.addAll(threadResult.getErrData());
            }
            if (threadResult.getErrMsg() != null) {
                logger.info("{} - 执行线程报错 >>> {}", i + 1, threadResult.getErrMsg());
                continue;
            }
        }

        for (String err : warningData) {
            logger.info(">>> 警告数据 {}", err);
        }

        for (String err : errData) {
            logger.info(">>> 错误数据 {}", err);
        }

        logger.info(">>> 数据迁移执行结果汇总 handleSize:{}, orderSize:{}, billSize:{}, errSizeSize:{}, warningSize:{}", handleSize, orderSize, billSize,
                errSizeSize, warningSizeSize);
    }

}
