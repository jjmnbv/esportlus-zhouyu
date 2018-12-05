package com.kaihei.esportingplus.common.thread.pool;

import static com.kaihei.esportingplus.common.thread.pool.ThreadPoolConfig.REJECTED_POLICY_ABORT;
import static com.kaihei.esportingplus.common.thread.pool.ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS;

import com.kaihei.esportingplus.common.tools.JVMUtil;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程拒绝处理策略
 * ThreadPoolManager -> CommonExecutorFactory -> DefaultExecutor -> NamedPoolThreadFactory(ThreadFactory)
 * @author LiuQing.Qin
 * @date 2017年12月8日 下午4:11:52
 */
public final class DumpThreadRejectedHandler implements RejectedExecutionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(DumpThreadRejectedHandler.class);

    private volatile boolean dumping = false;

    //TODO
    private static final String DUMP_DIR = "/tmp";

    private final ThreadPoolConfig poolConfig;

    private final int rejectedPolicy;

    public DumpThreadRejectedHandler(ThreadPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        this.rejectedPolicy = poolConfig.getRejectedPolicy();
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        LOGGER.warn("one task rejected, poolConfig={}, poolInfo={}", poolConfig, ThreadPoolManager.getPoolInfo(e));
        if (!dumping) {
            dumping = true;
            dumpJVMInfo();
        }

        if (rejectedPolicy == REJECTED_POLICY_ABORT) {
            throw new RejectedExecutionException("one task rejected, pool=" + poolConfig.getName());
        } else if (rejectedPolicy == REJECTED_POLICY_CALLER_RUNS) {
            if (!e.isShutdown()) {
                r.run();
            }
        }
    }

    private void dumpJVMInfo() {
        LOGGER.info("start dump jvm info");
        JVMUtil.dumpJstack(DUMP_DIR + "/" + poolConfig.getName());
        LOGGER.info("end dump jvm info");
    }
}

