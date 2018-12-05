package com.kaihei.esportingplus.user.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.common.algorithm.AESUtils;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.user.api.params.MiniprogramPhoneInfo;
import com.kaihei.esportingplus.user.api.params.MiniprogramUserInfo;
import com.kaihei.esportingplus.user.api.vo.WxUserInfo;
import com.kaihei.esportingplus.user.config.UserProperties;
import com.kaihei.esportingplus.user.domain.service.validate.WxService;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-24 14:51
 * @Description:
 */
@Service
public class WxServiceImpl implements WxService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int SUCC = 0, ERROR = -1, INVALID = 40029, TO_MANY_TIMES = 45011;

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Autowired
    private UserProperties userProperties;

    /**
     * 根据凭证code去微信小程序接口获取sessionKey秘钥
     * @param code
     * @return
     */
    @Override
    public String getSessionKey(String code) {
        String url = String
                .format(userProperties.getWxMpAuthCodeUrl(), userProperties.getWxMpAppId(),
                        userProperties.getWxMpAppSecret(), code);
        logger.info("cmd=WxService.getSessionKey | msg={} | req={} | url={}", "获取微信sessionKey开始",
                code, url);
        String respString;
        try {
            respString = restTemplateExtrnal.getForObject(url, String.class);
        } catch (Exception e) {
            logger.error("cmd=WxService.getSessionKey | msg={} | req={} | url={}", "调用微信api获取sessionKey出现异常",
                    code, url);
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
        if (StringUtils.isBlank(respString)) {
            logger.error("cmd=WxService.getSessionKey | msg={} | req={} | url={}", "获取微信sessionKey返回空",
                    code, url);
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
        JSONObject obj = null;
        try {
            obj = JSON.parseObject(respString);
        } catch (Exception e) {
            logger.error("cmd=WxService.getSessionKey | msg={} | req={} | url={}", "解析返回值出现异常",
                    code, url);
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
        checkValid(obj, code);

        return obj.getString("session_key");
    }

    /**
     * 根据微信sessionKey秘钥解密客户端传过来的用户数据
     * @param param
     * @param sessionKey
     * @return
     */
    @Override
    public WxUserInfo getWxUserInfo(MiniprogramUserInfo param, String sessionKey) {
        String json = AESUtils.decrypt(param.getEncryptedData(), sessionKey, param.getIv());
        WxUserInfo userInfo = null;
        try {
            userInfo = JacksonUtils.toBean(json, WxUserInfo.class);
        } catch (Exception e) {
            logger.error("cmd=WxService.getWxUserInfo | msg={} | req={} | sessionKey={}| user_data={}", "解密用户数据出现异常",
                    JacksonUtils.toJson(param), sessionKey, json);
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
        if (userInfo == null) {
            logger.error("cmd=WxService.getWxUserInfo | msg={} | req={} | sessionKey={}| user_data={}", "解密用户数据为null",
                    JacksonUtils.toJson(param), sessionKey, json);
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
        if (!userInfo.getappId().equals(userProperties.getWxMpAppId())) {
            logger.error("cmd=WxService.getWxUserInfo | msg={} | req={} | sessionKey={}| user_data={}", "用户appId与系统appId不符",
                    JacksonUtils.toJson(param), sessionKey, json);
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
        return userInfo;
    }

    /**
     * 根据微信sessionKey秘钥解密客户端传过来的手机数据
     * @param info
     * @param sessionKey
     * @return
     */
    @Override
    public String getWxPhone(MiniprogramPhoneInfo info, String sessionKey) {
        String json = AESUtils.decrypt(info.getEncryptedData(), sessionKey, info.getIv());
        JSONObject obj = null;
        try {
            obj = JSON.parseObject(json);
        } catch (Exception e) {
            logger.error("cmd=WxService.getWxUserInfo | msg={} | req={} | sessionKey={}| user_data={}", "解析用户数据出现异常",
                    JacksonUtils.toJson(info), sessionKey, json);
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
        if (json == null) {
            logger.error("cmd=WxService.getWxPhone | msg={} | req={} | sessionKey={}", "解密用户数据为null" ,
                    JacksonUtils.toJson(info), sessionKey);
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
        if (!obj.getString("countryCode").startsWith("86")) {
            logger.error("cmd=WxService.getWxPhone | msg={} | req={} | sessionKey={}| user_data={}", "解密用户数据手机号为非中国手机号" ,
                    JacksonUtils.toJson(info), sessionKey, json);
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
        return obj.getString("purePhoneNumber");
    }

    private void checkValid(JSONObject obj, String code) {
        int errcode = obj.getIntValue("errcode");
        if (errcode == SUCC) {
            return ;
        } else if (errcode == ERROR) {
            logger.error("cmd=WxService.getSessionKey | msg={} | req={} | resp={}", "解析返回值出现异常,error",
                    code, obj.toJSONString());
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        } else if (errcode == INVALID) {
            logger.error("cmd=WxService.getSessionKey | msg={} | req={} | resp={}", "解析返回值出现异常,invalid",
                    code, obj.toJSONString());
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        } else if (errcode == TO_MANY_TIMES) {
            logger.error("cmd=WxService.getSessionKey | msg={} | req={} | resp={}", "解析返回值出现异常,to_many_times",
                    code, obj.toJSONString());
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        } else {
            logger.error("cmd=WxService.getSessionKey | msg={} | req={} | resp={}", "解析返回值出现异常",
                    code, obj.toJSONString());
            throw new BusinessException(BizExceptionEnum.WX_MP_AUTH_FAIL);
        }
    }


}
