package com.kaihei.esportingplus.riskrating.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.riskrating.api.enums.FreeTeamEnum;
import com.kaihei.esportingplus.riskrating.api.enums.RiskActionType;
import com.kaihei.esportingplus.riskrating.api.enums.ShumeiEventEnum;
import com.kaihei.esportingplus.riskrating.api.enums.ShumeiResultCodeEnum;
import com.kaihei.esportingplus.riskrating.api.params.ImMachineUpdateParams;
import com.kaihei.esportingplus.riskrating.api.params.ShumeiQueryParams;
import com.kaihei.esportingplus.riskrating.api.vo.*;
import com.kaihei.esportingplus.riskrating.domain.entity.*;
import com.kaihei.esportingplus.riskrating.repository.*;
import com.kaihei.esportingplus.riskrating.service.ImMachineService;
import com.kaihei.esportingplus.riskrating.util.ShumeiApiUtil;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImMachineServiceImpl implements ImMachineService {

    private static final Logger logger = LoggerFactory.getLogger(ImMachineServiceImpl.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    private static final String CONFIG_KEY = "risk:im_machine_config";

    /**
     * 查询用户
     */
    private static final String SELECT_TYPE_USER = "0";

    /**
     * 查询用户
     */
    private static final String SELECT_TYPE_DEVICE = "1";

    @Autowired
    private ImMachineConfigRepository configRepository;

    @Autowired
    private ImMachineDeviceBlackRepository deviceBlackRepository;

    @Autowired
    private ImMachineUserBlackRepository userBlackRepository;

    @Autowired
    private UserDeviceBindRepository userDeviceBindRepository;

    @Autowired
    private RiskControlNextdataRepository riskControlNextdataRepository;

    @Autowired
    private UserDeviceBehaviourRecordRepository behaviourRecordRepository;

    @Value("${app.shumei.baseurl}")
    private String shumeiBaseUrl;

    @Value("${app.shumei.accessKey}")
    private String accessKey;


    /**
     * 配置分页参数
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private PageRequest getPageRequest(String pageIndex, String pageSize) {
        //排序参数
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "createDate"));
        PageRequest pageRequest = null;
        if (StringUtils.isNotEmpty(pageIndex) && StringUtils
                .isNotEmpty(pageSize)) {
            int page = Integer.parseInt(pageIndex);
            int size = Integer.parseInt(pageSize);
            if (size == -1) {
                pageRequest = new PageRequest(page - 1, 9999, sort);
            } else {
                pageRequest = new PageRequest(page - 1, size, sort);
            }
        } else {
            pageRequest = new PageRequest(0, 20, sort);

        }
        return pageRequest;
    }

    @Override
    public ImMachineConfigVo getImMachineConfig() {
        ImMachineConfigVo configVo = new ImMachineConfigVo();
        ImMachineConfig config = this.getImMachineConfigFromRedis();
        BeanUtils.copyProperties(config, configVo);
        return configVo;
    }

    private ImMachineConfig getImMachineConfigFromRedis() {
        ImMachineConfig config = cacheManager.get(CONFIG_KEY, ImMachineConfig.class);
        if (null == config) {
            config = configRepository.findOne(1L);
            cacheManager.set(CONFIG_KEY, config, 24 * 60 * 60);
        }
        return  config;
    }

    @Override
    public void updateImMachineConfig(ImMachineConfigVo configVo) {
        logger.debug(">> IM防骚扰及虚拟机判断配置参数为 {}", configVo.toString());
        Long id = configVo.getId();
        ImMachineConfig config = configRepository.findOne(id);
        config.setMachineSwitch(configVo.getMachineSwitch() == null ? config.getMachineSwitch() : configVo.getMachineSwitch());
        config.setMachineScore(configVo.getMachineScore() == null ? config.getMachineScore() : configVo.getMachineScore());
        config.setBaojiChosen(configVo.getBaojiChosen() == null ? config.getBaojiChosen() : configVo.getBaojiChosen());
        config.setUserChosen(configVo.getUserChosen() == null ? config.getUserChosen() : configVo.getUserChosen());
        config.setChatCount(configVo.getChatCount() == null ? config.getChatCount() : configVo.getChatCount());
        config.setContentCount(configVo.getContentCount() == null ? config.getContentCount() : configVo.getContentCount());
        config.setTotalContentCount(configVo.getTotalContentCount() == null ? config.getTotalContentCount() : configVo.getTotalContentCount());
        config.setLoginLimit(configVo.getLoginLimit() == null ? config.getLoginLimit() : configVo.getLoginLimit());
        config.setRegisterLimit(configVo.getRegisterLimit() == null ? config.getLoginLimit() : configVo.getRegisterLimit());
        configRepository.save(config);

        cacheManager.set(CONFIG_KEY, config, 24 * 60 * 60);

    }

    @Override
    public PageInfo<ImMachineListVo> getDeviceBlackList(String deviceId, String page, String size) {
        PageInfo<ImMachineListVo> listVo = new PageInfo<>();

        PageRequest pageRequest = this.getPageRequest(page, size);

        Specification<ImMachineDeviceBlack> specification = this.getDeviceBlackSpecipicationInfo(deviceId);
        //查询列表
        Page<ImMachineDeviceBlack> respPage = deviceBlackRepository.findAll(specification, pageRequest);
        long total = respPage.getTotalElements();
        listVo.setTotal(total);

        List<ImMachineListVo> voList = new ArrayList<ImMachineListVo>();
        if (total > 0) {
            List<ImMachineDeviceBlack> blackList = respPage.getContent();
            logger.debug("getDeviceBlackList >> page ：{} ", blackList);
            for (ImMachineDeviceBlack black : blackList) {
                ImMachineListVo vo = new ImMachineListVo();
                vo.setId(black.getId());
                vo.setDeviceId(black.getDeviceId());
                vo.setCreateTime(DateUtil.fromDate2Str(black.getCreateDate()));

                voList.add(vo);
            }
        }
        listVo.setList(voList);

        return listVo;
    }

    public Specification<ImMachineDeviceBlack> getDeviceBlackSpecipicationInfo(String deviceId) {

        Specification<ImMachineDeviceBlack> querySpecifiction = new Specification<ImMachineDeviceBlack>() {
            @Override
            public Predicate toPredicate(Root<ImMachineDeviceBlack> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ObjectTools.isNotEmpty(deviceId)) {
                    predicates.add(criteriaBuilder.equal(root.get("deviceId").as(String.class), deviceId));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return querySpecifiction;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void insertDeviceBlack(String deviceId) {
        ImMachineDeviceBlack deviceBlack = deviceBlackRepository.findByDeviceId(deviceId);
        if (null != deviceBlack) {
            logger.error("ImMachineService >> insertDeviceBlack >> Exception : "
                    + BizExceptionEnum.IMMACHINE_DEVICE_ALREADY_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.IMMACHINE_DEVICE_ALREADY_EXIST);
        }
        ImMachineDeviceBlack black = new ImMachineDeviceBlack();
        black.setDeviceId(deviceId);
        deviceBlackRepository.save(black);

    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteDeviceBlack(long id) {
        deviceBlackRepository.delete(id);
    }

    private Specification<UserDeviceBind> getUserBindSpecipicationInfo(String type, String cloumn) {

        Specification<UserDeviceBind> querySpecifiction = new Specification<UserDeviceBind>() {
            @Override
            public Predicate toPredicate(Root<UserDeviceBind> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ObjectTools.isNotEmpty(cloumn)) {
                    if (SELECT_TYPE_USER.equals(type)) {
                        predicates.add(criteriaBuilder.equal(root.get("userId").as(String.class), cloumn));
                    } else if (SELECT_TYPE_DEVICE.equals(type)) {
                        predicates.add(criteriaBuilder.equal(root.get("deviceId").as(String.class), cloumn));
                    }

                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return querySpecifiction;
    }

    @Override
    public PageInfo<ImMachineListVo> getUserDeviceBindList(String type, String column, String page, String size) {
        PageInfo<ImMachineListVo> listVo = new PageInfo<>();

        PageRequest pageRequest = this.getPageRequest(page, size);

        Specification<UserDeviceBind> specification = this.getUserBindSpecipicationInfo(type, column);
        //查询列表
        Page<UserDeviceBind> respPage = userDeviceBindRepository.findAll(specification, pageRequest);
        long total = respPage.getTotalElements();
        listVo.setTotal(total);

        List<ImMachineListVo> voList = new ArrayList<ImMachineListVo>();
        if (total > 0) {
            List<UserDeviceBind> bindList = respPage.getContent();
            logger.debug("getUserDeviceBindList >> page ：{} ", bindList);
            for (UserDeviceBind bind : bindList) {
                ImMachineListVo vo = new ImMachineListVo();
                vo.setId(bind.getId());
                vo.setUserId(bind.getUserId());
                vo.setDeviceId(bind.getDeviceId());
                vo.setBindStatus(bind.getBindStatus());
                vo.setBindTime(bind.getBindTime() != null ? DateUtil.fromDate2Str(bind.getBindTime()) : "");
                vo.setUnbindTime(bind.getUnbindTime() != null ? DateUtil.fromDate2Str(bind.getUnbindTime()) : "");

                voList.add(vo);
            }
        }
        listVo.setList(voList);

        return listVo;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void unbindUserDeviceRelation(long id) {
        UserDeviceBind bind = userDeviceBindRepository.findOne(id);
        bind.setBindStatus(0);
        LocalDateTime now = LocalDateTime.now();
        Date unBindTime = DateUtil.localDateTime2Date(now);
        bind.setUnbindTime(unBindTime);
        userDeviceBindRepository.save(bind);

        // 解绑用户登陆设备绑定
        String userId = bind.getUserId();
        UserDeviceBehaviourRecord record = behaviourRecordRepository.findByUserId(userId);
        record.setLoginDeviceId("");
        behaviourRecordRepository.save(record);

    }

    @Override
    public PageInfo<ImMachineListVo> getUserImBlackList(String userId, String page, String size) {
        PageInfo<ImMachineListVo> listVo = new PageInfo<>();

        PageRequest pageRequest = this.getPageRequest(page, size);

        Specification<ImMachineUserBlack> specification = this.getUserBlackSpecipicationInfo(userId);
        //查询列表
        Page<ImMachineUserBlack> respPage = userBlackRepository.findAll(specification, pageRequest);
        long total = respPage.getTotalElements();
        listVo.setTotal(total);

        List<ImMachineListVo> voList = new ArrayList<ImMachineListVo>();
        if (total > 0) {
            List<ImMachineUserBlack> blackList = respPage.getContent();
            logger.debug("getUserImBlackList >> page ：{} ", blackList);
            for (ImMachineUserBlack black : blackList) {
                ImMachineListVo vo = new ImMachineListVo();
                vo.setId(black.getId());
                vo.setUserId(black.getUserId());
                vo.setLastLoginTime(DateUtil.fromDate2Str(black.getLastLoginTime()));

                voList.add(vo);
            }
        }
        listVo.setList(voList);

        return listVo;
    }

    public Specification<ImMachineUserBlack> getUserBlackSpecipicationInfo(String userId) {

        Specification<ImMachineUserBlack> querySpecifiction = new Specification<ImMachineUserBlack>() {
            @Override
            public Predicate toPredicate(Root<ImMachineUserBlack> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ObjectTools.isNotEmpty(userId)) {
                    predicates.add(criteriaBuilder.equal(root.get("userId").as(String.class), userId));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return querySpecifiction;
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteUserImBlack(long id) {
        userBlackRepository.delete(id);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public FreeTeamResponse registerCheck(ImMachineUpdateParams imMachineUpdateParams) {
        logger.debug(">> 用户注册-风控校验 入参为 {}", imMachineUpdateParams.toString());
        FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
        String uid = imMachineUpdateParams.getUserId();
        String deviceId = imMachineUpdateParams.getDeviceId();
//        String version = imMachineUpdateParams.getVersion();

        // 已有记录
//        RiskControlNextdata entity = riskControlNextdataRepository.findByUidAndDeviceIdAndAction(uid,
//                deviceId, RiskActionType.REGISTER.getCode());
//        if (null != entity) {
//            response.setRiskCode(FreeTeamEnum.REGISTER_REPEAT.getCode());
//            response.setRiskMsg(FreeTeamEnum.REGISTER_REPEAT.getMsg());
//            return response;
//        }

        String nextdataKey = "risk:nextdata:" + deviceId;
        RiskControlNextdata nextdata = cacheManager.get(nextdataKey, RiskControlNextdata.class);
        nextdata.setUid(uid);
        riskControlNextdataRepository.save(nextdata);

        UserDeviceBehaviourRecord record = new UserDeviceBehaviourRecord();
        record = this.buildRecordEntity(record, imMachineUpdateParams);
        behaviourRecordRepository.save(record);

        return response;
    }

    /**
     * 构建用户注册登陆设备行为记录实体类
     * @param record
     * @return
     */
    private UserDeviceBehaviourRecord buildRecordEntity(UserDeviceBehaviourRecord record, ImMachineUpdateParams imMachineUpdateParams) {
        record.setUserId(imMachineUpdateParams.getUserId());
        record.setRegisterDeviceId(imMachineUpdateParams.getDeviceId());
        record.setChannel(imMachineUpdateParams.getChannel());
        int systemPlatform = 1000;
        String platform = imMachineUpdateParams.getPlatform();
        if (StringUtils.isNotEmpty(platform)) {
            systemPlatform = Integer.valueOf(platform);
        }
        record.setPlatform(systemPlatform);
        record.setRcloudToken(imMachineUpdateParams.getRcloudToken());
        record.setUserAgent(imMachineUpdateParams.getUserAgent());

        return record;
    }

    /**
     * 构建数美查询参数
     * @param deviceId
     * @param eventType
     * @param phone
     * @return
     */
    private ShumeiQueryParams buildShumeiQueryParams(String deviceId, String eventType, String phone) {
        ShumeiQueryParams params = new ShumeiQueryParams();
        params.setAccessKey(accessKey);
        params.setShumeiBaseUrl(shumeiBaseUrl);
        params.setDeviceId(deviceId);
        params.setEventType(eventType);
        params.setPhone(StringUtils.isEmpty(phone) ? "" : phone);

        return params;
    }

    /**
     * 调用数美标识
     * @param version
     * @return
     */
    private boolean postFlag(String version) {
        boolean flag = true;
        if (StringUtils.isNotEmpty(version)) {
            String[] versionArray = version.split("_");
            String versionSource = versionArray[0];
            if ("mp".equalsIgnoreCase(versionSource)) {
                flag = false;
            }
        }
        return flag;

    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public FreeTeamResponse loginCheck(ImMachineUpdateParams imMachineUpdateParams) {
        logger.debug(">> 用户登陆-风控校验 入参为 {}", imMachineUpdateParams.toString());
        FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
        String uid = imMachineUpdateParams.getUserId();
        String deviceId = imMachineUpdateParams.getDeviceId();
        String version = imMachineUpdateParams.getVersion();

        ImMachineConfig config = this.getImMachineConfigFromRedis();
        int machineScore = config.getMachineScore();

        // 数美设备登陆上限校验
        int loginLimit = config.getLoginLimit();
        long loginCount = behaviourRecordRepository.countByLoginDeviceIdAndUserIdNot(deviceId, uid);
        if (loginCount >= loginLimit) {
            response.setRiskCode(FreeTeamEnum.LOGIN_OVER_LIMIT.getCode());
            response.setRiskMsg(FreeTeamEnum.LOGIN_OVER_LIMIT.getMsg());
            return response;
        }

        boolean postFlag = this.postFlag(version);

        // 已有记录
        RiskControlNextdata entity = riskControlNextdataRepository.findByUidAndDeviceIdAndAction(uid,
                deviceId, RiskActionType.LOGIN.getCode());
        if (null != entity) {
            int riskScore = entity.getRiskScore();
            if (riskScore >= machineScore) {
                response.setRiskCode(FreeTeamEnum.LOGIN_REJECT.getCode());
                response.setRiskMsg(FreeTeamEnum.LOGIN_REJECT.getMsg());
                return response;
            }
            // 有记录且正常
            postFlag = false;
        }

        // 小程序不调用数美
        if (postFlag) {
            String phone = imMachineUpdateParams.getPhone();
            ShumeiQueryParams params = this.buildShumeiQueryParams(deviceId, ShumeiEventEnum.LOGIN.getEventCode(), phone);
            // 数美调用
            String thirdResponse = ShumeiApiUtil.post(params);
            logger.debug("数美返回：【{}】", thirdResponse);
            if (StringUtils.isNotEmpty(thirdResponse)) {
                JSONObject jsonObject = JSONObject.parseObject(thirdResponse);
                logger.debug(" >> thirdResponse  >> " + jsonObject.toString());

                if (ShumeiResultCodeEnum.SUCCESS.getCode() == jsonObject.getInteger("code")) {
                    int score = jsonObject.getInteger("score");

                    if (null == entity) {
                        String riskLevel = jsonObject.getString("riskLevel");
                        JSONObject detail = jsonObject.getJSONObject("detail");
                        String model = detail.getString("model");
                        String description = detail.getString("description");
                        String riskDetail = description + "," + model;

                        RiskControlNextdata nextdata = new RiskControlNextdata();
                        nextdata.setUid(uid);
                        nextdata.setDeviceId(deviceId);
                        nextdata.setAction(RiskActionType.LOGIN.getCode());
                        nextdata.setRiskScore(score);
                        nextdata.setRiskLevel(riskLevel);
                        nextdata.setRiskDetail(riskDetail);
                        nextdata.setSource(jsonObject.toJSONString());
                        nextdata.setCreateDatetime(new Date());
                        riskControlNextdataRepository.save(nextdata);
                    }

                    if (score >= machineScore) {
                        response.setRiskCode(FreeTeamEnum.REGISTER_REJECT.getCode());
                        response.setRiskMsg(FreeTeamEnum.REGISTER_REJECT.getMsg());
                        logger.info("用户{} 发起登陆操作，数美设备标识{}，评估风险分为{}，判定虚拟机标准分为{} ", uid, deviceId,
                                score, machineScore);

                        return response;
                    }

                } else {
                    logger.error("riskrating >> loginCheck >> Exception : "
                            + BizExceptionEnum.SHUMEI_API_CALL_FAIL.getErrMsg());
                    throw new BusinessException(BizExceptionEnum.SHUMEI_API_CALL_FAIL);
                }

            }
        }
        UserDeviceBehaviourRecord record = behaviourRecordRepository.findByUserId(uid);
        // 老数据-注册登陆都用当前deviceId
        if (null == record) {
            record = new UserDeviceBehaviourRecord();
            record = this.buildRecordEntity(record, imMachineUpdateParams);
        }
        record.setLoginDeviceId(deviceId);
        behaviourRecordRepository.save(record);

        // 账户数美设备绑定
        UserDeviceBind bind = userDeviceBindRepository.findOneByUserIdAndDeviceId(uid, deviceId);
        if (bind == null) {
            bind = new UserDeviceBind();
            bind.setUserId(uid);
            bind.setDeviceId(deviceId);
            bind.setBindStatus(1);
            LocalDateTime now = LocalDateTime.now();
            Date bindTime = DateUtil.localDateTime2Date(now);
            bind.setBindTime(bindTime);
            userDeviceBindRepository.save(bind);
        }

        return response;
    }

    @Override
    public FreeTeamResponse registerCheckBeforeGenerateUserId(String deviceId, String phone, String version) {
        FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());
        // 数美设备注册上限校验
        ImMachineConfig config = configRepository.findOne(1L);
        int registerLimit = config.getRegisterLimit();
        // 判定虚拟机的数美风险分
        int machineScore = config.getMachineScore();
        long registerCount = behaviourRecordRepository.countByRegisterDeviceId(deviceId);
        if (registerCount >= registerLimit) {
            response.setRiskCode(FreeTeamEnum.REGISTER_OVER_LIMIT.getCode());
            response.setRiskMsg(FreeTeamEnum.REGISTER_OVER_LIMIT.getMsg());
            return response;
        }

        // 小程序不调用数美
        boolean flag = this.postFlag(version);

        if (flag) {
            ShumeiQueryParams params = this.buildShumeiQueryParams(deviceId, ShumeiEventEnum.REGISTER.getEventCode(), phone);
            // 数美调用
            String thirdResponse = ShumeiApiUtil.post(params);
            logger.debug("数美返回：【{}】", thirdResponse);
            if (StringUtils.isNotEmpty(thirdResponse)) {
                JSONObject jsonObject = JSONObject.parseObject(thirdResponse);
                logger.debug(" >> thirdResponse  >> " + jsonObject.toString());

                if (ShumeiResultCodeEnum.SUCCESS.getCode() == jsonObject.getInteger("code")) {
                    String riskLevel = jsonObject.getString("riskLevel");
                    JSONObject detail = jsonObject.getJSONObject("detail");
                    String model = detail.getString("model");
                    String description = detail.getString("description");
                    String riskDetail = description + "," + model;
                    int score = jsonObject.getInteger("score");

                    RiskControlNextdata nextdata = new RiskControlNextdata();
//                    nextdata.setUid(uid);
                    nextdata.setDeviceId(deviceId);
                    nextdata.setAction(RiskActionType.REGISTER.getCode());
                    nextdata.setRiskScore(score);
                    nextdata.setRiskLevel(riskLevel);
                    nextdata.setRiskDetail(riskDetail);
                    nextdata.setSource(jsonObject.toJSONString());
                    nextdata.setCreateDatetime(new Date());

                    String nextdataKey = "risk:nextdata:" + deviceId;
                    cacheManager.set(nextdataKey, nextdata, 24 * 60 * 60);
//                    riskControlNextdataRepository.save(nextdata);

                    if (score >= machineScore) {
                        response.setRiskCode(FreeTeamEnum.REGISTER_REJECT.getCode());
                        response.setRiskMsg(FreeTeamEnum.REGISTER_REJECT.getMsg());
                        logger.info(" 生成uid前，发起注册操作，数美设备标识{}，评估风险分为{}，判定虚拟机标准分为{} ", deviceId,
                                score, machineScore);

                        return response;
                    }

                } else {
                    logger.error("riskrating >> registerCheck >> Exception : "
                            + BizExceptionEnum.SHUMEI_API_CALL_FAIL.getErrMsg());
                    throw new BusinessException(BizExceptionEnum.SHUMEI_API_CALL_FAIL);
                }

            }
        }

        return response;
    }

    @Override
    public UserDeviceBehaviourRecordVo getBehaviourRecordInfo(String userId) {
        UserDeviceBehaviourRecordVo vo = new UserDeviceBehaviourRecordVo();

        UserDeviceBehaviourRecord record = behaviourRecordRepository.findByUserId(userId);
        if (null != record) {
            BeanUtils.copyProperties(record, vo);
        }

        return vo;
    }

    @Override
    public void updateBehaviourRecord(UserDeviceBehaviourRecordVo vo) {

        String userId = vo.getUserId();
        UserDeviceBehaviourRecord record = behaviourRecordRepository.findByUserId(userId);

        String userAgent = vo.getUserAgent() == null ? record.getUserAgent(): vo.getUserAgent();
        String channel = vo.getChannel() == null ? record.getChannel() : vo.getChannel();
        String rcloudToken = vo.getRcloudToken() == null ? record.getRcloudToken() : vo.getRcloudToken();
        Integer platform = vo.getPlatform() == null ? record.getPlatform() : vo.getPlatform();
        record.setUserAgent(userAgent);
        record.setChannel(channel);
        record.setRcloudToken(rcloudToken);
        record.setPlatform(platform);
        behaviourRecordRepository.save(record);
    }
}
