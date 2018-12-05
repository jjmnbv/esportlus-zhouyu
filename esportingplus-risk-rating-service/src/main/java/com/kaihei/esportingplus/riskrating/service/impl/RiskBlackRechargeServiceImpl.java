package com.kaihei.esportingplus.riskrating.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.riskrating.api.enums.EnableEnum;
import com.kaihei.esportingplus.riskrating.api.enums.RiskDictEnum;
import com.kaihei.esportingplus.riskrating.api.enums.RiskEnum;
import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskBlackCheckConsumerParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;
import com.kaihei.esportingplus.riskrating.bean.AlermContent;
import com.kaihei.esportingplus.riskrating.bean.BlackAmountResult;
import com.kaihei.esportingplus.riskrating.bean.BlackAmountTemCache;
import com.kaihei.esportingplus.riskrating.bean.RiskDeviceUserBindResult;
import com.kaihei.esportingplus.riskrating.bean.RiskRechargeResult;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDeviceUserRechargeBind;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDeviceUserRechargeLog;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDeviceWhite;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import com.kaihei.esportingplus.riskrating.event.AlermMsgSendEvent;
import com.kaihei.esportingplus.riskrating.repository.RiskDeviceUserRechargeBindRepository;
import com.kaihei.esportingplus.riskrating.repository.RiskDeviceUserRechargeLogRepository;
import com.kaihei.esportingplus.riskrating.repository.RiskDeviceWhiteRepository;
import com.kaihei.esportingplus.riskrating.service.RiskBlackRechargeService;
import com.kaihei.esportingplus.riskrating.service.RiskDictService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 用户充值黑名单风控相关服务实现类
 *
 * @author 谢思勇
 */
@Service
public class RiskBlackRechargeServiceImpl implements RiskBlackRechargeService {

    protected static final CacheManager cacheManager = CacheManagerFactory.create();

    private static final Logger logger = LoggerFactory.getLogger(RiskDictServiceImpl.class);
    @Autowired
    private RiskDictService riskDictService;
    @Autowired
    private RiskDeviceWhiteRepository riskDeviceWhiteRepository;
    @Autowired
    private RiskDeviceUserRechargeBindRepository rechargeBindRepository;
    @Autowired
    private RiskDeviceUserRechargeLogRepository riskDeviceUserRechargeLogRepository;
    @Autowired
    private RiskRatingService riskRatingService;

    @Resource
    private RechargeFreezeService rechargeFreezeService;

