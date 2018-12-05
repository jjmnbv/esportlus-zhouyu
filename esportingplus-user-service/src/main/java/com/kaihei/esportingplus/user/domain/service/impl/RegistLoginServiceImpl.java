package com.kaihei.esportingplus.user.domain.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.core.api.enums.SmsAuthenticationTypeEnum;
import com.kaihei.esportingplus.core.api.feign.SMSServiceClient;
import com.kaihei.esportingplus.core.api.params.SmsCredentialParam;
import com.kaihei.esportingplus.riskrating.api.feign.ImMachineBackendClient;
import com.kaihei.esportingplus.riskrating.api.params.ImMachineUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import com.kaihei.esportingplus.riskrating.api.vo.ImMachineConfigVo;
import com.kaihei.esportingplus.user.api.params.MiniprogramLoginParam;
import com.kaihei.esportingplus.user.api.params.MpPhoneBindLoginParam;
import com.kaihei.esportingplus.user.api.params.MpWxPhoneBindLoginParam;
import com.kaihei.esportingplus.user.api.params.PhoneRegistParam;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import com.kaihei.esportingplus.user.api.params.UserPhoneLoginParams;
import com.kaihei.esportingplus.user.api.vo.GreetingMessageVo;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import com.kaihei.esportingplus.user.api.vo.MiniprogramLoginVo;
import com.kaihei.esportingplus.user.api.vo.PhoneLoginContext;
import com.kaihei.esportingplus.user.api.vo.PhoneRegistVo;
import com.kaihei.esportingplus.user.api.vo.RegistLoginContext;
import com.kaihei.esportingplus.user.api.vo.ThirdpartLoginVo;
import com.kaihei.esportingplus.user.api.vo.UserPhoneLoginVo;
import com.kaihei.esportingplus.user.api.vo.WxUserInfo;
import com.kaihei.esportingplus.user.config.UserProperties;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.data.manager.MembersPhoneCacheManager;
import com.kaihei.esportingplus.user.data.manager.MembersUserManager;
import com.kaihei.esportingplus.user.data.pyrepository.MembersRegisteredDeviceRepository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersRegisteredDevice;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.service.AuthTokenService;
import com.kaihei.esportingplus.user.domain.service.MembersAuth3Service;
import com.kaihei.esportingplus.user.domain.service.MembersUserService;
import com.kaihei.esportingplus.user.domain.service.RegistLoginService;
import com.kaihei.esportingplus.user.domain.service.validate.UserValidate;
import com.kaihei.esportingplus.user.domain.service.validate.WxService;
import com.kaihei.esportingplus.user.external.rongyun.RongYunService;
import com.kaihei.esportingplus.user.mq.producer.CommonProducer;
import com.kaihei.esportingplus.user.utils.Utils;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-24 10:54
 * @Description:
 */
