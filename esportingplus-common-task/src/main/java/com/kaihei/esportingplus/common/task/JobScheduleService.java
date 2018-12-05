package com.kaihei.esportingplus.common.task;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobOperateAPI;
import com.dangdang.ddframe.job.lite.lifecycle.internal.operate.JobOperateAPIImpl;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.google.common.base.Optional;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * 基于elastic-job框架的任务调度服务
 *
 * @author LiuQing.Qin
 * @date 2018/4/12 13:48
 */
@Component
@ConditionalOnBean(JobRegistryCenterConfig.class)
public class JobScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduleService.class);

    /*分片数量*/
    private static final int SHARDING_TOTAL_COUNT = 1;

    private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

    @Resource
    private ZookeeperRegistryCenter regCenter;

    /**
     * 添加作业（支持基于spring容器获取job实例）
     * @param elasticJob
     * @param jobName
     * @param cron 任务执行时间，形如：0/5 * * * * ?
     */
    public void addJob(final ElasticJob elasticJob, final String jobName, final String cron) {
        try {
            this.removeJob(jobName);
            LiteJobConfiguration jobConfiguration = createJobConfiguration(
                    elasticJob.getClass(), jobName, cron, SHARDING_TOTAL_COUNT);
            new SpringJobScheduler(elasticJob, regCenter, jobConfiguration).init();
            LOGGER.info("add [{}] job successfully. cron=[{}]",jobName, cron);
        } catch (Exception e) {
            LOGGER.error("add [{}] Job Exception", jobName, e);
            throw new BusinessException(BizExceptionEnum.CREATE_ELASTIC_JOB_FAIL, jobName);
        }

    }

    /**
     * 添加作业（支持基于spring容器获取job实例）
     * @param elasticJob
     * @param jobName
     * @param dateTime 任务执行时间
     */
    public void addJob(final ElasticJob elasticJob, final String jobName, final Date dateTime) {
        try {
            this.removeJob(jobName);
            String cron = getCron(dateTime);
            LiteJobConfiguration jobConfiguration = createJobConfiguration(
                    elasticJob.getClass(), jobName, cron, SHARDING_TOTAL_COUNT);
            new SpringJobScheduler(elasticJob, regCenter, jobConfiguration).init();
            LOGGER.info("add [{}] job successfully. cron=[{}]",jobName, cron);
        } catch (Exception e) {
            LOGGER.error("add [{}] Job Exception", jobName, e);
            throw new BusinessException(BizExceptionEnum.CREATE_ELASTIC_JOB_FAIL, jobName);
        }

    }

    /**
     * 添加作业（支持基于spring容器获取job实例）
     *
     * @param dateTime 任务执行时间
     */
    public void addJob(final ElasticJob elasticJob, final String jobName, final Date dateTime,
            String jobParam) {
        try {
            this.removeJob(jobName);
            String cron = getCron(dateTime);
            LiteJobConfiguration jobConfiguration = createJobConfiguration(
                    elasticJob.getClass(), jobName, cron, SHARDING_TOTAL_COUNT, jobParam);
            new SpringJobScheduler(elasticJob, regCenter, jobConfiguration).init();
            LOGGER.info("add [{}] job successfully. cron=[{}]", jobName, cron);
        } catch (Exception e) {
            LOGGER.error("add [{}] Job Exception", jobName, e);
            throw new BusinessException(BizExceptionEnum.CREATE_ELASTIC_JOB_FAIL, jobName);
        }

    }

    /**
     * 添加作业
     * @param jobClass
     * @param jobName
     * @param cron 任务执行时间，形如：0/5 * * * * ?
     */
    public void addJob(final Class<? extends ElasticJob> jobClass, final String jobName, final String cron) {
        try {
            this.removeJob(jobName);
            LiteJobConfiguration jobConfiguration = createJobConfiguration(
                    jobClass, jobName, cron, SHARDING_TOTAL_COUNT);
            new JobScheduler(regCenter, jobConfiguration).init();
            LOGGER.info("add [{}] job successfully. cron=[{}]",jobName, cron);
        } catch (Exception e) {
            LOGGER.error("add [{}] Job Exception", jobName, e);
            throw new BusinessException(BizExceptionEnum.CREATE_ELASTIC_JOB_FAIL, jobName);
        }

    }

    /**
     * 添加作业
     * @param jobName
     * @param datetime
     */
    public void addJob(final Class<? extends ElasticJob> jobClass, final String jobName, final Date datetime) {
        String cron = this.getCron(datetime);
        if (StringUtils.isEmpty(cron)) {
            LOGGER.error("添加作业异常，输入的时间不能为空且不能小于当前时间");
        }
        this.addJob(jobClass, jobName, cron);
    }

    /**
     * 移除作业
     * @param jobName
     */
    public void removeJob(final String jobName) {
        try {
            JobOperateAPI jobOperateAPI = new JobOperateAPIImpl(regCenter);
            jobOperateAPI.remove(Optional.of(jobName), Optional.<String>absent());
            regCenter.remove("/" + jobName);
        } catch (Exception e) {
            LOGGER.error("Remove [{}] Job Exception", jobName, e);
        }
    }

    /**
     * 终止作业
     * @param jobName
     */
    public void shutdown(final String jobName) {
        try {
            JobOperateAPI jobOperateAPI = new JobOperateAPIImpl(regCenter);
            jobOperateAPI.shutdown(Optional.of(jobName), Optional.<String>absent());
        } catch (Exception e) {
            LOGGER.error("Shutdown [{}] Job Exception", jobName, e);
        }
    }

    private LiteJobConfiguration createJobConfiguration(final Class<? extends ElasticJob> jobClass,
            final String jobName, final String cron, final int shardingTotalCount) {

        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration
                .newBuilder(jobName, cron, shardingTotalCount).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig,
                jobClass.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig)
                .build();
        return simpleJobRootConfig;
    }

    private LiteJobConfiguration createJobConfiguration(final Class<? extends ElasticJob> jobClass,
            final String jobName, final String cron, final int shardingTotalCount,
            String jobParam) {

        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration
                .newBuilder(jobName, cron, shardingTotalCount).jobParameter(jobParam).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig,
                jobClass.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig)
                .build();
        return simpleJobRootConfig;
    }


    /***
     * 根椐指定的时间，生成cron表达式
     * @param date
     * @return
     */
    private String getCron(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        String formatTimeStr = "";
        if (date != null) {
            long now = System.currentTimeMillis();
            //输入时间小于当前时间视为无效
            if(date.getTime() < now) {
                return StringUtils.EMPTY;
            }
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

}
