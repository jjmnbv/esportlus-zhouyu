package com.kaihei.esportingplus.core.api.params;

import com.kaihei.esportingplus.core.api.enums.SmsAuthenticationTypeEnum;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author liuyang
 * @Description 验证码校验参数
 * @Date 2018/11/1 10:31
 **/
@Validated
public class SmsCredentialParam {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotNull(message = "类型不能为空")
    private SmsAuthenticationTypeEnum type;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SmsAuthenticationTypeEnum getType() {
        return type;
    }

    public void setType(SmsAuthenticationTypeEnum type) {
        this.type = type;
    }
}
