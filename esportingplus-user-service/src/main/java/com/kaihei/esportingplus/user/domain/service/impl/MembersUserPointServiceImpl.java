package com.kaihei.esportingplus.user.domain.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.feign.ChickenpointUseConfigClient;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointUseConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointUseConfigVO.ExchangeTime.ExchangeTimeConfig;
import com.kaihei.esportingplus.api.vo.freeteam.DictionaryVO;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.payment.api.feign.WithdrawServiceClient;
import com.kaihei.esportingplus.user.api.enums.UserPointItemType;
import com.kaihei.esportingplus.user.api.vo.UserPointItemsQueryVo;
import com.kaihei.esportingplus.user.api.vo.UserPointItemsVo;
import com.kaihei.esportingplus.user.api.vo.UserPointQueryVo;
import com.kaihei.esportingplus.user.config.CopywritingConfig;
import com.kaihei.esportingplus.user.data.repository.MembersUserPointItemRepository;
import com.kaihei.esportingplus.user.data.repository.MembersUserPointRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersUserPoint;
import com.kaihei.esportingplus.user.domain.entity.MembersUserPointItem;
import com.kaihei.esportingplus.user.domain.service.MembersUserPointService;
import com.kaihei.esportingplus.user.domain.service.MembersUserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户鸡分服务实现类，处理鸡分服务接口业务逻辑
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/9 15:00
 */
