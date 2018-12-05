package com.kaihei.esportingplus.core.data.manager;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/14 12:10
 **/
public interface RonyunUserTokenCacheManager {

    boolean saveToken(String user, String token);

    String getToken(String user);
}
