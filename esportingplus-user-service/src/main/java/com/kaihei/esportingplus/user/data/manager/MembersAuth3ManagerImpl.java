package com.kaihei.esportingplus.user.data.manager;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.core.api.enums.SmsAuthenticationTypeEnum;
import com.kaihei.esportingplus.core.api.feign.SMSServiceClient;
import com.kaihei.esportingplus.core.api.params.SmsCredentialParam;
import com.kaihei.esportingplus.user.api.params.PhoneBindParams;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import com.kaihei.esportingplus.user.api.vo.BindListVo;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.data.pyrepository.MembersAuth3Repository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersAuth3;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 第三方用户表数据处理实现类
 *
 * @author yangshidong
 * @date 2018/10/24
 */
@Service
public class MembersAuth3ManagerImpl implements MembersAuth3Manager {
    private static final Logger logger = LoggerFactory.getLogger(MembersAuth3ManagerImpl.class);

    @Autowired
    private MembersAuth3Repository membersAuth3Repository;

    @Autowired
    private MembersUserRepository membersUserRepository;

    @Autowired
    private SMSServiceClient smsServiceClient;

    @Autowired
    private MembersPhoneCacheManager membersPhoneCacheManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createBindAuth3(ThirdPartLoginParams partLoginParams, Integer userId) {
        MembersAuth3 auth3 = new MembersAuth3();
        auth3.setCredential(partLoginParams.getCredential());
        auth3.setIdentifier(partLoginParams.getIdentifier());
        auth3.setPackageName(MembersAuthConstants.PACKAGE_NAME_KH);
        auth3.setPlatform(partLoginParams.getPlatform());
        auth3.setUnionid(partLoginParams.getUnionid());
        auth3.setUserId(userId);
        int i = membersAuth3Repository.insertSelective(auth3);
        if (i < 0) {
            logger.error("cmd=MembersAuth3Manager.MembersAuth3ManagerImpl | msg={} | req={}",
                    "创建第三方用户失败",
                    JSON.toJSON(partLoginParams));
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_ACCOUNT_BIND_FAIL);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createAuth3AndUpdatePhone(PhoneBindParams params) {
        MembersAuth3 auth3 = new MembersAuth3();
        auth3.setIdentifier(params.getPhone());
        auth3.setUserId(UserSessionContext.getUser().getId());
        auth3.setPackageName(MembersAuthConstants.PACKAGE_NAME_KH);
        auth3.setPlatform(MembersAuthConstants.PLATFORM_PHONE);
        auth3.setCredential("");
        auth3.setUnionid("");
        int i = membersAuth3Repository.insertSelective(auth3);
        if (i < 0) {
            logger.error("cmd=MembersAuth3Manager.createAuth3AndUpdatePhone | msg={} | req={}",
                    "第三方用户表插入绑定手机号记录失败",
                    JSON.toJSON(params));
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_PHONE_BIND_FAIL);
        }
        int j = membersUserRepository.updateUserPhoneAndSexByUserId(params.getPhone(), params.getSex(), UserSessionContext.getUser().getId());
        if (j < 0) {
            logger.error("cmd=MembersAuth3Manager.createAuth3AndUpdatePhone | msg={} | req={}",
                    "绑定手机号更新user表手机号失败",
                    JSON.toJSON(params));
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_PHONE_BIND_FAIL);
        }
    }

    @Override
    public boolean validatePhoneNumberAndCode(String phoneNumber, String code) {
        String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        if (!Pattern.matches(REGEX_MOBILE, phoneNumber)) {
            logger.error("cmd=MembersAuth3ManagerImpl.validatePhoneNumberAndCode | msg={} | phoneNumber={} | code={}",
                    "手机号格式错误！", phoneNumber, code);
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_PHONE_IS_INVALID);
        }
        //TODO 校验验证码是否正确 不正确则返回 8202
        SmsCredentialParam smsCredentialParam = new SmsCredentialParam();
        smsCredentialParam.setPhone(phoneNumber);
        smsCredentialParam.setCode(code);
        smsCredentialParam.setType(SmsAuthenticationTypeEnum.BIND);
        ResponsePacket responsePacket = smsServiceClient.credential(smsCredentialParam);
        if (responsePacket.getCode() == BizExceptionEnum.SUCCESS.getErrCode()) {
            boolean result = (boolean) responsePacket.getData();
            if (!result) {
                logger.error("cmd=MembersAuth3ManagerImpl.validatePhoneNumberAndCode | msg={} | phoneNumber={} | code={}",
                        "验证码错误！", phoneNumber, code);
                throw new BusinessException(BizExceptionEnum.USER_PHONE_CODE_FAIL);
            }
            return true;
        }
        logger.error("cmd=MembersAuth3ManagerImpl.validatePhoneNumberAndCode | msg={} | phoneNumber={} | code={} | smsServiceClient response={}",
                "调用短信服务验证手机号以及验证码返回错误！", phoneNumber, code, responsePacket.toString());
        throw new BusinessException(BizExceptionEnum.USER_PHONE_CODE_FAIL);
    }

    @Override
    public boolean validatePhoneIsBind(String phoneNumber) {
        Integer userId = membersPhoneCacheManager.getUserIdByPhone(phoneNumber);
        if (userId != null || userId.intValue() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, String> getBindList(Integer userId, String uid) {
        Map<String, String> result = new HashMap<>(16);
        BindListVo bindListVo = membersAuth3Repository.getBindList(userId, uid);
        String phone = StringUtils.defaultString(bindListVo.getPhone());
        if (phone.length() > 10) {
            phone = phone.replaceAll(phone.substring(3, 9), "******");
        }
        result.put("phone", phone);
        result.put("wx", bindListVo.getWx());
        result.put("qq", bindListVo.getQq());
        //result.put("alipay", bindListVo.getAlipay());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkAndUnBindAuth3(Integer userId, String platform, String uid) {
        Map<String, String> bindList = getBindList(userId, uid);
        if (!bindList.get(platform.toLowerCase()).equals("bind")) {
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_PLATFORM_UNBIND_EMPTY);
        }
        MembersAuth3 membersAuth3 = new MembersAuth3();
        membersAuth3.setUserId(userId);
        membersAuth3.setPlatform(platform.toLowerCase());
        int i = membersAuth3Repository.delete(membersAuth3);
        if (i != 1) {
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_PLATFORM_UNBIND_FAIL);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePhone(Integer userId, String phoneNumber) {
        //根据userId获取旧的电话号码
        String oldPhoneNumber = membersAuth3Repository.getOldPhoneNumberByUserId(userId);
        //更新members_auth3电话号码
        int i = membersAuth3Repository.updatePhoneNumByUserId(phoneNumber, userId);
        if (i < 0) {
            logger.error("cmd=MembersAuth3ManagerImpl.updatePhone | msg={} | phoneNumber={} | userId={}",
                    "更新members_auth3电话号码失败！", phoneNumber, userId);
            throw new BusinessException(BizExceptionEnum.USER_PHONE_CODE_FAIL);
        }
        //更新members_user电话号码
        int j = membersUserRepository.updateUserPhoneAndSexByUserId(phoneNumber, 0, userId);
        if (j < 0) {
            logger.error("cmd=MembersAuth3Manager.updatePhone | msg={} | phoneNumber={} | userId={}",
                    "绑定手机号更新user表手机号失败",
                    phoneNumber, userId);
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_PHONE_BIND_FAIL);
        }
        //更新redis中旧的电话号码为新的号码
        logger.info("cmd=MembersAuth3Manager.updatePhone | msg={} | phoneNumber={} | userId={}",
                "更新redis中userId绑定的电话号码",
                phoneNumber, userId);
        membersPhoneCacheManager.updatePhoneBindUserId(oldPhoneNumber, phoneNumber, userId);
    }
}
