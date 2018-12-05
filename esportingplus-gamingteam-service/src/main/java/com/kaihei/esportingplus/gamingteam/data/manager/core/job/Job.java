package com.kaihei.esportingplus.gamingteam.data.manager.core.job;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.kaihei.esportingplus.common.task.JobScheduleService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterRestoreOperation;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时任务
 */
public abstract class Job<T extends TeamGame, R> extends AfterRestoreOperation<T> implements
        SimpleJob {

    @Autowired
    protected JobScheduleService jobScheduleService;
    private Class<R> rType;


    @SuppressWarnings("unchecked")
    public Job() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass()
                .getGenericSuperclass();

        rType = (Class<R>) genericSuperclass.getActualTypeArguments()[1];
    }

    protected void addJob(String key, long delay, String jobParam) {
        jobScheduleService.addJob(this, this.getClass().getSimpleName() + ":" + key,
                new Date(System.currentTimeMillis() + delay), jobParam);
    }

    public void removeJob(String key) {
        jobScheduleService.removeJob(this.getClass().getSimpleName() + ":" + key);
    }

    /**
     * 执行作业.
     *
     * @param shardingContext 分片上下文
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        R r = JSON.parseObject(shardingContext.getJobParameter(), rType);
        execute(r);
    }

    /**
     * 执行作业.
     *
     * @param r 参数
     */
    protected abstract void execute(R r);
}
