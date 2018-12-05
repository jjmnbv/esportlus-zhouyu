package com.kaihei.esportingplus.user.data.manager;

/**
 * @Description: 第三方绑定缓存管理接口
 * @Author: xiekeqing
 * @Date: 2018年9月11日
 */

public interface MembersAuth3CacheManager {

    /**
     * 根据第三方唯一凭证查询绑定用户ID
     * @param openid
     * @return MembersUser
     */
    public Integer getBindUserIdOpenid(String openid);

    /**
     * 缓存传入的用户id
     * @param openid
     * @param userId
     */
    public void setBindUserIdOpenid(String openid,Integer userId);
}
