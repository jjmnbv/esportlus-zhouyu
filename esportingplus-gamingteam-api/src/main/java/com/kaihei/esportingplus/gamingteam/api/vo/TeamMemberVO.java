package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队队员通用 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberVO implements Serializable {

    private static final long serialVersionUID = 4825863866807437426L;

    /**
     * 队员 uid
     */
    private String uid;

    /**
     * 车队队员身份 {@link com.kaihei.esportingplus.common.enums.UserIdentityEnum}
     */
    private Integer userIdentity;

    /**
     * 暴鸡等级 {@link com.kaihei.esportingplus.common.enums.BaojiLevelEnum}
     */
    private Integer baojiLevel;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
