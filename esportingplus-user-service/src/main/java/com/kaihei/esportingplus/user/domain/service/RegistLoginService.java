package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.params.MiniprogramLoginParam;
import com.kaihei.esportingplus.user.api.params.MpPhoneBindLoginParam;
import com.kaihei.esportingplus.user.api.params.MpWxPhoneBindLoginParam;
import com.kaihei.esportingplus.user.api.params.PhoneRegistParam;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import com.kaihei.esportingplus.user.api.vo.MiniprogramLoginVo;
import com.kaihei.esportingplus.user.api.vo.PhoneLoginContext;
import com.kaihei.esportingplus.user.api.vo.PhoneRegistVo;
import com.kaihei.esportingplus.user.api.vo.ThirdpartLoginVo;
import com.kaihei.esportingplus.user.api.vo.UserPhoneLoginVo;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-24 10:53
 * @Description:
 */
public interface RegistLoginService {

    /**
     * 第三方注册或登录接口
     *
     * @param params post参数
     * @param mDeviceId header数美设备指纹参数
     * @return token和融云token
     */
    ResponsePacket<ThirdpartLoginVo> loginOrRegist(ThirdPartLoginParams params, String mDeviceId,
            String version);

    /**
     * 手机登录
     */
    ResponsePacket<UserPhoneLoginVo> loginPhone(PhoneLoginContext phoneLoginContext);

    /**
     * 手机注册方法
     */
    ResponsePacket<PhoneRegistVo> registPhone(PhoneRegistParam params, String mDevice,
            String version);

    /**
     * 小程序union认证
     */
    ResponsePacket<MiniprogramLoginVo> miniprogramLogin(MiniprogramLoginParam param,
            String mDeviceId, String version);

    /**
     * 小程序微信手机号登录
     */
    ResponsePacket<MiniprogramLoginVo> wxPhoneBindLogin(MpWxPhoneBindLoginParam param,
            String mDeviceId, String version);

    /**
     * 小程序自选手机号登录
     * @param param
     * @param mDeviceId
     * @param version
     * @return
     */
    ResponsePacket<MiniprogramLoginVo> phoneBindLogin(MpPhoneBindLoginParam param,
            String mDeviceId, String version);
}
