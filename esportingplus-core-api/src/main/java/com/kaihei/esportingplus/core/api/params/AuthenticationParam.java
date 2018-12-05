package com.kaihei.esportingplus.core.api.params;

import com.kaihei.esportingplus.core.api.enums.SmsAuthenticationTypeEnum;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author liuyang
 * @Description 发送验证码参数
 * @Date 2018/11/1 12:13
 **/
@Validated
public class AuthenticationParam {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotNull(message = "类型不能为空")
    private SmsAuthenticationTypeEnum type;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SmsAuthenticationTypeEnum getType() {
        return type;
    }

    public void setType(SmsAuthenticationTypeEnum type) {
        this.type = type;
    }
}
