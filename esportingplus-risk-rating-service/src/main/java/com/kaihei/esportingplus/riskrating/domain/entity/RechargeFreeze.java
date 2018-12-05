package com.kaihei.esportingplus.riskrating.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;

/**
 * 充值功能冻结
 */
@Entity(name = "risk_rating_freeze")
@Data
public class RechargeFreeze extends AbstractEntity {

    /**
     * 被冻结帐号的uid
     */
    @Column(columnDefinition = "varchar(8) NOT NULL COMMENT '用户uid'", length = 8)
    private String uid;

    /**
     * 推送消息
     */
    @Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '推送消息'")
    private String msg;

    /**
     * 操作人Id
     */
    @Column(columnDefinition = "bigint(20) DEFAULT NULL COMMENT '操作人Id'")
    private Long operatorId;

    public RechargeFreeze() {
    }

    private RechargeFreeze(Builder builder) {
        setUid(builder.uid);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static final class Builder {

        private String uid;

        private Builder() {
        }

        public Builder uid(String val) {
            uid = val;
            return this;
        }

        public RechargeFreeze build() {
            return new RechargeFreeze(this);
        }
    }
}
