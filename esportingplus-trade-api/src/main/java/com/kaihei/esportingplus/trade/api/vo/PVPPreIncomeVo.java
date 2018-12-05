package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *@Description: 预计收益VO
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/8/20 19:30
*/
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PVPPreIncomeVo implements Serializable {

    private static final long serialVersionUID = 431380371068334654L;
    /**
     * 老板支付单价或暴鸡收益金额
     */
    private Integer price;

    private List<PVPTeamMembersIncome> teamMembersIncomes;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<PVPTeamMembersIncome> getTeamMembersIncomes() {
        return teamMembersIncomes;
    }

    public void setTeamMembersIncomes(
            List<PVPTeamMembersIncome> teamMembersIncomes) {
        this.teamMembersIncomes = teamMembersIncomes;
    }
}
