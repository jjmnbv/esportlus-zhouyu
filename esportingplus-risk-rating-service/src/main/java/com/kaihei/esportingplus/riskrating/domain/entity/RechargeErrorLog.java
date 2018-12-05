package com.kaihei.esportingplus.riskrating.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 消息达到重试最大次数时
 *
 * 放弃重试改为记Log
 */
@Entity(name = "risk_rating_recharge_errlog")
public class RechargeErrorLog extends AbstractEntity {

    /**
     * MQ组
     */
    @Column(name = "mq_group", length = 40, nullable = false, columnDefinition = "varchar(40) NOT NULL COMMENT '消费组'")
    private String group;

    /**
     * 订阅的Topic
     */
    @Column(columnDefinition = "varchar(40) NOT NULL COMMENT '订阅的Topic'", length = 40)
    private String topic;

    /**
     * 标签
     */
    @Column(columnDefinition = "varchar(40) NOT NULL COMMENT '订阅Tag'", length = 40)
    private String tag;

    /**
     * 消息体
     */
    @Column(columnDefinition = "varchar(1000) NOT NULL COMMENT '消息体'", length = 1000)
    private String msg;

    /**
     * 消息Id
     */
    @Column(columnDefinition = "varchar(32) NOT NULL COMMENT '消息Id'", length = 32)
    private String msgId;
    /**
     * 消息重试次数
     */
    @Column(columnDefinition = "int(11) NOT NULL COMMENT '重试次数'", length = 11)
    private int reconsumeTimes;

    public RechargeErrorLog(){

    }

    private RechargeErrorLog(Builder builder) {
        setGroup(builder.group);
        setTopic(builder.topic);
        setTag(builder.tag);
        setMsg(builder.msg);
        setMsgId(builder.msgId);
        setReconsumeTimes(builder.reconsumeTimes);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(RechargeErrorLog copy) {
        Builder builder = new Builder();
        builder.group = copy.getGroup();
        builder.topic = copy.getTopic();
        builder.tag = copy.getTag();
        builder.msg = copy.getMsg();
        builder.msgId = copy.getMsgId();
        builder.reconsumeTimes = copy.getReconsumeTimes();
        return builder;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getReconsumeTimes() {
        return reconsumeTimes;
    }

    public void setReconsumeTimes(int reconsumeTimes) {
        this.reconsumeTimes = reconsumeTimes;
    }

    public static final class Builder {

        private String group;
        private String topic;
        private String tag;
        private String msg;
        private String msgId;
        private int reconsumeTimes;

        private Builder() {
        }

        public Builder group(String val) {
            group = val;
            return this;
        }

        public Builder topic(String val) {
            topic = val;
            return this;
        }

        public Builder tag(String val) {
            tag = val;
            return this;
        }

        public Builder msg(String val) {
            msg = val;
            return this;
        }

        public Builder msgId(String val) {
            msgId = val;
            return this;
        }

        public Builder reconsumeTimes(int val) {
            reconsumeTimes = val;
            return this;
        }

        public RechargeErrorLog build() {
            return new RechargeErrorLog(this);
        }
    }
}
