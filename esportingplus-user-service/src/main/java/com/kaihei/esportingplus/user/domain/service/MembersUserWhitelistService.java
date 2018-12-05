package com.kaihei.esportingplus.user.domain.service;

/**
 * @author xiekeqing
 * @Title: MembersUserWhitelistService
 * @Description: 用户白名单服务
 * @date 2018/9/2017:44
 */
public interface MembersUserWhitelistService {

    public boolean  exists(long userId);

    public void initToCache();

}
