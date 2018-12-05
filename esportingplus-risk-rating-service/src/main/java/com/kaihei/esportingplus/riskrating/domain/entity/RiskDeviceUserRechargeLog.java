package com.kaihei.esportingplus.riskrating.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @program: esportingplus-business
 * @description: 充值订单表
 * @author: xusisi
 * @create: 2018-08-17 10:45
 **/
@Entity(name = "risk_device_user_recharge_log")
@Table(name = "risk_device_user_recharge_log")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskDeviceUserRechargeLog extends AbstractEntity {

    private static final long serialVersionUID = 3110900593841978147L;

    @Column(columnDefinition = "varchar(64) NOT NULL", length = 64)
    private String msgId;
    /***
     * 设备唯一识别号
     */
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '设备唯一识别号'", length = 100)
    private String deviceNo;


    /***
     * 用户uid
     */
    @Column(columnDefinition = "varchar(8) NOT NULL COMMENT '用户uid'", length = 8)
    private String uid ;
    /***
     * 是否是白名单设备
     */
    @Column(columnDefinition = "tinyint(1) DEFAULT '0' COMMENT '是否是白名单设备'", length = 1)
    private Integer isWhite;
    /***
     * 充值金额，单位分
     */
    @Column(columnDefinition = "int(11) NOT NULL COMMENT '充值金额，单位分'", length = 11)
    private Integer rechargeMoney;
    /***
     * 货币类型
     */
    @Column(columnDefinition = "varchar(20) DEFAULT NULL COMMENT '货币类型'", length = 20)
    private String currencyType;
    /***
     * 风险评估等级
     */
    @Column(columnDefinition = "varchar(30) DEFAULT NULL COMMENT '风险评估等级名称'", length = 30)
    private String riskCode;

    /***
     * 风险评估等级名称
     */
    @Column(columnDefinition = "varchar(100) DEFAULT '1.00' COMMENT '风险评估分数'", length = 100)
    private String riskDesc;

    public RiskDeviceUserRechargeLog(){

    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(Integer rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    public String getRiskDesc() {
        return riskDesc;
    }

    public void setRiskDesc(String riskDesc) {
        this.riskDesc = riskDesc;
    }

    public Integer getIsWhite() {
        return isWhite;
    }

    public void setIsWhite(Integer isWhite) {
        this.isWhite = isWhite;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
