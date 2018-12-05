package com.kaihei.esportingplus.riskrating.api.params;

import com.kaihei.esportingplus.common.data.Castable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
public class RechargeRiskParams implements Castable {
    @NotBlank(message = "用户uid不能为空")
    private String uid;
    @NotNull(message = "充值金额不能为空")
    private Integer amount;
    @NotBlank(message = "设备唯一识别号不能为空")
    private String deviceNo;
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
