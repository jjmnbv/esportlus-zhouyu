package com.kaihei.esportingplus.marketing.api.event;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * 车队成员
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/10 11:48
 */

@Validated
public class TeamMember implements Serializable {

    private static final long serialVersionUID = -5393613626088705077L;
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

    /**
     * 老板匹配选择段位
     */
    private String dan;


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

    public String getDan() {
        return dan;
    }

    public void setDan(String dan) {
        this.dan = dan;
    }
}
