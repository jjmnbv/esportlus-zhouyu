package com.kaihei.esportingplus.payment.api.params;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * 暴击值兑换暴鸡币入参
 *
 * @author: chenzhenjun
 **/
@Validated
public class StarlightExchangeParams implements Serializable {

    private static final long serialVersionUID = 3737828163879846399L;

    /***
     * 用户Id
     */
    private String userId;
    /***
     * 兑换数值 单位:分
     */
    private int amount;

    private String pythonToken;

    /***
     * 数据来源
     * （ANDROID表示android、IOS表示ios、H5表示H5、MP表示小程序）
     */
    private String sourceId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPythonToken() {
        return pythonToken;
    }

    public void setPythonToken(String pythonToken) {
        this.pythonToken = pythonToken;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
