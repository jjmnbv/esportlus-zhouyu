package com.kaihei.esportingplus.riskrating.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 免费车队风控基础参数
 * @author chenzhenjun
 */
@Validated
@ApiModel(value = "用户注册绑定数美ID", description = "用户注册绑定数美ID")
public class FreeTeamBasicParams implements Serializable {

    private static final long serialVersionUID = 3994560975334942187L;

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id", required = true, example = "9c18f861")
    private String uid;

    @NotNull(message = "数美id不能为空")
    @ApiModelProperty(value = "数美id", required = true, example = "20180404064433981bf74f8873a0f2bccd2b29dabb472e014219554aa584f6")
    private String deviceId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
