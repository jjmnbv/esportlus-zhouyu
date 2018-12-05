package com.kaihei.esportingplus.api.vo.freeteam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel("分享邀请任务配置")
public class InvitionShareConfigVo implements Serializable {

    private static final long serialVersionUID = 8333566131160970974L;
    /**
     * 邀请分享id
     */
    @NotNull
    @ApiModelProperty(value = "id",required = true, position = 1, example = "1")
    private Integer id;
    /**
     * 类别code
     */
    @ApiModelProperty(value = "类别code",required = true, position = 1, example = "")
    private String categoryCode;
    @ApiModelProperty(value = "任务code",required = true, position = 1, example = "")
    private String code;
    /**
     * 邀请分享任务名称
     */
    @ApiModelProperty(value = "任务名称", position = 1, example = "")
    private String taskName;

    /**
     *邀请分享任务标题
     */
    @ApiModelProperty(value = "任务标题",position = 1, example = "")
    private String taskTitle;
    /**
     *新版本奖励版本号
     */
    @ApiModelProperty(value = "版本号",position = 1, example = "")
    private String version;
    /**
     *邀请好友数量配置
     */
    @ApiModelProperty(value = "邀请人数", position = 1, example = "")
    private Integer inviteCount;
    /**
     *邀请的好友完成车队次数配置
     */
    @ApiModelProperty(value = "完成车队次数", position = 1, example = "")
    private Integer finishCount;
    /**
     *奖励免单次数配置
     */
    @ApiModelProperty(value = "奖励免单次数", position = 1, example = "")
    private Integer rewardFreeCount;
    /**
     *邀请的好友消费暴鸡币配置
     */
    @ApiModelProperty(value = "消费金额", position = 1, example = "")
    private String consumeAmount;
    /**
     *奖励金额配置
     */
    @ApiModelProperty(value = "奖励金额", position = 1, example = "")
    private String rewardAmount;
    /**
     *图标
     */
    @ApiModelProperty(value = "图标", position = 1, example = "")
    private String icon;
    /**
     *连续奖励天数
     */
    @ApiModelProperty(value = "连续奖励天数", position = 1, example = "")
    private Integer rewardDays;
    /**
     *连续奖励天数
     */
    @ApiModelProperty(value = "有效期(自然日 天)", position = 1, example = "")
    private Integer expiryDate;
    /**
     *更新时间
     */
    @ApiModelProperty(value = "更新时间",required = true, position = 1, example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh", timezone = "GMT+8")
    private Date gmtModified;
    /**
     *状态 0：禁用1：启用
     */
    @ApiModelProperty(value = "状态 0：下架 1：上架",required = true, position = 1, example = "")
    private Integer status;
    @ApiModelProperty(value = "上架时间",required = true, position = 1, example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh", timezone = "GMT+8")
    private Date onlineTime;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
    }

    public Integer getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(Integer finishCount) {
        this.finishCount = finishCount;
    }

    public Integer getRewardFreeCount() {
        return rewardFreeCount;
    }

    public void setRewardFreeCount(Integer rewardFreeCount) {
        this.rewardFreeCount = rewardFreeCount;
    }

    public String getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(String consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public String getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getRewardDays() {
        return rewardDays;
    }

    public void setRewardDays(Integer rewardDays) {
        this.rewardDays = rewardDays;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Integer getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
