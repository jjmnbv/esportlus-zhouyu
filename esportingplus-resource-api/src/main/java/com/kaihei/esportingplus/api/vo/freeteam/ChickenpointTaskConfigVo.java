package com.kaihei.esportingplus.api.vo.freeteam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Administrator
 */
@ApiModel("鸡分任务配置")
public class ChickenpointTaskConfigVo implements Serializable {

    private static final long serialVersionUID = -927950632352900086L;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "鸡分任务配置id", required = false, position = 1, example = "")
    private Long taskId;

    /**
     * 任务标题
     */
    @ApiModelProperty(value = "任务标题", required = false, position = 1, example = "")
    private String title;

    /**
     * 任务副标题
     */
    @ApiModelProperty(value = "任务副标题", required = false, position = 1, example = "")
    private String subtitle;

    /**
     * 图标icon网络地址
     */
    @ApiModelProperty(value = "图标icon网络地址", required = false, position = 1, example = "")
    private String imgUrl;

    /**
     * 生效开始时间
     */
    @ApiModelProperty(value = "生效开始时间", required = false, position = 1, example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 生效截止时间
     */
    @ApiModelProperty(value = "生效截止时间", required = false, position = 1, example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 状态，0：禁用，1：启用，默认值
     */
    @ApiModelProperty(value = "状态，0：禁用，1：启用，默认值", required = false, position = 1, example = "")
    private Integer status;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号", required = false, position = 1, example = "")
    private Integer orderIndex;

    /**
     * 关联逻辑
     */
    @ApiModelProperty(value = "关联逻辑", required = false, position = 1, example = "")
    private String relatedLogic;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;


    public ChickenpointTaskConfigVo() {
        super();
    }

    /**
     * 获取任务id
     *
     * @return id - 任务id
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * 设置任务id
     *
     * @param taskId 任务id
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}