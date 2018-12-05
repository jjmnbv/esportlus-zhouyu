package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

/**
 * @author admin
 */
public class ComplaintInfo implements Serializable {

    private static final long serialVersionUID = -6420878930241723175L;

    /**
     * 用户ID
     */
    private String uid;

    /**
     *鸡牌号
     */
    private String chickenId;

    /**
     * 昵称
     */
    private String username;

    /**
     * 投诉人信息
     */
    private String complaintText;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    @Override
    public String toString() {
        return "ComplaintInfo{" +
                "uid='" + uid + '\'' +
                ", chickenId='" + chickenId + '\'' +
                ", username='" + username + '\'' +
                ", complaintText='" + complaintText + '\'' +
                '}';
    }
}
