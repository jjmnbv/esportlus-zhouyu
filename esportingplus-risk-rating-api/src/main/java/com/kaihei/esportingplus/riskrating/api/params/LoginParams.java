package com.kaihei.esportingplus.riskrating.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: esportingplus-risk-rating
 * @description: 登陆注册接口参数
 * @author: chenzhenjun
 * @create: 2018-09-19 21:20
 **/
@ApiModel(value = "登陆注册接口参数", description = "登陆注册接口参数")
@Validated
public class LoginParams implements Serializable {

    private static final long serialVersionUID = -2361791993245422196L;

    /***
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true, example = "223867704556126208")
    private String userId;

    /***
     * 数美ID
     */
    @NotNull(message = "数美ID不能为空")
    @ApiModelProperty(value = "数美ID", required = true, example = "2236126208")
    private String deviceId;

    @NotNull(message = "电话号码不能为空")
    @ApiModelProperty(value = "电话号码", required = true, example = "13000000000")
    private String phone;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型", required = true, example = "1")
    private int type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
