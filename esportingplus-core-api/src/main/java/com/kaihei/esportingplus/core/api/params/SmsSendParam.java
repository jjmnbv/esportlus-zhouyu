package com.kaihei.esportingplus.core.api.params;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author liuyang
 * @Description 短信发送参数
 * @Date 2018/10/24 15:16
 **/
@Validated
public class SmsSendParam implements Serializable {

    @Min( value = 1,message = "模板号不能为空")
    private int templateId;

    @NotBlank(message = "手机号不正确")
    private String phone;

    private String data;

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static void main(String[] args) {
        String p = "17603056890";
        String re = "^\\d{11}$";
//        String regex="^1((3[0-9])|(4[5|7])|(5([0-3]|[5-9]))|(8[0,5-9]))\\d{8}$";
        System.out.println(java.util.regex.Pattern.matches(re,p));
    }
}
