package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author admin
 */
public class ComplaintListVo implements Serializable {

    private static final long serialVersionUID = -2257455730773869980L;

    /**
     * 投诉单主键ID
     */
    private int id;

    /**
     * 投诉单号 sequence
     */
    private String complaintOid;

    /**
     * 申诉状态
     */
    private Integer status;

    /**
     *车队订单号
     */
    private String premadeOrderOid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *剩余时间
     */
    private String remainingTime;
    /**
     * 申诉时间
     */
    private String appealCreateTime = "";


    /**
     * 游戏类型
     */
    private GameType gameType;

    /**
     * 被投诉人信息
     */
    private BeComplaintInfo beComplaintInfo;

    /**
     *投诉人信息
     */
    private ComplaintInfo complaintInfo;

    public String getAppealCreateTime() {
        return appealCreateTime;
    }

    public void setAppealCreateTime(Date appealCreateTime) {
        if (appealCreateTime == null) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.appealCreateTime = simpleDateFormat.format(appealCreateTime);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComplaintOid() {
        return complaintOid;
    }

    public void setComplaintOid(String complaintOid) {
        this.complaintOid = complaintOid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPremadeOrderOid() {
        return premadeOrderOid;
    }

    public void setPremadeOrderOid(String premadeOrderOid) {
        this.premadeOrderOid = premadeOrderOid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public BeComplaintInfo getBeComplaintInfo() {
        return beComplaintInfo;
    }

    public void setBeComplaintInfo(BeComplaintInfo beComplaintInfo) {
        this.beComplaintInfo = beComplaintInfo;
    }

    public ComplaintInfo getComplaintInfo() {
        return complaintInfo;
    }

    public void setComplaintInfo(ComplaintInfo complaintInfo) {
        this.complaintInfo = complaintInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
