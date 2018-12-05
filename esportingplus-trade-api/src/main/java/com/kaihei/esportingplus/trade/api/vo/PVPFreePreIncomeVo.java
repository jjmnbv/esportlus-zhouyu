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
public class PVPFreePreIncomeVo implements Serializable {

    private static final long serialVersionUID = 3966976612915923251L;
    /**
     * 暴鸡收益总金额
     */
    private Integer totalIncome;

    private List<PVPFreeTeamMembersIncome> freeTeamMembersIncomes;

    public Integer getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Integer totalIncome) {
        this.totalIncome = totalIncome;
    }

    public List<PVPFreeTeamMembersIncome> getFreeTeamMembersIncomes() {
        return freeTeamMembersIncomes;
    }

    public void setFreeTeamMembersIncomes(
            List<PVPFreeTeamMembersIncome> freeTeamMembersIncomes) {
        this.freeTeamMembersIncomes = freeTeamMembersIncomes;
    }
}