    /**
     * 充值后，通过MQ调用此接口，用户充值后校验告警
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechargeRiskAlerm(RiskBlackCheckConsumerParams rechargeRiskParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, rechargeRiskParams);
        //1:做设备绑定充值检查
        RiskDeviceUserBindResult riskDeviceUserBindResult = checkDeviceUserRecharge(
                rechargeRiskParams.getDeviceNo(), rechargeRiskParams.getUid());
        //2：是否保存或更新绑定表
        if (riskDeviceUserBindResult.isSave()) {
            this.saveRiskDeviceUserRechargeBind(riskDeviceUserBindResult
                    .getRiskDeviceUserRechargeBind(), rechargeRiskParams);
        }


        //3：再检查货币充值类型, 这里判断一下上面有没有产生风险，如果产生了，则不用检查，直接跳过发送告警
        RiskEnum currencyRiskEnum = RiskEnum.NOMARL;
        if (riskDeviceUserBindResult.getRiskEnum() == RiskEnum.NOMARL) {
            currencyRiskEnum = checkCurrencyType(rechargeRiskParams.getCurrencyType());
        }
        //4:再做小额检查
        BlackAmountResult blackAmountResult = checkSmallAmountRecharge(
                rechargeRiskParams.getDeviceNo(), rechargeRiskParams.getUid(),
                rechargeRiskParams.getAmount(),
                riskDeviceUserBindResult.getRiskEnum() == RiskEnum.NOMARL
                        && currencyRiskEnum == RiskEnum.NOMARL);
        //如果是小额充值，写入redis
        if (blackAmountResult.isSmallFlag()) {
            this.saveOrUpdateCountToRedis(blackAmountResult,
                    rechargeRiskParams);
        }
        //3：日志需要入库
        this.saveDeviceUserRechargeLog(rechargeRiskParams,riskDeviceUserBindResult.isWhite(),
                this.findWorngRiskEnum(riskDeviceUserBindResult.getRiskEnum(),currencyRiskEnum,blackAmountResult.getRiskEnum()));
        //发送告警
        this.sendAlermMsg(riskDeviceUserBindResult.getRiskEnum(),
                blackAmountResult.getRiskEnum(), currencyRiskEnum,
                rechargeRiskParams, blackAmountResult);
    }
    private RiskEnum findWorngRiskEnum(RiskEnum deviceUserRiskEnum,RiskEnum currencyRiskEnum, RiskEnum rechargeCountRiskEnum
            ){
        if(deviceUserRiskEnum!=null && RiskEnum.NOMARL != deviceUserRiskEnum){
            return deviceUserRiskEnum;
        }
        if(currencyRiskEnum!=null && RiskEnum.NOMARL != currencyRiskEnum){
            return currencyRiskEnum;
        }
        if(rechargeCountRiskEnum!=null && RiskEnum.NOMARL != rechargeCountRiskEnum){
            return rechargeCountRiskEnum;
        }
        return RiskEnum.NOMARL;
    }
    /**
     * 发送告警服务
     */
    private void sendAlermMsg(RiskEnum deviceUserRiskEnum, RiskEnum rechargeCountRiskEnum,
            RiskEnum currencyRiskEnum, RiskBlackCheckConsumerParams rechargeRiskParams,
            BlackAmountResult result) {
        //下面的逻辑判断告警
        AlermMsgSendEvent alermMsgSendEvent = new AlermMsgSendEvent();
        AlermContent content = new AlermContent();
        alermMsgSendEvent.setText(content);
        if (deviceUserRiskEnum != null && RiskEnum.NOMARL != deviceUserRiskEnum) {
            content.setContent(String.format(RiskEnum.DEVICE_USER_BIND_LIMIT.getAlermFormat(),
                    rechargeRiskParams.getDeviceNo()));
        }

        if (rechargeCountRiskEnum != null && RiskEnum.NOMARL != rechargeCountRiskEnum) {
            switch (rechargeCountRiskEnum) {
                case DEVICE_DAY_RECHARGE_LIMIT:
                    content.setContent(
                            String.format(RiskEnum.DEVICE_USER_BIND_LIMIT.getAlermFormat(),
                                    rechargeRiskParams.getDeviceNo(), result.getRechargeCount(),
                                    result.getThreshold()));
                    break;
                case DEVICE_HOUR_RECHARGE_LIMIT:
                    content.setContent(
                            String.format(RiskEnum.DEVICE_HOUR_RECHARGE_LIMIT.getAlermFormat(),
                                    rechargeRiskParams.getDeviceNo(), result.getCurrentHour(),
                                    result.getRechargeCount(), result.getThreshold()));
                    break;
                case USER_DAY_RECHARGE_LIMIT:
                    content.setContent(
                            String.format(RiskEnum.USER_DAY_RECHARGE_LIMIT.getAlermFormat(),
                                    rechargeRiskParams.getUid(), result.getRechargeCount(),
                                    result.getThreshold()));
                    break;
                case USER_HOUR_RECHARGE_LIMIT:
                    content.setContent(
                            String.format(RiskEnum.USER_HOUR_RECHARGE_LIMIT.getAlermFormat(),
                                    rechargeRiskParams.getUid(), result.getCurrentHour(),
                                    result.getRechargeCount(), result.getThreshold()));
                    break;
                default:
                    break;
            }
        }
        if (currencyRiskEnum != null && RiskEnum.NOMARL != currencyRiskEnum) {
            content.setContent(String.format(RiskEnum.RECHARGE_CURRENCY_ILLEGAL.getAlermFormat(),
                    rechargeRiskParams.getUid(), rechargeRiskParams.getCurrencyType()));
        }
        if (ObjectTools.isNotEmpty(content.getContent())) {
            //发送异步事件告警
            EventBus.post(alermMsgSendEvent);
        }

    }

