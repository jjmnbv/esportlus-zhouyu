package com.kaihei.esportingplus.trade.mq.message;

import com.kaihei.esportingplus.trade.domain.entity.Order;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 消息幂等处理
 * 1.消息是否已处理过
 * 2.重试中校验
 * @author liangyi
 */
public class RepeatConsume implements Serializable {

    private static final long serialVersionUID = 919816315163326796L;
    private Integer retryTimes;
    private String msgId;

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
