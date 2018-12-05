package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 提供给管理后台的投诉单使用
 * 车队队员信息
 * @author liangyi
 */
public class TeamMemberCompaintAdminVO implements Serializable{

    private static final long serialVersionUID = 4620772405439737867L;

    /** 用户 id */
    private Integer id;

    /** 用户 uid */
    private String uid;

    /** 用户鸡牌号 */
    private String chickenId;

    /** 用户身份, 0/1/2/9 */
    private Integer userIdentityCode;

    /** 用户身份描述, 0: 老板 1: 暴鸡 2:暴娘 9: 队长 */
    private String userIdentityDesc;

    /** 队员信息 username/uid/chickenId */
    private String label;

    /** 暴鸡等级, 100: 普通暴鸡 101:优选暴鸡 102: 超级暴鸡 300: 暴娘 0: 老板 */
    private Integer baojiLevelCode;

    /** 暴鸡等级描述 普通暴鸡/优选暴鸡/超级暴鸡/暴娘/老板 */
    private String baojiLevelDesc;

    public TeamMemberCompaintAdminVO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChickenId() {
        return chickenId;
    }

    public void setChickenId(String chickenId) {
        this.chickenId = chickenId;
    }

    public Integer getUserIdentityCode() {
        return userIdentityCode;
    }

    public void setUserIdentityCode(Integer userIdentityCode) {
        this.userIdentityCode = userIdentityCode;
    }

    public String getUserIdentityDesc() {
        return userIdentityDesc;
    }

    public void setUserIdentityDesc(String userIdentityDesc) {
        this.userIdentityDesc = userIdentityDesc;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getBaojiLevelCode() {
        return baojiLevelCode;
    }

    public void setBaojiLevelCode(Integer baojiLevelCode) {
        this.baojiLevelCode = baojiLevelCode;
    }

    public String getBaojiLevelDesc() {
        return baojiLevelDesc;
    }

    public void setBaojiLevelDesc(String baojiLevelDesc) {
        this.baojiLevelDesc = baojiLevelDesc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
