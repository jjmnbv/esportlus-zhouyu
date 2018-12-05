package com.kaihei.esportingplus.gamingteam.event;

import com.kaihei.esportingplus.common.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 老板确认支付成功后的事件
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class BossConfirmPaidSuccessEvent implements Event {

    /**
     * 老板 uid
     */
    private String bossUid;

    /**
     * 车队序列号, 为了使分布式锁生效这里就不定义成 teamSequence了
     */
    private String sequence;

    /**
     * 订单序列号
     */
    private String orderSequence;

    /**
     * 是否是 RPG 车队
     */
    private boolean isRPG;

    public BossConfirmPaidSuccessEvent() {
    }

    public String getBossUid() {
        return bossUid;
    }

    public void setBossUid(String bossUid) {
        this.bossUid = bossUid;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getOrderSequence() {
        return orderSequence;
    }

    public void setOrderSequence(String orderSequence) {
        this.orderSequence = orderSequence;
    }

    public boolean isRPG() {
        return isRPG;
    }

    public void setRPG(boolean RPG) {
        isRPG = RPG;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
