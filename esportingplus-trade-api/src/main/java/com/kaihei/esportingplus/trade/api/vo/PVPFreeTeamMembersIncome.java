package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *@Description: 免费车队针对暴鸡：各个老板付出的收益
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/12/3 17:07
*/
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PVPFreeTeamMembersIncome implements Serializable{

    private static final long serialVersionUID = 7362243450157880240L;
    /**
     * 老板uid
     */
    private String uid;

    /**
     * 暴鸡应得预计收益金额
     */
    private Integer price;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
