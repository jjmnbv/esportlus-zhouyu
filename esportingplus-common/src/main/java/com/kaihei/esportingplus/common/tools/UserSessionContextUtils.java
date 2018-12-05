package com.kaihei.esportingplus.common.tools;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.web.UserSessionContext;

/**
 * 用户信息工具类
 * @author liangyi
 */
public final class UserSessionContextUtils {

    private static CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 修改 redis 中的用户头像
     * @param token Authentication token
     * @param avatar 用户头像链接
     */
    public static void updateUserAvatar(String token, String avatar) {
        ValidateAssert.allNotNull(BizExceptionEnum.INVALID_TOKEN, token);
        ValidateAssert.allNotNull(BizExceptionEnum.USER_AVATAR_NOT_EXIST, avatar);
        UserSessionContext user = UserSessionContext.getUser();
        user.setAvatar(avatar);
        cacheManager.set(RedisKey.UID_ACCESS_TOKEN + token,JsonsUtils.toJson(user));
    }

}

