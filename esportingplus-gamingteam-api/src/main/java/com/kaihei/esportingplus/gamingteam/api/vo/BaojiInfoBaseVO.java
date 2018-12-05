package com.kaihei.esportingplus.gamingteam.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡 VO
 * @author liangyi
 */
public class BaojiInfoBaseVO extends TeamMemberVO {

    private static final long serialVersionUID = -2450120849598947672L;

    /**
     * 车队队员状态
     * {@link com.kaihei.esportingplus.gamingteam.api.enums.TeamMemberStatusEnum}
     * {@link com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum}
     * {@link com.kaihei.esportingplus.gamingteam.api.enums}
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
