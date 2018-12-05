package com.kaihei.esportingplus.marketing.domian.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "market_user_free_coupons")
public class MarketUserFreeCoupons {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 用户uid
     */
    private String uid;

    /**
     * 是否可用
     */
    private Integer status;

    /**
     * 失效时间
     */
    @Column(name = "invalid_time")
    private Date invalidTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 来源 0-未知 1-新版本登录任务
     */
    private Integer source;

    /**
     * 券码类型 1-免费车队 
     */
    private Integer type;

    public MarketUserFreeCoupons(Long id, String uid, Integer status, Date invalidTime, Date createTime, Date updateTime, Integer source, Integer type) {
        this.id = id;
        this.uid = uid;
        this.status = status;
        this.invalidTime = invalidTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.source = source;
        this.type = type;
    }

    public MarketUserFreeCoupons() {
        super();
    }

    /**
     * 获取id
     *
     * @return id - id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户uid
     *
     * @return uid - 用户uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置用户uid
     *
     * @param uid 用户uid
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取是否可用
     *
     * @return status - 是否可用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置是否可用
     *
     * @param status 是否可用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取失效时间
     *
     * @return invalid_time - 失效时间
     */
    public Date getInvalidTime() {
        return invalidTime;
    }

    /**
     * 设置失效时间
     *
     * @param invalidTime 失效时间
     */
    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取来源 0-未知 1-新版本登录任务
     *
     * @return source - 来源 0-未知 1-新版本登录任务
     */
    public Integer getSource() {
        return source;
    }

    /**
     * 设置来源 0-未知 1-新版本登录任务
     *
     * @param source 来源 0-未知 1-新版本登录任务
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * 获取券码类型 1-免费车队 
     *
     * @return type - 券码类型 1-免费车队 
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置券码类型 1-免费车队 
     *
     * @param type 券码类型 1-免费车队 
     */
    public void setType(Integer type) {
        this.type = type;
    }
}