package com.kaihei.esportingplus.core.data.manager;

/**
 * @Author liuyang
 * @Description //TODO
 * @Date 2018/11/8 16:57
 **/
public interface WxTokenCacheManager {

    void setToken(String token, int expire);

    String getToken();


}