@Service
public class RegistLoginServiceImpl implements RegistLoginService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MembersUserService membersUserService;

    @Autowired
    private MembersUserManager membersUserManager;

    @Autowired
    private MembersUserRepository membersUserRepository;

    @Autowired
    private CommonProducer commonProducer;

    @Autowired
    private RongYunService rongYunService;

    @Autowired
    private MembersAuth3Service membersAuth3Service;

    @Resource(name = "userLoginValidate")
    private UserValidate userLoginValidate;

    @Resource(name = "pythonCacheManager")
    private CacheManager pythonCacheManager;

    @Autowired
    private MembersPhoneCacheManager membersPhoneCacheManager;

    @Autowired
    private WxService wxService;

    @Autowired
    private UserProperties userProperties;

    @Autowired
    private MembersRegisteredDeviceRepository membersRegisteredDeviceRepository;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private SMSServiceClient sMSServiceClient;

    @Autowired
    private ImMachineBackendClient imMachineBackendClient;

    private boolean shouldVerifyNextData() {
        ResponsePacket<ImMachineConfigVo> resp = imMachineBackendClient.getImMachineConfig();
        if (resp == null || BizExceptionEnum.SUCCESS.getErrCode() != resp.getCode()) {
            logger.error("cmd=RegistLoginService.shouldVerifyNextData | msg=获取虚拟机开关服务返回null或失败");
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        }
        ImMachineConfigVo vo = resp.getData();
        if (vo == null || vo.getMachineSwitch() == null) {
            logger.error(
                    "cmd=RegistLoginService.shouldVerifyNextData | msg=获取虚拟机开关服务data返回null或machineswitch返回null");
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        }
        return vo.getMachineSwitch() == 0 ? false : true;
    }

    /**
     * 第三方注册或登录接口
     *
     * @param params post参数
     * @param mDeviceId header数美设备指纹参数
     * @return token和融云token
     */
    @Override
    public ResponsePacket<ThirdpartLoginVo> loginOrRegist(ThirdPartLoginParams params,
            String mDeviceId, String version) {
        //1.检查系统是否关闭
        membersUserManager.checkSystemSwitch(null, false);
        //2.检查第三方登录合法性。（目前只有微信、QQ。如果是QQ登录，同时保存unionid）
        if (!membersUserManager.checkLegalThirdpart(params)) {
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_THIRDPART_VALID_FAIL);
        }

        ThirdpartLoginVo resp = new ThirdpartLoginVo();
        //3.调用redis返回数据判断是走注册流程还是走登录流程
        Integer userId = membersAuth3Service
                .getByIdentifierOrUnionId(params.getPlatform(), params.getIdentifier(),
                        params.getUnionid());
        boolean isRegist = userId == null ? Boolean.TRUE : Boolean.FALSE;
        MembersUser user = null;
        boolean shouldVerify = false;
        if (isRegist) {
            //检查系统是否在白名单模式，是的话禁止注册
            membersUserManager.checkSystemSwitch(null, true);
            //开始校验风控：1.虚拟机开关。2.注册次数风控
            shouldVerify = shouldVerifyNextData();
            if (shouldVerify) {
                ResponsePacket re = registCheck(mDeviceId, null, version);
                if (!re.responseSuccess()) {
                    return re;
                }
            }
            //系统无此用户，先走注册逻辑
            user = registUser(params, mDeviceId, resp, version);
            if (shouldVerify) {
                // 注册风控
                thirdPartRegistNotifyRisk(params, mDeviceId, version, "register");
            }
            resp.setPhoneBind(false);
        } else {
            //注册过的用户直接找出user
            user = membersUserRepository.selectByUserId(userId);
            params.setUid(user.getUid());
            //检查系统是否在白名单模式，如在白名单模式只允许白名单用户登录
            membersUserManager.checkSystemSwitch(user, false);
            shouldVerify = shouldVerifyNextData();
            if (shouldVerify) {
                // 登录风控
                ResponsePacket r = thirdpartLoginCheck(params, mDeviceId, version, "login");
                //风控校验不合格
                if (!r.responseSuccess()) {
                    return r;
                }
            }
            if (StringUtils.isEmpty(user.getPhone()) || user.getUid().equals(user.getPhone())) {
                resp.setPhoneBind(false);
            } else {
                resp.setPhoneBind(true);
            }
        }
        //1. 检查用户是否被限制登录
        checkUserValid(user);
        thirdProcessLogin(new RegistLoginContext(BeanMapper.map(user, MembersUserVo.class), params,
                isRegist, mDeviceId, version), resp);
        return ResponsePacket.onSuccess(resp);
    }

    private boolean codeValid(String code, String phone, SmsAuthenticationTypeEnum type) {
        //白名单模式白名单的用户无需校验，非白名单模式校验验证码
        if (userProperties.getUserSystemSwitch() == MembersAuthConstants.SYSTEM_WHITE_LIST
                || userProperties.getTestPhone().contains(phone)) {
            return true;
        }
        SmsCredentialParam pa = new SmsCredentialParam();
        pa.setCode(code);
        pa.setPhone(phone);
        pa.setType(type);
        ResponsePacket<Boolean> resp = sMSServiceClient.credential(pa);
        if (!resp.responseSuccess() || resp.getData() == null) {
            logger.info("cmd=RegistLoginService.codeValid | msg=调用验证短信验证码返回失败 | req={}",
                    JacksonUtils.toJson(pa));
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        }
        if (!resp.getData()) {
            logger.info("cmd=RegistLoginService.codeValid | msg=验证码错误 | req={}",
                    JacksonUtils.toJson(pa));
            return false;
        }
        return true;
    }

    private ResponsePacket registCheck(String mDevice, String phone, String version) {
        ResponsePacket<FreeTeamResponse> re = imMachineBackendClient
                .registerCheckBeforeGenerateUserId(mDevice, phone, version);
        if (!re.responseSuccess() || re.getData() == null) {
            logger.info(
                    "cmd=RegistLoginService.registCheck | msg=注册调用注册风控校验失败 | req={} ｜ mDevice={} | phone={} | version={}",
                    mDevice, phone, version);
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        }
        FreeTeamResponse vo = re.getData();
        if (vo == null || !vo.getRiskCode().equals("success")) {
            logger.error(
                    "cmd=RegistLoginService.registCheck | msg=注册风控校验不通过 | req={} ｜ mDevice={} | phone={} | version={}",
                    mDevice, phone, version);
            return ResponsePacket.onError(BizExceptionEnum.RISK_VERIFY_NOT_PASS);
        }
        return ResponsePacket.onSuccess();
    }

    /**
     * 手机注册方法
     */
    @Override
    public ResponsePacket<PhoneRegistVo> registPhone(PhoneRegistParam params, String mDevice,
            String version) {
        PhoneRegistVo resp = new PhoneRegistVo();
        //1. 检查系统是否关闭
        membersUserManager.checkSystemSwitch(null, true);
        //此处传注册码类型为登录是因为前端流程导致：前端先调登录接口发现token为空则用同一个
        //验证码调用注册接口，所以类型依然是登录类型
        if (!codeValid(params.getCode(), params.getPhone(), SmsAuthenticationTypeEnum.LOGIN)) {
            throw new BusinessException(BizExceptionEnum.USER_PHONE_CODE_FAIL);
        }

        //从缓存查询手机号对应的用户id,看是否存在该用户
        Integer userId = membersPhoneCacheManager
                .getUserIdByPhone(params.getPhone());
        boolean isRegisted = (userId != null && userId.intValue() > 0);
        if (isRegisted) {
            logger.info("cmd=RegistLoginService.registPhone | msg=手机号码已注册 | req={}",
                    JacksonUtils.toJson(params));
            return ResponsePacket.onError(BizExceptionEnum.PHONE_HAS_REGIST_USER);
        }

        //开始校验风控：1.虚拟机开关。2.注册次数风控
        boolean shouldVerify = shouldVerifyNextData();
        if (shouldVerify) {
            ResponsePacket re = registCheck(mDevice, params.getPhone(), version);
            if (!re.responseSuccess()) {
                return re;
            }
        }

        membersUserManager.checkUserNameForPhoneRegist(params.getUsername());
        //3. TODO 预先生成user的uid，下面会用到
        String uid = membersUserService.getRandomUid();
        params.setUid(uid);
        //创建user
        MembersUser user = membersUserManager.phoneRegistCreateUser(params);

        //获取融云token
        String rToken = rongYunService
                .getToken(user.getUid(), user.getUsername(), user.getThumbnail());
        resp.setRcloud_token(rToken);

        //通知风控服务注册事件
        if (shouldVerify) {
            registNotifyRisk(params, mDevice, version, "register");
        }

        if (StringUtils.isNotBlank(params.getInvitingUid())) {
            //邀请注册事件上报
            commonProducer.sendAsync(RocketMQConstant.TOPIC_SHARE_INVIT,
                    RocketMQConstant.INVIT_REGIST_TAG,
                    Utils.getMessageKey(params.getInvitingUid(), "inviting", params.getUid()),
                    Utils.userRegistEvent(params.getUid(), params.getInvitingUid()));
        }
        processLogin(new RegistLoginContext(BeanMapper.map(user, MembersUserVo.class), params, true,
                mDevice, version), resp);
        return ResponsePacket.onSuccess(resp);
    }

    private ResponsePacket thirdpartLoginCheck(ThirdPartLoginParams param, String mDeviceId,
            String version,
            String type) {
        PhoneRegistParam p = new PhoneRegistParam();
        p.setUser_agent(param.getUser_agent());
        p.setUid(param.getUid());
        p.setPlatform(param.getPhonePlatform());
        return loginCheck(p, mDeviceId, version, type);
    }

    private ResponsePacket loginCheck(PhoneRegistParam param, String mDeviceId,
            String version,
            String type) {
        ResponsePacket<FreeTeamResponse> resp = notifyRisk(param, mDeviceId, version, type);
        if (resp == null || BizExceptionEnum.SUCCESS.getErrCode() != resp.getCode()) {
            logger.error(
                    "cmd=RegistLoginService.loginCheck | msg=通知风控服务返回null或失败 | req={} | resp={} | mDeviceId={} | version={} | type={}",
                    JacksonUtils.toJson(param), JacksonUtils.toJson(resp), mDeviceId, version,
                    type);
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        }
        FreeTeamResponse vo = resp.getData();
        if (vo == null || !vo.getRiskCode().equals("success")) {
            logger.error(
                    "cmd=RegistLoginService.loginCheck | msg=风控校验不通过 | req={} | resp={} | mDeviceId={} | version={} | type={}",
                    JacksonUtils.toJson(param), JacksonUtils.toJson(resp), mDeviceId, version,
                    type);
            return ResponsePacket.onError(BizExceptionEnum.RISK_VERIFY_NOT_PASS);
        }
        return ResponsePacket.onSuccess();
    }

    private boolean thirdPartRegistNotifyRisk(ThirdPartLoginParams param, String mDeviceId,
            String version,
            String type) {
        PhoneRegistParam p = new PhoneRegistParam();
        p.setUser_agent(param.getUser_agent());
        p.setUid(param.getUid());
        p.setPlatform(param.getPhonePlatform());
        return registNotifyRisk(p, mDeviceId, version, type);
    }

    private boolean registNotifyRisk(PhoneRegistParam param, String mDeviceId, String version,
            String type) {
        ResponsePacket<FreeTeamResponse> resp = notifyRisk(param, mDeviceId, version, type);
        if (resp == null || BizExceptionEnum.SUCCESS.getErrCode() != resp.getCode()) {
            logger.error(
                    "cmd=RegistLoginService.registNotifyRisk | msg=通知风控服务返回null或失败 | req={} | resp={} | mDeviceId={} | version={} | type={}",
                    JacksonUtils.toJson(param), JacksonUtils.toJson(resp), mDeviceId, version,
                    type);
            return false;
        }
        FreeTeamResponse vo = resp.getData();
        if (vo == null || !vo.getRiskCode().equals("success")) {
            logger.error(
                    "cmd=RegistLoginService.registNotifyRisk | msg=通知风控服务失败 | req={} | resp={} | mDeviceId={} | version={} | type={}",
                    JacksonUtils.toJson(param), JacksonUtils.toJson(resp), mDeviceId, version,
                    type);
            return false;
        }
        return true;
    }

    private ResponsePacket<FreeTeamResponse> notifyRisk(PhoneRegistParam param, String mDeviceId,
            String version,
            String type) {
        ImMachineUpdateParams pm = new ImMachineUpdateParams();
        pm.setChannel(param.getChannel());
        pm.setDeviceId(mDeviceId);
        pm.setPhone(param.getPhone());
        pm.setUserId(param.getUid());
        pm.setUserAgent(param.getUser_agent());
        pm.setPlatform(param.getPlatform());
        pm.setType(type);
        pm.setVersion(version);
        ResponsePacket<FreeTeamResponse> resp = imMachineBackendClient.loginRegisterCheck(pm);
        return resp;
    }

    /**
     * 小程序union认证
     */
    @Override
    public ResponsePacket<MiniprogramLoginVo> miniprogramLogin(MiniprogramLoginParam param,
            String mDeviceId, String version) {

        String sessionKey = wxService.getSessionKey(param.getCode());
        WxUserInfo userInfo = wxService.getWxUserInfo(param.getUserInfo(), sessionKey);
        cacheSessionKey(userInfo.getOpenId(), sessionKey);
        MembersUser user = membersUserService
                .getUserByAuth3(userInfo.getUnionId(), MembersAuthConstants.PLATFORM_WX);
        MiniprogramLoginVo vo = new MiniprogramLoginVo();
        if (user == null || user.getUid().equals(user.getPhone())) {
            vo.setOpenid(userInfo.getOpenId());
            vo.setUnionid(userInfo.getUnionId());
            return ResponsePacket.onSuccess(vo);
        }
        // 更新token
        String token = authTokenService
                .getAccessToken(user.getUid(), version);
        vo.setToken(token);
        String rToken = mpGetOrCreateRtoken(user, false);
        vo.setRcloudToken(rToken);
        return ResponsePacket.onSuccess(vo);
    }


    /**
     * 小程序微信手机号登录
     */
    @Override
    public ResponsePacket<MiniprogramLoginVo> wxPhoneBindLogin(MpWxPhoneBindLoginParam param,
            String mDeviceId, String version) {

        String sessionKey = getSessionKey(param.getOpenid());
        if (sessionKey == null) {
            logger.info("cmd=RegistLoginService.wxPhoneBindLogin | msg=没有sessionKey | openid={}",
                    param.getOpenid());
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }

        WxUserInfo userInfo = wxService.getWxUserInfo(param.getUserInfo(), sessionKey);
        MembersUser userByUnionId = membersUserService
                .getUserByAuth3(userInfo.getUnionId(), MembersAuthConstants.PLATFORM_WX);
        //1.如果该用户phone不是初始化的uid，代表手机号已经绑定了
        if (userByUnionId != null && !userByUnionId.getUid().equals(userByUnionId.getPhone())) {
            logger.info("cmd=RegistLoginService.wxPhoneBindLogin | msg=该微信已经绑定手机 | openid={}",
                    param.getOpenid());
            throw new BusinessException(BizExceptionEnum.WX_PHONE_HAS_BINDED);
        }

        String phone = wxService.getWxPhone(param.getPhoneInfo(), sessionKey);
        MembersUser userByPhone = membersUserService.getUserByPhone(phone);
        MembersUser user = null;
        boolean isRegist = false;
        if (userByPhone != null) {//该手机有用户
            //2.不是该unionid绑定的，则该手机号码被注册了
            if (userByUnionId != null && userByPhone.getId().intValue() != userByUnionId.getId()
                    .intValue()) {
                logger.info(
                        "cmd=RegistLoginService.wxPhoneBindLogin | msg=手机号码已注册 | openid={} | phone={}",
                        param.getOpenid(), phone);
                throw new BusinessException(BizExceptionEnum.PHONE_HAS_REGIST_USER);
            }
            //3.检查手机号用户有没有绑定微信
            if (membersUserService
                    .userHasBindAuth(userByPhone.getId(), MembersAuthConstants.PLATFORM_WX)) {
                logger.info(
                        "cmd=RegistLoginService.wxPhoneBindLogin | msg=手机号已经绑定微信 | openid={} | phone={}",
                        param.getOpenid(), phone);
                throw new BusinessException(BizExceptionEnum.PHONE_HAS_BINDED_WX);
            }
            //绑定微信
            membersAuth3Service.createAuth3Wx(userByPhone.getId(), userInfo.getUnionId());
            user = userByPhone;
        } else {//该手机没有用户
            if (userByUnionId != null) {//更新手机号码
                userByUnionId.setPhone(phone);
                int i = membersUserRepository.updateByPrimaryKeySelective(userByUnionId);
                if (i < 0) {
                    logger.error(
                            "cmd=RegistLoginService.wxPhoneBindLogin | msg=更新用户手机号码失败 | openid={} | phone={}",
                            param.getOpenid(), phone);
                    throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
                }
                user = userByUnionId;
            } else {//全都不存在，则创建user,auth3,registdevice
                user = mpRegist(userInfo, phone, param.getChannel(), version);
                isRegist = true;
            }
        }

        String token = authTokenService
                .getAccessToken(user.getUid(), version);// 获取token
        String rToken = mpGetOrCreateRtoken(user, isRegist);
        MiniprogramLoginVo vo = new MiniprogramLoginVo();
        vo.setRcloudToken(rToken);
        vo.setToken(token);
        vo.setUnionid(userInfo.getUnionId());
        vo.setOpenid(userInfo.getOpenId());
        return ResponsePacket.onSuccess(vo);
    }

    /**
     * 小程序自选手机号登录
     */
    @Override
    public ResponsePacket<MiniprogramLoginVo> phoneBindLogin(MpPhoneBindLoginParam param,
            String mDeviceId, String version) {

        if (!codeValid(param.getCode(), param.getPhone(), SmsAuthenticationTypeEnum.LOGIN)) {
            throw new BusinessException(BizExceptionEnum.USER_PHONE_CODE_FAIL);
        }

        String sessionKey = getSessionKey(param.getOpenid());
        if (sessionKey == null) {
            logger.info("cmd=RegistLoginService.phoneBindLogin | msg=没有sessionKey | openid={}",
                    param.getOpenid());
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }

        WxUserInfo userInfo = wxService.getWxUserInfo(param.getUserInfo(), sessionKey);
        MembersUser userByUnionId = membersUserService
                .getUserByAuth3(userInfo.getUnionId(), MembersAuthConstants.PLATFORM_WX);
        MembersUser userByPhone = membersUserService.getUserByPhone(param.getPhone());
        MembersUser user = null;
        boolean isRegist = false;
        if (userByPhone != null) {
            if (userByUnionId == null) {
                membersAuth3Service.createAuth3Wx(userByPhone.getId(), userInfo.getUnionId());
            }
            user = userByPhone;
        } else {
            if (userByUnionId != null) {
                user = userByUnionId;
                userByUnionId.setPhone(param.getPhone());
                int i = membersUserRepository.updateByPrimaryKeySelective(userByUnionId);
                if (i < 0) {
                    logger.error(
                            "cmd=RegistLoginService.wxPhoneBindLogin | msg=更新用户手机号码失败 | openid={} | phone={}",
                            param.getOpenid(), param.getPhone());
                    throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
                }
            } else {
                mpRegist(userInfo, param.getPhone(), param.getChannel(), version);
                isRegist = true;
            }
        }
        String token = authTokenService
                .getAccessToken(user.getUid(), version);// 获取token
        String rToken = mpGetOrCreateRtoken(user, isRegist);
        MiniprogramLoginVo vo = new MiniprogramLoginVo();
        vo.setRcloudToken(rToken);
        vo.setToken(token);
        vo.setUnionid(userInfo.getUnionId());
        vo.setOpenid(userInfo.getOpenId());

        return ResponsePacket.onSuccess(vo);
    }

    private MembersUser mpRegist(WxUserInfo info, String phone, String channel,
            String version) {
        MembersUser user = membersUserService.mpCreateUser(info, phone);
        membersAuth3Service.createAuth3Wx(user.getId(), info.getUnionId());
        createRegistDevice(user, channel);
        //TODO 发放优惠券
        // 异步神策注册
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_PRFILESET_KEY,
                Utils.getMessageKey("prfileset", user.getUid()),
                Utils.registingMessageVo(user, version, channel));
        return user;
    }

    private void createRegistDevice(MembersUser user, String channel) {
        MembersRegisteredDevice d = new MembersRegisteredDevice();
        d.setUserId(user.getId());
        d.setDeviceId(user.getUid());
        d.setLoginDeviceId(user.getUid());
        d.setChannel(channel);
        d.setPlatform(1000);
        int i = membersRegisteredDeviceRepository.insertSelective(d);
        if (i < 0) {
            logger.error(
                    "cmd=RegistLoginService.createRegistDevice | msg=新增RegistDevice失败 | record={}",
                    JacksonUtils.toJson(d));
        }
    }

    public String getSessionKey(String openId) {
        String pythonRedisKey = String
                .format(UserRedisKey.PYTHON_WX_SESSIONKEY_KEY, userProperties.getWxMpAppId(),
                        openId);
        String sessionKey = pythonCacheManager.get(pythonRedisKey, String.class);
        return sessionKey;
    }

    public String mpGetOrCreateRtoken(MembersUser user, boolean isNew) {
        String rToken = null;
        if (!isNew) {
            rToken = pythonCacheManager
                    .get(String.format(UserRedisKey.PYTHON_RTOKEN_KEY, user.getUid()),
                            String.class);
            if (rToken != null) {
                return rToken;
            }
            MembersRegisteredDevice d = membersUserService.getOrCreateDevice(user);
            if (StringUtils.isNotBlank(d.getRcloudToken())) {
                pythonCacheManager
                        .set(String.format(UserRedisKey.PYTHON_RTOKEN_KEY, user.getUid()), rToken,
                                86400);
                return d.getRcloudToken();
            }
        }
        //获取融云token
        rToken = rongYunService
                .getToken(user.getUid(), user.getUsername(), user.getThumbnail());

        pythonCacheManager
                .set(String.format(UserRedisKey.PYTHON_RTOKEN_KEY, user.getUid()), rToken,
                        86400);

        // 异步：发送信息通知
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_GREETING_KEY,
                Utils.getMessageKey("greeting", user.getUid()),
                new GreetingMessageVo(user.getUid()));

        return rToken;
    }

    private void cacheSessionKey(String openId, String sessionKey) {
        String pythonRedisKey = String
                .format(UserRedisKey.PYTHON_WX_SESSIONKEY_KEY, userProperties.getWxMpAppId(),
                        openId);
        pythonCacheManager.set(pythonRedisKey, sessionKey, 1800);//python是1800s
    }

    /**
     * 手机登录
     */
    @Override
    public ResponsePacket<UserPhoneLoginVo> loginPhone(PhoneLoginContext phoneLoginContext) {

        //1. 检查系统是否关闭
        membersUserManager.checkSystemSwitch(null, false);
        UserPhoneLoginVo userPhoneLoginVo = new UserPhoneLoginVo();
        if (!codeValid(phoneLoginContext.getParams().getCode(),
                phoneLoginContext.getParams().getPhone(),
                SmsAuthenticationTypeEnum.LOGIN)) {
            userPhoneLoginVo.setIs_valid(false);
            return ResponsePacket.onSuccess(userPhoneLoginVo);
        }

        //从缓存查询手机号对应的用户id
        Integer userId = membersPhoneCacheManager
                .getUserIdByPhone(phoneLoginContext.getParams().getPhone());

        boolean isValid = (userId != null && userId.intValue() > 0);
        //客户端以此字段判断到底是验证码错误还是没有该用户，isvalid字段为true，且token为null则无此用户
        userPhoneLoginVo.setIs_valid(true);
        if (isValid) {
            //用户存在走登录流程
            MembersUser membersUser = membersUserService.getMembersUserById(userId);
            phoneLoginContext.setUid(membersUser.getUid());

            //开始校验风控：1.虚拟机开关。2.登录次数风控
            boolean shouldVerify = shouldVerifyNextData();
            if (shouldVerify) {
                ResponsePacket resp = loginCheck(
                        convertToPhoneLoginParam(phoneLoginContext.getParams(),
                                membersUser.getUid()),
                        phoneLoginContext.getmDeviceId(), phoneLoginContext.getVersion(), "login");
                //风控校验不合格
                if (!resp.responseSuccess()) {
                    resp.setData(userPhoneLoginVo);
                    return resp;
                }
            }
            //1. 检查是否白名单状态，白名单下白名单用户才能登录
            membersUserManager.checkSystemSwitch(membersUser, false);

            //用户是否允许登录校验
            userLoginValidate.validate(membersUser);
            String token = authTokenService
                    .getAccessToken(membersUser.getUid(), phoneLoginContext.getVersion());
            //获取融云token
            String rToken = rongYunService
                    .getToken(membersUser.getUid(), membersUser.getUsername(),
                            membersUser.getThumbnail());
            userPhoneLoginVo.setToken(token);
            userPhoneLoginVo.setRcloud_token(rToken);

            //更新登录时间
            membersUserManager
                    .updateLoginRegistAndLoginTime(membersUser.getId(),
                            null);

            // 异步：设置神策用户profile, 神策上报
            commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                    MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_SALOGIN_KEY,
                    Utils.getMessageKey("track", phoneLoginContext.getUid()),
                    Utils.phoneSALoginMessageVo(phoneLoginContext));

        }

        return ResponsePacket.onSuccess(userPhoneLoginVo);
    }

    private PhoneRegistParam convertToPhoneLoginParam(UserPhoneLoginParams params,
            String uid) {
        PhoneRegistParam p = new PhoneRegistParam();
        p.setUid(uid);
        p.setPhone(params.getPhone());
        p.setUser_agent(params.getUserAgent());
        return p;
    }

    private MembersUser registUser(ThirdPartLoginParams params, String mDeviceId,
            ThirdpartLoginVo resp, String version) {

        //1. TODO 预先生成user的uid，下面会用到
        String uid = membersUserService.getRandomUid();
        params.setUid(uid);

        //3. TODO 发放注册福利

        //4. 创建user账号，auth3表记录
        MembersUser user = membersUserManager.createUserAndAuth3(params);

        RegistLoginContext context = new RegistLoginContext(BeanMapper.map(user,
                MembersUserVo.class), params, true, mDeviceId, version);

        //5. TODO 调用融云接口获取融云token
        String rToken = rongYunService
                .getToken(user.getUid(), user.getUsername(), user.getThumbnail());
        resp.setRcloud_token(rToken);
        //6. 异步：发送信息通知
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_GREETING_KEY,
                Utils.getMessageKey("greeting", user.getUid()), Utils.greetingMessageVo(context));

        //8. 异步：神策注册事件
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_PRFILESET_KEY,
                Utils.getMessageKey("prfileset", user.getUid()),
                Utils.registingMessageVo(context));

        //9. 异步：神策绑定和注册账号
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_SASIGNUP_KEY,
                Utils.getMessageKey("sasignup", user.getUid()),
                Utils.registTrackSignUpMessageVo(context));
        return user;
    }

    private void checkUserValid(MembersUser user) {
        if (user == null) {//数据库有可能没找到该用户
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_THIRDPART_VALID_FAIL);
        }
        //检查用户活动标志，判断是否允许登录
        if (user.getIsActive() == null || (user.getIsActive() != null
                && user.getIsActive() != 1)) {
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_LOGIN_LOCK);
        }
    }

    private void processLogin(RegistLoginContext context, PhoneRegistVo resp) {
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_GREETING_KEY,
                Utils.getMessageKey("greeting", context.getUserVo().getUid()),
                Utils.greetingMessageVo(context));

        // TODO 发放注册福利

        // 异步：神策注册事件
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_PRFILESET_KEY,
                Utils.getMessageKey("prfileset", context.getUserVo().getUid()),
                Utils.registingMessageVo(context));

        // 异步：设置神策用户profile, 神策上报
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_SALOGIN_KEY,
                Utils.getMessageKey("track", context.getUserVo().getUid()),
                Utils.registSALoginMessageVo(context));

        // 异步：神策绑定和注册账号
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_SASIGNUP_KEY,
                Utils.getMessageKey("sasignup", context.getUserVo().getUid()),
                Utils.registTrackSignUpMessageVo(context));

        //统一获取token
        resp.setToken(authTokenService
                .getAccessToken(context.getUserVo().getUid(), context.getVersion()));
        resp.setSaid(context.getUserVo().getUid());

        resp.setSaid(context.getUserVo().getUid());
        return;
    }

    private void thirdProcessLogin(RegistLoginContext context, ThirdpartLoginVo resp) {
        MembersUserVo user = context.getUserVo();
        if (!context.isRegist()) {//正常登录还要做一系列工作
            //更新RegisterDevice登录设备号和登录时间
            membersUserManager
                    .updateLoginRegistAndLoginTime(context.getUserVo().getId(), null);

            // TODO 获取融云token
            String rToken = rongYunService
                    .getToken(user.getUid(), user.getUsername(), user.getThumbnail());
            resp.setRcloud_token(rToken);
            // 异步：发送短信
            commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                    MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_GREETING_KEY,
                    Utils.getMessageKey("greeting", context.getUserVo().getUid()),
                    Utils.greetingMessageVo(context));

            // TODO 异步：如有指纹，验证数美指纹
            if (context.getmDeviceId() != null) {

            }

            if (MembersAuthConstants.PLATFORM_WX
                    .equals(((ThirdPartLoginParams) context.getParams()).getPlatform())) {
                //微信登录把auth3.packange_name改为kaihei?

            }
        }

        // 异步：设置神策用户profile, 神策上报
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_TAG_SALOGIN_KEY,
                Utils.getMessageKey("track", context.getUserVo().getUid()),
                Utils.registSALoginMessageVo(context));

        // TODO 异步：上报数美

        //统一获取token
        resp.setAuth_token(authTokenService
                .getAccessToken(context.getUserVo().getUid(), context.getVersion()));
        resp.setSaid(user.getUid());
        return;
    }
}
