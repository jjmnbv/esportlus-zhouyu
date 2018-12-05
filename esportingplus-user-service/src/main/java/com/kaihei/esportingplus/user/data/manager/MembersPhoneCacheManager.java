package com.kaihei.esportingplus.user.data.manager;

/**
 * @author xiekeqing
 * @Title: MembersPhoneCacheManager
 * @Description: TODO
 * @date 2018/9/2110:10
 */
public interface MembersPhoneCacheManager {

    public Integer getUserIdByPhone(String phone);

    /**
     * 更新绑定的电话号码时更新redis中userId对应的电话号码
     * 首先删除对应的绑定记录，然后新增一条新的记录
     *
     * @param oldPhone 旧的手机号
     * @param newPhone 新绑定的手机号
     * @param userId
     */
    void updatePhoneBindUserId(String oldPhone, String newPhone,Integer userId);
}