    /**
     * 充值前接口调用，用户判断用户能否进行充值
     */
    @Override
    public RiskBaseResponse checkRechargeStatus(RechargeRiskParams rechargeRiskParams) {
        RiskBaseResponse riskBaseResult = new RiskBaseResponse();
        if(!riskDictService.checkRiskSwitchStatus()){
            riskBaseResult.setRiskMsg(RiskEnum.NOMARL.getMarkWords());
            riskBaseResult.setRiskCode(RiskEnum.NOMARL.getCode());
            riskBaseResult.setRechargeFlag(true);
            return riskBaseResult;
        }
        RiskRechargeResult riskRechargeResult = this.rechargeCheck(rechargeRiskParams);
        if (riskRechargeResult.getRiskDeviceUserBindResult().getRiskEnum() != RiskEnum.NOMARL) {
            riskBaseResult
                    .setRiskCode(riskRechargeResult.getRiskDeviceUserBindResult().getRiskEnum()
                            .getCode());
            riskBaseResult
                    .setRiskMsg(riskRechargeResult.getRiskDeviceUserBindResult().getRiskEnum()
                            .getMarkWords());
            riskBaseResult.setRechargeFlag(false);
            return riskBaseResult;
        }
        if (riskRechargeResult.getBlackAmountResult().getRiskEnum() != RiskEnum.NOMARL) {
            riskBaseResult
                    .setRiskCode(riskRechargeResult.getBlackAmountResult().getRiskEnum().getCode());
            riskBaseResult
                    .setRiskMsg(riskRechargeResult.getBlackAmountResult().getRiskEnum().getMarkWords());
            riskBaseResult.setRechargeFlag(false);
            return riskBaseResult;
        }

        RiskBaseResponse riskBaseResponse = riskRatingService.hasRisk(rechargeRiskParams);
        if (riskBaseResponse != null) {
            return riskBaseResponse;
        }

        riskBaseResult.setRiskCode(RiskEnum.NOMARL.getCode());
        riskBaseResult.setRiskMsg(RiskEnum.NOMARL.getMarkWords());
        riskBaseResult.setRechargeFlag(true);
        return riskBaseResult;
    }

    private RiskEnum checkCurrencyType(String currencyType) {
        RiskDict riskDict = riskDictService
                .findByGroupCodeAndItemCode(RiskDictEnum.RECHARGE_CURRENCY_TYPE.getGroupCode(),
                        RiskDictEnum.RECHARGE_CURRENCY_TYPE.getItemCode());
        ValidateAssert.hasNotNull(BizExceptionEnum.RISK_DICT_NOT_INIT, riskDict);
        if (riskDict.getItemValue().toUpperCase().indexOf(currencyType.toUpperCase() + ",") < 0) {
            logger.warn("货币类型非法，传入的货币类型或国家地区编码为{}",currencyType);
            return RiskEnum.RECHARGE_CURRENCY_ILLEGAL;
        }
        return RiskEnum.NOMARL;
    }

