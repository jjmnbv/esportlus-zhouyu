package com.kaihei.esportingplus.user.domain.service.validate;

import com.kaihei.esportingplus.user.api.params.MiniprogramPhoneInfo;
import com.kaihei.esportingplus.user.api.params.MiniprogramUserInfo;
import com.kaihei.esportingplus.user.api.vo.WxUserInfo;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-24 14:51
 * @Description:
 */
public interface WxService {

    /**
     * 根据凭证code去微信小程序接口获取sessionKey秘钥
     * @param code
     * @return
     */
    String getSessionKey(String code);

    /**
     * 根据微信sessionKey秘钥解密客户端传过来的用户数据
     * @param param
     * @param sessionKey
     * @return
     */
    WxUserInfo getWxUserInfo(MiniprogramUserInfo param, String sessionKey);

    /**
     * 根据微信sessionKey秘钥解密客户端传过来的手机数据
     * @param info
     * @param sessionKey
     * @return
     */
    String getWxPhone(MiniprogramPhoneInfo info, String sessionKey);
}
