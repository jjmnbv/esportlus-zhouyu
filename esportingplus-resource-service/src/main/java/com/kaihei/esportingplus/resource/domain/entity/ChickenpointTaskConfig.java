package com.kaihei.esportingplus.resource.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "chickenpoint_task_config")
public class ChickenpointTaskConfig {
    /**
     * 任务id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务副标题
     */
    private String subtitle;

    /**
     * 图标icon网络地址
     */
    @Column(name = "img_url")
    private String imgUrl;

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
     * 状态，0：禁用，1：启用，默认值
     */
    private Integer status;

    /**
     * 排序号
     */
    @Column(name = "order_index")
    private Integer orderIndex;

    /**
     * 关联逻辑
     */
    @Column(name = "related_logic")
    private String relatedLogic;

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

    public ChickenpointTaskConfig(Long id, String title, String subtitle, String imgUrl, Date startTime, Date endTime, Integer status, Integer orderIndex, String relatedLogic, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.imgUrl = imgUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.orderIndex = orderIndex;
        this.relatedLogic = relatedLogic;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public ChickenpointTaskConfig() {
        super();
    }

    /**
     * 获取任务id
     *
     * @return id - 任务id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置任务id
     *
     * @param id 任务id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取任务标题
     *
     * @return title - 任务标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置任务标题
     *
     * @param title 任务标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取任务副标题
     *
     * @return subtitle - 任务副标题
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * 设置任务副标题
     *
     * @param subtitle 任务副标题
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? null : subtitle.trim();
    }

    /**
     * 获取图标icon网络地址
     *
     * @return img_url - 图标icon网络地址
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置图标icon网络地址
     *
     * @param imgUrl 图标icon网络地址
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
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
     * 获取状态，0：禁用，1：启用，默认值
     *
     * @return status - 状态，0：禁用，1：启用，默认值
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态，0：禁用，1：启用，默认值
     *
     * @param status 状态，0：禁用，1：启用，默认值
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取排序号
     *
     * @return order_index - 排序号
     */
    public Integer getOrderIndex() {
        return orderIndex;
    }

    /**
     * 设置排序号
     *
     * @param orderIndex 排序号
     */
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    /**
     * 获取关联逻辑
     *
     * @return related_logic - 关联逻辑
     */
    public String getRelatedLogic() {
        return relatedLogic;
    }

    /**
     * 设置关联逻辑
     *
     * @param relatedLogic 关联逻辑
     */
    public void setRelatedLogic(String relatedLogic) {
        this.relatedLogic = relatedLogic == null ? null : relatedLogic.trim();
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