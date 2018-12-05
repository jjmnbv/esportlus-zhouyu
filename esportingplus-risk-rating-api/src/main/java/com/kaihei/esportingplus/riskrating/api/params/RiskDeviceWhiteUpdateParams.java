package com.kaihei.esportingplus.riskrating.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = " 增加或者修改用户白名单", description = "增加或者修改用户白名单")
public class RiskDeviceWhiteUpdateParams {
    private Long id;
    /***
     * 设备唯一识别号
     */
    @NotBlank(message = "设备号不能为空")
    @ApiModelProperty(value = "设备号", required = true, example = "9c18f861")
    private String deviceNo;
    /***
     * 用户uid
     */
    @ApiModelProperty(value = "设备描述", required = false, example = "xxx工作室设备")
    private String deviceDesc ;

    /***
     * 白名单设备状态，0 禁用 1启用
     */
    @ApiModelProperty(value = "白名单状态 0:禁用 1启用", required = true, example = "1")
    @NotNull(message = " 状态不能为空")
    private Integer whiteStatus;
    @NotNull(message = "操作员id")
    @ApiModelProperty(value = "操作员id", required = true, example = "1234567")
    private Long operaterId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getDeviceDesc() {
        return deviceDesc;
    }

    public void setDeviceDesc(String deviceDesc) {
        this.deviceDesc = deviceDesc;
    }

    public Integer getWhiteStatus() {
        return whiteStatus;
    }

    public void setWhiteStatus(Integer whiteStatus) {
        this.whiteStatus = whiteStatus;
    }

    public Long getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(Long operaterId) {
        this.operaterId = operaterId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
