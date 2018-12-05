package com.kaihei.esportingplus.riskrating.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.riskrating.api.enums.FreeTeamEnum;
import com.kaihei.esportingplus.riskrating.api.enums.RiskActionType;
import com.kaihei.esportingplus.riskrating.api.enums.ShumeiEventEnum;
import com.kaihei.esportingplus.riskrating.api.enums.ShumeiResultCodeEnum;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamBasicParams;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamWhiteQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.ShumeiQueryParams;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamConfigVo;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamWhiteDeviceVo;
import com.kaihei.esportingplus.riskrating.domain.entity.*;
import com.kaihei.esportingplus.riskrating.repository.*;
import com.kaihei.esportingplus.riskrating.service.FreeTeamService;
import com.kaihei.esportingplus.riskrating.util.ShumeiApiUtil;
import com.kaihei.esportingplus.user.api.feign.UserRiskServiceClient;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 免费车队风控相关服务实现类
 *
 * @author chenzhenjun
 */
@Service
public class FreeTeamServiceImpl implements FreeTeamService {

    private static final Logger logger = LoggerFactory.getLogger(FreeTeamServiceImpl.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    private static final String FREE_TEAM = "freeteam";

    private static final String FREETEAM_REDIS_KEY = "riskrating:freeteam:config";

    private static final int MAX_PAGE_SIZE = 9999;

    @Autowired
    private FreeTeamDeviceBindRepository freeTeamDeviceBindRepository;

    @Autowired
    private FreeteamConfigRepository freeteamConfigRepository;

    @Autowired
    private FreeTeamDeviceBlackRepository blackRepository;

    @Autowired
    private FreeTeamChanceRepository chanceRepository;

    @Autowired
    private FreeTeamRecordRepository recordRepository;

    @Autowired
    private FreeTeamDeviceWhiteRepository whiteRepository;

    @Autowired
    private UserRiskServiceClient riskServiceClient;

    @Autowired
    private FreeteamConfigRepository configRepository;

    @Autowired
    private UserDeviceBehaviourRecordRepository behaviourRecordRepository;

    @Autowired
    private RiskControlNextdataRepository riskControlNextdataRepository;

    @Value("${app.shumei.baseurl}")
    private String shumeiBaseUrl;

    @Value("${app.shumei.accessKey}")
    private String accessKey;


    @Override
    public FreeTeamResponse checkRegisterReward(FreeTeamBasicParams freeTeamBasicParams) throws BusinessException {
        FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
        String uid = freeTeamBasicParams.getUid();
        String deviceId = freeTeamBasicParams.getDeviceId();

        RiskControlNextdata entity = riskControlNextdataRepository.findByUidAndDeviceIdAndAction(uid, deviceId,
                RiskActionType.REGISTER.getCode());
        if (null != entity) {
//            FreeTeamDeviceBind bind = new FreeTeamDeviceBind();
//            bind.setUid(uid);
//            bind.setDeviceId(deviceId);
//            freeTeamDeviceBindRepository.save(bind);
            response.setRiskCode(FreeTeamEnum.REGISTER_AWARD_REJECT.getCode());
            response.setRiskMsg(FreeTeamEnum.REGISTER_AWARD_REJECT.getMsg());
        }

        return response;
    }

    /**
     * 获取全局配置
     *
     * @return
     */
    public FreeTeamConfig getconfig() {
        FreeTeamConfig config = cacheManager.get(FREETEAM_REDIS_KEY, FreeTeamConfig.class);
        if (null == config) {
            config = freeteamConfigRepository.findOneByModuleCode(FREE_TEAM);
            cacheManager.set(FREETEAM_REDIS_KEY, config, 24 * 60 * 60);
        }

        return config;
    }

    @Override
    public FreeTeamConfigVo getConfig() {
        FreeTeamConfigVo configVo = new FreeTeamConfigVo();
        FreeTeamConfig config = this.getconfig();
        BeanUtils.copyProperties(config, configVo);
        return configVo;
    }

    @Transactional
    @Override
    public void updateFreeTeamConfig(FreeTeamConfigVo configVo) {
        try {
            FreeTeamConfig config = this.getconfig();
            config.setFreeDayCount(configVo.getFreeDayCount());
            config.setFreeWeekCount(configVo.getFreeWeekCount());
            config.setRiskScoreLimit(configVo.getRiskScoreLimit());
            configRepository.save(config);

            cacheManager.set(FREETEAM_REDIS_KEY, config, 24 * 60 * 60);
        } catch (BusinessException e) {
            logger.error("updateFreeTeamConfig >> exception :{} ", e.getMessage());
            throw new BusinessException(e.getErrCode(), e.getErrMsg());
        }
    }

    @Override
    public FreeTeamResponse checkFreeTeamChance(FreeTeamBasicParams freeTeamBasicParams) throws BusinessException {
        FreeTeamResponse response = null;
        String uid = freeTeamBasicParams.getUid();
        String deviceId = freeTeamBasicParams.getDeviceId();

        // 获取全局配置
        FreeTeamConfig config = this.getconfig();

        // 恶意设备检验
        response = this.checkMaliceDevice(freeTeamBasicParams, config);
        if (!FreeTeamEnum.SUCCESS.getCode().equals(response.getRiskCode())) {
            return response;
        }
        // 次数校验
        response = this.checkTimes(uid, deviceId, config);
        if (!FreeTeamEnum.SUCCESS.getCode().equals(response.getRiskCode())) {
            return response;
        }


        return response;
    }

    @Override
    public void updateTimes(String uids) throws BusinessException {
        long start = System.currentTimeMillis();
        if (StringUtils.isEmpty(uids)) {
            logger.error("riskrating >> updateTimes >> Exception : "
                    + BizExceptionEnum.FREE_TEAM_UID_NOT_NULL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.FREE_TEAM_UID_NOT_NULL);
        }

        List<FreeTeamBasicParams> paramsList = new ArrayList<>();
        String[] idArray = uids.split(",");
        List<String> uidList= Arrays.asList(idArray);
        List<UserDeviceBehaviourRecord> recordList = behaviourRecordRepository.findByUserIdIn(uidList);
        if (CollectionUtils.isNotEmpty(recordList)) {
            for (UserDeviceBehaviourRecord record : recordList) {
                FreeTeamBasicParams params = new FreeTeamBasicParams();
                params.setUid(record.getUserId());
                params.setDeviceId(record.getLoginDeviceId());
                paramsList.add(params);
            }
        }

        FreeTeamConfig config = this.getconfig();
        // 天
        int dayLimit = config.getFreeDayCount();

        try {
            // 启用多线程 执行每个deviceId 核减次数
            if (CollectionUtils.isNotEmpty(paramsList)) {
                ExecutorService pool = Executors.newFixedThreadPool(paramsList.size());
                UpdateTimesAsync task = new UpdateTimesAsync(paramsList, dayLimit);
                Future<String> future = pool.submit(task);
                pool.shutdown();

                String state =  future.get();
                logger.debug("UpdateTimesAsync.call() >> 出参 >> " + state);
            }
        } catch(InterruptedException ex) {
            logger.error("updateTimes >> Exception :  " + ex.getMessage());
            throw new BusinessException(BizExceptionEnum.FREE_TEAM_TIMES_ERROR);
        } catch (ExecutionException ex) {
            logger.error("updateTimes >> Exception :  " + ex.getMessage());
            throw new BusinessException(BizExceptionEnum.FREE_TEAM_TIMES_ERROR);
        }

        logger.info(">>riskrating >> updateTimes cost " + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 次数校验
     *
     * @param uid
     * @param deviceId
     * @return
     */
    private FreeTeamResponse checkTimes(String uid, String deviceId, FreeTeamConfig config) {
        FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());

        int dayLimit = config.getFreeDayCount(); // 天
        int weekLimit = config.getFreeWeekCount(); // 周

        LocalDate currentDate = DateUtil.now();
        FreeTeamChance chance = chanceRepository.findByDeviceIdAndStatisticsDate(deviceId, currentDate.toString());
        if (null != chance && 1 > chance.getFreeChanceRemain()) {
            response.setRiskCode(FreeTeamEnum.DAY_CHECK.getCode());
            response.setRiskMsg(FreeTeamEnum.DAY_CHECK.getMsg());
            return response;
        }

        LocalDateTime startDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime endDate = LocalDateTime.now();
        long dayTimes = recordRepository.countByDeviceIdAndEndDateBetween(deviceId, startDate, endDate);
        if (dayTimes >= dayLimit) {
            response.setRiskCode(FreeTeamEnum.DAY_CHECK.getCode());
            response.setRiskMsg(FreeTeamEnum.DAY_CHECK.getMsg());
            return response;
        }

        LocalDate monday = currentDate.with(DayOfWeek.MONDAY);
        LocalDateTime weekBeginDate = LocalDateTime.of(monday, LocalTime.MIDNIGHT);
        long weekTimes = recordRepository.countByDeviceIdAndEndDateBetween(deviceId, weekBeginDate, endDate);
        if (weekTimes >= weekLimit) {
            response.setRiskCode(FreeTeamEnum.WEEK_CHECK.getCode());
            response.setRiskMsg(FreeTeamEnum.WEEK_CHECK.getMsg());
            return response;
        }

        return response;
    }

    /**
     * 恶意设备校验
     *
     * @param freeTeamBasicParams
     * @param config
     * @return
     */
    private FreeTeamResponse checkMaliceDevice(FreeTeamBasicParams freeTeamBasicParams, FreeTeamConfig config) {
        FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
        String uid = freeTeamBasicParams.getUid();
        String deviceId = freeTeamBasicParams.getDeviceId();

        // 白名单
        FreeTeamDeviceWhite white = whiteRepository.findOneByDeviceId(deviceId);
        if (null != white) {
            return response;
        }

        long count = blackRepository.countByDeviceId(deviceId);
        // 已在黑名单中
        if (0 < count) {
            response.setRiskCode(FreeTeamEnum.MALICE_DEVICE.getCode());
            response.setRiskMsg(FreeTeamEnum.MALICE_DEVICE.getMsg());
            return response;
        }
        int limit = config.getRiskScoreLimit();

        ResponsePacket<MembersUserVo> userVo = riskServiceClient.getUserInfo(uid);
        MembersUserVo membersUserVo = userVo.getData();

        // 先查库，没有就调数美接口获取风险分及详情
        RiskControlNextdata entity = riskControlNextdataRepository.findByUidAndDeviceIdAndAction(uid, deviceId,
                RiskActionType.LOGIN.getCode());
        if (null != entity) {
            int checkScore = entity.getRiskScore();
            if (checkScore >= limit) {
                response.setRiskCode(FreeTeamEnum.MALICE_DEVICE.getCode());
                response.setRiskMsg(FreeTeamEnum.MALICE_DEVICE.getMsg());

                // 入库
                FreeTeamDeviceBlack black = new FreeTeamDeviceBlack();
                black.setUid(uid);
                black.setDeviceId(deviceId);
                black.setChickenId(Integer.valueOf(membersUserVo.getChickenId()));
                black.setNickName(membersUserVo.getUsername());
                black.setRiskScore(entity.getRiskScore());
                black.setRiskDetail(entity.getRiskDetail());
                blackRepository.save(black);

                return response;
            }

        } else {

            String phone = membersUserVo.getPhone();
            ShumeiQueryParams params = this.buildShumeiQueryParams(deviceId, ShumeiEventEnum.LOGIN.getEventCode(), phone);
            // 数美调用
            String thirdResponse = ShumeiApiUtil.post(params);
            if (StringUtils.isNotEmpty(thirdResponse)) {
                JSONObject jsonObject = JSONObject.parseObject(thirdResponse);
                logger.debug(" >> thirdResponse  >> " + jsonObject.toString());

                if (ShumeiResultCodeEnum.SUCCESS.getCode() == jsonObject.getInteger("code")) {
                    int score = jsonObject.getInteger("score");
                    if (score >= limit) {
                        response.setRiskCode(FreeTeamEnum.MALICE_DEVICE.getCode());
                        response.setRiskMsg(FreeTeamEnum.MALICE_DEVICE.getMsg());

                        JSONObject detail = jsonObject.getJSONObject("detail");
                        String model = detail.getString("model");
                        String description = detail.getString("description");
                        String riskDetail = description + "," + model;

                        // 入库
                        FreeTeamDeviceBlack black = new FreeTeamDeviceBlack();
                        black.setUid(uid);
                        black.setDeviceId(deviceId);
                        black.setChickenId(Integer.valueOf(membersUserVo.getChickenId()));
                        black.setNickName(membersUserVo.getUsername());
                        black.setRiskScore(score);
                        black.setRiskDetail(riskDetail);
                        blackRepository.save(black);

                        return response;
                    }
                } else {
                    logger.error("riskrating >> checkMaliceDevice >> Exception : "
                            + BizExceptionEnum.SHUMEI_API_CALL_FAIL.getErrMsg());
                    throw new BusinessException(BizExceptionEnum.SHUMEI_API_CALL_FAIL);
                }
            }
        }

        return response;
    }

    private ShumeiQueryParams buildShumeiQueryParams(String deviceId, String eventType, String phone) {
        ShumeiQueryParams params = new ShumeiQueryParams();
        params.setAccessKey(accessKey);
        params.setShumeiBaseUrl(shumeiBaseUrl);
        params.setDeviceId(deviceId);
        params.setEventType(eventType);
        params.setPhone(StringUtils.isEmpty(phone) ? "" : phone);

        return params;
    }

    @Override
    public void insertWhiteList(String deviceId) throws BusinessException {
        FreeTeamDeviceWhite entity = whiteRepository.findOneByDeviceId(deviceId);
        if (null != entity) {
            logger.error("riskrating >> updateTimes >> Exception : "
                    + BizExceptionEnum.FREE_TEAM_DEVICE_WHITE_ERROR.getErrMsg());
            throw new BusinessException(BizExceptionEnum.FREE_TEAM_DEVICE_WHITE_ERROR);
        }

        entity = new FreeTeamDeviceWhite();
        entity.setDeviceId(deviceId);
        whiteRepository.save(entity);
    }

    @Override
    public Map<String, Object> getWhiteList(FreeTeamWhiteQueryParams queryParams) throws BusinessException {
        Map<String, Object> map = new HashMap<>();

        String deviceId = queryParams.getDeviceId();
        String beginDate = queryParams.getBeginDate();
        String endDate = queryParams.getEndDate();
        //排序参数
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "createDate"));
        //分页参数
        PageRequest pageRequest = null;
        if (StringUtils.isNotEmpty(queryParams.getPage()) && StringUtils
                .isNotEmpty(queryParams.getSize())) {
            int page = Integer.parseInt(queryParams.getPage());
            int size = Integer.parseInt(queryParams.getSize());
            if (size == -1) {
                pageRequest = new PageRequest(page - 1, MAX_PAGE_SIZE, sort);
            } else {
                pageRequest = new PageRequest(page - 1, size, sort);
            }
        } else {
            pageRequest = new PageRequest(0, 20, sort);

        }

        Specification<FreeTeamDeviceWhite> specification = this.getSpecipicationInfo(deviceId, beginDate, endDate);

        //查询列表
        Page<FreeTeamDeviceWhite> respPage = whiteRepository.findAll(specification, pageRequest);
        long total = respPage.getTotalElements();
        map.put("total", total);

        List<FreeTeamWhiteDeviceVo> voList = new ArrayList<>();

        if (0 < total) {
            List<FreeTeamDeviceWhite> orderList = respPage.getContent();
            logger.debug("getWhiteList >> page ：{} ", orderList);

            for (FreeTeamDeviceWhite order : orderList) {
                FreeTeamWhiteDeviceVo vo = new FreeTeamWhiteDeviceVo();
                vo.setId(order.getId());
                vo.setDeviceId(order.getDeviceId());
                vo.setCreateDate(DateUtil.fromDate2Str(order.getCreateDate()));

                voList.add(vo);
            }
        }
        map.put("list", voList);

        return map;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public boolean deleteWhite(long id) {
        whiteRepository.delete(id);
        return true;
    }

    /**
     * 拼接查询条件
     *
     * @param deviceId
     * @param beginDate
     * @param endDate
     * @return
     */
    public Specification<FreeTeamDeviceWhite> getSpecipicationInfo(String deviceId, String beginDate, String endDate) {

        Specification<FreeTeamDeviceWhite> querySpecifiction = new Specification<FreeTeamDeviceWhite>() {
            @Override
            public Predicate toPredicate(Root<FreeTeamDeviceWhite> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ObjectTools.isNotEmpty(deviceId)) {
                    predicates.add(criteriaBuilder.equal(root.get("deviceId").as(String.class), deviceId));
                }

                if (ObjectTools.isNotEmpty(beginDate)) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate").as(String.class), beginDate));
                }

                if (ObjectTools.isNotEmpty(endDate)) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate").as(String.class), endDate));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return querySpecifiction;
    }


    /**
     * 线程池处理核减
     */
    class UpdateTimesAsync implements Callable<String> {

        private List<FreeTeamBasicParams> voList;
        private int dayLimit;

        public UpdateTimesAsync(List<FreeTeamBasicParams> voList, int dayLimit) {
            this.voList = voList;
            this.dayLimit = dayLimit;
        }

        @Override
        public String call() throws BusinessException {
            LocalDate currentDate = DateUtil.now();
            LocalDateTime endDate = LocalDateTime.now();
            for (int i = 0; i < voList.size(); i++) {
                FreeTeamBasicParams vo = voList.get(i);
                String deviceId = vo.getDeviceId();
                String uid = vo.getUid();
                FreeTeamChance chance = chanceRepository.findByDeviceIdAndStatisticsDate(deviceId, currentDate.toString());
                if (null == chance) {
                    chance = new FreeTeamChance();
                    chance.setDeviceId(deviceId);
                    chance.setStatisticsDate(currentDate.toString());
                    chance.setFreeChanceUsed(1);
                    chance.setFreeChanceRemain(dayLimit - 1);
                    chanceRepository.save(chance);
                } else {
                    int usedIndb = chance.getFreeChanceUsed();
                    int remainIndb = chance.getFreeChanceRemain();
                    logger.debug("数美设备id的免费车队上车次数参数为：deviceId={},used={},remain={}", deviceId, usedIndb, remainIndb);
                    if (usedIndb >= dayLimit) {
                        logger.error("riskrating >> updateTimes >> Exception : "
                                + BizExceptionEnum.FREE_TEAM_TIMES_ERROR.getErrMsg());
                        throw new BusinessException(BizExceptionEnum.FREE_TEAM_TIMES_ERROR);
                    }

                    AtomicInteger used = new AtomicInteger();
                    used.set(usedIndb);
                    Integer usedAfter = used.incrementAndGet();
                    chance.setFreeChanceUsed(usedAfter);
                    chance.setFreeChanceRemain(dayLimit - usedAfter);
                    chanceRepository.save(chance);
                }

                FreeTeamRecord record = new FreeTeamRecord();
                record.setUid(uid);
                record.setDeviceId(deviceId);
                record.setEndDate(endDate);
                recordRepository.save(record);

            }

            return "ok";
        }
    }

}
