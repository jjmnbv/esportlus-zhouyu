package com.kaihei.esportingplus.core.data.dto;

import com.kaihei.esportingplus.core.api.enums.SmsAuthenticationTypeEnum;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/1 12:17
 **/
public class SmsSendDto {

    private int templateId;
    private String phone;
    private String data;
    private SmsAuthenticationTypeEnum type;

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

    public SmsAuthenticationTypeEnum getType() {
        return type;
    }

    public void setType(SmsAuthenticationTypeEnum type) {
        this.type = type;
    }
}
