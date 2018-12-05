package com.kaihei.esportingplus.core.constant;

/**
 * @Author liuyang
 * @Description  core  模块redis key
 * @Date 2018/10/25 16:49
 **/
public interface CoreRediskey {

    String KEY_SEPARATOR = ":";

    interface SmsKey{
        String VERIFICATION_CODE = "msg:sms:ver:code:%s";
    }

    interface WxKey{
        String ACCESS_TOKEN = "msg:wx:token:";
    }

    interface RonyunKey{
        String USER_TOKEN = "msg:user:token:%s";
    }

    interface WhiteListKey{
        String USER_WHITELIST = "user:whitelist";
    }
}
