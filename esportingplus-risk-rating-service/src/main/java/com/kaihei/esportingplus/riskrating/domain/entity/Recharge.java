package com.kaihei.esportingplus.riskrating.domain.entity;

import com.kaihei.esportingplus.riskrating.api.enums.SourceTypeEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity(name = "risk_rating_recharge")
public class Recharge extends AbstractEntity {

    /**
     * 用户Id
     */
    @Column(length = 8, nullable = false, columnDefinition = "varchar(8) NOT NULL COMMENT '用户uid'")
    private String uid;

    /**
     * 操作系统
     *
     * PC表示PC端、ANDROID表示android、IOS表示ios
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false, columnDefinition = "varchar(8) NOT NULL COMMENT '充值平台'")
    private SourceTypeEnum sourceId;

    @Column(length = 32, columnDefinition = "varchar(32) DEFAULT NULL COMMENT '消息Id'")
    private String msgId;
    /**
     * 充值额度 (分)
     */
    @Column(length = 11, nullable = false, columnDefinition = "int(11) NOT NULL COMMENT '充值金额'")
    private Integer amount;

    /**
     * 设备号
     */
    @Column(columnDefinition = "varchar(100) DEFAULT NULL COMMENT '设备唯一标识'", length = 100)
    private String deviceNo;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public SourceTypeEnum getSourceId() {
        return sourceId;
    }

    public void setSourceId(SourceTypeEnum sourceId) {
        this.sourceId = sourceId;
    }

}
