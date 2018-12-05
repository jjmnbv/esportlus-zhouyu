package com.kaihei.esportingplus.core.data.manager;

/**
 * @Author liuyang
 * @Description 验证码cache管理
 * @Date 2018/10/25 16:37
 **/
public interface VerificationCodeCacheManager {

    void setPhoneCode(String key, String code);

    String getCodeByPhone(String key);
}
