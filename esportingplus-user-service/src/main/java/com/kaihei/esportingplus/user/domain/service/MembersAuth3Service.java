package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.user.api.params.PhoneBindParams;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 第三方绑定服务
 * @Author: xiekeqing
 * @Date: 2018年9月11日
 */
public interface MembersAuth3Service {

    /**
     * 根据第三方id或unionid查询绑定信息
     *
     * @param platform
     * @param identifier
     * @param unionId
     * @return
     */
    public Integer getByIdentifierOrUnionId(String platform, String identifier, String unionId);

    /**
     * 绑定第三方账号
     *
     * @param params 第三方账户信息
     * @return
     */
    void bindAuth3(ThirdPartLoginParams params);

    /**
     * 绑定手机账号
     *
     * @param params 手机号以及验证码信息
     * @return
     */
    void bindPhone(PhoneBindParams params);

    /**
     * 获取用户下绑定列表信息
     *
     * @return 该用户下的绑定列表信息
     */
    Map<String, String> bindList();

    /**
     * 创建微信第三方
     *
     * @param userId
     * @param unionId
     */
    void createAuth3Wx(Integer userId, String unionId);

    /**
     * 解绑旧手机验证
     *
     * @param params 手机号以及验证码信息
     * @return
     */
    boolean verifyOldPhone(PhoneBindParams params);

    /**
     * 解绑第三方账户信息
     *
     * @param platform 解绑类型 WX/QQ
     */
    void unBindAuth3(String platform);

    /**
     * 更改绑定手机号
     *
     * @param params 手机号以及验证码信息
     **/
    void updatePhone(PhoneBindParams params);

    /**
     * 根据uid获取绑定的微信unionid
     *
     * @param uids 批量查询
     * @return uid:unionid的键值对集合
     */
    Map<String,String> getUnionIdByUids(List<String> uids);
}
