package com.kaihei.esportingplus.core.api.params;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @Author liuyang
 * @Description //TODO
 * @Date 2018/11/8 17:37
 **/
@Validated
public class MpFormUploadParam {

    @NotNull(message = "unionId 不能为空")
    private String unionId;
    @NotNull(message = "openId 不能为空")
    private String openId;
    private String formId;
    @NotNull(message = "time 不能为空")
    private String time;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
