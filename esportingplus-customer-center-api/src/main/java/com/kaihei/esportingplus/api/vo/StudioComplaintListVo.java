package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
public class StudioComplaintListVo implements Serializable {

    private static final long serialVersionUID = -6679837873853565919L;

    /**
     * 投诉表主键ID
     */
    private int oid;

    /**
     * 投诉单号
     */
    private String complaintOid;

    /**
     * 投诉类型
     */
    private int type;

    /**
     * 申诉状态
     */
    private int status;

    /**
     * 投诉内容
     */
    private String content;

    /**
     *图片地址
     */
    private List<CompaintItemPictureVo> compaintItemPictureVo;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     * 剩余时间
     */
    private String remainingTime;

    /**
     * 被投诉人信息
     */
    private BeComplaintInfo beComplaintInfo;

    /**
     *投诉人信息
     */
    private ComplaintInfo complaintInfo;

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getComplaintOid() {
        return complaintOid;
    }

    public void setComplaintOid(String complaintOid) {
        this.complaintOid = complaintOid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CompaintItemPictureVo> getCompaintItemPictureVo() {
        return compaintItemPictureVo;
    }

    public void setCompaintItemPictureVo(
            List<CompaintItemPictureVo> compaintItemPictureVo) {
        this.compaintItemPictureVo = compaintItemPictureVo;
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
        return "StudioComplaintListVo{" +
                "oid=" + oid +
                ", complaintOid='" + complaintOid + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", content='" + content + '\'' +
                ", compaintItemPictureVo=" + compaintItemPictureVo +
                ", createTime=" + createTime +
                ", remainingTime='" + remainingTime + '\'' +
                ", beComplaintInfo=" + beComplaintInfo +
                ", complaintInfo=" + complaintInfo +
                '}';
    }
}
