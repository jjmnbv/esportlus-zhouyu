package com.kaihei.esportingplus.core.utils;

import com.kaihei.esportingplus.core.config.CoreContext;
import com.kaihei.esportingplus.core.config.MessageConfig;
import com.kaihei.esportingplus.core.data.manager.WhiteListManager;
import io.rong.util.CodeUtil;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author liuyang
 * @Description 融云utils
 * @Date 2018/10/29 14:25
 **/
public class RonYunUtils {

    private static MessageConfig messageConfig = CoreContext.context().getBean(MessageConfig.class);

    private static Environment env = CoreContext.context().getEnvironment();
    private static WhiteListManager whiteListManager = CoreContext.context().getBean(WhiteListManager.class);

    private static final String SYSTEM_SWITCH = "system.switch";
    private static final String SYSTEM_LOGIN_BAN = "system.login.ban";

    public static String encodeFrom(String from) {
        if (from.startsWith("bjdj_") || from.contains("_")) {
            return from;
        } else {
            return encodeIMUser(from);
        }
    }

    public static HttpHeaders getHeader(){
        HttpHeaders headers = new HttpHeaders();
        String nonce = String.valueOf(Math.random() * 1000000.0D);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        StringBuilder toSign = (new StringBuilder(messageConfig.getRonyun().getAppSecret())).append(nonce).append(timestamp);
        String sign = CodeUtil.hexSHA1(toSign.toString());
        headers.set("App-Key", messageConfig.getRonyun().getAppKey());
        headers.set("Nonce", nonce);
        headers.set("Timestamp", timestamp);
        headers.set("Signature", sign);
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        return headers;
    }

    public static List<String> encodeIMUser(List<String> uids) {
        return inWhiteList(uids).stream().map(s -> encode(s)).collect(Collectors.toList());
    }

    public static String encodeIMUser(String uid) {
        List<String> inWhiteList = inWhiteList(Arrays.asList(uid));
        return inWhiteList.stream().map(s -> encode(s)).findFirst().get();
    }

    public static List<String> inWhiteList(List<String> uids) {
        List<String> result = uids;
        int sysSwitch = Integer.parseInt(env.getProperty(SYSTEM_SWITCH));
        List<String> ban = Arrays.asList(env.getProperty(SYSTEM_LOGIN_BAN).split(","));
        if (sysSwitch == 1){
            return new ArrayList<>();
        }

        if (sysSwitch == 2 && ban.containsAll(Arrays.asList(env.getActiveProfiles()))) {
            //查詢白名单进行过滤
            result = whiteListManager.inWhiteList(uids);
        }

        return result;
    }

    //将userid编码成对应的IM uid   对应关系： userid - uid + '_' + md5(User'id + SECRET_KEY)[:6]
    private static String encode(String uid) {
        String re_md5 = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest
                    .digest((uid + messageConfig.getRonyun().getImSecretKey()).getBytes(Charset.forName("UTF-8")));
            StringBuffer buf = new StringBuffer("");
            int i;
            for (int offset = 0; offset < bytes.length; offset++) {
                i = bytes[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (Exception e) {
//            logger.error("RongYun Error:{}, encodeIMUserId failed, uid ={}", e.getContent(), uid);
        }
        return uid + "_" + re_md5.substring(0, 6);
    }
}