@Service
public class MembersUserPointServiceImpl implements MembersUserPointService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MembersUserPointRepository membersUserPointRepository;

    @Autowired
    private MembersUserPointItemRepository membersUserPointItemRepository;

    @Autowired
    private ChickenpointUseConfigClient chickenpointUseConfigClient;

    @Autowired
    private WithdrawServiceClient withdrawServiceClient;

    @Autowired
    private MembersUserService membersUserService;

    @Autowired
    private CopywritingConfig copywritingConfig;

    private static CacheManager cacheManager = CacheManagerFactory.create();

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer incrPoint(Integer userId, Integer incrPointAmount, Integer itemType, String slug) {
        return incrPoint(userId, incrPointAmount, itemType,
                new Integer(0), slug).getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MembersUserPointItem incrPoint(Integer userId, Integer incrPointAmount, Integer itemType,
            Integer operationUserId, String slug) {
        try {
            //保存用户鸡分
            saveOrUpdateUserPoint(userId, incrPointAmount);

            //保存鸡分明细
            MembersUserPointItem  pointItem = saveUserPointItem(userId, incrPointAmount, itemType, operationUserId, slug);
            return pointItem;
        } catch (Exception e) {
            logger.error("incr point error.", e);
            throw new BusinessException(BizExceptionEnum.INCR_POINT_FAIL);
        }
    }

    @Override
    public UserPointQueryVo queryUserPoint(String uid) {
        //根据uid查询userID
        Integer userId = membersUserService.getUserIdByUid(uid);

        MembersUserPoint membersUserPoint = membersUserPointRepository.selectByPrimaryKey(userId);

        UserPointQueryVo userPointQueryVo = new UserPointQueryVo();
        userPointQueryVo
                .setPointAmount(membersUserPoint == null ? 0 : membersUserPoint.getPointAmount());

        return userPointQueryVo;
    }

    @Override
    public PagingResponse<UserPointItemsQueryVo> listUserPointItems(String uid,
            PagingRequest pagingRequest) {
        //根据uid查询userID
        Integer userId = membersUserService.getUserIdByUid(uid);

        //开启分页插件
        Page<MembersUserPointItem> page = PageHelper
                .startPage(pagingRequest.getOffset(), pagingRequest.getLimit())
                .doSelectPage(() -> membersUserPointItemRepository.selectByUserId(userId));

        List<UserPointItemsQueryVo> list = (List<UserPointItemsQueryVo>) BeanMapper
                .map(page.getResult(), UserPointItemsQueryVo.class);

        PagingResponse<UserPointItemsQueryVo> pagingResponse = new PagingResponse<UserPointItemsQueryVo>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), list);
        return pagingResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean exchangeScore(String uid, Integer exchangeAmount) throws BusinessException {
        RLock rLock = null;
        long start = 0L;
        try {
            //开启分布式锁
            RedissonClient redissonClient = cacheManager.redissonClient();
            String distlockKey = String.format(RedisKey.REDIS_DISTLOCK_PREFIX, uid);
            rLock = redissonClient.getLock(distlockKey);
            rLock.lock(300, TimeUnit.MILLISECONDS);
            start = System.currentTimeMillis();

            //根据uid查询userID
            Integer userId = membersUserService.getUserIdByUid(uid);

            //获取鸡分兑换配置
            ChickenpointUseConfigVO config = loadPointExchangeConfig();

            if (validateExchangeScore(userId, exchangeAmount, config)) {
                //计算兑换的暴鸡值
                Integer scoreAmount = computeExchangeScoreAmount(userId, exchangeAmount, config);

                //修改暴鸡值
                Integer outTradeNo = incrPoint(userId, -exchangeAmount, UserPointItemType.EXCHANGE.getCode(), "");

                logger.info("cmd=MembersUserPointServiceImpl.exchangeScore | msg={} | uid={}| scoreAmount={}| outTradeNo={}",
                        "开发调用支付接口，兑换暴鸡值", uid, scoreAmount, outTradeNo);

                //调用暴鸡值兑换接口
                withdrawServiceClient.
                        convertScoreToStarlight(uid,scoreAmount,String.valueOf(outTradeNo));

                logger.info("cmd=MembersUserPointServiceImpl.exchangeScore | msg={} | uid={}| scoreAmount={}| outTradeNo={}",
                        "结束调用兑换暴鸡值支付接口", uid, scoreAmount, outTradeNo);
            }
        } catch (BusinessException bex) {
            //业务异常直接抛出
            logger.error("exchange score error.", bex);
            throw bex;
        } catch (Exception e) {
            logger.error("exchange score error.", e);
            return false;
        }finally {
            //关闭分布式锁
            try {
                if (rLock != null && rLock.isLocked()) {
                    rLock.unlock();
                }
            } catch (Exception e) {
                logger.error("redis 分布式锁释放异常!", e);
            }
            logger.info(">> exchangeScore cost "+(System.currentTimeMillis()-start)+" ms");
        }

        return true;
    }

    @Override
    public UserPointItemsQueryVo getUserPointItem(Integer itemType, String slug) {
        if (slug == null) {
            logger.info("cmd=MembersUserPointServiceImpl.getUserPorintItem | msg=参数有误 | req={}", slug);
            return null;
        }

        MembersUserPointItem userPointItem = membersUserPointItemRepository.selectByTypeAndSlug(itemType, slug);

        UserPointItemsQueryVo userPointQueryVo = new UserPointItemsQueryVo();
        if(userPointItem != null){
            userPointQueryVo.setItemAcmount(userPointItem.getItemAcmount() == null ? 0 : userPointItem.getItemAcmount());
        }
        return userPointQueryVo;
    }

    /**
     * 统计积分累计信息
     *
     * @param uid 用户uid
     */
    @Override
    public UserPointItemsVo getUserAccumulatePoint(String uid) {
        //查询累计积分信息
        Integer userId = membersUserService.getUserIdByUid(uid);
        MembersUserPoint membersUserPoint = membersUserPointRepository.selectByPrimaryKey(userId);

        UserPointItemsVo vo = new UserPointItemsVo();
        vo.setPointAmount((membersUserPoint == null || membersUserPoint.getPointAmount() == null) ? 0:membersUserPoint.getPointAmount());

        //查询当日获得积分信息
        Integer todayAccPointAmount = membersUserPointItemRepository.selectCountByUserId(userId);
        vo.setTodayAccPointAmount(todayAccPointAmount);
        return vo;
    }

    /**
     * 获取积分兑换配置
     *
     * @return ChickenpointUseConfigVo
     */
    private ChickenpointUseConfigVO loadPointExchangeConfig() {
        //调用鸡分配置接口
        ResponsePacket<DictionaryVO<ChickenpointUseConfigVO>> config = chickenpointUseConfigClient.getChickenpointUseConfigVo();
        if (null == config) {
            throw new BusinessException(BizExceptionEnum.USER_POINT_EXCHANGE_CONFIG_EMPTY);
        }

        return config.getData().getValue();
    }

    /**
     * 校验兑换条件
     *
     * @param userId 用户ID
     * @param exchangeAmount 兑换鸡分
     * @param config 鸡分兑换配置
     * @return boolean 校验结果
     */
    private boolean validateExchangeScore(Integer userId, Integer exchangeAmount, ChickenpointUseConfigVO config)
            throws BusinessException {
        //校验鸡分兑换开关是否开启，开关为1表示开启
        if (config.getChickenUseSuport() == null || config.getChickenUseSuport().intValue() == 0) {
            throw new BusinessException(BizExceptionEnum.USER_POINT_EXCHANGE_SWITCH_DISABLE);
        }

        //校验兑换时间
        if (!validateExchangeTimeFrame(config)) {
            throw new BusinessException(BizExceptionEnum.USER_POINT_EXCHANGE_TIME_FRAME_ERROR);
        }

        //校验兑换范围
        if (!validateExchangeAmountFram(exchangeAmount, config)) {
            throw new BusinessException(BizExceptionEnum.USER_POINT_EXCHANGE_AMOUNT_FRAME_ERROR);
        }

        //校验用户鸡分值是否足够
        MembersUserPoint membersUserPoint = membersUserPointRepository.selectByPrimaryKey(userId);
        if (membersUserPoint == null || membersUserPoint.getPointAmount() < exchangeAmount) {
            throw new BusinessException(BizExceptionEnum.USER_POINT_EXCHANGE_AVAILABLE_AMOUNT);
        }

        return true;
    }

    /**
     * 根据配置的兑换时间范围校验当前时间是否允许兑换
     *
     * @param config 鸡分兑换配置
     * @return boolean 校验结果，成功/失败
     */
    private boolean validateExchangeTimeFrame(ChickenpointUseConfigVO config) {
        //校验星期
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        LocalDateTime fromLocalDateTime = null;
        LocalDateTime toLocalDateTime = null;
        if (config.getExchangeTime() != null && config.getExchangeTime().getFrom() != null) {
            fromLocalDateTime = parseExchangeTime(config.getExchangeTime().getFrom());
        }
        if (config.getExchangeTime() != null && config.getExchangeTime().getTo() != null) {
            toLocalDateTime = parseExchangeTime(config.getExchangeTime().getTo());
        }

        //已配置起始时间，且当前时间在起始时间之前，说明未到允许兑换的时间范围
        if (fromLocalDateTime != null && nowLocalDateTime.isBefore(fromLocalDateTime)) {
            return false;
        }

        //已配置终止时间，且当前时间在终止时间之后，说明已超过允许兑换的时间范围
        if (toLocalDateTime != null && nowLocalDateTime.isAfter(toLocalDateTime)) {
            return false;
        }

        return true;
    }

    /**
     * 根据配置 星期+小时+分钟解析对应的日期
     *
     * @param exchangeTimeConfig 日期配置
     * @return LocalDateTime
     */
    private LocalDateTime parseExchangeTime(ExchangeTimeConfig exchangeTimeConfig) {
        LocalDate nowLocalDate = LocalDate.now();
        LocalDate localDate = nowLocalDate
                .plusDays(exchangeTimeConfig.getWeek() - nowLocalDate.getDayOfWeek().getValue());
        LocalTime localTime = LocalTime
                .of(exchangeTimeConfig.getHour(), exchangeTimeConfig.getMinute());
        return LocalDateTime.of(localDate, localTime);
    }

    /**
     * 校验兑换积分是否超过配置范围
     *
     * @param exchangeAmount 待兑换值
     * @param config 积分兑换配置
     * @return boolean
     */
    private boolean validateExchangeAmountFram(Integer exchangeAmount,
            ChickenpointUseConfigVO config) {
        if (config.getMinExchangeChickenPoint() != null && exchangeAmount < config
                .getMinExchangeChickenPoint()) {
            return false;
        }
        if (config.getMaxExchangeChickenPoint() != null && config
                .getMaxExchangeChickenPoint() < exchangeAmount) {
            return false;
        }
        return true;
    }

    /**
     * 计算兑换后的暴鸡值
     *
     * @param userId 用户ID
     * @param exchangeAmount 兑换鸡分
     * @param config 鸡分兑换配置
     * @return boolean 校验结果
     */
    private Integer computeExchangeScoreAmount(Integer userId, Integer exchangeAmount,
            ChickenpointUseConfigVO config) {

        //根据配置计算兑换的暴鸡值，比例乘以兑换的鸡分值
        BigDecimal ratio = new BigDecimal(config.getExchangeRatio().getValue());
        BigDecimal exchangeValue =  ratio.multiply(new BigDecimal(exchangeAmount*100));

        //向下取整
        return exchangeValue.setScale(0,BigDecimal.ROUND_FLOOR).intValue();
    }

    /**
     * 增加用户鸡分，若记录不存在则插入新记录
     *
     * @param userId 用户ID
     * @param incrPointAmount 待增加的鸡分值（可为负数）
     */
    private void saveOrUpdateUserPoint(Integer userId, Integer incrPointAmount) {
        MembersUserPoint membersUserPoint = new MembersUserPoint();
        membersUserPoint.setUserId(userId);
        membersUserPoint.setPointAmount(incrPointAmount);
        membersUserPointRepository.insertOrUpdate(membersUserPoint);
    }

    /**
     * 保存用户鸡分明细
     *
     * @param userId 用户ID
     * @param incrPointAmount 待增加的鸡分值（可为负数）
     * @param itemType 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     * @param operationUserId 积分来源操作用户ID
     */
    private MembersUserPointItem saveUserPointItem(Integer userId, Integer incrPointAmount, Integer itemType,
            Integer operationUserId, String slug) {
        MembersUserPointItem membersUserPointItem = new MembersUserPointItem();
        membersUserPointItem.setUserId(userId);
        membersUserPointItem.setItemAcmount(incrPointAmount);
        membersUserPointItem.setItemType(itemType);
        membersUserPointItem.setOperationUserId(operationUserId);
        membersUserPointItem.setInstructions(getItemInstructions(itemType));
        membersUserPointItem.setCreateDatetime(new Date());
        membersUserPointItem.setSlug(slug);

        membersUserPointItemRepository.insert(membersUserPointItem);
        return membersUserPointItem;
    }

    /**
     * 获取明细说明 TODO 从配置文件或字典获取
     *
     * @param itemType 明细类型
     * @return String
     */
    private String getItemInstructions(Integer itemType) {
        String instructions = null;

        if (itemType.intValue() == UserPointItemType.EXCHANGE.getCode().intValue()) {
            instructions = copywritingConfig.getPointExchange();
        } else if (itemType.intValue() == UserPointItemType.TEAM_DRIVE.getCode().intValue()) {
            instructions = copywritingConfig.getPointTeamDrive();
        } else if (itemType.intValue() == UserPointItemType.TEAM_OBTAINSTAR.getCode().intValue()) {
            instructions = copywritingConfig.getPointObtainstar();
        }

        return instructions;
    }

}
