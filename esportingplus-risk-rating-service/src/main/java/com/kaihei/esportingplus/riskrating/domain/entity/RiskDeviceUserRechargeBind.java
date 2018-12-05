package com.kaihei.esportingplus.riskrating.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户充值设备风控
 **/
@Entity(name = "risk_device_user_recharge_bind")
@Table(name = "risk_device_user_recharge_bind")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskDeviceUserRechargeBind extends AbstractEntity {

    private static final long serialVersionUID = 3110900593841978147L;

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
     * 充值次数
     */
    @Column(columnDefinition = "int(11) NOT NULL COMMENT '充值次数'", length = 11)
    private Integer rechargeCount;

    /***
     * 风险评估等级
     */
    @Column(columnDefinition = "varchar(10) DEFAULT NULL COMMENT '风险评估等级'", length = 10)
    private String riskLevel;

    /***
     * 风险评估等级名称
     */
    @Column(columnDefinition = "varchar(100) DEFAULT NULL COMMENT '风险评估等级名称'", length = 100)
    private String riskName;

    /***
     * 风险评估分数
     */
    @Column(columnDefinition = "decimal(5,2) NOT NULL DEFAULT '1.00' COMMENT '风险评估分数'")
    private BigDecimal riskScout=new BigDecimal(0);

    public RiskDeviceUserRechargeBind(){

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

    public Integer getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Integer rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskName() {
        return riskName;
    }

    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }

    public BigDecimal getRiskScout() {
        return riskScout;
    }

    public void setRiskScout(BigDecimal riskScout) {
        this.riskScout = riskScout;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
