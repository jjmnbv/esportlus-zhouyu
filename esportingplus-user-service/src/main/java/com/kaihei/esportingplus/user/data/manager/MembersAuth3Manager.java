package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.esportingplus.user.api.params.PhoneBindParams;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;

import java.util.Map;

/**
 * 第三方用户表数据处理
 *
 * @author yangshidong
 * @date 2018/10/24
 * */
public interface MembersAuth3Manager {
    /**
     * 绑定第三方账户，保存第三方账户信息到members_auth3
     *
     * @param partLoginParams 第三方账号信息
     * @param userId  绑定的userId
     * */
    void createBindAuth3(ThirdPartLoginParams partLoginParams,Integer userId);

    /**
     *绑定手机号，保存手机信息到members_auth3，同时更新members_user表中phone字段
     *
     * @param params 手机号以及userId
     **/
    void createAuth3AndUpdatePhone(PhoneBindParams params);

    /**
     * 校验手机号合法性以及验证码正确性
     *
     * @param phoneNumber  手机号码
     * @param code 手机验证码
     * @return string 验证结果：1 验证通过 8402 手机号码不合法   8202 验证码错误
     * */
    boolean validatePhoneNumberAndCode(String phoneNumber,String code);

    /**
     * 验证手机号码是否已经绑定过
     *
     * @param phoneNumber 手机号码
     * @return 校验结果
     * */
    boolean validatePhoneIsBind(String phoneNumber);

    /**
     * 获取用户绑定列表信息
     *
     * @param  userId
     * @param uid
     * @return
     * */
    Map<String,String> getBindList(Integer userId, String uid);

    /**
     * 解绑第三方账号
     *
     * @param userId
     * @param platform 解绑类型 WX/QQ
     * @param uid
     * */
    void checkAndUnBindAuth3(Integer userId,String platform,String uid);


    void updatePhone(Integer userId,String phoneNumber);
}
