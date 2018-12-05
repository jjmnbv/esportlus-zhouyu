package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author admin
 */
public class BeComplaintInfo implements Serializable {

    private static final long serialVersionUID = 1443892459028988563L;

    /**
     * 用户ID
     */
    private String beUid;

    /**
     *鸡牌号
     */
    private String beChickenId;

    /**
     * 昵称
     */
    private String beUsername;

    /**
     * 暴鸡等级
     */
    private int baojiLevel;

    /**
     * 暴鸡等级名称
     */
    private String baojiLevelName;

    /**
     * 被投诉信息
     */
    private String beComplaintText;


    public String getBeUid() {
        return beUid;
    }

    public void setBeUid(String beUid) {
        this.beUid = beUid;
    }

    public String getBeChickenId() {
        return beChickenId;
    }

    public void setBeChickenId(String beChickenId) {
        this.beChickenId = beChickenId;
    }

    public String getBeUsername() {
        return beUsername;
    }

    public void setBeUsername(String beUsername) {
        this.beUsername = beUsername;
    }

    public int getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(int baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public String getBaojiLevelName() {
        return baojiLevelName;
    }

    public void setBaojiLevelName(String baojiLevelName) {
        this.baojiLevelName = baojiLevelName;
    }

    public String getBeComplaintText() {
        return beComplaintText;
    }

    public void setBeComplaintText(String beComplaintText) {
        this.beComplaintText = beComplaintText;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