    /**
     * 设备绑定和充值次数检查
     */
    private RiskRechargeResult rechargeCheck(RechargeRiskParams rechargeRiskParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, rechargeRiskParams);
        RiskRechargeResult riskRechargeResult = new RiskRechargeResult();
        //1：第一步检查设备充值用户数量
      RiskDeviceUserBindResult riskDeviceUserBindResult = checkDeviceUserRecharge(
                rechargeRiskParams.getDeviceNo(), rechargeRiskParams.getUid());
        riskRechargeResult.setRiskDeviceUserBindResult(riskDeviceUserBindResult);
        //2：充值次数检查，根据第一步的结果，判断是否会做次数校验，只有在是状态正常的情况下，才会检查，否则只会判断是否是小额充值
        BlackAmountResult blackAmountResult = checkSmallAmountRecharge(
                rechargeRiskParams.getDeviceNo(), rechargeRiskParams.getUid(),
                rechargeRiskParams.getAmount(),
                riskDeviceUserBindResult.getRiskEnum() == RiskEnum.NOMARL);
        riskRechargeResult.setBlackAmountResult(blackAmountResult);
        return riskRechargeResult;
    }

    /**
     * 将充值次数写入redis中
     */
    private void saveOrUpdateCountToRedis(BlackAmountResult blackAmountResult,
            RechargeRiskParams rechargeRiskParams) {
        Pipeline pipelined = cacheManager.pipelined();
        String deviceHourRechargeCountKey = String
                .format(RedisKey.DEVICE_HOUR_RECHARGE_COUNT, rechargeRiskParams.getDeviceNo(),
                        blackAmountResult.getCurrentHour());
        String deviceDayRechargeCountKey = String
                .format(RedisKey.DEVICE_DAY_RECHARGE_COUNT, rechargeRiskParams.getDeviceNo(),
                        blackAmountResult.getCurrentDay());
        String userHourRechargeCountKey = String
                .format(RedisKey.USER_HOUR_RECHARGE_COUNT, rechargeRiskParams.getUid(),
                        blackAmountResult.getCurrentHour());
        String userDayRechargeCountKey = String
                .format(RedisKey.USER_DAY_RECHARGE_COUNT, rechargeRiskParams.getUid(),
                        blackAmountResult.getCurrentDay());
        Response<Long> deviceHourRechargeCount = pipelined.incr(deviceHourRechargeCountKey);
        Response<Long> deviceDayRechargeCount = pipelined.incr(deviceDayRechargeCountKey);
        Response<Long> userHourRechargeCount = pipelined.incr(userHourRechargeCountKey);
        Response<Long> userDayRechargeCount = pipelined.incr(userDayRechargeCountKey);
        pipelined.sync();
        if (deviceHourRechargeCount.get() == 1) {
            pipelined.expire(deviceHourRechargeCountKey, CommonConstants.ONE_HOUR_SECONDS);
        }
        if (deviceDayRechargeCount.get() == 1) {
            pipelined.expire(deviceDayRechargeCountKey, CommonConstants.ONE_DAY_SECONDS);
        }
        if (userHourRechargeCount.get() == 1) {
            pipelined.expire(userHourRechargeCountKey, CommonConstants.ONE_HOUR_SECONDS);
        }
        if (userDayRechargeCount.get() == 1) {
            pipelined.expire(userHourRechargeCountKey, CommonConstants.ONE_DAY_SECONDS);
        }
        pipelined.sync();

    }

    /**
     * 保存设备用户充值日志
     */
    private void saveDeviceUserRechargeLog(RiskBlackCheckConsumerParams rechargeRiskParams,boolean isWhite,
            RiskEnum riskEnum) {
        RiskDeviceUserRechargeLog rechargeLog = new RiskDeviceUserRechargeLog();
        rechargeLog.setDeviceNo(rechargeRiskParams.getDeviceNo());
        rechargeLog.setRechargeMoney(rechargeRiskParams.getAmount());
        if(isWhite){
            rechargeLog.setIsWhite(1);
        }else {
            rechargeLog.setIsWhite(0);
        }
        rechargeLog.setCurrencyType(rechargeRiskParams.getCurrencyType());
        rechargeLog.setRiskCode(riskEnum.getCode());
        rechargeLog.setRiskDesc(riskEnum.getDesc());
        rechargeLog.setUid(rechargeRiskParams.getUid());
        rechargeLog.setMsgId(rechargeRiskParams.getMsgId());
        riskDeviceUserRechargeLogRepository.save(rechargeLog);
    }

    /**
     * 增加或者更新设备充值绑定表
     */
    private void saveRiskDeviceUserRechargeBind(
            RiskDeviceUserRechargeBind riskDeviceUserRechargeBind,
            RechargeRiskParams rechargeRiskParams) {
        if (riskDeviceUserRechargeBind != null) {
            riskDeviceUserRechargeBind
                    .setRechargeCount(riskDeviceUserRechargeBind.getRechargeCount() + 1);
        } else {
            riskDeviceUserRechargeBind = new RiskDeviceUserRechargeBind();
            riskDeviceUserRechargeBind.setDeviceNo(rechargeRiskParams.getDeviceNo());
            riskDeviceUserRechargeBind.setRechargeCount(1);
            riskDeviceUserRechargeBind.setUid(rechargeRiskParams.getUid());

        }
        rechargeBindRepository.save(riskDeviceUserRechargeBind);
    }

    public RiskDeviceUserBindResult checkDeviceUserRecharge(String deviceNo, String uid) {
        RiskDeviceUserBindResult riskResult = new RiskDeviceUserBindResult();
        riskResult.setRiskEnum(RiskEnum.NOMARL);
        RiskDeviceWhite riskDeviceWhite = riskDeviceWhiteRepository.findByDeviceNo(deviceNo);
        //1:判断是否在白名单中，如果在白名单中，则返回正常
        if (riskDeviceWhite != null && EnableEnum.ENABLE.equals(riskDeviceWhite.getWhiteStatus())) {
            logger.info(" 设备号[{}] 已被加入白名单中", deviceNo);
            riskResult.setWhite(true);
            return riskResult;
        }
        //2：从设备用户绑定表里查询出该设备支持的用户
        List<RiskDeviceUserRechargeBind> deviceUserRechargeBindList = rechargeBindRepository
                .findByDeviceNo(deviceNo);
        //3：如果设备支持的用户数为空，则一定可以通过
        if (ObjectTools.isEmpty(deviceUserRechargeBindList)) {
            logger.info(" 设备号[{}] 暂时没有给用户用户充值过", deviceNo);
            riskResult.setSave(true);
            return riskResult;
        }
        //4:判断绑定表中是否有这个设备充值过这个用户，如果有，可以通过
        long count = deviceUserRechargeBindList.stream().filter(it -> it.getUid().equals(uid))
                .count();
        if (count > 0) {
            //将实体类类找出，方便直接使用存入db
            riskResult.setRiskDeviceUserRechargeBind(
                    deviceUserRechargeBindList.stream().filter(it -> it.getUid().equals(uid))
                            .findFirst().get());
            riskResult.setSave(true);
            return riskResult;
        }
        //5:没有从redis中查找出设备用户账号配置数量
        RiskDict riskDict = riskDictService
                .findByGroupCodeAndItemCode(RiskDictEnum.DEVICE_USER_BIND_COUNT.getGroupCode(),
                        RiskDictEnum.DEVICE_USER_BIND_COUNT.getItemCode());
        ValidateAssert.hasNotNull(BizExceptionEnum.RISK_DICT_NOT_INIT, riskDict);
        //6:如果配置数量大于原绑定用户数，通过
        logger.info(" 设备号[{}] 设置的用户充值配置数{}，当前设备已经充值过的用户数为{}", deviceNo,
                Integer.valueOf(riskDict.getItemValue()), deviceUserRechargeBindList.size());
        if (Integer.valueOf(riskDict.getItemValue()) > deviceUserRechargeBindList.size()) {
            riskResult.setSave(true);
            return riskResult;
        }
        //7:配置数量小于绑定数，不能通过，返回错误风控码
        riskResult.setRiskEnum(RiskEnum.DEVICE_USER_BIND_LIMIT);
        return riskResult;
    }

    public boolean isSmallAmount(int gcoin) {
        RiskDict riskDict = riskDictService
                .findByGroupCodeAndItemCode(RiskDictEnum.SMALL_MONEY_CHECK_AMOUNT.getGroupCode(),
                        RiskDictEnum.SMALL_MONEY_CHECK_AMOUNT.getItemCode());
        ValidateAssert
                .isTrue(ObjectTools.isNotEmpty(riskDict), BizExceptionEnum.RISK_DICT_NOT_INIT);
        if (gcoin > Integer.valueOf(
                riskDict.getItemValue())) {
            return false;
        }
        return true;
    }

    /**
     * 充值次数检查，根据第一步的结果，判断是否会做次数校验，只有在是状态正常的情况下，才会检查，否则只会判断是否是小额充值
     */
    public BlackAmountResult checkSmallAmountRecharge(String deviceNo, String uid, Integer gcoin,
            boolean needCheck) {
        BlackAmountResult result = new BlackAmountResult();
        LocalDateTime now = LocalDateTime.now();
        String currentHour = DateUtil.getHour(now);
        String currentDay = DateUtil.getDay(now);
        result.setCurrentHour(currentHour);
        result.setCurrentDay(currentDay);
        if (!this.isSmallAmount(gcoin)) {
            result.setRiskEnum(RiskEnum.NOMARL);
            return result;
        }
        logger.info("用户[{}]在设备号[{}]上充值金额为{}分，属于小额充值",uid,deviceNo,gcoin);
        result.setSmallFlag(true);
        BlackAmountTemCache blackAmountTemCache = this
                .buildBlackAmountTemCache(deviceNo, uid, currentHour, currentDay);
        //如果第一步结果是false，则进行下面检查，否则只判断是否是小额充值，进行入库
        if (!needCheck) {
            return result;
        }
        List<RiskDict> riskDictList = riskDictService
                .findByGroupCode(RiskDictEnum.DEVICE_USER_BIND_COUNT.getGroupCode());
        ValidateAssert
                .isTrue(ObjectTools.isNotEmpty(riskDictList), BizExceptionEnum.RISK_DICT_NOT_INIT);
        //以数据库中的iteamCode为键组装成map，方便查找
        Map<String, RiskDict> map = riskDictList.stream()
                .collect(Collectors.toMap(RiskDict::getItemCode, i -> i));

        //如果设备每日充值数为空，则设备小时充值数不用再检查，所以，这里直接判断不为空,检查有值部分
        //先判断每日充值是否超限
        if (blackAmountTemCache.getDeviceDayRechargeCount() + 1 > Integer.valueOf(
                map.get(RiskDictEnum.DEVICE_DAY_RECHARGE_COUNT.getItemCode()).getItemValue())) {
            //返回不正常
            logger.info("设备号[{}]每日小额充值次数已达上限",deviceNo);
            result.setRechargeCount(blackAmountTemCache.getDeviceDayRechargeCount() + 1);
            result.setThreshold(Integer.valueOf(
                    map.get(RiskDictEnum.DEVICE_DAY_RECHARGE_COUNT.getItemCode()).getItemValue()));
            result.setRiskEnum(RiskEnum.DEVICE_DAY_RECHARGE_LIMIT);
            return result;
        }
        if (blackAmountTemCache.getDeviceHourRechargeCount() + 1 > Integer.valueOf(
                map.get(RiskDictEnum.DEVICE_HOUR_RECHARGE_COUNT.getItemCode()).getItemValue())) {
            //返回不正常
            logger.info("设备号[{}]每时小额充值次数已达上限",deviceNo);
            result.setRechargeCount(blackAmountTemCache.getDeviceHourRechargeCount() + 1);
            result.setThreshold(Integer.valueOf(
                    map.get(RiskDictEnum.DEVICE_HOUR_RECHARGE_COUNT.getItemCode()).getItemValue()));
            result.setRiskEnum(RiskEnum.DEVICE_HOUR_RECHARGE_LIMIT);
            return result;
        }
        //先判断每日充值是否超限
        if (blackAmountTemCache.getUserDayRechargeCount() + 1 > Integer.valueOf(
                map.get(RiskDictEnum.USER_DAY_RECHARGE_COUNT.getItemCode()).getItemValue())) {
            //返回不正常
            logger.info("用户[{}]每日小额充值次数已达上限",uid);
            result.setRechargeCount(blackAmountTemCache.getUserDayRechargeCount() + 1);
            result.setThreshold(Integer.valueOf(
                    map.get(RiskDictEnum.USER_DAY_RECHARGE_COUNT.getItemCode()).getItemValue()));
            result.setRiskEnum(RiskEnum.USER_DAY_RECHARGE_LIMIT);
            return result;
        }
        if (blackAmountTemCache.getUserHourRechargeCount() + 1 > Integer.valueOf(
                map.get(RiskDictEnum.USER_HOUR_RECHARGE_COUNT.getItemCode()).getItemValue())) {
            //返回不正常
            logger.info("用户[{}]每时小额充值次数已达上限",uid);
            result.setRechargeCount(blackAmountTemCache.getUserHourRechargeCount() + 1);
            result.setThreshold(Integer.valueOf(
                    map.get(RiskDictEnum.USER_HOUR_RECHARGE_COUNT.getItemCode()).getItemValue()));
            result.setRiskEnum(RiskEnum.USER_HOUR_RECHARGE_LIMIT);
            return result;
        }
        //返回正常
        result.setRiskEnum(RiskEnum.NOMARL);
        return result;
    }

    private BlackAmountTemCache buildBlackAmountTemCache(String deviceNo, String uid,
            String currentHour, String currentDay) {
        BlackAmountTemCache blackAmountTemCache = new BlackAmountTemCache();
        //否则走校验
        //获取设备每日设备充值数
        String deviceDayRechargeCount = cacheManager
                .get(String.format(RedisKey.DEVICE_DAY_RECHARGE_COUNT, deviceNo, currentDay),
                        String.class);
        //获取设备每小时设备充值数
        String deviceHourRechargeCount = cacheManager
                .get(String.format(RedisKey.DEVICE_HOUR_RECHARGE_COUNT, deviceNo, currentHour),
                        String.class);
        //获取用户每日充值数
        String userDayRechargeCount = cacheManager
                .get(String.format(RedisKey.USER_DAY_RECHARGE_COUNT, uid, currentDay),
                        String.class);
        //获取用户每小时充值数
        String userHourRechargeCount = cacheManager
                .get(String.format(RedisKey.USER_HOUR_RECHARGE_COUNT, uid, currentHour),
                        String.class);
        blackAmountTemCache.setDeviceDayRechargeCount(
                deviceDayRechargeCount == null ? 0 : Integer.valueOf(deviceDayRechargeCount));
        blackAmountTemCache.setDeviceHourRechargeCount(
                deviceHourRechargeCount == null ? 0 : Integer.valueOf(deviceHourRechargeCount));
        blackAmountTemCache.setUserDayRechargeCount(
                userDayRechargeCount == null ? 0 : Integer.valueOf(userDayRechargeCount));
        blackAmountTemCache.setUserHourRechargeCount(
                userHourRechargeCount == null ? 0 : Integer.valueOf(userHourRechargeCount));
        return blackAmountTemCache;
    }
}
