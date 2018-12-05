package com.kaihei.esportingplus.riskrating.api.params;

import com.kaihei.esportingplus.riskrating.api.enums.SourceTypeEnum;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public class RiskBlackCheckConsumerParams extends RechargeRiskParams {
    /**
     * 操作系统
     *
     * PC表示PC端、ANDROID表示android、IOS表示ios
     */
    @Enumerated(EnumType.STRING)
    private SourceTypeEnum sourceId;
    /**
     * msgId 用户防重
     */
    @NotBlank(message = "消息msgId不能为空")
    private String msgId;

    @NotBlank(message = "货币类型不能为空")
    private String currencyType;
    public SourceTypeEnum getSourceId() {
        return sourceId;
    }

    public void setSourceId(SourceTypeEnum sourceId) {
        this.sourceId = sourceId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
