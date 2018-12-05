package com.kaihei.esportingplus.user.domain.service;

/**
 * 访问token管理服务
 * <p>1、token生成</p>
 * <p>2、token刷新</p>
 * <p>3、token清除</p>
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/29 16:02
 */
public interface AuthTokenService {

    /**
     * 获取访问token
     *
     * @param uid 用户uid
     * @param version 版本
     * @return 返回token
     */
    public String getAccessToken(String uid, String version);

    /**
     * 刷新token
     * @param pythonToken 用户uid
     * @param version 版本
     * @return 返回token
     */
    public String refreshToken(String pythonToken, String version);

    /**
     * 清除token
     * @param pythonToken 用户uid
     * @param version 版本
     */
    public Boolean removeAccessToken(String pythonToken, String version);


}
