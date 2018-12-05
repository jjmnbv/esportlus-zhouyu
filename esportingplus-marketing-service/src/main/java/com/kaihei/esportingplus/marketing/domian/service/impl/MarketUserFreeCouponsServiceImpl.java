package com.kaihei.esportingplus.marketing.domian.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.marketing.api.enums.FreeCouponsESTypeEnum;
import com.kaihei.esportingplus.marketing.api.enums.FreeCouponsSourceEnum;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsDtlVo;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsInfoVo;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsQueryResultVo;
import com.kaihei.esportingplus.marketing.config.ContentConfig;
import com.kaihei.esportingplus.marketing.data.manager.MarketUserFreeCouponsESManager;
import com.kaihei.esportingplus.marketing.data.repository.MarketUserFreeCouponsRepository;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserFreeCoupons;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserInvitStatistics;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserFreeCouponsService;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserTaskService;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zl.zhao
 * @description: 用户免费券管理实现类
 * @date: 2018/11/19 14:55
 */
@Service
public class MarketUserFreeCouponsServiceImpl implements MarketUserFreeCouponsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MarketUserFreeCouponsRepository marketUserFreeCouponsRepository;

    @Autowired
    private MarketUserFreeCouponsESManager marketUserFreeCouponsESManager;

    @Autowired
    private ContentConfig contentConfig;

    @Autowired
    private MarketUserTaskService marketUserTaskService;

    private static CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 添加免费次数
     * @param uid
     * @param invalidTime
     * @param type
     * @param source
     */
    public void incrCoupons(String uid, Date invalidTime, Integer type, Integer source){
        MarketUserFreeCoupons coupons = new MarketUserFreeCoupons();
        Date date = new Date();
        coupons.setCreateTime(date);
        coupons.setUpdateTime(date);
        coupons.setInvalidTime(invalidTime);
        coupons.setType(type);
        coupons.setSource(source);
        coupons.setUid(uid);
        marketUserFreeCouponsRepository.insertSelective(coupons);
    }

    /**
     * 组装对象list
     * @param uid
     * @param invalidTime
     * @param type
     * @param source
     */
    public List<MarketUserFreeCoupons> assembleCouponsList(String uid, Date invalidTime, Integer type, Integer source, Integer freeCount){
        List<MarketUserFreeCoupons> couponsList = Lists.newArrayList();
        for(int i = 0; i < freeCount ; i ++) {
            MarketUserFreeCoupons coupons = new MarketUserFreeCoupons();
            Date date = new Date();
            coupons.setCreateTime(date);
            coupons.setUpdateTime(date);
            coupons.setInvalidTime(invalidTime);
            coupons.setType(type);
            coupons.setSource(source);
            coupons.setUid(uid);
            couponsList.add(coupons);
        }
       return couponsList;
    }

    /**
     * 添加免费上车次数
     *
     * @param uid
     * @param freeCount
     * @param invalidTime
     * @param type
     * @param source
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addUserFreeCoupons(String uid, Integer freeCount, Date invalidTime, Integer type, Integer source) {
        try {
            List<MarketUserFreeCoupons> couponsList = assembleCouponsList(uid, invalidTime, type, source, freeCount);
            marketUserFreeCouponsRepository.insertBatch(couponsList);

            return true;
        } catch (Exception e) {
            logger.error("incr coupons Exception. msg={} | uid={} | type={} | source={}", e, uid, type, source);
            throw new BusinessException(BizExceptionEnum.FREE_TEAM_COUPONS_ERROR);
        }
    }

    @Override
    public UserFreeCouponsInfoVo getUserFreeCouponsInfo(String uid) {
        //查询可用的
        MarketUserFreeCoupons coupons = new MarketUserFreeCoupons();
        coupons.setUid(uid);
        Integer availableCount = marketUserFreeCouponsRepository.selectCount(coupons);
        UserFreeCouponsInfoVo vo = new UserFreeCouponsInfoVo();
        vo.setAvailableCount(availableCount == null ? 0:availableCount);
        if(0 == vo.getAvailableCount()){
            vo.setText(contentConfig.getCouponsNoChances());
        }else {
            vo.setText(contentConfig.getCouponsTeamDrive());
        }

        //ES中查询已用的(免费车队版本需求改动，用户无法看到已使用的)
//        Integer usedCount = marketUserFreeCouponsESManager.termQuery(uid+FreeCouponsESTypeEnum.USED.getCode());
//        vo.setUsedCount(usedCount == null ? 0:usedCount);
        return vo;
    }

    /**
     * 扣减免费上车次数
     *
     * @param uid
     * @param reduceCount
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserFreeCouponsQueryResultVo reduceFreeCoupons(String uid, Integer reduceCount) {
        UserFreeCouponsQueryResultVo vo = new UserFreeCouponsQueryResultVo();
        RLock rLock = null;
        long start = 0L;
        try {
            //开启分布式锁
            RedissonClient redissonClient = cacheManager.redissonClient();
            String distlockKey = String.format(RedisKey.REDIS_DISTLOCK_PREFIX, uid + ":reduceFreeCoupons");
            rLock = redissonClient.getLock(distlockKey);
            rLock.lock(300, TimeUnit.MILLISECONDS);
            start = System.currentTimeMillis();

            //根据uid查询免费券
            Page<MarketUserFreeCoupons> page = PageHelper
                    .startPage(0, reduceCount)
                    .doSelectPage(() -> marketUserFreeCouponsRepository.selectByUid(uid));

            List<MarketUserFreeCoupons> list = (List<MarketUserFreeCoupons>) BeanMapper
                    .map(page.getResult(), MarketUserFreeCoupons.class);
            if(CollectionUtils.isEmpty(list)){
                vo.setOprResult(Boolean.FALSE);
                return vo;
            }

            //已使用的券存放至ES
            marketUserFreeCouponsESManager.saveUserFreeCouponsES(list, FreeCouponsESTypeEnum.USED.getCode());

            //删除已使用的免费券
            List<Long> ids = Lists.newArrayList();
            for(MarketUserFreeCoupons coupons : list){
                ids.add(coupons.getId());
            }
            marketUserFreeCouponsRepository.deleteByIds(ids);

            //返回使用的免费券id集合
            vo.setCouponsIds(ids);
            vo.setOprResult(Boolean.TRUE);
            return vo;
        }  catch (BusinessException bex) {
            //业务异常直接抛出
            logger.error("MembersUserFreeCouponsServiceImpl.reduceFreeCoupons BusinessException. " +
                            "msg={} | uid={} | reduceCount={}", bex, uid, reduceCount);
            throw bex;
        } catch (Exception e) {
            logger.error("MembersUserFreeCouponsServiceImpl.reduceFreeCoupons Exception. " +
                            "msg={} | uid={} | reduceCount={}", e, uid, reduceCount);
            vo.setOprResult(Boolean.FALSE);
            return vo;
        } finally {
            //释放分布式锁
            try {
                if (rLock != null && rLock.isLocked()) {
                    rLock.unlock();
                }
            } catch (Exception e) {
                logger.error("redis 分布式锁释放异常!", e);
            }
            logger.info(">> MembersUserFreeCouponsServiceImpl.reduceFreeCoupons cost "
                                +(System.currentTimeMillis()-start)+" ms");
        }
    }

    /**
     * 返还免费次数券
     *
     * @param couponsIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserFreeCouponsQueryResultVo returnFreeCoupons(List<Long> couponsIds) {
        logger.info("cmd=MembersUserFreeCouponsServiceImpl.returnFreeCoupons msg='返还免费次数开始! " +
                            "couponsIds={}", couponsIds);

        UserFreeCouponsQueryResultVo vo = new UserFreeCouponsQueryResultVo();
        RLock rLock = null;
        long start = 0L;
        try {
            //查询次数免费券
            List<MarketUserFreeCoupons> couponsList = marketUserFreeCouponsESManager.
                                                            getUserFreeCouponsESByBatch(couponsIds);

            //次数券返还入库
            if(ObjectTools.isEmpty(couponsList)) {
                logger.error("cmd=MembersUserFreeCouponsServiceImpl.returnFreeCoupons msg='未查询到使用免费次数!" +
                        "couponsIds = {}",couponsIds);
                vo.setOprResult(Boolean.FALSE);
                return vo;
            }
            marketUserFreeCouponsRepository.insertAndUpdate(couponsList);

            //批量删除ES的免费次数券
            marketUserFreeCouponsESManager.delUserFreeCouponsESByBatch(couponsIds);

            logger.info("cmd=MembersUserFreeCouponsServiceImpl.returnFreeCoupons msg='返还免费次数完成! " +
                            "couponsIds={}", couponsIds);
            vo.setOprResult(Boolean.TRUE);
            return vo;
        } catch (BusinessException bex){
            logger.error("cmd=MembersUserFreeCouponsServiceImpl.returnFreeCoupons.BusinessException " +
                            "msg='返还免费次数失败!",bex);
            throw bex;
        } catch (Exception e){
            logger.error("cmd=MembersUserFreeCouponsServiceImpl.returnFreeCoupons.Exception " +
                            "msg='返还免费次数失败!",e);
            vo.setOprResult(Boolean.FALSE);
            return vo;
        }
    }

    /**
     * 清理已过期免费券至ES
     *
     * @param offset
     * @param limit
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanFreeCouponsExpired(Integer offset, Integer limit) {
        //查询已过期免费券
        Page<MarketUserFreeCoupons> page = PageHelper
                .startPage(offset, limit)
                .doSelectPage(() -> marketUserFreeCouponsRepository.selectListByExpired());

        List<MarketUserFreeCoupons> list = (List<MarketUserFreeCoupons>) BeanMapper
                .map(page.getResult(), MarketUserFreeCoupons.class);

        //已过期的券存放至ES
        marketUserFreeCouponsESManager.saveUserFreeCouponsES(list, FreeCouponsESTypeEnum.EXPIRED.getCode());

        //删除已过期的免费券
        List<Long> ids = Lists.newArrayList();
        for(MarketUserFreeCoupons coupons : list){
            ids.add(coupons.getId());
        }
        if(CollectionUtils.isNotEmpty(ids))
            marketUserFreeCouponsRepository.deleteByIds(ids);
    }

    /**
     * 查询匹配机会信息
     *
     */
    @Override
    public UserFreeCouponsDtlVo getUserFreeCouponsDetail() {
        String uid = UserSessionContext.getUser().getUid();
        Integer userId = UserSessionContext.getUser().getId();

        //每日赠送匹配次数
        Integer systemCount = marketUserFreeCouponsRepository.selectByUidAndSource(uid, FreeCouponsSourceEnum.SOURCE_SYSTEM_GIVE.getCode());

        //邀请匹配次数
        Integer inviteCount = marketUserFreeCouponsRepository.selectByUidAndSource(uid, FreeCouponsSourceEnum.SOURCE_INVITE_GIVE.getCode());

        //成功邀请好友数量
        MarketUserInvitStatistics statistics = marketUserTaskService.getInvitStatistics(userId);

        UserFreeCouponsDtlVo vo = new UserFreeCouponsDtlVo();
        vo.setSystemCount(systemCount);
        vo.setInviteCount(inviteCount);
        vo.setAvailableCount(inviteCount + systemCount);
        vo.setInvitedAmount((statistics == null || statistics.getInvitedAmount()==null) ?
                0:statistics.getInvitedAmount().intValue());
        return vo;
    }
}
