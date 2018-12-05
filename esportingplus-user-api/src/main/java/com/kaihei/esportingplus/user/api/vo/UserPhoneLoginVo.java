package com.kaihei.esportingplus.user.api.vo;

/**
 * @author xiekeqing
 * @Title: UserPhoneLoginVo
 * @Description: TODO
 * @date 2018/9/219:51
 */
public class UserPhoneLoginVo {

    /**
     * 融云token
     */
    private String rcloud_token;

    /**
     * true 验证码有效 \ false 验证码无效	是
     */
    private boolean is_valid;

    /**
     * 用户token，空字符串时为新用户，不空时为老用户
     */
    private String token;

    public String getRcloud_token() {
        return rcloud_token;
    }

    public void setRcloud_token(String rcloud_token) {
        this.rcloud_token = rcloud_token;
    }

    public boolean isIs_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
