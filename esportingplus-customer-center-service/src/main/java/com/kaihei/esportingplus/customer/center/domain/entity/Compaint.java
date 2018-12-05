package com.kaihei.esportingplus.customer.center.domain.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Table(name = "compaint")
public class Compaint {

    /**
     * 投诉ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 投诉单类型，0:未知，1为普通订单投诉，2为悬赏单投诉，3为技能订单投诉，4为车队订单投诉，5为DNF车队订单投诉
     */
    private Byte type;

    /**
     * 业务订单号，如：DNF车队订单
     */
    @Column(name = "biz_order_sequeue")
    private String bizOrderSequeue;

    /**
     * 投诉人uid
     */
    private String uid;

    /**
     * 投诉人鸡牌号
     */
    private String chickenId;

    /**
     * 投诉人昵称
     */
    private String username;

    /**
     * 被投诉人uid
     */
    private String beUid;

    /**
     * 被投诉人鸡牌号
     */
    private String beChickenId;

    /**
     * 被投诉人昵称
     */
    private String beUsername;

    /**
     * 暴鸡等级
     */
    private int baojiLevel;

    /**
     * 游戏code
     */
    private int gameCode;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 投诉单号
     */
    private String sequeue;

    /**
     * 申诉状态，0：全部，1：申诉中，3：需二次审核，4：超时未申诉，5：暴鸡已申诉，10：投诉成立 11：投诉不成立
     */
    private Byte status;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Compaint() {
        super();
    }

    @Override
    public String toString() {
        return "Compaint{" +
                "id=" + id +
                ", type=" + type +
                ", bizOrderSequeue='" + bizOrderSequeue + '\'' +
                ", uid='" + uid + '\'' +
                ", chickenId='" + chickenId + '\'' +
                ", username='" + username + '\'' +
                ", beUid='" + beUid + '\'' +
                ", beChickenId='" + beChickenId + '\'' +
                ", beUsername='" + beUsername + '\'' +
                ", baojiLevel=" + baojiLevel +
                ", gameCode=" + gameCode +
                ", gameName='" + gameName + '\'' +
                ", sequeue='" + sequeue + '\'' +
                ", status=" + status +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }

    /**
     * 获取投诉ID
     *
     * @return id - 投诉ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置投诉ID
     *
     * @param id 投诉ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取投诉单类型，0:未知，1为普通订单投诉，2为悬赏单投诉，3为技能订单投诉，4为车队订单投诉，5为DNF车队订单投诉
     *
     * @return type - 投诉单类型，0:未知，1为普通订单投诉，2为悬赏单投诉，3为技能订单投诉，4为车队订单投诉，5为DNF车队订单投诉
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置投诉单类型，0:未知，1为普通订单投诉，2为悬赏单投诉，3为技能订单投诉，4为车队订单投诉，5为DNF车队订单投诉
     *
     * @param type 投诉单类型，0:未知，1为普通订单投诉，2为悬赏单投诉，3为技能订单投诉，4为车队订单投诉，5为DNF车队订单投诉
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取业务订单号，如：DNF车队订单
     *
     * @return biz_order_sequeue - 业务订单号，如：DNF车队订单
     */
    public String getBizOrderSequeue() {
        return bizOrderSequeue;
    }

    /**
     * 设置业务订单号，如：DNF车队订单
     *
     * @param bizOrderSequeue 业务订单号，如：DNF车队订单
     */
    public void setBizOrderSequeue(String bizOrderSequeue) {
        this.bizOrderSequeue = bizOrderSequeue == null ? null : bizOrderSequeue.trim();
    }

    /**
     * 获取投诉人uid
     *
     * @return uid - 投诉人uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置投诉人uid
     *
     * @param uid 投诉人uid
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public String getBeUid() {
        return beUid;
    }

    public void setBeUid(String beUid) {
        this.beUid = beUid;
    }

    public String getChickenId() {
        return chickenId;
    }

    public void setChickenId(String chickenId) {
        this.chickenId = chickenId;
    }

    public int getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(int baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public int getGameCode() {
        return gameCode;
    }

    public void setGameCode(int gameCode) {
        this.gameCode = gameCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    /**
     * 获取投诉单号
     *
     * @return sequeue - 投诉单号
     */
    public String getSequeue() {
        return sequeue;
    }

    /**
     * 设置投诉单号
     *
     * @param sequeue 投诉单号
     */
    public void setSequeue(String sequeue) {
        this.sequeue = sequeue == null ? null : sequeue.trim();
    }

    /**
     * 获取申诉状态，0：全部，1：申诉中，3：需二次审核，4：超时未申诉，5：暴鸡已申诉，10：投诉成立 11：投诉不成立
     *
     * @return status - 申诉状态，0：全部，1：申诉中，3：需二次审核，4：超时未申诉，5：暴鸡已申诉，10：投诉成立 11：投诉不成立
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置申诉状态，0：全部，1：申诉中，3：需二次审核，4：超时未申诉，5：暴鸡已申诉，10：投诉成立 11：投诉不成立
     *
     * @param status 申诉状态，0：全部，1：申诉中，3：需二次审核，4：超时未申诉，5：暴鸡已申诉，10：投诉成立 11：投诉不成立
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}