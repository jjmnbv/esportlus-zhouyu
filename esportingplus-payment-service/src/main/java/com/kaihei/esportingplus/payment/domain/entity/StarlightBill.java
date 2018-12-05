package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 暴击值流水记录表
 *
 * @author xiaolijun
 * @create 2018-08-17 10:45
 **/
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name="uk_flow_no", columnNames = "flowNo") },
        indexes = { @Index(name = "idx_user_id_and_order_type", columnList = "userId, orderType")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarlightBill extends AbstractBill  {

    private static final long serialVersionUID = 3994560975334942159L;

    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }

}
