package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *@Description: 收益VO
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/8/20 19:30
*/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InComeVo implements Serializable {

    private static final long serialVersionUID = -591609983108913591L;
    /**
     * 老板支付总额
     */
    private int paySum;
    /**
     * 用户uid
     */
    private String uid;
    /**
     * 收益
     */
    private Integer inComeAmounts;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
