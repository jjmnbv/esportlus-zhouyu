package com.kaihei.esportingplus.payment.migrate;

import static java.lang.System.out;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kaihei.esportingplus.payment.migrate.constant.Constant;
import com.kaihei.esportingplus.payment.migrate.constant.ThreadResult;
import com.kaihei.esportingplus.payment.migrate.service.MigratePaymentAliService;
import com.kaihei.esportingplus.payment.migrate.service.MigratePaymentQQService;
import com.kaihei.esportingplus.payment.migrate.service.MigratePaymentWechatService;
import com.kaihei.esportingplus.payment.migrate.service.MigrateRechargeAliService;
import com.kaihei.esportingplus.payment.migrate.service.MigrateRechargeQQService;
import com.kaihei.esportingplus.payment.migrate.service.MigrateRechargeWechatService;
import com.kaihei.esportingplus.payment.migrate.service.MigrateRefundService;
import com.kaihei.esportingplus.payment.migrate.service.MigrateTransferYunService;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author: tangtao
 **/
@ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
@Service
public class MigrateService {

    private Logger logger = LoggerFactory.getLogger(MigrateService.class);


    @Autowired
    private MigrateRechargeQQService migrateRechargeQQService;
    @Autowired
    private MigrateRechargeAliService migrateRechargeAliService;
    @Autowired
    private MigrateRechargeWechatService migrateRechargeWechatService;

    @Autowired
    private MigrateRefundService migrateRefundService;

    @Autowired
    private MigratePaymentWechatService paymentWechatService;

    @Autowired
    private MigratePaymentAliService paymentAliService;

    @Autowired
    private MigratePaymentQQService paymentQQService;

    @Autowired
    private MigrateTransferYunService migrateTransferYunService;

    public static void main(String[] args) {
        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(80, 80,
                60L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(96),
                new ThreadFactoryBuilder().setNameFormat("migrate-%d").build());

        AtomicInteger page = new AtomicInteger(0);

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.submit(() -> {
                int p = page.getAndIncrement();
                out.println(p);
            });

        }


    }

    public void migrate(String types, Integer threadSize, Integer maxTaskSize, Integer maxPageSize) {

        int thread = (threadSize == null || threadSize < 1) ? 40 : threadSize;
        int task = (maxTaskSize == null || maxTaskSize < 1) ? 56 : maxTaskSize;
        int pageSize = (maxPageSize == null || maxPageSize < 1) ? Constant.DEFAULT_PAGE_SIZE : maxPageSize;

        Semaphore semaphore = new Semaphore(task);

        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(thread, thread,
                60L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(task),
                new ThreadFactoryBuilder().setNameFormat("migrate-%d").build());
        CompletionService<ThreadResult> completionService = new ExecutorCompletionService<>(threadPoolExecutor);
        try {
            logger.info(">>> 数据迁移开始执行");

            if (types.contains("recharge_qq")) {
                migrateRechargeQQService.migrate(completionService, semaphore, pageSize);
            }
            if (types.contains("recharge_ali")) {
                migrateRechargeAliService.migrate(completionService, semaphore, pageSize);
            }
            if (types.contains("recharge_wx")) {
                migrateRechargeWechatService.migrate(completionService, semaphore, pageSize);
            }
            if (types.contains("payment_qq")) {
                paymentQQService.migrate(completionService, semaphore, pageSize);
            }
            if (types.contains("payment_ali")) {
                paymentAliService.migrate(completionService, semaphore, pageSize);
            }
            if (types.contains("payment_wx")) {
                paymentWechatService.migrate(completionService, semaphore, pageSize);
            }
            if (types.contains("withdraw")) {
                migrateTransferYunService.migrate(completionService, semaphore, pageSize);
            }
            if (types.contains("refund")) {
                migrateRefundService.migrate(completionService, semaphore, pageSize);
            }
        } catch (Exception e) {
            logger.error("数据迁移出错", e);
        } finally {
            threadPoolExecutor.shutdown();
        }
    }

}
