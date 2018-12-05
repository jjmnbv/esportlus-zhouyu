package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;

import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 暴鸡币流水记录表
 *
 * @author xiaolijun
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name="uk_flow_no", columnNames = "flowNo") },
        indexes = {
        @Index(name = "idx_user_id_and_order_type", columnList = "userId, orderType")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GCoinBill extends AbstractBill {

    private static final long serialVersionUID = -441712387124573410L;
    
    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }
}
