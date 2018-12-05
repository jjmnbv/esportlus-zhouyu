package com.kaihei.esportingplus.user.api.params;

import java.io.Serializable;

/**
 * 手机绑定参数
 *
 * @author yangshidong
 * @date 2018/10/24
 */
public class PhoneBindParams implements Serializable {

    private static final long serialVersionUID = 5902683012521464619L;

    /**
     * 性别:1 男，2 女
     */
    private int sex;

    /**
     * 验证码
     */
    private String code;

    /**
     * 手机号
     */
    private String phone;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
