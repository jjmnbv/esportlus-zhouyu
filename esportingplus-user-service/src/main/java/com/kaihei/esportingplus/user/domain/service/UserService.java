package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.common.web.UserSessionContext;
import java.util.List;

/**
 * @author zhangfang
 */
public interface UserService {

    /**
     * 获取用户信息接口
     * @param uids
     * @return
     */
    public List<UserSessionContext> getUserInfosByIds( List<String> uids);

    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    public UserSessionContext getUserInfoByUid(String uid);


    /**
     * 获取用户七牛头像，如果不是则转成七牛头像地址,并通知修改
     * @param uid
     * @return
     */
    public String getAvatarLinkAndNotifyUpdateAvatar(String uid);

    /**
     * 获取用户七牛头像，如果不是则转成七牛头像地址
     * @param uid
     * @return
     */
    public String getAvatarLink(String uid);
    /**
     * 获取用户七牛头像，如果不是则转成七牛头像地址,并通知修改
     * @param uid
     * @return
     */
    public String changeAndUpdateAvatar(String uid,String avatar);
}
