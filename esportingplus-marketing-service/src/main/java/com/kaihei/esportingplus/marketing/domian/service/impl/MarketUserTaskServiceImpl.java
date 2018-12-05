package com.kaihei.esportingplus.marketing.domian.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.marketing.constants.MarketRedisKey;
import com.kaihei.esportingplus.marketing.data.repository.MarketUserInvitStatisticsRepository;
import com.kaihei.esportingplus.marketing.data.repository.MarketUserInvitingRelationRepository;
import com.kaihei.esportingplus.marketing.data.repository.MarketUserTaskAccumualteHistoryRepository;
import com.kaihei.esportingplus.marketing.data.repository.MarketUserTaskAccumualteRepository;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserInvitStatistics;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserInvitingRelation;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserTaskAccumualte;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserTaskAccumualteHistory;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserTaskService;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-12 14:40
 * @Description:
 */
@Component("MarketUserTaskServiceImpl")
public class MarketUserTaskServiceImpl implements MarketUserTaskService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MarketUserTaskAccumualteRepository marketUserTaskAccumualteRepository;

    @Autowired
    private MarketUserInvitingRelationRepository marketUserInvitingRelationRepository;

    @Autowired
    private MarketUserTaskAccumualteHistoryRepository marketUserTaskAccumualteHistoryRepository;

    @Autowired
    private MarketUserInvitStatisticsRepository marketUserInvitStatisticsRepository;

    @Autowired
    private MarketUserTaskService marketUserTaskService;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public boolean updateAccumulate(MarketUserTaskAccumualte accumualte) {
        int i = marketUserTaskAccumualteRepository
                .updateByPrimaryKeySelective(accumualte);//把更新的值更新回去
        if (i < 0) {
            logger.error(
                    "cmd=MarketUserTaskService.updateAccumulate | msg=更新accumulate失败 | req={}",
                    JacksonUtils.toJson(accumualte));
            return false;
        }
        return true;
    }

    @Override
    public MarketUserInvitingRelation findRelationByUserId(Integer userId) {
        MarketUserInvitingRelation record = new MarketUserInvitingRelation();
        record.setUserId(userId);
        MarketUserInvitingRelation relation = marketUserInvitingRelationRepository
                .selectOne(record);
        return relation;
    }

    @Override
    public MarketUserTaskAccumualte getOrCreateAccumulate(Integer userId, Integer type,
            Date onlinTime) {
        MarketUserTaskAccumualte accumualte = new MarketUserTaskAccumualte();
        accumualte.setUserId(userId);
        accumualte.setType(type);
        accumualte = marketUserTaskAccumualteRepository.selectOne(accumualte);
        if (accumualte == null) {//没有则新增
            accumualte = new MarketUserTaskAccumualte();
            accumualte.setUserId(userId);
            accumualte.setAccumulate(0);
            accumualte.setAwardSnapshot(0);
            accumualte.setBatchDate(onlinTime);
            accumualte.setType(type);//3：好友消费
            createAccumulate(accumualte);
        }
        return accumualte;
    }

    @Override
    public MarketUserTaskAccumualte getAccumulate(Integer userId, Integer type) {
        MarketUserTaskAccumualte con = new MarketUserTaskAccumualte();
        con.setUserId(userId);
        con.setType(type);
        MarketUserTaskAccumualte acc = marketUserTaskAccumualteRepository.selectOne(con);
        return acc;
    }

    @Override
    public MarketUserTaskAccumualte createAccumulate(MarketUserTaskAccumualte accumualte) {
        int i = marketUserTaskAccumualteRepository.insertSelective(accumualte);
        if (i < 0) {
            logger.error(
                    "cmd=MarketUserTaskService.createAccumulate | msg=新增accumulate失败 | req={}",
                    JacksonUtils.toJson(accumualte));
            return null;
        }
        return accumualte;
    }

    @Override
    public MarketUserTaskAccumualteHistory createAccumulateHistroyByAcc(
            MarketUserTaskAccumualte acc) {
        MarketUserTaskAccumualteHistory history = new MarketUserTaskAccumualteHistory();
        history.setAccumulate(acc.getAccumulate());
        history.setAwardSnapshot(acc.getAwardSnapshot());
        history.setBatchDate(acc.getBatchDate());
        history.setType(acc.getType());
        history.setUserId(acc.getUserId());
        int i = marketUserTaskAccumualteHistoryRepository.insertSelective(history);
        if (i < 0) {
            logger.error(
                    "cmd=MarketUserTaskService.createAccumulateHistroyByAcc | msg=新增accumulate_history失败 | req={}",
                    JacksonUtils.toJson(history));
            return null;
        }
        return history;
    }

    @Transactional
    @Override
    public MarketUserInvitStatistics getOrCreateInvitStatistics(Integer userId) {
        if (userId == null || userId < 0) {
            return null;
        }
        MarketUserInvitStatistics req = new MarketUserInvitStatistics();
        req.setUserId(userId);
        MarketUserInvitStatistics statistics = marketUserInvitStatisticsRepository
                .selectOne(req);
        if (statistics == null) {
            RLock lock = null;
            try {
                lock = cacheManager.redissonClient()
                        .getLock(String.format(MarketRedisKey.STATISTICS_INIT_REDIS_LOCK, userId));
                lock.lock(5, TimeUnit.SECONDS);
                statistics = marketUserInvitStatisticsRepository
                        .selectOne(req);
                if (statistics != null) {
                    return statistics;
                }
                statistics = new MarketUserInvitStatistics();
                statistics.setUserId(userId);
                statistics.setCoinAwardAmount(0L);
                statistics.setInvitedAmount(0L);
                int i = marketUserInvitStatisticsRepository.insertSelective(statistics);
                if (i < 0) {
                    logger.error(
                            "cmd=MarketUserTaskService.getOrCreateInvitStatistics | msg=新增statistics失败 | req={}",
                            JacksonUtils.toJson(statistics));
                    throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                logger.error(
                        "cmd=MarketUserTaskService.getOrCreateInvitStatistics | msg=异常错误", e);
                throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
            } finally {
                if (lock != null && lock.isLocked()) {
                    lock.unlock();
                }
            }
        }
        return statistics;
    }

    @Override
    public MarketUserInvitStatistics getInvitStatistics(Integer userId) {
        MarketUserInvitStatistics req = new MarketUserInvitStatistics();
        req.setUserId(userId);
        MarketUserInvitStatistics statistics = marketUserInvitStatisticsRepository
                .selectOne(req);
        return statistics;
    }

    @Transactional
    @Override
    public boolean incrStatistics(Integer userId, Long invitCount, Long awardCount) {
        //先确保增加了记录
        MarketUserInvitStatistics statistics = marketUserTaskService.getOrCreateInvitStatistics(userId);
        MarketUserInvitStatistics record = new MarketUserInvitStatistics();
        record.setId(statistics.getId());
        record.setUserId(userId);
        record.setInvitedAmount(invitCount);
        record.setCoinAwardAmount(awardCount);
        int i = marketUserInvitStatisticsRepository.incrInvitCountOrAwardCount(record);
        if (i < 0) {
            logger.error(
                    "cmd=MarketUserTaskService.incrStatistics | msg=累加statistics失败 | req={}",
                    JacksonUtils.toJson(record));
            return false;
        }
        return true;
    }
}
