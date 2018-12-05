package com.kaihei.esportingplus.resource.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "banner_config")
public class BannerConfig {
    /**
     * banner id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    /**
     * banner类型，预留属性，0：图片，1：纯文本，2：图文
     */
    /**
     * 用户类型 0 老板 1暴鸡
     */
    public Integer userType;
    /**
     * banner名称
     */
    private String name;



    private Integer type;

    /**
     * 投放位置，预留属性
     */
    private String position;

    /**
     * banner图片网络地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 点击时，跳转的目标地址
     */
    @Column(name = "redirect_url")
    private String redirectUrl;

    /**
     * 生效开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 生效截止时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 是否冻结，0：默认值，不冻结，1：冻结，过期自动冻结
     */
    @Column(name = "is_freezed")
    private Integer isFreezed;

    /**
     * 权重、排序号
     */
    @Column(name = "order_index")
    private Integer orderIndex;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    public BannerConfig(Long id,Integer userType, String name, Integer type, String position, String imgUrl, String redirectUrl, Date startTime, Date endTime, Integer isFreezed, Integer orderIndex, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.userType=userType;
        this.name = name;
        this.type = type;
        this.position = position;
        this.imgUrl = imgUrl;
        this.redirectUrl = redirectUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isFreezed = isFreezed;
        this.orderIndex = orderIndex;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public BannerConfig() {
        super();
    }

    /**
     * 获取banner id
     *
     * @return id - banner id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置banner id
     *
     * @param id banner id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    /**
     * 获取banner名称
     *
     * @return name - banner名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置banner名称
     *
     * @param name banner名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取banner类型，预留属性，0：图片，1：纯文本，2：图文
     *
     * @return type - banner类型，预留属性，0：图片，1：纯文本，2：图文
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置banner类型，预留属性，0：图片，1：纯文本，2：图文
     *
     * @param type banner类型，预留属性，0：图片，1：纯文本，2：图文
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取投放位置，预留属性
     *
     * @return position - 投放位置，预留属性
     */
    public String getPosition() {
        return position;
    }

    /**
     * 设置投放位置，预留属性
     *
     * @param position 投放位置，预留属性
     */
    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    /**
     * 获取banner图片网络地址
     *
     * @return img_url - banner图片网络地址
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置banner图片网络地址
     *
     * @param imgUrl banner图片网络地址
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    /**
     * 获取点击时，跳转的目标地址
     *
     * @return redirect_url - 点击时，跳转的目标地址
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * 设置点击时，跳转的目标地址
     *
     * @param redirectUrl 点击时，跳转的目标地址
     */
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl == null ? null : redirectUrl.trim();
    }

    /**
     * 获取生效开始时间
     *
     * @return start_time - 生效开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置生效开始时间
     *
     * @param startTime 生效开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取生效截止时间
     *
     * @return end_time - 生效截止时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置生效截止时间
     *
     * @param endTime 生效截止时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取是否冻结，0：默认值，不冻结，1：冻结，过期自动冻结
     *
     * @return is_freezed - 是否冻结，0：默认值，不冻结，1：冻结，过期自动冻结
     */
    public Integer getIsFreezed() {
        return isFreezed;
    }

    /**
     * 设置是否冻结，0：默认值，不冻结，1：冻结，过期自动冻结
     *
     * @param isFreezed 是否冻结，0：默认值，不冻结，1：冻结，过期自动冻结
     */
    public void setIsFreezed(Integer isFreezed) {
        this.isFreezed = isFreezed;
    }

    /**
     * 获取权重、排序号
     *
     * @return order_index - 权重、排序号
     */
    public Integer getOrderIndex() {
        return orderIndex;
    }

    /**
     * 设置权重、排序号
     *
     * @param orderIndex 权重、排序号
     */
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
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
     * 获取更新时间
     *
     * @return gmt_modified - 更新时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置更新时间
     *
     * @param gmtModified 更新时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}